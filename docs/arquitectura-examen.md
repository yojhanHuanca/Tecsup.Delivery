# Sistema de Gestion de Pedidos - API REST Segura

## Objetivo

Desarrollar una API REST con Spring Boot para administrar clientes, categorias,
productos, pedidos, detalles de pedido y auditoria, aplicando arquitectura en
capas, persistencia con MySQL, validaciones, manejo global de excepciones, AOP y
Spring Security con Basic Auth.

## Base de Datos

```sql
CREATE DATABASE evaluacion_springboot_relaciones;
```

## Estructura del Proyecto

```text
src/main/java/com/Tecsup
|
├── controller
├── service
├── repository
├── model
├── dto
├── config
├── security
├── exception
├── aspect
├── util
└── Application.java
```

## Responsabilidad de Cada Capa

### controller

Recibe peticiones HTTP, valida datos con `@Valid`, retorna respuestas con
`ResponseEntity` y delega la logica de negocio a `service`.

### service

Define interfaces con los contratos de negocio de cada modulo.

Las implementaciones concretas se ubicaran directamente en `service`, por
indicacion del proyecto, sin usar el paquete `service/impl`.

### repository

Contiene interfaces Spring Data JPA para acceder a MySQL.

### model

Contiene entidades JPA y relaciones entre tablas.

### dto

Contiene objetos de entrada y salida para no exponer directamente entidades JPA.

### config

Contiene configuraciones generales y beans compartidos.

Archivos:

```text
AppConfig.java
CorsConfig.java
```

`AppConfig` define beans generales como `PasswordEncoder`.

`CorsConfig` centraliza la configuracion CORS para clientes HTTP externos como
Postman, Thunder Client o una futura aplicacion frontend.

### security

Contiene configuracion de Basic Auth, roles, usuarios y reglas de acceso.

### exception

Contiene excepciones personalizadas y `GlobalExceptionHandler`.

### aspect

Contiene `LoggingAspect`, `ErrorAspect` y `AuditoriaAspect`.

### util

Contiene `DataLoader` y utilidades puntuales.

## Entidades Principales

```text
Cliente
Categoria
Producto
Pedido
DetallePedido
AuditoriaLog
Usuario
Rol
```

## Relaciones JPA

```text
Cliente 1 --- N Pedido
Categoria 1 --- N Producto
Pedido 1 --- N DetallePedido
Producto 1 --- N DetallePedido
Usuario N --- N Rol
```

Si se requiere evidenciar `@OneToOne`, se puede relacionar `Usuario` con
`Cliente` como perfil unico.

## Flujo CRUD

```text
Cliente HTTP
   ↓
Controller
   ↓
Service
   ↓
ServiceImpl
   ↓
Repository
   ↓
MySQL
```

## Flujo de Registro de Pedido

```text
POST /pedidos
   ↓
Validar cliente
   ↓
Validar productos
   ↓
Validar stock
   ↓
Calcular subtotal por detalle
   ↓
Calcular total del pedido
   ↓
Disminuir stock
   ↓
Guardar pedido y detalles
   ↓
Registrar auditoria
   ↓
Responder JSON
```

## Seguridad

```text
GET    -> ROLE_USER y ROLE_ADMIN
POST   -> ROLE_ADMIN
PUT    -> ROLE_ADMIN
DELETE -> ROLE_ADMIN
```

Usuarios iniciales:

```text
admin / admin123 -> ROLE_ADMIN
user  / user123  -> ROLE_USER
```

Las contrasenas se almacenaran con `BCryptPasswordEncoder`.

## Validaciones

Se usaran:

```text
@NotBlank
@NotNull
@Positive
@Min
@Email
```

## Excepciones

Se implementaran:

```text
ResourceNotFoundException
StockInsuficienteException
GlobalExceptionHandler
```

Las respuestas de error seran JSON.

## AOP

Se implementaran:

```text
LoggingAspect
ErrorAspect
AuditoriaAspect
```

## Orden de Desarrollo

1. Dependencias Maven.
2. application.properties.
3. Base de datos.
4. Entities.
5. Relaciones JPA.
6. Repositories.
7. Services.
8. ServiceImpl.
9. Controllers.
10. DTOs.
11. Validaciones.
12. Exceptions.
13. GlobalExceptionHandler.
14. Security.
15. AOP.
16. Auditoria.
17. DataLoader.
18. Pruebas Postman.
19. Evidencias.
20. GitHub.
