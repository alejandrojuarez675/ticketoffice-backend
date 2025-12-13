# Persistencia de Datos

## Visión General

El sistema utiliza Amazon DynamoDB como base de datos principal, siguiendo un patrón de diseño de repositorio para abstraer el acceso a los datos.

## Estructura de Tablas

### 1. `users`
```
{
  "userId": "usuario123",
  "email": "usuario@ejemplo.com",
  "passwordHash": "$2a$10$N9qo8uLOickgx2ZMRZoMy...",
  "name": "Nombre Usuario",
  "role": "USER",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z"
}
```

### 2. `events`
```
{
  "eventId": "evento123",
  "organizerId": "organizador456",
  "title": "Concierto de Ejemplo",
  "description": "Descripción del evento...",
  "dateTime": "2023-12-31T20:00:00Z",
  "location": "Estadio Ejemplo",
  "totalTickets": 1000,
  "availableTickets": 500,
  "price": 99.99,
  "status": "ACTIVE",
  "createdAt": "2023-01-01T00:00:00Z",
  "updatedAt": "2023-01-01T00:00:00Z"
}
```

### 3. `tickets`
```
{
  "ticketId": "ticket789",
  "eventId": "evento123",
  "userId": "usuario123",
  "purchaseDate": "2023-01-15T14:30:00Z",
  "status": "PURCHASED",
  "qrCode": "base64encodedqrcode",
  "pricePaid": 99.99
}
```

## Patrón Repositorio

Cada entidad principal tiene su propio repositorio que implementa operaciones CRUD básicas:

```java
public interface Repository<T, ID> {
    Optional<T> findById(ID id);
    List<T> findAll();
    T save(T entity);
    void delete(ID id);
    boolean existsById(ID id);
}
```

## Estrategias de Consulta

1. **Consultas por Clave de Partición**: Para búsquedas rápidas
2. **Índices Secundarios Globales (GSI)**: Para consultas frecuentes
3. **Escaneo de Tabla**: Solo cuando sea necesario, ya que es menos eficiente

## Manejo de Transacciones

- Operaciones atómicas para actualizaciones críticas
- Patrón Unit of Work para operaciones complejas
- Manejo de concurrencia optimista

## Caché

- Redis para caché de consultas frecuentes
- Invalidation por tiempo (TTL) o eventos
- Estrategia Cache-Aside

## Migraciones

- Scripts de migración versionados
- Compatibilidad con versiones anteriores
- Rollback automático en caso de fallo

## Backup y Recuperación

- Backups automáticos diarios
- Retención de 30 días
- Pruebas de recuperación periódicas

## Rendimiento

- Particionamiento adecuado de las tablas
- Capacidad aprovisionada según demanda
- Monitoreo de métricas de rendimiento

## Seguridad

- Encriptación en tránsito (TLS)
- Encriptación en reposo
- Control de acceso basado en roles (RBAC)

## Consideraciones de Escalabilidad

- Escalado horizontal automático
- Particionamiento de datos para alta disponibilidad
- Replicación entre regiones para disponibilidad global
