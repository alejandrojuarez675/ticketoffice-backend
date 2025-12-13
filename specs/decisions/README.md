# Registro de Decisiones Técnicas

Este directorio contiene el registro de decisiones técnicas importantes tomadas durante el desarrollo del proyecto TicketOffice Backend.

## Estructura

```
decisions/
├── README.md           # Este archivo
├── template.md         # Plantilla para nuevas decisiones
└── YYYY/               # Año de la decisión
    └── YYYY-MM/        # Mes de la decisión
        └── YYYYMMDD-decision-title.md  # Archivo de decisión
```

## Cómo documentar una nueva decisión

1. Copiar la plantilla `template.md` a un nuevo archivo siguiendo el formato:
   - Ubicación: `YYYY/YYYY-MM/YYYYMMDD-titulo-corto.md`
   - Ejemplo: `2025/2025-12/20251210-autenticacion-jwt.md`

2. Completar la plantilla con los detalles de la decisión

3. Actualizar el índice de decisiones en este archivo si es necesario

## Índice de Decisiones

| Fecha       | Título | Estado    | Archivo |
|-------------|--------|-----------|---------|
| 2025-12-10 | Plantilla de Decisión Técnica | aceptada | [Ver](./template.md) |

## Estados de las Decisiones

- **propuesta**: La decisión está siendo evaluada
- **aceptada**: La decisión ha sido aprobada
- **rechazada**: La decisión ha sido rechazada
- **obsoleta**: La decisión ya no es relevante
