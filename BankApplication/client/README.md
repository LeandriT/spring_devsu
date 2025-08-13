
# Client Service

Microservicio encargado de la gestión de datos de clientes en un sistema financiero.

## 🛠️ Tecnologías

- Java 11
- Spring Boot
- Spring Data JPA
- H2 Database (dev/test)
- Rest template
- Maven
- Lombok
- JUnit 5

## 🔄 Endpoints REST

### ✅ `ClientController`
Ruta base: `/api/clients`

| Método | Ruta    | Descripción                         |
|--------|---------|-------------------------------------|
| GET    | `/`     | Listar clientes                     |
| GET    | `/{id}` | Obtener cliente por ID              |
| POST   | `/`     | Crear un nuevo cliente              |
| PUT    | `/{id}` | Actualizar cliente                  |
| PATCH  | `/{id}` | Actualización parcial de cliente    |
| DELETE | `/{id}` | Eliminar cliente                    |

## 🧾 Estructura de DTOs

### `ClientDto` (entrada)

```json
{
  "dni": "1234567890",
  "name": "John Doe",
  "password": "my_secure_password",
  "gender": "Male",
  "age": 30,
  "address": "123 Main St, Anytown, USA",
  "phone": "555-123-4567",
  "active": true
}
```

### `ClientDto` (respuesta)

```json
{
  "id": 1,
  "dni": "1234567890",
  "name": "John Doe",
  "password": "LJqNAvwXrnfpJtOP6Dw1KdZjjR1jY3lQPwxkAOBjRF8=",
  "gender": "Male",
  "age": 30,
  "address": "123 Main St, Anytown, USA",
  "phone": "555-123-4567",
  "active": true
}
```

## 🧑‍💻 Autor

- Desarrollado por **Gandhy Cuasapás**