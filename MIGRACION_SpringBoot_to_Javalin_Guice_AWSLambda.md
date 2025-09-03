# Migración: Spring Boot 3 → Javalin + Google Guice para deploy en AWS Lambda

**Resumen rápido (TL;DR)**
- Objetivo: migrar la capa HTTP/controller de tu repo `ticketoffice-backend` (Spring Boot) a **Javalin** usando **Google Guice** para DI, y preparar el proyecto para desplegarlo en **AWS Lambda**.
- Opciones de despliegue en Lambda: **a)** ZIP/JAR + *aws-serverless-java-container* (adaptador servlet) — menos trabajo con SAM, pero puede aumentar cold starts; **b)** Imagen de contenedor + **AWS Lambda Web Adapter** — más simple para correr servidores HTTP tradicionales dentro de Lambda (recomendada si aceptas usar imágenes).

> Este documento es una guía paso a paso. Incluye snippets listos para pegar, plantillas SAM y checklist de pruebas.

---

## Índice
1. Prerrequisitos
2. Estrategia y decisiones (qué elegir y por qué)
3. Preparar el repo (branch, limpieza)
4. Dependencias y `build.gradle` (qué agregar/quitar)
5. Migrar rutas / controladores a Javalin (ejemplos)
6. Inyección de dependencias con Guice (estructura y ejemplo)
7. Seguridad (migrar Spring Security → middleware en Javalin)
8. Swagger / OpenAPI (opciones para mantener documentación)
9. Persistencia / capa de datos (opciones según tu arquitectura hexagonal)
10. Adaptador para AWS Lambda — **2 opciones** (ZIP + aws-serverless-java-container vs contenedor + Lambda Web Adapter)
11. CI/CD / SAM / plantilla `template.yaml` de ejemplo
12. Pruebas locales, debugging y checklist de lanzamiento
13. Optimización y métricas (cold starts, GraalVM, SnapStart)
14. Rollback y pruebas de humo
15. Resumen final y próximos pasos

---

## 1) Prerrequisitos
- Java 17+ o Java 21 (recomiendo **Java 21** para Lambda `java21`).
- Gradle (tu repo ya lo usa).
- AWS CLI, SAM CLI o Docker (para la opción de contenedor).
- Acceso al repositorio y permisos para crear ramas / PR.

---

## 2) Estrategia y decisiones (elige una)
- **Opción A — ZIP/JAR + aws-serverless-java-container (servlet adapter):** buena si quieres desplegar como ZIP sin contenedores. Requiere adaptar Javalin para correr como servlet y usar el adaptador de AWS para convertir eventos API Gateway a `HttpServletRequest`/`Response`.
- **Opción B — Contenedor + AWS Lambda Web Adapter (recomendada si aceptas contenedores):** empaquetas una imagen Docker con tu Javalin y usas el *Web Adapter* para que Lambda rote el tráfico HTTP a tu servidor. Menos cambios de código, ideal si aceptas imágenes.

**Recomendación:** Si quieres mínimo cambio en runtime y control, usa **Opción B**. Si prefieres zip/jar y no te importa ajustar servlets, usa **Opción A**.

---

## 3) Preparar el repo
1. Crear branch: `git checkout -b feature/migrate-javalin-guice-lambda`
2. Añadir un `README` parcial explicando el objetivo de la rama.
3. Ejecutar tests actuales y crear un `issue` con las funcionalidades críticas que deben conservarse (endpoints auth, checkout, validate, emails, etc.).

---

## 4) Dependencias y `build.gradle`
**Quitar (o reemplazar):** `spring-boot-starter-web`, `spring-boot-starter-security` (o mantener sólo si usas partes puntuales), `spring-data-*` si vas a reemplazar repositorios.
**Agregar (ejemplo Gradle Groovy):**

```groovy
plugins {
  id 'java'
  id 'com.github.johnrengelman.shadow' version '8.1.1' // si necesitas fatJar
}

repositories { mavenCentral() }

dependencies {
  // Javalin (usa la versión estable más reciente)
  implementation 'io.javalin:javalin:6.7.0'

  // Guice (DI)
  implementation 'com.google.inject:guice:5.1.0'

  // OpenAPI/Swagger para Javalin
  implementation 'io.javalin.community.openapi:javalin-swagger-plugin:6.3.0'

  // AWS Serverless Java Container (si eliges ZIP/JAR approach)
  implementation 'com.amazonaws.serverless:aws-serverless-java-container-core:2.1.4'
  implementation 'com.amazonaws.serverless:aws-serverless-java-container-servlet:2.1.4'

  // AWS Lambda Java libs
  implementation 'com.amazonaws:aws-lambda-java-core:1.2.3'
  implementation 'com.amazonaws:aws-lambda-java-events:3.11.0'

  // JDBC / JPA / Hibernate (si mantienes JPA)
  // implementation 'org.hibernate:hibernate-core:6.x'

  // Testing
  testImplementation 'org.junit.jupiter:junit-jupiter:5.9.3'
}

// Shadow jar config si quieres zip/jar deploy
shadowJar {
  archiveClassifier.set('all')
}
```

> Nota: versiones indicadas como ejemplo; verifica en Maven Central antes de pinnear la versión final.

---

## 5) Migrar rutas / controladores a Javalin (ejemplo)
En Spring Boot tenías `@RestController` con métodos `@GetMapping`, `@PostMapping`, etc. En Javalin vas a registrar handlers.

**Ejemplo de controlador simple (antes: Spring):**
```java
@RestController
@RequestMapping("/api/public/v1/events")
public class EventController {
  @GetMapping("/search")
  public ResponseEntity<List<EventDto>> search(...) { ... }
}
```

**Después (Javalin + Guice-injected service):**
```java
public class EventController {
    private final EventService eventService;

    @Inject
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    public void registerRoutes(Javalin app) {
        app.get("/api/public/v1/events/search", ctx -> {
            List<EventDto> result = eventService.search(ctx.queryParamMap());
            ctx.json(result);
        });

        app.get("/api/public/v1/events/:eventId", ctx -> {
            String id = ctx.pathParam("eventId");
            ctx.json(eventService.findById(id));
        });
    }
}
```

**Main de arranque (local / pruebas):**
```java
public class Main {
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new AppModule());
        Javalin app = Javalin.create(config -> {
            config.defaultContentType = "application/json";
            config.registerPlugin(new OpenApiPlugin(...)); // si quieres OpenAPI
        });

        EventController controller = injector.getInstance(EventController.class);
        controller.registerRoutes(app);

        app.start(8080);
    }
}
```

---

## 6) Inyección de dependencias con Guice
Estructura sugerida:
- `module`/`di` package
- `AppModule.java` con bindings

**AppModule ejemplo:**
```java
public class AppModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(EventService.class).to(EventServiceImpl.class).in(Singleton.class);
        bind(UserRepository.class).to(UserRepositoryImpl.class);
        // Binding para EntityManager / Datasource si usas JPA/Hibernate
    }
}
```

**Uso en tests:** inyecta mocks con `Guice.createInjector(new TestModule())`.

---

## 7) Seguridad: migrar Spring Security → middleware en Javalin
Spring Security ofrece muchas features; en Javalin normalmente manejas seguridad mediante *before handlers* o **AccessManager**.

**Estrategia simple:**
1. Implementas un `AuthService` que valide JWT/Session (reutiliza código existente si está desacoplado).
2. Registrar un `before` global para rutas que requieran autenticación:

```java
app.before("/api/*", ctx -> {
    String header = ctx.header("Authorization");
    if (header == null || !authService.validate(header)) {
        ctx.status(401).result("Unauthorized");
    }
});
```

**Opción avanzada:** usa `app.config.accessManager()` con roles, que te permite declarar handlers con roles por ruta.

---

## 8) Swagger / OpenAPI
Si quieres seguir generando `/v3/api-docs` como hacías con `springdoc` puedes:
- **Re-generar OpenAPI** con el plugin `javalin-openapi` (recomendado). Hay tutorial oficial y ejemplo de repo.
- **Opción rápida:** servir el `api-docs.json` que ya generabas (archivo estático bajo `/docs`) mientras migras.

> Guía de como migrar [link](https://javalin.io/tutorials/openapi-example)

---

## 9) Persistencia / capa de datos
Tu repositorio usa probablemente Spring Data JPA. Opciones:
- **A) Mantener JPA/Hibernate pero sin Spring Data:** crear `EntityManagerFactory` y DAOs manuales, inyectados por Guice.
- **B) Reescribir DAOs con JDBC/jdbi** (más control y arranque más rápido).
- **C) Mantener Spring Data *si realmente quieres*:** posible ejecutar Spring Data fuera de Spring Boot pero no es lo más limpio.

**Ejemplo breve de EMF bind en Guice:**
```java
public class JpaModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(EntityManagerFactory.class).toInstance(
        Persistence.createEntityManagerFactory("my-persistence-unit"));
  }
}
```

Asegúrate de crear un `persistence.xml` si vas con JPA puro.

---

## 10) Adaptador para AWS Lambda — 2 opciones detalladas

### Opción A: ZIP/JAR + aws-serverless-java-container (servlet adapter)
**Resumen:** usas la librería `aws-serverless-java-container` que adapta requests API Gateway a modelo Servlet. Montas Javalin como servlet y creas una clase handler `RequestStreamHandler` que delega al container.

**Pasos principales:**
1. Añadir dependencias `aws-serverless-java-container-core` y `-servlet`.
2. Crear un `StreamLambdaHandler` que inicialice el container en `static { ... }` y en `handleRequest` llame a `handler.proxyStream(...)`.
3. Empaquetar con `./gradlew shadowJar` y subir ZIP/JAR a Lambda. Configurar función con runtime `java21` y handler: `com.tu.pkg.StreamLambdaHandler::handleRequest`.

**Pros:** no necesitas contenedor, control fino.  
**Contras:** montaje de servlet + Jetty puede aumentar cold start; ajustar memoria/timeout necesario.

> Referencia técnica: AWS Serverless Java Container adapta frameworks servlet a Lambda. (ver fuentes abajo). 

### Opción B: Contenedor + AWS Lambda Web Adapter (recomendada si aceptas contenedores)
**Resumen:** Empaquetas la app como una imagen Docker que ejecuta el JAR normal (o `java -jar`) en el `ENTRYPOINT` y añades el *Lambda Web Adapter* como extensión (o lo incluyes en la imagen). El Web Adapter se encarga de traducir requests Lambda <-> HTTP y forwardea al servidor que escucha en un puerto.

**Pasos principales:**
1. Crear `Dockerfile` que construya y copie el `shadowJar` y que exponga `8080` (o el puerto que uses).
2. En la imagen, instalar o copiar el binario `aws-lambda-adapter` en `/opt/extensions/` (o configurar según doc del proyecto Web Adapter).
3. Empujar la imagen a ECR y crear la función Lambda usando tipo de paquete `Image`.

**Pros:** menor trabajo de adaptación; puedes usar tu servidor HTTP tal cual; mejor compatibilidad con frameworks que esperan un verdadero servidor.
**Contras:** imagenes pueden ser mayores; SnapStart no aplica a imágenes (SnapStart aplica a funciones ZIP en Java según casos).

> Referencia técnica: AWS Lambda Web Adapter permite ejecutar aplicaciones web dentro de Lambda mediante un adaptador que reenvía HTTP a tu servidor. 

---

## 11) CI/CD / SAM y `template.yaml` de ejemplo (ZIP/JAR approach)
**Ejemplo `template.yaml` (SAM) para ZIP/JAR con handler `com.tu.pkg.StreamLambdaHandler`:**

```yaml
AWSTemplateFormatVersion: '2010-09-09'
Transform: AWS::Serverless-2016-10-31
Description: TicketOffice Backend - Javalin on Lambda (zip)
Resources:
  TicketOfficeFunction:
    Type: AWS::Serverless::Function
    Properties:
      FunctionName: ticketoffice-backend-javalin
      Handler: com.tu.pkg.StreamLambdaHandler::handleRequest
      Runtime: java21
      MemorySize: 1024
      Timeout: 30
      CodeUri: ./build/libs/yourapp-all.jar
      Policies: []
      Events:
        Api:
          Type: Api
          Properties:
            Path: /{proxy+}
            Method: ANY
      SnapStart:
        ApplyOn: PublishedVersions
```

**Para opción contenedor:** define `PackageType: Image` and usa la `ImageUri` en `Properties`.

---

## 12) Pruebas locales y debugging
- Ejecuta `Main#main` localmente y comprueba rutas con `curl`.
- Para SAM local (ZIP): `sam local start-api --template template.yaml`.
- Para Docker: `docker run -p 8080:8080 <image>` y prueba.
- Comprueba logs en CloudWatch después del deploy.

---

## 13) Optimización y métricas (cold start)
- **Aws Serverless Java Container:** es funcional pero puede añadir latencia (jetty, servlet). Considera SnapStart o ajustar memoria. 
- **SnapStart:** habilitar SnapStart en funciones Java puede reducir cold-starts dramáticamente para ZIP/JAR (ver docs).
- **GraalVM native-image:** reduce cold start aún más pero complica build y reflección (necesita configuración extensa). Útil si latencia es crítica.

---

## 14) Rollback y pruebas de humo
- Desplegar versión `v1` (branch) paralela y hacer pruebas de comparación p95/p99.
- Tener endpoint de health `/ping` que verifique DB y servicios externos.
- Si algo falla, rollback a la versión anterior en Lambda o volver a apuntar el API Gateway a la función previa.

---

## 15) Checklist final antes de merge
- [ ] Todas las rutas implementadas y cubiertas por tests
- [ ] Autenticación y roles validados
- [ ] OpenAPI funcionando o servido el `api-docs.json` existente
- [ ] Pruebas de integración (emails, QR, pagos) en staging
- [ ] Logs y métricas en CloudWatch / X-Ray configuradas
- [ ] Política de permisos (IAM) revisada

---

## Anexos: snippets útiles

### Ejemplo `StreamLambdaHandler` (esqueleto) para aws-serverless-java-container (ZIP/JAR)
```java
public class StreamLambdaHandler implements com.amazonaws.services.lambda.runtime.RequestStreamHandler {
    private static final com.amazonaws.serverless.proxy.internal.LambdaContainerHandler<AwsProxyRequest, AwsProxyResponse> handler;

    static {
        try {
            // Inicializa tu app Javalin dentro de un contexto Servlet aquí
            handler = AwsLambdaServletContainerHandler.getAwsProxyHandler();
            // Si necesitas pasar un ServletContext o configurar, hazlo aquí
        } catch (ContainerInitializationException e) {
            throw new RuntimeException("Could not initialize lambda servlet handler", e);
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        handler.proxyStream(inputStream, outputStream, context);
    }
}
```
> Este snippet es un esqueleto: la inicialización exacta depende de cómo montes Javalin como `Servlet`.

### Ejemplo `Dockerfile` (contenedor + Web Adapter)
```dockerfile
FROM eclipse-temurin:21-jdk-alpine as build
WORKDIR /app
COPY build/libs/yourapp-all.jar /app/yourapp.jar

FROM public.ecr.aws/amazonlinux/amazonlinux:2023
# Instala Java runtime mínimo o usa imagen base Java
COPY --from=build /app/yourapp.jar /app/yourapp.jar
# Copia el lambda web adapter en /opt/extensions/ si lo usas como extension
# COPY lambda-adapter /opt/extensions/lambda-adapter
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/yourapp.jar"]
```

---

## Recursos / Lecturas (para referencia técnica)
- Javalin (docs y ejemplos).  
- AWS Serverless Java Container (wiki) — para adaptar servlets a Lambda.  
- AWS Lambda Web Adapter — adaptar apps web empaquetadas como contenedores.  
- Javalin OpenAPI / Swagger plugin.  
- AWS docs sobre SnapStart y Java en Lambda.

---

## Próximos pasos que puedo hacer por ti (opcional)
1. Crear un PR en tu repo con los cambios mínimos (pom/gradle + plantilla `Main` + handler).  
2. Preparar la `Dockerfile` y el pipeline para pushed image a ECR + deploy.

Si querés, genero el PR en la rama `feature/migrate-javalin-guice-lambda` con los cambios base y un `README` que explique cómo probar localmente.

---

> Fin de la guía. Si querés que la convierta en un README más corto, o que implemente los cambios en código y cree el PR, decime cuál de las dos opciones (ZIP/JAR o Contenedor) preferís y arranco.
