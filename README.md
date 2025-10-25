# 🎟️ Ticket Office Backend

Backend para la gestión de venta y validación de entradas para eventos. Este proyecto permite a los organizadores de eventos (sellers) gestionar sus eventos y a los compradores (buyers) adquirir entradas de forma segura.

## 🚀 Tecnologías principales

- **Java 21** - Lenguaje de programación
- **Javalin** - Framework web ligero
- **Guice** - Inyección de dependencias
- **AWS Lambda** - Ejecución serverless
- **JWT** - Autenticación
- **Jackson** - Procesamiento JSON
- **Gradle** - Gestión de dependencias

## ⚙️ Configuración del entorno

### Requisitos previos

- Java 21 o superior
- Gradle 8.0 o superior
- AWS CLI (para despliegue)
- Docker (opcional, para ejecución local)

## 🏃‍♂️ Ejecución local

### Opción 1: Usando Gradle

```bash
# Construir el proyecto
./gradlew clean build

# Ejecutar la aplicación
./gradlew run
```

La aplicación estará disponible en: `http://localhost:8080`

### Opción 2: Usando Docker

```bash
# Construir la imagen
./gradlew shadowJar
docker build -t ticketoffice-backend .

# Ejecutar el contenedor
docker run -p 8080:8080 ticketoffice-backend
```

## 📚 Documentación de la API

La documentación interactiva de la API está disponible en:
[Ver documentación en línea](https://alejandrojuarez675.github.io/ticketoffice-backend/)

## 🚀 Despliegue

### Despliegue en AWS

1. Construir el archivo JAR con dependencias:
   ```bash
   ./gradlew shadowJar
   ```

2. El archivo se generará en: `build/libs/ticketoffice-backend.jar`

3. Sigue las instrucciones en `plan/deploy-aws-app-runner.md` para el despliegue en AWS App Runner.

## 🔄 Flujos de trabajo

### Para compradores (Buyers)

1. **Explorar eventos disponibles**
   ```
   GET /api/public/v1/events/search
   ```

2. **Ver detalles de un evento**
   ```
   GET /api/public/v1/events/{eventId}
   ```

3. **Iniciar proceso de compra**
   ```
   POST /api/public/v1/checkout/session
   ```

4. **Confirmar compra**
   ```
   POST /api/public/v1/checkout/session/{sessionId}/buy
   ```

### Para vendedores (Sellers)

1. **Autenticación**
   ```
   POST /auth/login
   ```

2. **Gestionar eventos**
   ```
   GET /api/v1/events          # Listar eventos
   POST /api/v1/events         # Crear evento
   GET /api/v1/events/{id}     # Ver detalle
   PUT /api/v1/events/{id}     # Actualizar
   DELETE /api/v1/events/{id}  # Eliminar
   ```

3. **Gestionar ventas**
   ```
   GET /api/v1/events/{eventId}/sales  # Ver ventas
   ```

4. **Validar entradas**
   ```
   POST /api/public/v1/checkout/session/{sessionId}/validate
   ```

## 📧 Notificaciones

- Los vendedores reciben un correo de confirmación por cada venta
- Los compradores reciben sus entradas por correo con códigos QR
- Las validaciones de entrada generan notificaciones en tiempo real

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Ver el archivo `LICENSE` para más detalles.
