# Especificación: Integración de DynamoDB para Caché de Sesiones de Checkout

**Fecha de Creación**: 19/12/2025  
**Última Actualización**: 19/12/2025  
**Estado**: Borrador  

## Descripción General

Implementar un sistema de caché para sesiones de checkout utilizando Amazon DynamoDB con TTL (Time To Live) para el almacenamiento persistente de sesiones, manteniendo la compatibilidad con el caché en memoria para desarrollo local.

## Objetivos

- Almacenar datos de sesiones de checkout en DynamoDB con TTL en entornos de producción
- Mantener el uso del caché en memoria para desarrollo local
- Garantizar la eliminación automática de sesiones expiradas
- Mantener la consistencia de datos entre sesiones activas

## Requisitos Funcionales

### RF-001: Almacenamiento en DynamoDB con TTL
- El sistema debe almacenar las sesiones de checkout en DynamoDB en entornos de producción
- Cada sesión debe tener un tiempo de expiración configurable (por defecto 24 horas)
- Las sesiones expiradas deben ser eliminadas automáticamente por DynamoDB

### RF-002: Caché en Memoria
- En entorno local, el sistema debe usar el caché en memoria (`CheckoutSessionInMemoryCache`)
- No debe haber dependencia con servicios externos en desarrollo local

### RF-003: Gestión de Sesiones
- Crear nuevas sesiones de checkout con tiempo de expiración
- Obtener sesiones por ID
- Actualizar el estado de las sesiones
- Eliminar sesiones manualmente cuando sea necesario
- Contar sesiones por estado

### RF-004: Comportamiento de TTL
- Las sesiones deben expirar exactamente después del tiempo configurado
- No se debe renovar el TTL con interacciones posteriores
- No se deben enviar notificaciones cuando las sesiones expiran

## Requisitos No Funcionales

### RNF-001: Rendimiento
- La operación de lectura/escritura de sesiones debe ser de baja latencia (<100ms)
- El sistema debe manejar altas tasas de solicitudes concurrentes

### RNF-002: Escalabilidad
- La solución debe escalar automáticamente según la carga
- Debe manejar picos de tráfico sin degradación del servicio

### RNF-003: Disponibilidad
- La disponibilidad del servicio debe ser de al menos 99.9%
- Debe implementar estrategias de reintento para fallos transitorios

## Modelo de Datos

### Tabla: checkout-sessions
- **Clave de Partición (PK)**: id (String) - ID único de la sesión
- **Atributo TTL**: expiresAt (Number) - Marca de tiempo de expiración en segundos desde la época

**Atributos**:
- `id` (String): Identificador único de la sesión
- `eventId` (String): ID del evento relacionado
- `status` (String): Estado actual de la sesión (PENDING, COMPLETED, EXPIRED)
- `tickets` (List<Map>): Lista de tickets en la sesión
- `createdAt` (String): Fecha de creación (ISO-8601)
- `expiresAt` (Number): Tiempo de expiración en segundos desde la época (para TTL)
- `metadata` (Map): Metadatos adicionales de la sesión

### Configuración de TTL
- El atributo `expiresAt` será utilizado por DynamoDB para la eliminación automática
- El tiempo de expiración será fijo de 24 horas desde la creación
- No se renovará el TTL con interacciones posteriores
- Las sesiones expiradas serán eliminadas automáticamente por DynamoDB sin notificaciones

## Configuración Requerida

### Variables de Entorno

1. **Variables de Entorno del Sistema** (usando `System.getenv()`):
   - `AWS_REGION`: Región de AWS donde se encuentra la tabla de DynamoDB
   - `DYNAMODB_CHECKOUT_SESSIONS_TABLE`: Nombre de la tabla de sesiones (opcional, por defecto: 'checkout-sessions')
   - `CHECKOUT_SESSION_TTL_HOURS`: Tiempo de vida de las sesiones en horas (opcional, por defecto: 24)

2. **Propiedades del Sistema** (usando `System.getProperty()`):
   - `-Denvironment=local|prod`: Define el entorno de ejecución
   - `-Ddynamodb.endpoint`: Punto de conexión de DynamoDB (solo para desarrollo local)

### Configuración de Producción

En producción, las credenciales se manejan a través de IAM roles. La configuración mínima necesaria es:

```bash
export AWS_REGION=us-east-1
export DYNAMODB_CHECKOUT_SESSIONS_TABLE=checkout-sessions
export CHECKOUT_SESSION_TTL_HOURS=24
```

### Configuración de Desarrollo Local

Para desarrollo local, se utiliza el caché en memoria. No se requiere configuración adicional:

```bash
-Denvironment=local
```

## Criterios de Aceptación

### Funcionalidad Básica
- [ ] Las sesiones de checkout se almacenan correctamente en DynamoDB en producción
- [ ] El caché en memoria funciona correctamente en desarrollo local
- [ ] Las consultas por ID de sesión son eficientes
- [ ] Se manejan correctamente los errores de conexión

### TTL y Expiración
- [ ] Las sesiones expiran automáticamente después del tiempo configurado (24h por defecto)
- [ ] Las sesiones expiradas se eliminan automáticamente de DynamoDB
- [ ] No se envían notificaciones cuando las sesiones expiran
- [ ] El TTL no se renueva con interacciones posteriores

### Configuración
- [ ] La configuración del TTL es ajustable mediante variables de entorno
- [ ] La implementación funciona tanto en entornos locales como en producción
- [ ] La documentación está actualizada con las configuraciones necesarias

### Rendimiento
- [ ] La implementación maneja correctamente altas cargas de trabajo
- [ ] No hay impacto significativo en el rendimiento por el manejo del TTL

## Notas de Implementación

### Patrones y Referencias Existentes

1. **Estructura de Paquetes**
   - `com.ticketoffice.backend.domain.ports.CheckoutSessionCache` - Interfaz del caché de sesiones
   - `com.ticketoffice.backend.infra.adapters.out.cache.CheckoutSessionInMemoryCache` - Implementación en memoria
   - `com.ticketoffice.backend.infra.adapters.out.db.repository` - Repositorios
   - `com.ticketoffice.backend.infra.adapters.out.db.dao` - DAOs para acceso a DynamoDB
   - `com.ticketoffice.backend.infra.adapters.out.db.mapper` - Mapeadores entre modelos de dominio y DynamoDB

2. **Componentes de Referencia**
   - `CheckoutSessionInMemoryCache` - Implementación actual del caché en memoria
   - `AbstractDynamoDao` - Clase base para DAOs de DynamoDB

3. **Patrones a Seguir**
   - Implementar el patrón Repository para el acceso a datos
   - Usar el SDK v2 de AWS para Java
   - Implementar patrones de reintento con backoff exponencial
   - Usar mapeo de objetos con DynamoDB Enhanced Client

4. **Consideraciones de TTL**
   - El TTL en DynamoDB puede tener una latencia de hasta 48 horas para la eliminación
   - Las operaciones de lectura no extienden el TTL automáticamente
   - No hay notificaciones cuando los elementos expiran

5. **Pruebas**
   - Pruebas unitarias para la lógica de negocio
   - Pruebas de integración con DynamoDB Local
   - Pruebas de rendimiento para validar el comportamiento bajo carga

## Plan de Implementación

1. Crear la interfaz `CheckoutSessionCache` con los métodos necesarios
2. Implementar `CheckoutSessionDynamoRepository` que implemente la interfaz
3. Crear `CheckoutSessionDynamoDBMapper` para el mapeo de objetos
4. Implementar `CheckoutSessionDynamoDao` para el acceso a DynamoDB
5. Crear `DynamoDBModule` para la configuración de Guice
6. Actualizar `AppModule` para usar la implementación adecuada según el entorno
7. Escribir pruebas unitarias y de integración
8. Documentar la configuración y uso
