# Health Check Endpoint

## Descripción
Endpoint de verificación del estado del servicio. Se utiliza para monitorear la disponibilidad de la API.

## URL
`GET /ping`

## Parámetros
Ninguno

## Respuestas

### 200 OK
El servicio está funcionando correctamente. Devuelve el texto plano "pong" como respuesta.

```
pong
```

### Ejemplo de Uso
```http
GET /ping
```

## Notas
- Este endpoint no requiere autenticación
- Debe responder en menos de 100ms
- Útil para health checks de balanceadores de carga y monitoreo
- La respuesta es un texto plano (text/plain) con el contenido "pong"
