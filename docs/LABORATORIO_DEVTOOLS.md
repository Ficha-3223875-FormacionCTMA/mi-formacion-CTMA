# 3.3 Laboratorio 1 - Análisis con Browser DevTools

**Responsable:** Juan Goez  
**Entorno de ejecución:** Google Chrome DevTools  
**Servicios inspeccionados:** `https://dummyjson.com/docs/todos` y `https://dummyjson.com/docs/auth`

---

## 1. Registro de Peticiones HTTP (Pestaña Network)

### Caso Positivo: Consulta General de Tareas (`GET /todos`)
* **URL:** `https://dummyjson.com/todos`
* **Método HTTP:** `GET`
* **Status Code:** `200 OK`
* **Request Headers:**
    * `Accept: */*`
    * `User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64)`
* **Response Body (Extracto):**
  ```json
  {
    "todos": [
      {
        "id": 1,
        "todo": "Do something nice for someone I care about",
        "completed": true,
        "userId": 26
      }
    ],
    "total": 254,
    "skip": 0,
    "limit": 30
  }