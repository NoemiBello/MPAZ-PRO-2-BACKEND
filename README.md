# MPAZ PRO 2

**Plataforma móvil de gestión y acceso a contenidos educativos**

MPAZ PRO 2 es una solución digital para organizar, administrar y consultar contenidos de aprendizaje de segundo básico. La plataforma contempla experiencias diferenciadas para los perfiles de **UTP, Docente y Estudiante**, con contenidos estructurados por asignatura, unidad y objetivo de aprendizaje.

La solución combina una aplicación Android desarrollada con **Kotlin y Jetpack Compose** y una **API REST en Java 21 y Spring Boot**, responsable de la gestión y persistencia de contenidos educativos.

## Características principales

- **Gestión de contenidos:** creación, consulta, actualización y eliminación de recursos educativos.
- **Organización curricular:** clasificación por asignatura, unidad y objetivo de aprendizaje.
- **Flujo de estados:** identificación de recursos como `PENDIENTE`, `APROBADO`, `RECHAZADO` u `OCULTO`.
- **Perfiles diferenciados:** experiencias de uso orientadas a UTP, docentes y estudiantes.
- **Materiales complementarios:** soporte para preguntas y referencias de imágenes asociadas a los contenidos.
- **Integración de servicios:** arquitectura preparada para la comunicación entre la aplicación móvil y la API REST.

## Arquitectura tecnológica

| Capa | Tecnologías | Responsabilidad |
| --- | --- | --- |
| Aplicación móvil | Kotlin, Jetpack Compose | Interfaz de usuario y navegación |
| Servicios | Java 21, Spring Boot 4.2.0 | Exposición de endpoints REST |
| Acceso a datos | Spring Data JPA, Hibernate | Persistencia y consultas |
| Base de datos | H2 | Almacenamiento de contenidos |
| Validación | Jakarta Validation | Reglas de obligatoriedad de campos |
| Versionamiento | Git, GitHub | Control de cambios y colaboración |

## API REST de contenidos

**URL base de desarrollo:** `http://localhost:8080`

| Método | Ruta | Descripción | Código de éxito |
| --- | --- | --- | --- |
| `GET` | `/api/contenidos` | Obtener todos los contenidos | `200 OK` |
| `GET` | `/api/contenidos/{id}` | Obtener un contenido por identificador | `200 OK` |
| `POST` | `/api/contenidos` | Registrar un nuevo contenido | `201 Created` |
| `PUT` | `/api/contenidos/{id}` | Actualizar un contenido existente | `200 OK` |
| `DELETE` | `/api/contenidos/{id}` | Eliminar un contenido | `204 No Content` |

Las consultas sobre identificadores inexistentes responden con `404 Not Found`. Las solicitudes que no satisfacen las validaciones de campos obligatorios responden con `400 Bad Request`.

### Modelo de datos

| Propiedad | Tipo | Descripción |
| --- | --- | --- |
| `id` | Long | Identificador generado automáticamente |
| `asignatura` | String | Asignatura del recurso |
| `unidad` | String | Unidad curricular |
| `objetivo` | String | Objetivo de aprendizaje |
| `tipo` | String | Categoría del contenido |
| `titulo` | String | Título del recurso |
| `contenido` | String | Cuerpo del contenido educativo |
| `pregunta` | String | Pregunta asociada, opcional |
| `imagenUri` | String | Referencia a imagen, opcional |
| `estado` | Enum | `PENDIENTE`, `APROBADO`, `RECHAZADO`, `OCULTO` |

### Ejemplo de creación

```http
POST /api/contenidos HTTP/1.1
Host: localhost:8080
Content-Type: application/json; charset=utf-8

{
  "asignatura": "Lenguaje",
  "unidad": "Unidad 1",
  "objetivo": "Comprender textos narrativos",
  "tipo": "Lectura",
  "titulo": "Mi primera lectura",
  "contenido": "Había una vez un gato que vivía en el campo.",
  "pregunta": "¿Dónde vivía el gato?",
  "imagenUri": null,
  "estado": "PENDIENTE"
}
```

**Respuesta de éxito:** `201 Created`, con el recurso creado y su identificador asignado.

## Ejecución del backend

### Requisitos

- Java Development Kit (JDK) 21.
- Entorno compatible con Gradle e IDE Java, como IntelliJ IDEA.

### Instalación

```bash
git clone https://github.com/NoemiBello/MPAZ-PRO-2-BACKEND.git
cd MPAZ-PRO-2-BACKEND
```

Para obtener la implementación actual del backend, utilizar la rama `noemi-bello`:

```bash
git switch noemi-bello
```

Iniciar el servicio desde IntelliJ IDEA ejecutando `MpazPro2BackendApplication.java`, o mediante Gradle Wrapper:

**Windows**

```powershell
.\gradlew.bat bootRun
```

**macOS/Linux**

```bash
./gradlew bootRun
```

La API estará disponible en `http://localhost:8080/api/contenidos`.

### Configuración de base de datos

El archivo `src/main/resources/application.properties` establece la conexión local a H2:

```properties
spring.application.name=MPAZ-PRO-2-BACKEND
spring.datasource.url=jdbc:h2:file:./data/mpazpro2
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
server.port=8080
```

**Consola H2:** `http://localhost:8080/h2-console`  
**URL JDBC:** `jdbc:h2:file:./data/mpazpro2`  
**Usuario:** `sa` · **Contraseña:** vacía

La base de datos se conserva en el directorio local `data/`, excluido del repositorio mediante `.gitignore`.

## Conectividad con Android

Para acceder al servicio desde el emulador estándar de Android se utiliza como dirección del equipo anfitrión `http://10.0.2.2:8080/`. En dispositivos físicos debe utilizarse la dirección de red accesible del equipo donde se ejecuta el backend.

El acceso por red requiere la configuración correspondiente de conectividad y permisos de la aplicación Android.

## Verificación funcional

Se ejecutaron pruebas HTTP sobre el backend para comprobar:

| Escenario | Resultado |
| --- | --- |
| Consulta de contenidos | Respuesta satisfactoria de `GET` |
| Creación de registro | `POST` con ID generado |
| Actualización de contenido | `PUT` con modificación de título y estado |
| Eliminación de registro | `DELETE` sin errores |
| Validación de campos obligatorios | Respuesta `400 Bad Request` ante título ausente |
| Persistencia de datos | Recuperación de un registro tras reiniciar el servidor |

Estas comprobaciones corresponden a la **API REST y su base de datos**.

## Estructura del servicio

```text
src/main/java/cl/mpazpro2/backend/
├── MpazPro2BackendApplication.java
├── controller/
│   └── ContenidoController.java
├── model/
│   ├── Contenido.java
│   └── EstadoContenido.java
├── repository/
│   └── ContenidoRepository.java
└── service/
    └── ContenidoService.java

src/main/resources/
└── application.properties
```

## Equipo

| Integrante | Área de desarrollo |
| --- | --- |
| Noemí Bello | API REST, modelo de datos, lógica de negocio, persistencia y validaciones |
| Jeanpiere Quito | Aplicación Android, interfaces, navegación e integración móvil |

## Repositorio

**Código fuente del backend:** [MPAZ-PRO-2-BACKEND](https://github.com/NoemiBello/MPAZ-PRO-2-BACKEND)  
**Rama de implementación:** `noemi-bello`
