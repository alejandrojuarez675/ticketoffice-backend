# Implementation Plan: Integración de DynamoDB para Caché de Sesiones de Checkout

**Date**: 19/12/2025  
**Spec**: [specs/features/add-checkout-session-cache-dynamo-db-integration/specs.md](specs/features/add-checkout-session-cache-dynamo-db-integration/specs.md)

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

## Implementation Progress

### Phase 1: Foundation (Week 1)
- [ ] T001: Crear estructura de paquetes y clases base
- [ ] T002: Implementar CheckoutSessionDynamoDBMapper para conversión de modelos
- [ ] T003: Implementar CheckoutSessionDynamoDao con operaciones CRUD básicas
- [ ] T004: Configurar TTL en la tabla de DynamoDB
- [ ] T005: Actualizar DynamoDBModule para inyección de dependencias
- [ ] T006: Refactorizar CheckoutSessionIdUtils para simplificar la generación de IDs
- [ ] T007: Actualizar CheckoutSessionCache para eliminar el patrón de búsqueda innecesario
  - [ ] Eliminar método `countKeysMatches` que usa patrón de búsqueda
  - [ ] Actualizar implementaciones existentes (CheckoutSessionInMemoryCache)
  - [ ] Actualizar pruebas unitarias afectadas
  - [ ] Actualizar documentación de la interfaz

### T018: Creación de la tabla de DynamoDB
**Paso 1: Definir los atributos principales**
- `id` (String) - Clave de partición (HASH)
- `createdAt` (Number) - Timestamp de creación (para ordenamiento)
- `expiresAt` (Number) - Atributo para TTL (en segundos desde la época)
- `status` (String) - Estado actual de la sesión
- `eventId` (String) - ID del evento relacionado
- `priceId` (String) - ID del precio relacionado
- `quantity` (Number) - Cantidad de entradas
- `sessionData` (String) - Datos adicionales de la sesión en formato JSON

**Paso 3: Configuración de TTL**
- Habilitar TTL en el atributo `expiresAt`
- Tiempo de expiración: 24 horas después de la creación

**Paso 4: Comando AWS CLI para crear la tabla**
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

### Phase 2: Core Implementation (Week 2)
- [ ] T008: Refactorizar código que usa el patrón de búsqueda
  - [ ] Identificar y actualizar todos los usos de `countKeysMatches`
  - [ ] Implementar alternativas para las funcionalidades que usaban el patrón
  - [ ] Verificar que no haya dependencias ocultas en el código
  
- [ ] T009: Implementar CheckoutSessionDynamoRepository
  - [ ] Implementar operaciones CRUD básicas
  - [ ] Integrar con DynamoDB Mapper
  - [ ] Manejar conversión entre modelos de dominio y DAO
  
- [ ] T010: Implementar manejo de TTL
  - [ ] Configurar atributo TTL en la tabla de DynamoDB
  - [ ] Asegurar que las sesiones expiren correctamente
  - [ ] Documentar el comportamiento de expiración
  
- [ ] T011: Implementar manejo de reintentos con RetryUtil
  - [ ] Configurar RetryUtil para operaciones de DynamoDB
  - [ ] Manejar errores específicos de DynamoDB
  - [ ] Documentar la estrategia de reintentos implementada

### Phase 3: Testing & Documentation (Week 3)
- [ ] T012: Escribir pruebas unitarias para el mapeador
- [ ] T013: Escribir pruebas para el DAO
- [ ] T014: Escribir pruebas de integración para el repositorio
- [ ] T015: Probar el comportamiento de TTL
- [ ] T016: Documentar la implementación y configuración

### Phase 4: Code Cleanup & Simplification (Week 4)
- [ ] T017: Actualizar pruebas existentes para el nuevo diseño simplificado
- [ ] T018: Eliminar código obsoleto y refactorizar dependencias
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
