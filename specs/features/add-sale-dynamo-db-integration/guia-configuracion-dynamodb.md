# Guía: Configuración de DynamoDB para el Módulo de Ventas

## Requisitos Previos
1. Tener una cuenta de AWS con los permisos necesarios para crear y administrar tablas de DynamoDB
2. Tener instalado y configurado AWS CLI con credenciales válidas
3. Tener los permisos de IAM necesarios para crear tablas, índices y políticas

## Pasos para Crear la Tabla de DynamoDB

### 1. Iniciar Sesión en la Consola de AWS
1. Accede a la [Consola de AWS](https://console.aws.amazon.com/)
2. Navega al servicio DynamoDB

### 2. Crear una Nueva Tabla
1. Haz clic en "Create table"
2. Configura los parámetros básicos:
   - **Table name**: `SalesTable`
   - **Partition key**: `saleId` (String)
   - **Sort key**: No es necesario para esta implementación
   - Configuración:
     - **Table settings**: Usar configuración predeterminada
     - **Capacity mode**: Provisioned (con autoescalado recomendado)

### 3. Configurar Índices Secundarios
Crea los siguientes índices secundarios para soportar las consultas requeridas:

#### Índice para Búsqueda por Evento
- **Index name**: `EventIdIndex`
- **Partition key**: `eventId` (String)
- **Projected attributes**: ALL

#### Índice para Búsqueda por Evento y Ticket
- **Index name**: `EventIdTicketIdIndex`
- **Partition key**: `eventId` (String)
- **Sort key**: `ticketId` (String)
- **Projected attributes**: ALL

### 4. Configurar Autoescalado (Recomendado)
1. En la pestaña "Additional settings":
   - Habilita "Auto Scaling" para la capacidad de lectura/escritura
   - Establece los siguientes parámetros:
     - **Mínimo**: 5 unidades de capacidad
     - **Máximo**: 100 unidades de capacidad
     - **Uso objetivo**: 70%

### 5. Configurar Encriptación
1. En la sección "Encryption":
   - Habilita la encriptación en reposo
   - Selecciona "AWS owned key" (o una clave KMS específica si es requerida)

### 6. Configurar TTL (Time To Live)
1. En la pestaña "Additional settings":
   - Habilita TTL
   - Establece el atributo `expirationTime` para la expiración automática de registros

### 7. Configuración de Seguridad
1. Crea una política de IAM para acceder a la tabla:
   ```json
   {
       "Version": "2012-10-17",
       "Statement": [
           {
               "Effect": "Allow",
               "Action": [
                   "dynamodb:GetItem",
                   "dynamodb:PutItem",
                   "dynamodb:UpdateItem",
                   "dynamodb:DeleteItem",
                   "dynamodb:Query",
                   "dynamodb:Scan",
                   "dynamodb:BatchGetItem",
                   "dynamodb:BatchWriteItem"
               ],
               "Resource": [
                   "arn:aws:dynamodb:REGION:ACCOUNT_ID:table/SalesTable",
                   "arn:aws:dynamodb:REGION:ACCOUNT_ID:table/SalesTable/index/*"
               ]
           }
       ]
   }
   ```

### 8. Configuración de Tags (Opcional)
1. Agrega tags para mejor gestión:
   - `Environment`: `production` (o `staging`/`development`)
   - `Project`: `ticketoffice`
   - `Module`: `sales`

### 9. Revisar y Crear
1. Revisa la configuración
2. Haz clic en "Create table"

## Configuración de la Aplicación
1. Configura las variables de entorno necesarias en tu aplicación:
   ```
   AWS_REGION=tu-region
   DYNAMODB_TABLE_NAME=SalesTable
   ```

## Pruebas
1. Verifica la conexión a la tabla
2. Prueba las operaciones CRUD básicas
3. Valida el rendimiento de las consultas

## Mantenimiento
- Monitorea el uso de capacidad y ajusta según sea necesario
- Revisa regularmente las métricas de rendimiento
- Implementa copias de seguridad periódicas

## Solución de Problemas Comunes
- **Acceso denegado**: Verifica los permisos de IAM
- **Límites de capacidad**: Ajusta las unidades de capacidad o habilita autoescalado
- **Latencia alta**: Revisa la ubicación de la tabla vs. la aplicación
