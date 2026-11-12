# Guía de sustentación — Clínica Piedra Azul

## Presentación breve

> Desarrollé un sistema web para la Clínica Piedra Azul con Angular y Spring Boot. La aplicación tiene dos perfiles autenticados: el administrador gestiona médicos, pacientes y citas; el paciente consulta únicamente sus propias citas. El backend emite y verifica JWT, y aplica autorización por roles. En el frontend organicé los componentes con Atomic Design, protegí rutas con guards y envié el token con un interceptor.

## Recorrido recomendado en vivo

1. Abrir `/doctors` en una ventana privada y mostrar el directorio público.
2. Abrir `/login`, iniciar como ADMIN (`admin@piedraazul.test` / `Admin123!`) y mostrar el panel y sus accesos.
3. Abrir **Médicos**, mostrar la tabla reutilizable y registrar un médico nuevo.
4. Abrir **Pacientes**, registrar un paciente y mostrar que la cédula se consulta en el backend al salir del campo. Probar una cédula existente para ver la validación.
5. Abrir **Citas**, programar una cita seleccionando los registros existentes y mostrarla en la agenda.
6. Cerrar sesión; iniciar como PACIENTE (`paciente@piedraazul.test` / `Paciente123!`) y abrir **Mis citas**.
7. Escribir directamente `/admin/patients` como paciente: Angular debe enviarte a `/forbidden`. Sin sesión, escribir `/admin`: debe redirigirte a `/login`.
8. En Postman, seleccionar el entorno local; ejecutar primero los dos logins para guardar tokens y luego las carpetas públicas, administrador, paciente y seguridad.
9. En Postman, mostrar 401 sin token, 403 al usar un rol incorrecto y 200 al usar el rol correspondiente.

## Cómo explicar cada criterio de la rúbrica

### 1. Funcionalidades — 0,8

Son nueve recorridos completos: login, médicos públicos, mis citas, registrar médico, listar médicos, registrar paciente, listar pacientes, crear cita y listar todas las citas. Se demuestra cada uno desde su pantalla y se confirma que usa el endpoint real, no una lista simulada. La diferencia de roles se ve al comparar el listado de citas del paciente con la agenda del administrador.

### 2. Atomic Design — 0,7

- **Átomos:** `UiButtonComponent`, `FormFieldComponent` y `DataTableComponent`. La tabla recibe columnas y filas como entradas.
- **Molécula:** `TableWithTitleComponent`, que combina título, descripción, contador y tabla.
- **Organismos:** formularios de acceso, médico, paciente y cita; listados usan la molécula.
- **Plantilla:** `ClinicLayoutComponent` proporciona cabecera, navegación, contenido y pie adaptables.
- **Páginas:** los componentes de `pages/` representan login, panel, formularios y listados configurados por ruta.

Para probar reutilización, abrir el listado de médicos y luego el de pacientes: cambia la configuración de columnas y las filas, pero se conserva el mismo átomo de tabla y la misma molécula.

### 3. Responsive — 0,3

La plantilla cambia a menú plegable en pantallas angostas; tarjetas y formularios pasan a una sola columna; la tabla conserva su legibilidad y permite desplazamiento horizontal en móvil. Se puede comprobar con el modo de dispositivo del navegador a unos 390 px, 768 px y escritorio.

### 4. Formularios reactivos — 0,4

Los tres formularios se construyen con `FormGroup` y `FormControl`: médico, paciente y cita. Usan validación obligatoria, correo, longitudes y fechas. El campo cédula del paciente consulta `GET /api/patients/exists/{cedula}` como validador asíncrono al perder el foco, con espera breve para evitar solicitudes repetidas. La creación de cita carga pacientes y médicos reales para sus selectores.

### 5. Estilos visuales — 0,3

La interfaz usa una paleta azul petróleo, turquesa y fondos claros, el símbolo SVG de la clínica, jerarquía tipográfica, estados vacíos, carga y error, además de transiciones hover y entrada animada de filas. El CSS está escrito en los componentes y en `styles.css`.

### 6. Guard de autenticación — 0,3

`authenticationGuard` verifica que existan token y rol localmente. Si no hay sesión, cancela la ruta y crea una redirección al login. Muéstralo pegando directamente `/admin` sin iniciar sesión.

### 7. Guard de autorización — 0,3

`roleGuard` compara el rol guardado con `data.role` de la ruta. Rechaza, por ejemplo, un paciente en `/admin/patients` y envía al acceso denegado. La comprobación del navegador mejora la navegación, pero no sustituye la del servidor.

### 8. Interceptor — 0,3

`jwtInterceptor` lee `clinic.token` y agrega `Authorization: Bearer <token>` a llamadas privadas de `/api`. Excluye login, directorios públicos y consulta de existencia de cédula. Se puede demostrar en Postman/captura de red observando que Angular manda el encabezado en una llamada privada.

### 9. Rutas públicas en frontend — 0,4

Probar `/doctors` y `/login` sin iniciar sesión. El directorio consulta el endpoint público del backend; no depende de datos locales.

### 10. Rutas privadas permitidas en frontend — 0,4

Iniciar sesión como ADMIN y abrir sus rutas; luego como PACIENTE y abrir `/patient/appointments`. Probar la URL directa, además de la navegación del menú.

### 11. Rutas privadas denegadas en frontend — 0,4

Sin token, las rutas administrativas deben llevar al login. Con token de paciente, las rutas ADMIN llevan a `/forbidden`; un administrador tampoco puede usar la ruta exclusiva `/patient/appointments`.

### 12. Rutas públicas en backend — 0,2

Con Postman y sin token, consultar login (para credenciales válidas), `GET /api/doctors/public`, `GET /api/patients/public` y el endpoint de existencia. La respuesta pública de pacientes es intencionalmente anónima; el listado con datos solo es ADMIN.

### 13. Rutas privadas en backend — 0,2

Spring Security valida el JWT en cada petición. Sin token o con token inválido, el resultado es **401**; token válido con rol incorrecto, **403**; rol correcto, **200** o **201**. En `GET /api/appointments/my`, el backend deriva el usuario del JWT y no recibe la identidad del paciente desde la URL.

## Preguntas probables

**¿Por qué hay guards si Spring Security también protege?** El guard evita mostrar navegación que el usuario no puede usar; la seguridad del backend es la protección efectiva porque cualquier cliente puede omitir Angular y llamar la API directamente.

**¿Cómo se evita que un paciente cambie el ID para ver citas ajenas?** El endpoint de citas propias no recibe un ID de paciente. El filtro valida el JWT, y el servicio busca las citas asociadas al usuario que el token identifica.

**¿Dónde se protege la contraseña?** Se almacena como hash BCrypt en `users.password_hash` (generado por JPA); nunca se guarda la contraseña original.

**¿Por qué la ruta pública de pacientes no entrega los nombres?** La guía pide un servicio público, pero el listado de gestión es administrativo. La ruta pública devuelve etiquetas sin cédula, nombre, dirección, correo ni teléfono; el listado completo requiere rol ADMIN.

**¿Qué evidencia falta antes de afirmar la demostración completa?** Angular compila y pasan sus 12 pruebas; Maven generó el JAR con `BUILD SUCCESS`. MySQL está activo, pero Spring Boot no conectó porque recibió la contraseña vacía. Hay que iniciar el backend con el `DB_PASSWORD` correcto y luego ejecutar la colección Postman contra la API.
