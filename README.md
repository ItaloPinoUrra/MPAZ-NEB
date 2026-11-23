# MPAZ NEB - Aplicación Móvil y Microservicios de Nivelación

**Proyecto:** MPAZ NEB (Nivelación en Lectura y Matemática - Colegio Escritora Marcela Paz)  
**Estudiante:** Ítalo Pino  
**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)  
**Institución:** Duoc UC  

---

## Descripción del Proyecto
MPAZ NEB es una solución móvil integral desarrollada con Android Studio (Jetpack Compose) en el frontend y un backend de Microservicios en Spring Boot. Está concebida para la Escuela Escritora Marcela Paz con el fin de servir como herramienta de nivelación académica en Lectura y Matemática para estudiantes de 1º a 4º básico. 

La plataforma permite la gestión de usuarios por roles (Administrador, Asistente y Estudiante), creación y resolución de actividades pedagógicas, adjuntar material educativo, gestión de preguntas asociadas y catálogo de lecturas sugeridas.

---

## Arquitectura del Sistema

El proyecto implementa una arquitectura desacoplada en 2 componentes principales:

```
+---------------------------------------------------+
|              Android Mobile App                   |
|        (Jetpack Compose + MVVM + Retrofit)        |
+-------------------------+-------------------------+
                          |
             HTTP / REST (JSON) | 10.0.2.2:8080
                          v
+---------------------------------------------------+
|             Microservicio Spring Boot             |
|       (Spring Web + JPA / Hibernate + REST API)   |
+---------------------------------------------------+
```

1. **Frontend Móvil (Android):**
   - **Patrón MVVM (Model-View-ViewModel):** Desacoplamiento de UI y lógica mediante `StateFlow` y Coroutines.
   - **Navegación:** `Navigation Compose` con rutas dinámicas y paso de argumentos.
   - **Persistencia de Sesión:** `Jetpack DataStore Preferences` para almacenar el estado del usuario logueado.
   - **Consumo REST:** `Retrofit 2` + `Gson` para comunicación con backend local y APIs externas.

2. **Backend Microservicios (Spring Boot - `mpazneb-api`):**
   - **Spring Web & REST:** Controladores `@RestController` para autenticación, actividades y preguntas.
   - **Spring Data JPA & Hibernate:** Persistencia de datos en base de datos relacional.
   - **Manejo de Archivos:** Carga y descarga de material adjunto (`MultipartFile`).

---

## Funcionalidades Principales

* **Autenticación y Roles:**
  - Login seguro con validación de credenciales en microservicio Spring Boot (`/api/auth/login`).
  - Roles soportados: `ADMIN`, `ASISTENTE`, `ESTUDIANTE`.
  - Persistencia de sesión en el dispositivo con `SesionManager` (DataStore).
* **Catálogo de Actividades:**
  - Consulta en tiempo real de actividades de nivelación (`/api/actividades`).
  - Creación de nuevas actividades desde la app móvil.
  - Subida de material educativo adjunto (PDF/imágenes) mediante multipart.
* **Gestión de Preguntas por Actividad:**
  - Listado, creación y eliminación de preguntas asociadas a cada actividad pedagógica.
* **Lecturas Sugeridas (API Externa):**
  - Integración con Google Books API (`googleapis.com/books/v1/volumes`) para obtener catálogo público de literatura infantil.
* **Pruebas Unitarias y Cobertura:**
  - Test unitarios en Android con Kotest + MockK + JUnit5.
  - Reporte de cobertura de código mediante JaCoCo.
* **Generación de APK Firmado:**
  - Configuración de firmado digital de APK mediante `signingConfigs` en Gradle y almacén de claves `.jks`.

---

## Pasos para Ejecutar el Proyecto

### 1. Iniciar el Backend (Spring Boot)
1. Navega a la carpeta del microservicio: `cd mpazneb-api`
2. Ejecuta el servidor desde la terminal:
   ```bash
   ./mvnw spring-boot:run
   ```
   *(El microservicio iniciará en `http://localhost:8080`)*
3. Credenciales de prueba autogeneradas al iniciar (`DataInitializer`):
   - **Admin:** `admin` / `admin123`
   - **Asistente:** `asistente` / `asist123`
   - **Estudiante:** `estudiante` / `estu123`

### 2. Iniciar la App Móvil (Android Studio)
1. Abre la carpeta raíz del proyecto en Android Studio.
2. Espera que Gradle sincronice todas las dependencias.
3. Asegúrate de tener un emulador Android (API 28+) o dispositivo físico conectado.
4. Ejecuta la app (`Run 'app'`).
   *(La app se conectará al microservicio usando `http://10.0.2.2:8080/`)*.

---

## Pruebas Unitarias y Firma

### Ejecutar Pruebas Unitarias:
```bash
./gradlew testDebugUnitTest
```

### Generar Reporte de Cobertura (JaCoCo):
```bash
./gradlew jacocoTestReport
```

### Generar APK Firmado (Release):
```bash
./gradlew assembleRelease
```

---

## Tecnologías Utilizadas

- **Frontend Móvil:** Kotlin, Jetpack Compose, Material 3, Navigation Compose, Coroutines, StateFlow, Retrofit 2, DataStore Preferences, Coil, Kotest, MockK, JaCoCo.
- **Backend Microservicio:** Java 17, Spring Boot 3, Spring Web, Spring Data JPA, H2/MySQL.
- **DevOps / Control de Versiones:** Git, GitHub, Trello (Kanban), Gradle.
