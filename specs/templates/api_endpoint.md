# [Nombre del Endpoint]

## Descripción
[Breve descripción del propósito del endpoint]

## URL
`[Método HTTP] /ruta/del/endpoint`

## Parámetros

### Query Parameters
| Nombre | Tipo | Requerido | Descripción |
|--------|------|-----------|-------------|
| param1 | tipo | Sí/No     | Descripción |

### Path Parameters
| Nombre | Tipo   | Requerido | Descripción |
|--------|--------|-----------|-------------|
| id     | string | Sí        | ID del recurso |

### Request Body
```json
{
  "campo1": "valor",
  "campo2": 123
}
```

## Respuestas

### 200 OK
```json
{
  "data": {},
  "message": "Mensaje de éxito"
}
```

### 400 Bad Request
```json
{
  "error": "Descripción del error"
}
```

## Ejemplo de Uso
```http
GET /api/v1/endpoint?param1=valor
```

## Notas
- [Cualquier nota adicional o consideración importante]
- [Restricciones de seguridad, límites de tasa, etc.]
