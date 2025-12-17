
# Integración paso a paso: Amazon DynamoDB con tu proyecto Java (Javalin)

**Objetivo:** conectar tu app Java + Javalin (corriendo en AWS — EC2 / App Runner) a **Amazon DynamoDB** (NoSQL, on‑demand) usando **AWS CLI** y el SDK de AWS para Java.

---

## Requisitos previos
- AWS CLI instalado y configurado (`aws configure`) con un perfil que tenga permisos para crear recursos (o usa `--profile` en los comandos).
- Java 11+ y tu proyecto Javalin listo.
- Gradle o Maven (ejemplos de dependencias más abajo).
- Cuenta AWS y permisos para DynamoDB e IAM.
- (Opcional) `jq` para facilitar el manejo de JSON en la terminal.

---

## 1) Crear la tabla DynamoDB (modo on‑demand) — CLI

Este ejemplo crea:
- `MyTable` con `id` como **Primary Key** (partition key).
- Un **Global Secondary Index (GSI)** llamado `status-index` para consultar por `status` = `"active"`.

Guarda esto en un archivo `create-table.json`:

```json
{
  "TableName": "MyTable",
  "AttributeDefinitions": [
    { "AttributeName": "id", "AttributeType": "S" },
    { "AttributeName": "status", "AttributeType": "S" }
  ],
  "KeySchema": [
    { "AttributeName": "id", "KeyType": "HASH" }
  ],
  "BillingMode": "PAY_PER_REQUEST",
  "GlobalSecondaryIndexes": [
    {
      "IndexName": "status-index",
      "KeySchema": [
        { "AttributeName": "status", "KeyType": "HASH" }
      ],
      "Projection": {
        "ProjectionType": "ALL"
      }
    }
  ]
}
```

Comando CLI para crear la tabla (ejecutar en la carpeta donde guardaste `create-table.json`):

```bash
aws dynamodb create-table --cli-input-json file://create-table.json --region us-east-1
```

Verifica que la tabla se haya creado:

```bash
aws dynamodb describe-table --table-name MyTable --region us-east-1
```

---

## 2) Permisos IAM — acceso de la app a DynamoDB

### A) Política mínima (JSON) — `dynamo-policy.json`
Crea una política que permita `GetItem`, `PutItem`, `Query`, `Scan`, `UpdateItem` en la tabla:

```json
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": [
        "dynamodb:GetItem",
        "dynamodb:PutItem",
        "dynamodb:Query",
        "dynamodb:Scan",
        "dynamodb:UpdateItem",
        "dynamodb:BatchGetItem",
        "dynamodb:BatchWriteItem"
      ],
      "Resource": [
        "arn:aws:dynamodb:us-east-1:YOUR_ACCOUNT_ID:table/MyTable",
        "arn:aws:dynamodb:us-east-1:YOUR_ACCOUNT_ID:table/MyTable/index/*"
      ]
    }
  ]
}
```

Reemplaza `YOUR_ACCOUNT_ID` por tu id de cuenta AWS.

#### Crear la política con AWS CLI:
```bash
aws iam create-policy --policy-name MyAppDynamoPolicy --policy-document file://dynamo-policy.json
```

### B) Asociar la política a tu compute (EC2 o App Runner)

#### Si tu app corre en **EC2** (instancia):
1. Crea un role para EC2 con un trust policy (EC2 service principal):

`trust-ec2.json`:
```json
{
  "Version":"2012-10-17",
  "Statement":[
    {
      "Effect":"Allow",
      "Principal":{ "Service": "ec2.amazonaws.com" },
      "Action":"sts:AssumeRole"
    }
  ]
}
```

Comandos:
```bash
aws iam create-role --role-name MyAppEC2Role --assume-role-policy-document file://trust-ec2.json
aws iam attach-role-policy --role-name MyAppEC2Role --policy-arn arn:aws:iam::YOUR_ACCOUNT_ID:policy/MyAppDynamoPolicy
```

2. Crea un instance profile y asócialo a la instancia EC2:
```bash
aws iam create-instance-profile --instance-profile-name MyAppEC2Profile
aws iam add-role-to-instance-profile --instance-profile-name MyAppEC2Profile --role-name MyAppEC2Role
```

En la consola EC2, edita la **Instance IAM role** y asigna `MyAppEC2Profile`. O si creas instancia por CLI, usa `--iam-instance-profile Name=MyAppEC2Profile`.

#### Si tu app corre en **App Runner**:
- App Runner permite especificar un **IAM role** para el servicio.  
- Crea un role con un trust policy que permita a App Runner asumirlo (en consola o CLI).  
  - Si prefieres la consola: en la pantalla de creación de servicio App Runner hay una sección para **IAM role** (Service role / instance role). Adjunta la política `MyAppDynamoPolicy`.
- Si prefieres CLI/CloudFormation para App Runner, revisá la documentación oficial de App Runner para el parámetro exacto del role. *(Nota: en la consola es directo y recomendado si no estás familiarizado con el CLI de App Runner.)*

---

## 3) Dependencias Java (SDK v2 — recomendado)

### Gradle (build.gradle)
```gradle
dependencies {
    implementation 'software.amazon.awssdk:dynamodb:2.20.0' // verificá versión
    implementation 'software.amazon.awssdk:regions:2.20.0'
}
```

### Maven (pom.xml)
```xml
<dependency>
  <groupId>software.amazon.awssdk</groupId>
  <artifactId>dynamodb</artifactId>
  <version>2.20.0</version>
</dependency>
```

> **Sugerencia:** usar AWS SDK v2 (modular y moderno). Ajusta la versión según tu proyecto.

---

## 4) Código Java de ejemplo (CRUD + consulta por `status` usando GSI)

```java
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

public class DynamoDao {
    private final DynamoDbClient client;
    private final String tableName = "MyTable";
    private final String statusIndex = "status-index";

    public DynamoDao() {
        client = DynamoDbClient.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();
    }

    public void putItem(String id, String status, Map<String, String> otherAttrs) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("id", AttributeValue.builder().s(id).build());
        item.put("status", AttributeValue.builder().s(status).build());
        if (otherAttrs != null) {
            otherAttrs.forEach((k, v) -> item.put(k, AttributeValue.builder().s(v).build()));
        }

        client.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build());
    }

    public Map<String, AttributeValue> getById(String id) {
        GetItemRequest req = GetItemRequest.builder()
                .tableName(tableName)
                .key(Collections.singletonMap("id", AttributeValue.builder().s(id).build()))
                .build();
        return client.getItem(req).item();
    }

    public List<Map<String, AttributeValue>> findByStatus(String status) {
        // Query usando el GSI: status-index (status como partition key)
        QueryRequest req = QueryRequest.builder()
                .tableName(tableName)
                .indexName(statusIndex)
                .keyConditionExpression("status = :v_status")
                .expressionAttributeValues(Collections.singletonMap(":v_status", AttributeValue.builder().s(status).build()))
                .build();

        QueryResponse resp = client.query(req);
        return resp.items();
    }
}
```

Notas:
- `DefaultCredentialsProvider` obtendrá credenciales a partir del environment, profile o IAM role asignado a la instancia/servicio.
- En local, exporta `AWS_PROFILE` o `AWS_ACCESS_KEY_ID`/`AWS_SECRET_ACCESS_KEY` para pruebas.

---

## 5) Variables de entorno / configuración en el runtime

- AWS region:
```bash
export AWS_REGION=us-east-1
# o en Windows PowerShell
$env:AWS_REGION="us-east-1"
```

- Si usás perfiles locales:
```bash
export AWS_PROFILE=mi-perfil
```

No es necesario establecer claves si la app corre en EC2/App Runner con el role correcto.

---

## 6) Probar localmente con AWS CLI y SDK

- Insertar un item de prueba con AWS CLI:
```bash
aws dynamodb put-item \
  --table-name MyTable \
  --item '{"id": {"S": "1"}, "status": {"S": "active"}, "name": {"S": "Test"}}' \
  --region us-east-1
```

- Consultar por GSI (Query requiere usar `--index-name`):
```bash
aws dynamodb query \
  --table-name MyTable \
  --index-name status-index \
  --key-condition-expression "status = :s" \
  --expression-attribute-values '{":s":{"S":"active"}}' \
  --region us-east-1
```

- Obtener por id:
```bash
aws dynamodb get-item --table-name MyTable --key '{"id": {"S":"1"}}' --region us-east-1
```

---

## 7) Consideraciones de costos
- Al usar `BillingMode: PAY_PER_REQUEST` (on‑demand) pagas por request y almacenamiento. Para apps con **bajo tráfico** suele ser la opción más barata y simple.
- Revisa el **Free Tier** si tu cuenta califica (25 GB storage + capacidad limitada).
- Monitorea con CloudWatch el consumo de lecturas/escrituras y storage.

---

## 8) Buenas prácticas
- Usa un **GSI** para consultas frecuentes por atributos distintos a la primary key (ej. `status`).
- Evita `Scan` en tablas grandes — preferí `Query` sobre índices.
- Limita el scope de la política IAM al ARN de la tabla e índices.
- Usa cifrado en reposo (DynamoDB lo soporta por defecto con claves gestionadas por AWS) si necesitás cumplimiento.
- Implementá backoff exponencial en clientes ante throttling (aunque con on‑demand suele no ocurrir en cargas pequeñas).

---

## 9) Checklist rápido (resumen de comandos)
1. Crear tabla:
```bash
aws dynamodb create-table --cli-input-json file://create-table.json --region us-east-1
```
2. Crear política IAM:
```bash
aws iam create-policy --policy-name MyAppDynamoPolicy --policy-document file://dynamo-policy.json
```
3. Crear role EC2 y asociarlo (si corres en EC2):
```bash
aws iam create-role --role-name MyAppEC2Role --assume-role-policy-document file://trust-ec2.json
aws iam attach-role-policy --role-name MyAppEC2Role --policy-arn arn:aws:iam::YOUR_ACCOUNT_ID:policy/MyAppDynamoPolicy
aws iam create-instance-profile --instance-profile-name MyAppEC2Profile
aws iam add-role-to-instance-profile --instance-profile-name MyAppEC2Profile --role-name MyAppEC2Role
# luego asigna MyAppEC2Profile a la instancia EC2 desde la consola o CLI al crear/editar la instancia
```
4. Probar con CLI (put/get/query) — ejemplos arriba.

---

## ¿Querés que te genere:
- el `create-table.json`, `dynamo-policy.json` y `trust-ec2.json` ya rellenados con tu `YOUR_ACCOUNT_ID` y región?  
- o un archivo Java completo listo para pegar en tu proyecto (incluyendo build.gradle)?  

Dime cuál y te lo genero.
