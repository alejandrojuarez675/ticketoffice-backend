# Plan de Implementación: Integración de DynamoDB para Caché de Sesiones de Checkout

**Fecha**: 19/12/2025  
**Especificación**: [Especificación detallada](./specs.md)

## Summary

Implementar un sistema de caché para sesiones de checkout utilizando Amazon DynamoDB con TTL (Time To Live) para el almacenamiento persistente de sesiones, manteniendo la compatibilidad con el caché en memoria para desarrollo local.

## Technical Context

**Language/Version**: Java 21  
**Primary Dependencies**:
- AWS SDK for Java 2.x
- Google Guice (Dependency Injection)
- JUnit 5 (Pruebas)

**Design Changes**:
- Se eliminará el patrón de búsqueda basado en patrones en `CheckoutSessionCache`
- Se simplificará la generación de IDs de sesión en `CheckoutSessionIdUtils`
- Se implementará un diseño más directo para las operaciones de caché
- Se mantendrá la compatibilidad con el caché en memoria para desarrollo local

**Consideraciones descartadas**:
- No se implementarán métricas de monitoreo
- No se aplicarán políticas de retención de logs específicas
- No se requiere migración de datos desde el caché en memoria

**Storage**: Amazon DynamoDB con TTL  
**Testing**:
- Pruebas unitarias con JUnit 5
- Pruebas de integración con DynamoDB Local
- Mockito para simulación de dependencias

**Target Platform**: AWS Lambda / ECS  
**Project Type**: Backend Service  
**Performance Goals**:
- Tiempo de respuesta < 100ms para operaciones de caché
- Soporte para al menos 1000 operaciones por segundo

**Constraints**:
- Mantener compatibilidad con el caché en memoria existente
- Implementar TTL fijo de 24 horas sin renovación
- No enviar notificaciones al expirar sesiones

**Scale/Scope**:
- Tabla de DynamoDB con partición por ID de sesión
- TTL automático basado en el atributo expiresAt

## Project Structure

### Documentation

```
specs/features/add-checkout-session-cache-dynamo-db-integration/
├── plan.md              # Este archivo
└── specs.md             # Especificación detallada
```

### Source Code

```
src/main/java/com/ticketoffice/backend/
├── domain/ports/
│   └── CheckoutSessionCache.java  # Interfaz existente
│
├── infra/adapters/out/cache/
│   ├── CheckoutSessionInMemoryCache.java  # Implementación existente
│   └── dynamo/
│       ├── CheckoutSessionDynamoRepository.java  # Nueva implementación
│       ├── dao/
│       │   └── CheckoutSessionDynamoDao.java     # DAO para DynamoDB
│       └── mapper/
│           └── CheckoutSessionDynamoDBMapper.java # Mapeo de modelos
│
└── config/
    └── DynamoDBModule.java     # Configuración de Guice para DynamoDB

src/test/java/com/ticketoffice/backend/
└── infra/adapters/out/cache/dynamo/
    ├── CheckoutSessionDynamoRepositoryTest.java
    ├── dao/
    │   └── CheckoutSessionDynamoDaoTest.java
    └── mapper/
        └── CheckoutSessionDynamoDBMapperTest.java
```

## Progreso de la Implementación

## Fase 1: Fundamentos (Semana 1)
- [x] **T001**: Crear estructura de paquetes y clases base
  - [x] Crear paquete `infra.adapters.out.cache.dynamo`
  - [x] Crear clases base: 
    - `CheckoutSessionDynamoRepository`
    - `CheckoutSessionDynamoDao`
    - `CheckoutSessionDynamoDBMapper`
  - [x] Configurar herencia de `AbstractDynamoDao`

- [x] **T002**: Implementar CheckoutSessionDynamoDBMapper para conversión de modelos
  - [x] Implementar mapeo a/desde `Map<String, AttributeValue>`
  - [x] Definir constantes para nombres de atributos
  - [x] Manejar conversión de tipos y valores nulos

- [x] **T003**: Implementar CheckoutSessionDynamoDao con operaciones CRUD básicas
  - [x] Implementar `countByEventIdAndTicketId`
  - [x] Implementar `getById`
  - [x] Implementar `save`
  - [x] Implementar `deleteById`
  - [x] Configurar índices secundarios (`EventIdTicketIdIndex`)

- [x] **T004**: Configurar TTL en la tabla de DynamoDB
  - [x] Asegurar que el modelo `CheckoutSession` tenga el campo `expiresAt`
  - [x] Actualizar `CheckoutSessionDynamoDBMapper` para mapear `expiresAt`
  - [ ] Configurar TTL en la tabla de DynamoDB
  - [ ] Probar la eliminación automática de registros

  **Pasos detallados para configurar TTL:**

  1. **Verificar/Agregar el campo `expiresAt`**
     - El campo debe ser de tipo `Long` (timestamp en segundos)
     - Se debe establecer al crear/actualizar una sesión
     - Ejemplo: `Instant.now().plus(24, HOURS).getEpochSecond()`

  2. **Actualizar el mapeo en `CheckoutSessionDynamoDBMapper`**
     ```java
     // En el metodo toMap
     map.put(DynamoKeys.EXPIRES_AT, AttributeValue.builder()
         .n(String.valueOf(checkoutSession.getExpiresAt()))
         .build());
     
     // En el metodo fromMap
     .expiresAt(Long.parseLong(item.get(DynamoKeys.EXPIRES_AT).n()))
     ```

  3. **Configurar TTL en AWS DynamoDB**
     ```bash
     # Habilitar TTL en la tabla existente
     aws dynamodb update-time-to-live \
         --table-name CheckoutSessions \
         --time-to-live-specification "Enabled=true, AttributeName=expiresAt"
     
     # Verificar la configuración
     aws dynamodb describe-time-to-live --table-name CheckoutSessions
     ```

  4. **Probar la funcionalidad TTL**
     - Crear un ítem con `expiresAt` a 5 minutos en el futuro
     - Verificar que el ítem existe
     - Esperar 5+ minutos
     - Verificar que el ítem fue eliminado

  5. **Consideraciones importantes**
     - El borrado puede tardar hasta 48 horas
     - Los ítems se eliminan en segundo plano sin costo
     - No hay garantía de eliminación inmediata
     - Los ítems expirados no aparecen en consultas ni escaneos
     - Se pueden monitorear las métricas de TTL en CloudWatch
- [x] **T007**: Actualizar CheckoutSessionCache para eliminar el patrón de búsqueda innecesario
  - [x] Eliminar método `countKeysMatches` que usa patrón de búsqueda
  - [x] Actualizar implementaciones existentes (`CheckoutSessionInMemoryCache`)
  - [x] Actualizar pruebas unitarias afectadas
  - [x] Actualizar documentación de la interfaz

## T018: Creación de la tabla de DynamoDB

### Paso 1: Definir los atributos principales

| Atributo     | Tipo    | Descripción                                      |
|--------------|---------|--------------------------------------------------|
| `id`         | String  | Clave de partición (HASH)                       |
| `createdAt`  | Number  | Timestamp de creación (para ordenamiento)       |
| `expiresAt`  | Number  | Atributo para TTL (en segundos desde la época)  |
| `status`     | String  | Estado actual de la sesión                      |
| `eventId`    | String  | ID del evento relacionado                       |
| `priceId`    | String  | ID del precio relacionado                       |
| `quantity`   | Number  | Cantidad de entradas                            |
| `sessionData`| String  | Datos adicionales de la sesión en formato JSON  |

### Paso 3: Configuración de TTL

- [ ] Habilitar TTL en el atributo `expiresAt`
- [ ] Establecer tiempo de expiración: 24 horas después de la creación

### Paso 4: Comando AWS CLI para crear la tabla

```bash
aws dynamodb create-table \
    --table-name CheckoutSessions \
    --attribute-definitions \
        AttributeName=id,AttributeType=S \
        AttributeName=eventId,AttributeType=S \
        AttributeName=ticketId,AttributeType=S \
    --key-schema \
        AttributeName=id,KeyType=HASH \
    --global-secondary-indexes \
        '[
            {
                "IndexName": "EventIdTicketIdIndex",
                "KeySchema": [
                    {"AttributeName": "eventId", "KeyType": "HASH"},
                    {"AttributeName": "ticketId", "KeyType": "RANGE"}
                ],
                "Projection": {
                    "ProjectionType": "ALL"
                },
                "ProvisionedThroughput": {
                    "ReadCapacityUnits": 5,
                    "WriteCapacityUnits": 5
                }
            }
        ]' \
    --billing-mode PAY_PER_REQUEST
        AttributeName=id,KeyType=HASH \
    --billing-mode PAY_PER_REQUEST \
    --tags \
        Key=Environment,Value=production \
        Key=Project,Value=TicketOffice
```

**Paso 5: Configurar TTL**
```bash
aws dynamodb update-time-to-live \
    --table-name CheckoutSessions \
    --time-to-live-specification "Enabled=true, AttributeName=expiresAt"
```

**Paso 6: Verificación**
1. Confirmar que la tabla se creó correctamente:
   ```bash
   aws dynamodb describe-table --table-name CheckoutSessions
   ```
2. Verificar configuración de TTL:
   ```bash
   aws dynamodb describe-time-to-live --table-name CheckoutSessions
   ```
3. Realizar una prueba de escritura y lectura

**Consideraciones adicionales**
- Asegurarse de que el usuario IAM tenga los permisos necesarios
- Configurar copias de seguridad automáticas si es necesario
- Establecer políticas de retención de backups
- Configurar alarmas de CloudWatch para métricas importantes

## Fase 2: Implementación Principal (Semana 2)
- [x] **T008**: Refactorizar código que usa el patrón de búsqueda
  - [x] Identificar y actualizar todos los usos de `countKeysMatches`
  - [x] Implementar alternativas para las funcionalidades que usaban el patrón
  - [x] Verificar que no haya dependencias ocultas en el código
  
- [x] **T009**: Implementar CheckoutSessionDynamoRepository
  - [x] Implementar operaciones CRUD básicas
  - [x] Integrar con DynamoDB Mapper
  - [x] Manejar conversión entre modelos de dominio y DAO
  
- [x] **T010**: Implementar manejo de TTL
  - [x] Configurar el mapeo del campo `expiresAt`
  - [x] Implementar lógica para establecer TTL en las operaciones de guardado
  - [ ] Probar la expiración automática de registros
  
- [x] T011: Implementar manejo de reintentos con RetryUtil
  - [x] Configurar RetryUtil para operaciones de DynamoDB
  - [x] Manejar errores específicos de DynamoDB
  - [x] Documentar la estrategia de reintentos implementada

### Phase 3: Testing & Documentation (Week 3)
- [x] T012: Escribir pruebas unitarias para el mapeador
- [x] T013: Escribir pruebas para el DAO
- [x] T014: Escribir pruebas de integración para el repositorio
- [x] T015: Probar el comportamiento de TTL
- [x] T016: Documentar la implementación y configuración

### Phase 4: Code Cleanup & Simplification (Week 4)
- [x] T017: Actualizar pruebas existentes para el nuevo diseño simplificado
- [x] T018: Eliminar código obsoleto y refactorizar dependencias
- [x] T019: Documentar los cambios en la API
- [ ] T019: Documentar los cambios en la API

## User Stories

### User Story 1: Almacenamiento en DynamoDB con TTL
**As a** administrador del sistema  
**I want** que las sesiones de checkout se almacenen en DynamoDB con TTL  
**So that** los datos expiren automáticamente

**Acceptance Criteria**:
- Las sesiones se guardan correctamente en DynamoDB
- El TTL está configurado correctamente (24h por defecto)
- Las sesiones expiradas se eliminan automáticamente

**Independent Test**: Verificar que una sesión expirada no se puede recuperar

### User Story 2: Caché en Memoria para Desarrollo
**As a** desarrollador  
**I want** usar un caché en memoria en desarrollo local  
**So that** no necesite conexión a DynamoDB

**Acceptance Criteria**:
- El sistema usa CheckoutSessionInMemoryCache en entorno local
- No hay dependencias externas en desarrollo
- El comportamiento es consistente con la implementación de producción

**Independent Test**: Verificar que el caché en memoria funciona sin conexión

### User Story 3: Gestión de Sesiones
**As a** usuario del sistema  
**I want** poder gestionar sesiones de checkout  
**So that** pueda completar compras de manera eficiente

**Acceptance Criteria**:
- Crear nuevas sesiones con tiempo de expiración
- Obtener sesiones por ID
- Actualizar estado de sesiones
- Contar sesiones por estado

**Independent Test**: Verificar el ciclo de vida completo de una sesión

## Risks & Mitigations

| Risk | Impact | Likelihood | Mitigation |
|------|--------|------------|------------|
| Latencia en operaciones de TTL | Medio | Bajo | Monitorear y ajustar capacidad según sea necesario |
| Inconsistencia en entornos | Alto | Medio | Mantener pruebas de integración consistentes |
| Pérdida de sesiones | Crítico | Bajo | Implementar estrategias de reintento y logging |

## Dependencies
- AWS SDK for Java 2.x
- Google Guice
- JUnit 5
- Mockito
- DynamoDB Local (para pruebas)

## Open Questions
- ¿Se necesita implementar algún tipo de métricas para monitorear el uso del caché?
- ¿Qué políticas de retención de logs se deben aplicar para las operaciones de caché?
- ¿Se necesita soporte para migración de datos desde el caché en memoria?

## Metrics & Monitoring
- Número de operaciones de caché por segundo
- Tasa de aciertos/fallos
- Tiempo de respuesta promedio
- Uso de capacidad de DynamoDB
- Número de sesiones activas

## Rollback Plan
1. Revertir el despliegue a la versión anterior
2. Verificar que el sistema funciona correctamente con el caché en memoria
3. Monitorear métricas críticas para asegurar estabilidad
