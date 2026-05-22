CREATE DATABASE IF NOT EXISTS evaluacion_springboot_relaciones;

USE evaluacion_springboot_relaciones;

CREATE TABLE IF NOT EXISTS cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(100) NOT NULL,
    apellidos VARCHAR(100) NOT NULL,
    correo VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL,
    direccion VARCHAR(200) NOT NULL
);

CREATE TABLE IF NOT EXISTS categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(250) NOT NULL,
    precio DECIMAL(10, 2) NOT NULL,
    stock INT NOT NULL,
    categoria_id BIGINT NOT NULL,
    CONSTRAINT fk_producto_categoria
        FOREIGN KEY (categoria_id)
        REFERENCES categoria(id)
);

CREATE TABLE IF NOT EXISTS pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    total DECIMAL(10, 2) NOT NULL,
    cliente_id BIGINT NOT NULL,
    CONSTRAINT fk_pedido_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente(id)
);

CREATE TABLE IF NOT EXISTS detalle_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cantidad INT NOT NULL,
    subtotal DECIMAL(10, 2) NOT NULL,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    CONSTRAINT fk_detalle_pedido
        FOREIGN KEY (pedido_id)
        REFERENCES pedido(id),
    CONSTRAINT fk_detalle_producto
        FOREIGN KEY (producto_id)
        REFERENCES producto(id)
);

CREATE TABLE IF NOT EXISTS auditoria_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    accion VARCHAR(80) NOT NULL,
    metodo VARCHAR(200) NOT NULL,
    fecha DATETIME NOT NULL,
    usuario VARCHAR(80) NOT NULL
);

CREATE TABLE IF NOT EXISTS rol (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE
);

CREATE TABLE IF NOT EXISTS usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(80) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    enabled BIT NOT NULL,
    cliente_id BIGINT UNIQUE,
    CONSTRAINT fk_usuario_cliente
        FOREIGN KEY (cliente_id)
        REFERENCES cliente(id)
);

CREATE TABLE IF NOT EXISTS usuario_rol (
    usuario_id BIGINT NOT NULL,
    rol_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id, rol_id),
    CONSTRAINT fk_usuario_rol_usuario
        FOREIGN KEY (usuario_id)
        REFERENCES usuario(id),
    CONSTRAINT fk_usuario_rol_rol
        FOREIGN KEY (rol_id)
        REFERENCES rol(id)
);

INSERT INTO rol (nombre)
SELECT 'ROLE_ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre = 'ROLE_ADMIN'
);

INSERT INTO rol (nombre)
SELECT 'ROLE_USER'
WHERE NOT EXISTS (
    SELECT 1 FROM rol WHERE nombre = 'ROLE_USER'
);

INSERT INTO usuario (username, password, enabled)
SELECT
    'admin',
    '$2a$10$FzzQOLnex9o9ULYb7TaAQeqzcZYz5kYjZvpl5guWW5S6pgv0FcNu6',
    1
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE username = 'admin'
);

INSERT INTO usuario (username, password, enabled)
SELECT
    'user',
    '$2a$10$mLVoEYoIU4NVP5LkYyPNjuIeBCdhI5CDnTZXpABxYvsZLvX8V4HiG',
    1
WHERE NOT EXISTS (
    SELECT 1 FROM usuario WHERE username = 'user'
);

INSERT INTO usuario_rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuario u
JOIN rol r ON r.nombre = 'ROLE_ADMIN'
WHERE u.username = 'admin'
  AND NOT EXISTS (
      SELECT 1
      FROM usuario_rol ur
      WHERE ur.usuario_id = u.id
        AND ur.rol_id = r.id
  );

INSERT INTO usuario_rol (usuario_id, rol_id)
SELECT u.id, r.id
FROM usuario u
JOIN rol r ON r.nombre = 'ROLE_USER'
WHERE u.username = 'user'
  AND NOT EXISTS (
      SELECT 1
      FROM usuario_rol ur
      WHERE ur.usuario_id = u.id
        AND ur.rol_id = r.id
  );

INSERT INTO cliente (nombres, apellidos, correo, telefono, direccion)
SELECT 'Juan', 'Perez', 'juan.perez@correo.com', '987654321', 'Av. Principal 123'
WHERE NOT EXISTS (
    SELECT 1 FROM cliente WHERE correo = 'juan.perez@correo.com'
);

INSERT INTO categoria (nombre)
SELECT 'Bebidas'
WHERE NOT EXISTS (
    SELECT 1 FROM categoria WHERE nombre = 'Bebidas'
);

INSERT INTO producto (nombre, descripcion, precio, stock, categoria_id)
SELECT 'Agua Mineral', 'Botella de 625 ml', 2.50, 30, c.id
FROM categoria c
WHERE c.nombre = 'Bebidas'
  AND NOT EXISTS (
      SELECT 1 FROM producto WHERE nombre = 'Agua Mineral'
  );
