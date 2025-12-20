# Guía de Configuración de DynamoDB para Checkout Sessions

Esta guía te ayudará a configurar una tabla en DynamoDB para almacenar las sesiones de pago (checkout sessions) utilizando la consola web de AWS.

## 1. Iniciar sesión en la consola de AWS

1. Abre tu navegador y ve a [AWS Console](https://aws.amazon.com/console/)
2. Inicia sesión con tus credenciales de AWS
3. Asegúrate de estar en la región correcta (ejemplo: N. Virginia - us-east-1)

## 2. Crear una nueva tabla de DynamoDB

1. En la consola de AWS, busca "DynamoDB" en la barra de búsqueda superior
2. Haz clic en "Crear tabla"
3. Configura los siguientes parámetros:
   - **Nombre de la tabla**: `CheckoutSessions`
   - **Clave de partición (PK)**: `sessionId` (tipo String)
   - **Clave de ordenación (SK)**: `createdAt` (tipo String)
   - **Configuración predeterminada**: Selecciona "Personalizar configuración"

## 3. Configuración de capacidad

1. En "Configuración de capacidad":
   - **Modo de capacidad**: Aprovisionado
   - **Capacidad de escritura**: 5 (puedes ajustar según necesidades)
   - **Capacidad de lectura**: 5 (puedes ajustar según necesidades)
   - **Escalado automático**: Opcional, activa si necesitas escalado automático

## 4. Configuraciones adicionales

1. **Configuración de tabla**:
   - **Clave de ordenación (SK)**: `createdAt` (String)
   - **Configuración predeterminada**: Mantén los valores por defecto

2. **Índices globales secundarios (GSI)**:
   - Agrega un GSI con las siguientes características:
     - **Nombre del índice**: `UserIdIndex`
     - **Clave de partición**: `userId` (String)
     - **Clave de ordenación**: `createdAt` (String)
     - Proyecta todos los atributos

## 5. Configuración de cifrado

1. En la sección "Cifrado":
   - **Tipo de clave de cifrado**: Clave administrada por AWS (puedes elegir una clave personalizada si es necesario)

## 6. Etiquetas (opcional)

1. Agrega etiquetas para mejor organización:
   - **Clave**: `Environment`
   - **Valor**: `Production` (o el entorno correspondiente)

## 7. Revisar y crear

1. Revisa todas las configuraciones
2. Haz clic en "Crear tabla"

## 8. Configuración de TTL (Time To Live)

1. Una vez creada la tabla, ve a la pestaña "Tablas"
2. Selecciona tu tabla `CheckoutSessions`
3. Ve a la pestaña "TTL (Time To Live)"
4. Habilita el TTL
5. Establece el nombre del atributo: `expirationTime`
6. Haz clic en "Guardar configuraciones"

## 9. Configuración de alarmas (opcional pero recomendado)

1. Ve a CloudWatch en la consola de AWS
2. Crea una alarma para monitorear el uso de capacidad
3. Configura notificaciones SNS si es necesario

## 10. Configuración de IAM

Asegúrate de que el rol IAM que usa tu aplicación tenga los siguientes permisos para la tabla:

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
                "dynamodb:Scan"
            ],
            "Resource": [
                "arn:aws:dynamodb:REGION:ACCOUNT_ID:table/CheckoutSessions",
                "arn:aws:dynamodb:REGION:ACCOUNT_ID:table/CheckoutSessions/index/*"
            ]
        }
    ]
}
```

## Notas importantes

- Reemplaza `REGION` y `ACCOUNT_ID` con tus valores reales de AWS
- Ajusta las capacidades de lectura/escritura según el volumen de tráfico esperado
- Considera habilitar PITR (Point in Time Recovery) para copias de seguridad
- Configura políticas de retención adecuadas para los registros de DynamoDB Streams si los habilitas

## Solución de problemas

- Si ves errores de capacidad, verifica las métricas de CloudWatch
- Para problemas de rendimiento, considera agregar índices adicionales según tus patrones de consulta
- Asegúrate de que las credenciales de tu aplicación tengan los permisos necesarios