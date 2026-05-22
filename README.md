# Sistema de Gestion de Pedidos - API REST Segura

Proyecto academico desarrollado con Spring Boot para gestionar clientes,
categorias, productos, pedidos, detalle de pedidos, seguridad y auditoria.

## Tecnologias

- Java 21
- Spring Boot 3.5.14
- Spring Web
- Spring Data JPA
- Hibernate
- MySQL
- Spring Security
- Basic Auth
- Lombok
- Spring AOP
- Maven

## Base de Datos

Nombre de la base de datos:

```sql
evaluacion_springboot_relaciones
```

Script:

```sql
CREATE DATABASE IF NOT EXISTS evaluacion_springboot_relaciones;
```

El proyecto tambien puede crear la base de datos automaticamente porque la URL
JDBC usa `createDatabaseIfNotExist=true`.

## Configuracion

Archivo:

```text
src/main/resources/application.properties
```

Configuracion actual:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/evaluacion_springboot_relaciones?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Lima
spring.datasource.username=root
spring.datasource.password=
```

Si tu MySQL tiene clave, cambia:

```properties
spring.datasource.password=TU_PASSWORD
```

## Ejecucion

Primero verifica que `JAVA_HOME` apunte a JDK 21.

En PowerShell:

```powershell
$env:JAVA_HOME='C:\Program Files\Eclipse Adoptium\jdk-21.0.11.10-hotspot'
.\mvnw.cmd spring-boot:run
```

La API se ejecuta en:

```text
http://localhost:8080
```

## Usuarios Iniciales

Se crean automaticamente con `DataLoader`.

| Usuario | Password | Rol |
|---|---|---|
| admin | admin123 | ROLE_ADMIN |
| user | user123 | ROLE_USER |

Las contrasenas se guardan con `BCryptPasswordEncoder`.

## Seguridad

La API usa Basic Auth.

La configuracion general esta separada en:

```text
src/main/java/com/Tecsup/config/AppConfig.java
src/main/java/com/Tecsup/config/CorsConfig.java
src/main/java/com/Tecsup/security/SecurityConfig.java
```

`AppConfig` contiene el `BCryptPasswordEncoder`, `CorsConfig` configura CORS y
`SecurityConfig` define las reglas de acceso por rol y metodo HTTP.

| Metodo | Acceso |
|---|---|
| GET | ROLE_USER, ROLE_ADMIN |
| POST | ROLE_ADMIN |
| PUT | ROLE_ADMIN |
| DELETE | ROLE_ADMIN |

## Endpoints

### Clientes

```text
GET    /clientes
GET    /clientes/{id}
POST   /clientes
PUT    /clientes/{id}
DELETE /clientes/{id}
```

### Categorias

```text
GET    /categorias
GET    /categorias/{id}
POST   /categorias
PUT    /categorias/{id}
DELETE /categorias/{id}
```

### Productos

```text
GET    /productos
GET    /productos/{id}
POST   /productos
PUT    /productos/{id}
DELETE /productos/{id}
```

### Pedidos

```text
GET    /pedidos
GET    /pedidos/{id}
POST   /pedidos
DELETE /pedidos/{id}
```

### Auditoria

```text
GET /auditoria
```

## Ejemplos JSON

### Crear Cliente

```json
{
  "nombres": "Juan",
  "apellidos": "Perez",
  "correo": "juan.perez@correo.com",
  "telefono": "987654321",
  "direccion": "Av. Principal 123"
}
```

### Crear Categoria

```json
{
  "nombre": "Bebidas"
}
```

### Crear Producto

```json
{
  "nombre": "Agua Mineral",
  "descripcion": "Botella de 625 ml",
  "precio": 2.50,
  "stock": 30,
  "categoriaId": 1
}
```

### Crear Pedido

```json
{
  "clienteId": 1,
  "detalles": [
    {
      "productoId": 1,
      "cantidad": 2
    }
  ]
}
```

## Logica del Pedido

Al registrar un pedido:

1. Valida que el cliente exista.
2. Valida que cada producto exista.
3. Valida stock suficiente.
4. Calcula subtotal por detalle.
5. Calcula total del pedido.
6. Disminuye automaticamente el stock.
7. Guarda pedido y detalles en una transaccion.

Formula:

```text
subtotal = cantidad * precio
total = suma de subtotales
```

## Validaciones

Se usan:

- `@NotBlank`
- `@NotNull`
- `@Positive`
- `@Min`
- `@Email`
- `@NotEmpty`

## Excepciones

Se implemento:

- `ResourceNotFoundException`
- `StockInsuficienteException`
- `BusinessException`
- `GlobalExceptionHandler`

Las respuestas de error son JSON.

## AOP

Se implemento:

- `LoggingAspect`: registra inicio y fin de metodos en service.
- `ErrorAspect`: registra errores producidos en service.
- `AuditoriaAspect`: registra POST, PUT y DELETE en `auditoria_log`.

## Entregables

Archivos incluidos:

```text
docs/arquitectura-examen.md
docs/script.sql
docs/TecsupDelivery.postman_collection.json
```

## Evidencias Recomendadas

Capturas sugeridas para entregar:

1. Base de datos creada.
2. Tablas relacionadas.
3. CRUD de clientes.
4. CRUD de productos.
5. Registro de pedido.
6. Disminucion automatica de stock.
7. Validaciones funcionando.
8. Seguridad con Basic Auth.
9. Acceso denegado con usuario ROLE_USER en POST.
10. Registros en `auditoria_log`.
