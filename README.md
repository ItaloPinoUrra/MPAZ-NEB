# MPAZ NEB - Aplicación de Nivelación

**Integrantes:** Ítalo Pino
**Asignatura:** Desarrollo de Aplicaciones Móviles_003D
**Institución:** Duoc UC

## Descripción del Proyecto
MPAZ NEB es una aplicación móvil desarrollada en Android Studio (Jetpack Compose) diseñada para la Escuela Escritora Marcela Paz. Su objetivo es servir como herramienta de apoyo para la nivelación en Lectura y Matemática de estudiantes de 1º a 4º básico, permitiendo a docentes y alumnos gestionar perfiles y acceder a actividades.

## Funcionalidades Implementadas
*   **Interfaz Gráfica y Navegación:** Panel de inicio (Dashboard) con tarjetas informativas, Menú inferior (Bottom Bar) y navegación fluida entre pantallas mediante `NavHost` y `NavController`. Incluye *Snackbars* para notificar estados vacíos.
*   **Gestión de Usuarios (Formulario):** Registro de usuarios con validaciones reactivas, manejo de errores visuales, selección de rol (Alumno/Docente) mediante menú desplegable e incorporación de foto de perfil.
*   **Persistencia Local (Room):** Almacenamiento seguro de los usuarios registrados en una base de datos local SQLite. El perfil del usuario se actualiza en tiempo real utilizando `StateFlow`.
*   **Actualización de Perfil:** El usuario puede visualizar sus datos guardados y modificar su foto de perfil en cualquier momento desde la pantalla de "Perfil".
*   **Recursos Nativos:**
    *   **Galería de imágenes:** Acceso al almacenamiento del dispositivo para seleccionar y actualizar la foto de perfil.
    *   **Vibración (Feedback Háptico):** El dispositivo vibra para alertar al usuario cuando intenta registrar un formulario con datos inválidos.
*   **Diseño y Animaciones:** Interfaz construida con Material 3 respetando un tema de colores personalizado (Modo Claro/Oscuro), con transiciones suaves y animaciones de carga (spinners) en los botones de acción.

## Pasos para Ejecutar
1. Clona este repositorio en tu máquina local o descarga el archivo ZIP.
2. Abre el proyecto en **Android Studio**.
3. Espera a que Gradle sincronice las dependencias.
4. Ejecuta la aplicación en un emulador o dispositivo físico con **API 28 (Android 9.0) o superior**.
5. (Opcional) Para probar la vibración, se recomienda ejecutar en un dispositivo físico, ya que algunos emuladores no simulan esta función por defecto.

## Tecnologías Utilizadas
*   Kotlin
*   Jetpack Compose (Material 3)
*   Navigation Compose
*   ViewModel, StateFlow y Coroutines
*   Room Database
*   Coil (Carga de imágenes)