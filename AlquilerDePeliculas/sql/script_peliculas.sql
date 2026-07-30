CREATE DATABASE IF NOT EXISTS sistema_peliculas CHARACTER SET utf8mb4;
USE sistema_peliculas;

-- Módulo 1: Películas
CREATE TABLE IF NOT EXISTS pelicula (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(150) NOT NULL,
    genero VARCHAR(50) NOT NULL,
    precio_alquiler DECIMAL(6,2) NOT NULL
);

-- Módulo 2: Clientes
CREATE TABLE IF NOT EXISTS cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    dni VARCHAR(15) NOT NULL UNIQUE,
    telefono VARCHAR(20) NOT NULL
);

-- Módulo 3: Alquileres (Relaciona Película y Cliente)
CREATE TABLE IF NOT EXISTS alquiler (
    id INT AUTO_INCREMENT PRIMARY KEY,
    pelicula_id INT NOT NULL,
    cliente_id INT NOT NULL,
    fecha_alquiler DATE NOT NULL,
    FOREIGN KEY (pelicula_id) REFERENCES pelicula(id) ON DELETE CASCADE,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE
);

-- Registros iniciales
INSERT INTO pelicula (titulo, genero, precio_alquiler) VALUES 
('Interstellar', 'Ciencia Ficción', 12.50),
('El Padrino', 'Drama', 10.00);

INSERT INTO cliente (nombre, dni, telefono) VALUES 
('Hector Diego', '75849302', '987654321'),
('Ana Maria', '84732910', '912345678');