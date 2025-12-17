# Especificación: Integración de DynamoDB para Gestión de Ventas

**Fecha de Creación**: 17/12/2025  
**Última Actualización**: 17/12/2025  
**Estado**: Borrador  

## Descripción General

Implementar la integración con Amazon DynamoDB para el almacenamiento persistente de ventas, manteniendo la compatibilidad con el repositorio en memoria para desarrollo local.

## Objetivos

- Almacenar datos de ventas en DynamoDB en entornos de producción
- Mantener el uso del repositorio en memoria para desarrollo local
- Garantizar la transparencia en el cambio entre repositorios
- Mantener la consistencia de datos

## Requisitos Funcionales

### RF-001: Almacenamiento en DynamoDB
- El sistema debe almacenar las ventas en DynamoDB en entornos de producción
- Debe manejar correctamente las operaciones CRUD
- Debe implementar reintentos para operaciones fallidas

### RF-002: Repositorio en Memoria
- En entorno local, el sistema debe usar el repositorio en memoria
- No debe haber dependencia con servicios externos en desarrollo local

### RF-003: Consultas por Evento
- Debe permitir consultas eficientes de ventas por ID de evento
- Debe soportar paginación para grandes volúmenes de datos

### RF-004: Conteo de Ventas por Ticket y Evento
- El sistema debe permitir contar la cantidad de ventas para un ticket específico en un evento determinado
- La operación debe ser eficiente incluso con grandes volúmenes de datos
- Debe soportar filtros adicionales para refinar la búsqueda
- El conteo debe ser consistente con los datos almacenados

### RF-005: Actualización de Ventas
- El sistema debe permitir actualizar las ventas existentes
- Debe manejar correctamente las operaciones de actualización
- Debe implementar reintentos para operaciones fallidas

### RF-006: Eliminación de Ventas
- El sistema debe permitir eliminar las ventas existentes
- Debe manejar correctamente las operaciones de eliminación
- Debe implementar reintentos para operaciones fallidas

## Requisitos No Funcionales

### RNF-002: Escalabilidad
- La solución debe escalar automáticamente según la carga
- Debe manejar al menos 1000 operaciones por segundo

### RNF-003: Disponibilidad
- La disponibilidad del servicio debe ser de al menos 99.9%
- Debe implementar estrategias de reintento para fallos transitorios

## Modelo de Datos

### Tabla: sales
- **Clave de Partición (PK)**: id (String)
- **Índices Secundarios Globales**:
  - **GSI1 (eventId-index)**:
    - Clave de Partición: eventId (String)
    - Clave de Ordenación: id (String)
    - Proyección: ALL
  - **GSI2 (event-ticket-index)**:
    - Clave de Partición: eventId#ticketId (String) - Clave compuesta
    - Clave de Ordenación: id (String)
    - Proyección: KEYS_ONLY (para optimizar el conteo)
- **Atributos**:
  - id (String) - Identificador único de la venta
  - eventId (String) - ID del evento
  - ticketId (String) - ID del ticket
  - quantity (Number) - Cantidad de tickets vendidos
  - price (Number) - Precio total de la venta
  - buyer (List<Map>) - Información de los compradores
  - mainEmail (String) - Email principal de la compra
  - validated (Boolean) - Indica si la venta ha sido validada
  - createdAt (String) - Fecha de creación (ISO-8601)
  - updatedAt (String) - Fecha de última actualización (ISO-8601)
  - eventId_ticketId (String) - Atributo derivado para GSI2 (eventId#ticketId)

## Configuración Requerida

### Variables de Entorno

El proyecto sigue las siguientes convenciones para la configuración:

1. **Variables de Entorno del Sistema** (usando `System.getenv()`):
   - `AWS_REGION`: Región de AWS donde se encuentra la tabla de DynamoDB
   - `DYNAMODB_TABLE_NAME`: Nombre de la tabla de DynamoDB (opcional, por defecto: 'sales')

2. **Propiedades del Sistema** (usando `System.getProperty()`):
   - `-Denvironment=local|prod`: Define el entorno de ejecución

### Configuración Requerida

En producción, las credenciales se manejan a través de IAM roles. La configuración mínima necesaria es:

```bash
export AWS_REGION=us-east-1
export DYNAMODB_TABLE_NAME=sales
```

### Módulo de Configuración

La configuración se inyecta a través de módulos de Guice, siguiendo el patrón establecido en `SesModule` y otros módulos existentes. El módulo para DynamoDB deberá:

1. Leer las variables de entorno necesarias
2. Proporcionar valores por defecto cuando corresponda
3. Lanzar excepciones claras si falta configuración requerida
4. Configurar el cliente de DynamoDB con la región apropiada

## Pruebas

### Pruebas Unitarias
- Deben cubrir al menos el 80% del código
- Deben incluir pruebas para casos de error

### Pruebas de Integración
- Pruebas con DB en memoria

## Criterios de Aceptación

- [ ] Las ventas se almacenan correctamente en DynamoDB en producción
- [ ] El repositorio en memoria funciona correctamente en desarrollo local
- [ ] Las consultas por ID de evento son eficientes
- [ ] Se manejan correctamente los errores de conexión
- [ ] La documentación está actualizada

## Notas de Implementación

### Patrones y Referencias Existentes

1. **Estructura de Paquetes**
   - Seguir la estructura de paquetes existente:
     - `com.ticketoffice.backend.infra.adapters.out.db.repository` - Repositorios
     - `com.ticketoffice.backend.infra.adapters.out.db.dao` - DAOs para acceso a DynamoDB
     - `com.ticketoffice.backend.infra.adapters.out.db.mapper` - Mapeadores entre modelos de dominio y DynamoDB

2. **Componentes de Referencia**
   - `EventDynamoRepository` - Implementación de repositorio para eventos
   - `AbstractDynamoDao` - Clase base para DAOs de DynamoDB
   - `EventDynamoDBMapper` - Ejemplo de mapeo de objetos a/desde DynamoDB

3. **Patrones a Seguir**
   - Usar el patrón DAO para el acceso a datos
   - Implementar mapeadores específicos para la conversión entre modelos
   - Usar el SDK v2 de AWS para Java
   - Implementar patrones de reintento con backoff exponencial
   - Usar mapeo de objetos con DynamoDB Enhanced Client

4. **Configuración**
   - Revisar `dynamo-policy.json` para los permisos necesarios
   - Configurar métricas de CloudWatch para monitoreo
   - Seguir las convenciones de nomenclatura existentes para tablas e índices

5. **Pruebas**
   - Seguir el patrón de pruebas existente
   - Usar el repositorio en memoria para pruebas unitarias
   - Implementar pruebas de integración con DynamoDB Local

## Referencias

- [Documentación de DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Introduction.html)
- [SDK de AWS para Java v2](https://docs.aws.amazon.com/sdk-for-java/latest/developer-guide/home.html)
- [Mejores Prácticas de DynamoDB](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/best-practices.html)
