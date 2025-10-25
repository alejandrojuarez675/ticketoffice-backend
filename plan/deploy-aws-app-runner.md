# Despliegue económico de ticketoffice-backend (Javalin + Java 21) en AWS App Runner

Esta guía explica **cómo desplegar tu API REST en AWS gastando lo menos posible**, usando **AWS App Runner**, el servicio administrado de AWS que ejecuta contenedores y puede **escalar hasta cero instancias** cuando no hay tráfico.

Repositorio base: [alejandrojuarez675/ticketoffice-backend](https://github.com/alejandrojuarez675/ticketoffice-backend)

---

## 🎯 Objetivo

* Hacer deploy de la app Javalin (Java 21) que actualmente corre local con `java -jar`.
* Obtener una URL pública en AWS.
* Minimizar costos (permitir que se apague automáticamente sin tráfico).
* Facilitar futuras integraciones con otros servicios AWS (RDS, S3, SNS, etc.).

---

## 💡 Por qué AWS App Runner

AWS App Runner es la opción más equilibrada para tu caso:

* **Ejecuta contenedores** (Docker) sin que tengas que administrar servidores.
* **Escala automáticamente hasta cero instancias** cuando no hay tráfico.
* Se paga **solo cuando hay solicitudes activas**.
* Permite exponer una **URL HTTPS pública** sin configurar balanceadores o dominios.
* Soporta Java 21 y tu stack actual sin adaptaciones especiales (a diferencia de Lambda).

Alternativas:

| Servicio                   | Ventajas                      | Desventajas                                |
| -------------------------- | ----------------------------- | ------------------------------------------ |
| **AWS Lambda (container)** | Muy bajo costo                | Cold start alto, no ideal para REST server |
| **App Runner ✅**           | REST real, escala a 0, simple | Pequeño costo base si hay tráfico          |
| **EC2 + Auto Scaling**     | Control total                 | No escala a 0, gestión compleja            |

---

## 🧱 Paso 1: Crear el contenedor Docker

Asegúrate de generar un *fat jar* con todas las dependencias:

```bash
./gradlew shadowJar
```

Esto produce un archivo como `build/libs/ticketoffice-backend-all.jar`.

Luego crea un `Dockerfile` en la raíz del proyecto:

```dockerfile
# Dockerfile para ticketoffice-backend
FROM amazoncorretto:21-alpine
WORKDIR /app
COPY build/libs/ticketoffice-backend-all.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
```

Verificá que el contenedor funcione localmente:

```bash
docker build -t ticketoffice-backend .
docker run -p 8080:8080 ticketoffice-backend
```

Si accedés a `http://localhost:8080/ping` y responde correctamente, podés seguir.

---

## 📦 Paso 2: Subir la imagen a Amazon ECR

1. **Crear repositorio ECR:**

   ```bash
   aws ecr create-repository --repository-name ticketoffice-backend --region us-east-1
   ```

2. **Autenticarse y subir la imagen:**

   ```bash
   aws ecr get-login-password --region us-east-1 | docker login --username AWS --password-stdin <account-id>.dkr.ecr.us-east-1.amazonaws.com

   docker tag ticketoffice-backend:latest <account-id>.dkr.ecr.us-east-1.amazonaws.com/ticketoffice-backend:latest

   docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/ticketoffice-backend:latest
   ```

> 💡 Reemplazá `<account-id>` por tu número de cuenta AWS. Podés obtenerlo desde la consola.

---

## 🚀 Paso 3: Crear el servicio en AWS App Runner

Podés hacerlo desde la consola o con CLI. Aquí te dejo el flujo más simple vía **AWS Console**:

1. Abrí **App Runner → Create service**.
2. En *Source*, elegí **Container registry → Amazon ECR**.
3. Seleccioná la imagen que subiste.
4. **Port:** 8080.
5. **Deployment settings:** Manual o automático (según prefieras).
6. **Auto scaling:** activá *Enable auto scaling* y marcá *Scale to zero*.
7. **Compute configuration:** 0.25 vCPU / 0.5 GB (suficiente para pruebas).
8. **Security:** Auth pública (para probar). Podés restringirla más adelante.
9. Esperá unos minutos a que cree el servicio.

Obtendrás una URL pública tipo:

```
https://<random-id>.awsapprunner.com
```

---

## 🔍 Paso 4: Probar el acceso

Probá tu API con `curl` o el navegador:

```bash
curl https://<random-id>.awsapprunner.com/ping
```

Tu API debería responder igual que en local. Si ves errores 503 justo al despertar, es normal: la instancia se está encendiendo (demora 5–10 segundos).

---

## ⚙️ Paso 5: Monitoreo y costos

* **Sin tráfico:** App Runner apaga la instancia → costo ≈ $0.
* **Con tráfico:** se cobra por CPU/memoria durante el tiempo activo.
* **Logs:** disponibles en CloudWatch automáticamente.
* **Escalado:** se ajusta según la cantidad de requests concurrentes.

> 💸 El plan gratuito cubre 100 horas/mes, ideal para desarrollo y pruebas.

---

## 🔄 Paso 6: Actualizar la app

Cada vez que hagas un cambio:

```bash
./gradlew shadowJar
docker build -t ticketoffice-backend .
docker push <account-id>.dkr.ecr.us-east-1.amazonaws.com/ticketoffice-backend:latest
```

Luego, en App Runner → “Deployments” → “Deploy latest image”.

Podés automatizar este flujo con GitHub Actions y ECR para CI/CD.

---

## 🧩 Integraciones futuras

Tu servicio App Runner podrá integrarse fácilmente con:

* **Amazon RDS** (bases de datos relacionales)
* **Amazon S3** (almacenamiento de archivos)
* **AWS Secrets Manager** (credenciales seguras)
* **AWS CloudWatch** (logs y métricas)

Para conexiones a RDS o VPC, podés habilitar *VPC Connector* desde la configuración del servicio.

---

## 🧼 Limpieza

Si querés evitar cualquier costo cuando termines de probar:

```bash
aws apprunner delete-service --service-arn <service-arn>
aws ecr delete-repository --repository-name ticketoffice-backend --force
```

---

## 📚 Referencias útiles

* [AWS App Runner – Developer Guide](https://docs.aws.amazon.com/apprunner/latest/dg/what-is-apprunner.html)
* [AWS ECR – Push and Pull Images](https://docs.aws.amazon.com/AmazonECR/latest/userguide/docker-push-ecr-image.html)
* [Javalin Documentation](https://javalin.io/documentation)

---

¿Querés que agregue un ejemplo de **GitHub Actions** para que el deploy a App Runner se haga automáticamente cada vez que hagas push al main?
