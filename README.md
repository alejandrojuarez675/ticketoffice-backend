# Ticket Office Backend
## Como levantar la app localmente
Ejecutar los siguientes comandos en la terminal

```bash
./gradlew clean build
./gradlew bootRun
```

## Como crear la documentación

Para crear la documentación, se debe ejecutar el siguiente comando para levantar la aplicación:

```bash
./gradlew clean build
./gradlew bootRun
```

Y en otra terminal:
```bash
curl http://localhost:8080/v3/api-docs -o docs/api-docs.json
```

Para acceder a la documentación, abrir el archivo `docs/api-docs.json` en un navegador web. O simplemente acceder a la siguiente URL: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) cuando esté el app levantada.

Cada vez que se haga un push a la rama `main`, la documentación se actualizará automáticamente en la siguiente URL:
https://alejandrojuarez675.github.io/ticketoffice-backend/docs/ con lo que haya en el archivo `docs/api-docs.json` en la rama `main`.

## Flujo de trabajo

### Buyer
Para un buyer el camino es el siguiente:

1. GET /api/public/v1/events/search: devolvemos los eventos que se pueden comprar
2. GET /api/public/v1/events/{eventId}: devolvemos el detalle del evento
3. POST /api/public/v1/checkout/session: se reserva el stock de entradas
4. POST /api/public/v1/checkout/session/{sessionId}/buy: se compra las entradas

Luego del paso 4 el seller recibe un email de confirmación de la compra y luego cada uno de los
asistentes recibe un email con la entrada comprada y un QR que lo redirige a la página que tiene
el seller para validar la asistencia al evento y asegurarse que no entren dos personas con la
misma entrada.

### Seller

1. POST /auth/login: el seller se autentica
2. GET /api/v1/events: el seller ve todos sus eventos
3. POST /api/v1/events: el seller sube un evento
4. GET /api/v1/events/{eventId}/sales: el seller ve las ventas de un evento
5. POST /api/public/v1/checkout/session/{sessionId}/validate: el seller valida una entrada para marcarla como que ya ingreso al evento
