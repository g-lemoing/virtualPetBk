# Creación de ApiRest con SpringBoot para una aplicación de cuidado de mascotas virtuales
Este proyecto gestiona la creación, modificación, lectura y eliminación de unas mascotas virtuales, exponiendo unos endpoints para ello. Estos endpoints son consumidos por una aplicación React front-end disponible en esta url: https://github.com/g-lemoing/virtualPetFrontEnd
Esta APIRest incorpora JSON Web Token Security (JWT) para todas las operaciones de CRUD, y permisos basados en el rol del usuario autenticado (USER o ADMIN)

Creamos un proyecto Spring con Spring Initializr (https://start.spring.io/), con gestor de dependencias Maven y añadimos las dependencias necesarias relacionadas en el apartado "Requisitos técnicos" más abajo, las que no están disponibles en ese paso se añaden luego manualmente en el proyecto a posteriori.

### Bases de datos:
Utilizamos mySQL para persistir los datos de los usuarios registrados y mascotas existentes. Creamos dos controladores, uno que gestiona el registro y autenticación de los usuarios, y el otro las operationes con las mascotas:
### El AuthenticationController gestiona dos endpoints:
- "/auth/signup" para registrar un nuevo usuario
- "/auth/login" para autenticarse e iniciar sesión
### El PetController gestiona las operaciones CRUD con cuatro endpoints:
- "/pet/create" para crear una nueva mascota
- "/pet/read" para listar todas las mascotas (del usuario autenticado si tiene rol USER, o de todos los usuarios si tiene el rol ADMIN)
- "/pet/update/" para interactuar con una mascota
- "/pet/delete/" para eliminar una mascota
### Documentació de l'API
La documentación completa de los endpoints con sus parámetros, y la de los esquemas está disponible a http://localhost:8080/swagger-ui/index.html, con la aplicación arrancada.

## Configuración
Disponemos de cuatro clases de configuración:
- ApplicationConfiguration: donde se encuentran los Beans referentes a la autenticación del usuario
- JwtAuthenticationFilter: donde se encuentra la lógica del filtrado y validación del JSON Web Token
- SecurityConfiguration: donde se establece la política de CORS y también la lista blanca d'URLs que no requieren autenticación (JWT)
- SwaggerConfituration: donde se define la configuración de la documentación API.
  
### Servicios
Correspondientes a los dos controladores, disponemos de 2 servicios:
- AuthenticationService
- PetService
a los cuales se añade el JWTService, que contiene la lógica necesaria para encriptar y leer los tokens y recuperar la información que contienen.

### Control de excepciones
El control d'excepciones está centralizado en la classe GlobalExceptionHandler i devuelve para cada excepción capturada una respuesta HTTP adecuada.

### Tests
Se han implementado tests de los métodos del controlador PetController y del servicio PetServiceImpl. Para el servicio, los tests son unitarios y para el controlador, se han implementado tests de integración.

## Requisits tècnics
- MySQL WorkBench 8.0
- Maven: Apache Maven 3.9.9
- Java version 22
- Sistema operatiu: Windows

Projecte Maven amb les dependències següents:
- Aplicación no reactiva: org.springframework.boot:spring-boot-starter-data-jpa
- JSON Web Token: 3 dependencias de io.jsonwebtoken
- Seguridad: org.springframework.boot:spring-boot-starter-security
- DevTools: org.springframework.boot:spring-boot-devtools
- Conexiones a bases de datos: 
  SQL (JPA)--> com.mysql:mysql-connector-j
- Documentació Swagger --> io.swagger.core.v3:swagger-annotations, org.springdoc:springdoc-openapi-starter-webmvc-ui
- Validaciones: jakarta.validation:jakarta.validation-api
- Logger --> org.apache.logging.log4j:log4j-core, org.apache.logging.log4j:log4j-api
- Tests --> org.springframework.boot:spring-boot-starter-test, org.springframework.security:spring-security-test
## Instalación: 
1. Clonar el repositorio de Github
git clone https://github.com/g-lemoing/virtualPetBk.git
2. Abrir el IDE e importar el proyecto desde el repositorio local desde File > Open.
3. Abrir el MySQLWorkbench (descargarlo e instalarlo si necesario desde https://dev.mysql.com/downloads/) y crear una conexión si no existe. Importar el fichero virtual_pet_DDL.sql para crear la base de datos.
4. En la consola, navegar hasta el directorio del proyecto virtualPetBk, y ejecutar mvn clean install (o .\mvnw.cmd clean install) para garantizar la correcta instalación de las dependencias.
5. Actualizar el fichero application.properties con los parámetros de conexión a la base de datos MySQL en cas de ser distintas a las establecidas en este proyecto, y con la clave secreta para el JWT.

## Ejecución:
Localizar la clase Main y ejecutar virtualPetBkApplication.
Abrir el Swagger (http://localhost:8080/swagger-ui/index.html) donde se pueden probar los endpoints
Ver el Readme de la parte Front-end (https://github.com/g-lemoing/virtualPetFrontEnd) para instalarla y disfrutar de la aplicación en su totalidad.

## Contribucions:
1. Crear un fork del repositorio: 
2. Clonar el repositorio hacia el directorio local marcado por git bash
 git clone https://github.com/YOUR-USERNAME/virtualPetFrontEnd
3. Crear una rama
git branch BRANCH-NAME
git checkout BRANCH-NAME
4. Realizar cambios o comentarios, y hacer un commit: git commit -m 'mensaje cambios'
5. Subir cambios a tu nueva rama: git push origin BRANCH-NAME
6. Hacer un pull request
