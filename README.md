# Clínica Piedra Azul

Aplicación de gestión clínica para administrar médicos, pacientes y citas. El frontend está desarrollado con Angular y Atomic Design; el backend usa Java, Spring Boot, Spring Security, JPA y JWT. La base de datos es MySQL.

## Funcionalidades

1. Inicio de sesión con JWT para administradores y pacientes.
2. Directorio público de médicos.
3. Consulta de citas propias del paciente autenticado.
4. Registro de médicos para ADMIN.
5. Listado administrativo de médicos.
6. Registro de pacientes para ADMIN con comprobación asíncrona de cédula.
7. Listado administrativo de pacientes.
8. Creación de citas por ADMIN.
9. Listado de todas las citas para ADMIN.

## Requisitos locales

- Java 21.
- Node.js compatible con Angular 22 y npm.
- MySQL 8 o compatible.
- Postman para importar la colección de `postman/`.

### Qué hace cada programa

- **MySQL Server** guarda médicos, pacientes, usuarios y citas. Debe estar instalado y funcionando como servicio en Windows.
- **MySQL Workbench** es una aplicación gráfica opcional para administrar y consultar MySQL; el backend no la necesita.
- **Spring Boot** es la API; se conecta a MySQL y atiende peticiones de Angular y Postman.
- **Postman** prueba la API. No instala, inicia ni reemplaza MySQL.

Si no tienes MySQL Server, instálalo desde la [página oficial de MySQL para Windows](https://dev.mysql.com/downloads/). En el asistente incluye **MySQL Server**, configura una contraseña para `root`, conserva el puerto `3306` e inicia el servicio de Windows. El configurador oficial puede dejarlo instalado como servicio para que arranque con Windows. Workbench es opcional.

Comprueba que el servidor esté en marcha desde PowerShell:

```powershell
Test-NetConnection localhost -Port 3306
```

Cuando `TcpTestSucceeded` muestre `True`, configura la misma contraseña de `root` en `DB_PASSWORD` y arranca el backend. La URL predeterminada crea `piedra_azul` al conectar, siempre que el usuario tenga permiso para crear bases de datos.

## Base de datos y backend

El backend crea el esquema `piedra_azul` si el usuario MySQL tiene permiso. Puede configurarse por variables de entorno; los valores predeterminados locales son `root` sin contraseña.

En PowerShell, desde la carpeta `backend`, usa la contraseña de `root` que elegiste al instalar MySQL. El error `Access denied for user 'root' ... (using password: NO)` significa que el backend recibió la contraseña vacía; no la compartas en el chat.

```powershell
$env:DB_URL='jdbc:mysql://localhost:3306/piedra_azul?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
$env:DB_USERNAME='root'
$env:DB_PASSWORD='REEMPLAZA_CON_LA_CLAVE_DE_ROOT'
$env:JWT_SECRET='reemplaza-esto-por-una-clave-local-de-al-menos-32-caracteres'
.\mvnw.cmd -s .\maven-settings-local.xml spring-boot:run
```

Para compilar el backend:

```powershell
.\mvnw.cmd -s .\maven-settings-local.xml -DskipTests package
```

JPA mantiene el esquema mediante `ddl-auto=update`. Al iniciar la aplicación se crean registros de demostración si no existen. Para cualquier despliegue real se deben configurar secretos propios y migraciones de esquema.

### Cuentas de demostración

| Rol | Correo | Contraseña |
|---|---|---|
| ADMIN | `admin@piedraazul.test` | `Admin123!` |
| PACIENTE (Sofía Ramírez) | `paciente@piedraazul.test` | `Paciente123!` |
| PACIENTE (Mateo Torres) | `mateo@piedraazul.test` | `Paciente123!` |

Las contraseñas se guardan con BCrypt. El seeder incluye tres médicos y varias citas futuras vinculadas a los pacientes. El cargador es idempotente para esas cuentas y evita duplicar la agenda si ya existen citas.

## Frontend

En una segunda terminal, desde `frontend`:

```powershell
npm install
npm start
```

Angular queda disponible en `http://localhost:4200`. El backend debe estar en `http://localhost:8080`; CORS permite ese origen por defecto (`FRONTEND_ORIGIN` permite cambiarlo).

## Rutas y permisos

| Ruta | Acceso |
|---|---|
| `/doctors` | Público |
| `/login` | Público |
| `/admin` | ADMIN |
| `/admin/doctors` y `/admin/doctors/new` | ADMIN |
| `/admin/patients` y `/admin/patients/new` | ADMIN |
| `/admin/appointments` y `/admin/appointments/new` | ADMIN |
| `/patient/appointments` | PACIENTE |

El Guard de autenticación redirige a `/login` sin token; el Guard de rol envía a `/forbidden` cuando el rol no coincide. La seguridad real también se aplica en Spring Security.

## API REST

| Método y ruta | Acceso | Resultado |
|---|---|---|
| `POST /api/auth/login` | Público | JWT, rol y correo |
| `GET /api/doctors/public` | Público | Directorio médico |
| `GET /api/patients/public` | Público | Etiquetas anónimas sin información identificable |
| `GET /api/patients/exists/{cedula}` | Público | Solo `{ "exists": true/false }` |
| `GET /api/doctors` | ADMIN | Listado médico administrativo |
| `POST /api/doctors` | ADMIN | Crear médico (201) |
| `GET /api/patients` | ADMIN | Listado administrativo de pacientes |
| `POST /api/patients` | ADMIN | Crear paciente y credenciales (201) |
| `POST /api/appointments` | ADMIN | Crear cita (201) |
| `GET /api/appointments` | ADMIN | Todas las citas |
| `GET /api/appointments/my` | PACIENTE | Citas ligadas al usuario del JWT |

El listado público de pacientes aparece en el apartado de servicios de la guía, mientras que el listado completo de la funcionalidad está restringido a ADMIN. Para respetar ambas indicaciones sin divulgar datos personales, el endpoint público devuelve etiquetas genéricas; los datos reales solo se entregan a ADMIN. Las citas propias se filtran por el ID del usuario extraído del token y el endpoint no acepta un ID de paciente del cliente.

Respuestas de seguridad: petición privada sin token o con token inválido = **401**; token válido con rol incorrecto = **403**. Datos inválidos = 400; duplicados = 409; recurso inexistente = 404.

## Atomic Design

- `frontend/src/app/atoms/`: botón, campo de formulario y tabla genérica.
- `frontend/src/app/molecules/`: tabla con título y subtítulo.
- `frontend/src/app/pages/`: login, paneles, formularios y página de listados configurable por ruta.
- `frontend/src/app/templates/`: plantilla clínica con navegación y pie adaptable.
- `frontend/src/app/core/`: servicios HTTP, autenticación, guards e interceptor JWT.
- `frontend/src/app/models/`: contratos del frontend.

Los listados de médicos y citas reutilizan la misma molécula y tabla genérica con columnas configuradas desde el enrutador.

## Postman

Usa los archivos exportados en formato estándar de Postman Collection v2.1:

1. `postman/Clinica-Piedra-Azul.postman_collection.json`
2. `postman/Clinica-Piedra-Azul-Local.postman_environment.json`

En Postman, pulsa **Import**, selecciona **Files** y elige los dos JSON. Selecciona el entorno `Clínica Piedra Azul Local`. Ejecuta primero **Login administrador** y **Login paciente**; sus scripts guardan `adminToken` y `patientToken`. La colección incluye servicios públicos, operaciones administrativas, citas de paciente, ausencia de token (401), rol incorrecto (403), token inválido (401) y comprobación de que un parámetro de paciente no cambia la identidad del JWT. Las solicitudes de creación usan datos de ejemplo; si vuelves a ejecutarlas, cambia cédulas o correo para evitar el conflicto 409. Estos JSON son los archivos oficiales para importar y ejecutar las pruebas.

## Comprobación realizada

- `npm run build` en `frontend`: compilación Angular completada correctamente.
- `npm test -- --watch=false` en `frontend`: 12 pruebas pasaron, incluidas navegación directa a rutas públicas, rutas privadas permitidas/denegadas y guards.
- `.\mvnw.cmd -s .\maven-settings-local.xml -U compile` en `backend`: compiló correctamente las 30 clases Java.
- El comando `-s .\maven-settings-local.xml -U -DskipTests package` terminó con `BUILD SUCCESS` en la consola del usuario y generó el JAR ejecutable.
- MySQL está instalado y activo en el puerto 3306. Spring Boot alcanza el servidor, pero la conexión fue rechazada porque `DB_PASSWORD` no se configuró; al introducir la contraseña local correcta se podrá completar la prueba integrada de API y Postman.

## Lista de entrega

- [x] Nueve funcionalidades implementadas y conectadas a endpoints.
- [x] Atomic Design con tabla y molécula tabla+título.
- [x] Plantilla adaptable a móvil, tableta y escritorio.
- [x] Formularios reactivos de médico, paciente y cita.
- [x] Validador asíncrono de cédula mediante HTTP.
- [x] Guard de autenticación y Guard de rol.
- [x] Interceptor Bearer JWT para rutas privadas.
- [x] Pruebas frontend de rutas públicas, rutas privadas permitidas/denegadas, sesión y rol.
- [x] Endpoints públicos y privados con permisos backend.
- [x] Datos de prueba de administrador, pacientes, médicos y citas.
- [x] Colección Postman y entorno exportables.
- [ ] Ejecutar backend y recorrer las pruebas integradas contra MySQL local.
