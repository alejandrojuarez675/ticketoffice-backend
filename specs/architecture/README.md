# Arquitectura del Sistema

Este directorio contiene la documentación de la arquitectura del sistema TicketOffice Backend.

## Índice

1. [Visión General](#visión-general)
2. [Arquitectura General](#arquitectura-general)
3. [Componentes Principales](#componentes-principales)
4. [Flujos de Datos](#flujos-de-datos)
5. [Decisiones de Diseño](#decisiones-de-diseño)
6. [Estrategia de Pruebas](#estrategia-de-pruebas)
7. [Seguridad](#seguridad)
8. [Documentación de API](#documentación-de-api)
9. [Registro de Decisiones Técnicas](#registro-de-decisiones-técnicas)
10. [Patrones Arquitectónicos](#patrones-arquitectónicos)

## Visión General

TicketOffice es una plataforma de venta de entradas en línea que permite a los usuarios buscar, comprar y gestionar entradas para eventos. El backend está construido siguiendo una arquitectura hexagonal (Ports & Adapters) y utiliza Javalin como framework web.

## Arquitectura General

```
┌─────────────────────────────────────────────────────────┐
│                   Puertos de Entrada                    │
│  ┌─────────────────┐    ┌───────────────────────────┐  │
│  │ Controladores   │    │  Controladores de Comando  │  │
│  │  (REST/HTTP)    │    │   (Eventos/Mensajes)       │  │
│  └────────┬────────┘    └─────────────┬─────────────┘  │
└───────────┼───────────────────────────┼─────────────────┘
            │                           │
            ▼                           ▼
┌─────────────────────────────────────────────────────────┐
│                   Puertos de Salida                     │
│  ┌─────────────────┐    ┌───────────────────────────┐  │
│  │  Repositorios   │    │  Servicios Externos       │  │
│  │  (Puertos)      │    │  (Email, AWS, etc.)       │  │
│  └────────┬────────┘    └─────────────┬─────────────┘  │
└───────────┼───────────────────────────┼─────────────────┘
            │                           │
            ▼                           ▼
┌─────────────────────────────────────────────────────────┐
│                  Dominio (Núcleo)                       │
│  ┌─────────────────────────────────────────────────┐    │
│  │  Lógica de Negocio                              │    │
│  │  - Entidades                                    │    │
│  │  - Servicios de Dominio                         │    │
│  │  - Reglas de Negocio                            │    │
│  └─────────────────────────────────────────────────┘    │
└─────────────────────────────────────────────────────────┘
```

## Componentes Principales

1. **API REST**: Implementada con Javalin
2. **Autenticación**: Basada en JWT (sin OAuth2)
   - Tipos de usuarios: Compradores, Vendedores, Administradores
3. **Almacenamiento**: Amazon DynamoDB
4. **Despliegue**: AWS AppRunner
5. **Inyección de Dependencias**: Google Guice
6. **Logging**: SLF4J con implementación Simple

## Flujos de Datos

1. **Autenticación de Usuario**
2. **Búsqueda de Eventos**
3. **Proceso de Compra**
4. **Gestión de Eventos (Admin)**

## Decisiones de Diseño

- **Arquitectura Hexagonal (Ports & Adapters)**: Para una clara separación entre el dominio de negocio y las tecnologías externas
- **Inyección de Dependencias**: Para facilitar el testing y la mantenibilidad
- **JWT para Autenticación**: Sin OAuth2, con roles específicos (USER, SELLER, ADMIN)
- **AWS**: 
  - DynamoDB para almacenamiento principal
  - AppRunner para el despliegue
  - Sin API Gateway + Lambda (usando Javalin directamente)
- **Dominio**: Diferentes tipos de eventos soportados
- **Modelos de Dominio**: Ubicados en `com.ticketoffice.backend.domain.models`
- **CI/CD**: No implementado actualmente
- **Monitoreo y Métricas**: No implementado actualmente
- **Backup y Recuperación**: No implementado actualmente

## Estrategia de Pruebas

### Pruebas Unitarias
- **Cobertura de componentes clave**:
  - Controladores
  - Servicios de dominio
  - Utilidades
  - Manejadores de autenticación
- **Frameworks**:
  - JUnit 5
  - Mockito para mocks y stubs
  - AssertJ para aserciones más legibles

## Seguridad

### Manejo de Secretos
- **Variables de entorno** para toda la configuración sensible
- No hardcodear credenciales en el código fuente
- Uso de `.env` para desarrollo local (incluido en `.gitignore`)

## Documentación de API

- **OpenAPI/Swagger** para documentación interactiva de la API
- Especificación disponible en formato YAML/JSON
- Documentación generada automáticamente desde el código
- Incluye:
  - Endpoints disponibles
  - Parámetros de entrada
  - Formatos de respuesta
  - Códigos de estado HTTP
  - Ejemplos de solicitudes/respuestas
  - Esquemas de datos

## Registro de Decisiones Técnicas

Se mantendrá un registro de decisiones técnicas importantes en el directorio `specs/decisions/` siguiendo el formato:

```markdown
# [Título de la Decisión]

## Estado
[propuesta | aceptada | rechazada | obsoleta]

## Contexto
[Descripción del problema o necesidad]

## Decisiones
[La decisión que se ha tomado]

## Consecuencias
[Implicaciones y consecuencias de la decisión]
```

## Patrones Arquitectónicos

- **Ports & Adapters**: Para aislar el dominio de las tecnologías externas
- **Repository**: Para el acceso a datos (implementado como puerto)
- **Dependency Injection**: Para conectar puertos con sus adaptadores
- **CQRS**: Para separar operaciones de lectura y escritura cuando sea necesario
- **Domain Events**: Para manejar efectos secundarios de manera desacoplada
