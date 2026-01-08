# Feature Specification: Recuperación de contraseña ("Olvidé mi contraseña")

**Created**: 2026-01-04

---

## User Scenarios & Testing *(mandatory)*

### User Story 1 - Solicitar recuperación de contraseña (Priority: P1)

Como usuario que olvidó su contraseña, quiero poder ingresar mi email y solicitar la recuperación de acceso para recibir instrucciones que me permitan restablecer mi contraseña de forma segura.

**Why this priority**:
Es el punto de entrada del flujo. Sin esta funcionalidad, el usuario no tiene forma de iniciar la recuperación, por lo que es crítica para la experiencia mínima del sistema.

**Independent Test**:
Puede testearse enviando una solicitud con un email válido o inválido y verificando que el sistema siempre responda de forma uniforme y, en caso de existir el usuario, dispare el envío de un email.

**Acceptance Scenarios**:

1. **Scenario**: Solicitud con email existente

    * **Given** un usuario registrado con un email válido
    * **When** el usuario envía una solicitud de recuperación de contraseña
    * **Then** el sistema responde con un mensaje genérico y genera un userToken de recuperación con expiración

2. **Scenario**: Solicitud con email inexistente

    * **Given** un email que no pertenece a ningún usuario
    * **When** se envía la solicitud de recuperación
    * **Then** el sistema responde exactamente igual que si el email existiera, sin revelar información

---

### User Story 2 - Recepción del email de recuperación (Priority: P1)

Como usuario que solicitó la recuperación, quiero recibir un email con un enlace seguro para poder continuar el proceso de restablecimiento de contraseña.

**Why this priority**:
Sin el email, el flujo no puede continuar. Es esencial para completar la recuperación.

**Independent Test**:
Puede probarse verificando que, tras una solicitud válida, se envía un email con el contenido correcto y un enlace funcional.

**Acceptance Scenarios**:

1. **Scenario**: Envío exitoso de email

    * **Given** una solicitud de recuperación válida
    * **When** el sistema procesa la solicitud
    * **Then** se envía un email con un link que contiene un userToken de recuperación

---

### User Story 3 - Validar userToken de recuperación (Priority: P1)

Como usuario que hace clic en el enlace recibido, quiero que el sistema valide el userToken para asegurarme de que es válido y no está vencido antes de permitir cambiar la contraseña.

**Why this priority**:
Garantiza la seguridad del flujo y evita accesos no autorizados.

**Independent Test**:
Puede testearse accediendo al link con tokens válidos, vencidos, inexistentes o ya usados.

**Acceptance Scenarios**:

1. **Scenario**: Token válido

    * **Given** un userToken existente, no usado y no vencido
    * **When** el usuario accede al link
    * **Then** el sistema permite mostrar el formulario de nueva contraseña

2. **Scenario**: Token inválido o vencido

    * **Given** un userToken inválido, vencido o ya utilizado
    * **When** el usuario accede al link
    * **Then** el sistema muestra un error genérico y no permite continuar

---

### User Story 4 - Restablecer contraseña (Priority: P1)

Como usuario con un userToken válido, quiero definir una nueva contraseña para recuperar el acceso a mi cuenta.

**Why this priority**:
Es el objetivo final del flujo y entrega valor directo al usuario.

**Independent Test**:
Puede probarse enviando una nueva contraseña válida junto con un userToken válido y verificando que el acceso se restablece.

**Acceptance Scenarios**:

1. **Scenario**: Cambio exitoso de contraseña

    * **Given** un userToken válido
    * **When** el usuario envía una nueva contraseña que cumple las reglas
    * **Then** la contraseña se actualiza, el userToken se invalida y el usuario puede loguearse

2. **Scenario**: Contraseña inválida

    * **Given** un userToken válido
    * **When** el usuario envía una contraseña que no cumple las reglas
    * **Then** el sistema rechaza la solicitud con un mensaje de validación

---

### Edge Cases

* ¿Qué sucede si el usuario solicita múltiples recuperaciones seguidas?
* ¿Qué pasa si el userToken expira mientras el usuario está en la pantalla de cambio de contraseña?
* ¿Cómo responde el sistema ante ataques de fuerza bruta al endpoint de recuperación?
* ¿Qué ocurre si el email falla al enviarse?

---

## Requirements *(mandatory)*

### Functional Requirements

* **FR-001**: El sistema DEBE permitir solicitar la recuperación de contraseña mediante email.
* **FR-002**: El sistema DEBE responder de forma genérica, sin revelar si un email existe o no.
* **FR-003**: El sistema DEBE generar tokens de recuperación únicos, seguros y temporales.
* **FR-004**: El sistema DEBE almacenar únicamente el hash del userToken.
* **FR-005**: El sistema DEBE enviar un email con instrucciones usando un template predefinido.
* **FR-006**: El sistema DEBE validar que el userToken no esté vencido ni haya sido utilizado.
* **FR-007**: El sistema DEBE permitir definir una nueva contraseña cumpliendo políticas de seguridad.
* **FR-008**: El sistema DEBE invalidar el userToken una vez utilizado.
* **FR-009**: El sistema DEBE registrar eventos de seguridad relacionados al flujo.

### Key Entities

* **User**: Representa al usuario del sistema. Atributos clave: id, email, passwordHash, estado.
* **PasswordResetToken**: Representa un userToken de recuperación. Atributos clave: id, userId, tokenHash, expiresAt, used.

---

## Success Criteria *(mandatory)*

### Measurable Outcomes

* **SC-001**: El 100% de las solicitudes de recuperación responden sin filtrar información sensible.
* **SC-002**: Los tokens de recuperación expiran correctamente en el tiempo configurado.
* **SC-003**: Al menos el 95% de los usuarios logra restablecer su contraseña sin asistencia.
* **SC-004**: Reducción de tickets de soporte por contraseñas olvidadas en al menos un 50%.
