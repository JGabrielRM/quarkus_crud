# 🏗️ Arquitectura Hexagonal - Proyecto Empleados

## 📐 Diagrama de Arquitectura

```
┌──────────────────────────────────────────────────────────────┐
│                      CLIENTE (Navegador/App)                  │
└───────────────────────┬──────────────────────────────────────┘
                        │ HTTP Request (JSON)
                        ↓
┌────────────────────────────────────────────────────────────────┐
│                   ADAPTADORES IN (Puerto de Entrada)           │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ EmpleadoResource (REST API)                              │ │
│  │ - GET /api/empleados                                     │ │
│  │ - POST /api/empleados                                    │ │
│  │ - PUT /api/empleados/{id}                                │ │
│  │ - DELETE /api/empleados/{id}                             │ │
│  └──────────────────────────────────────────────────────────┘ │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ DTOs (Request/Response)                                  │ │
│  │ - EmpleadoRequestDTO                                     │ │
│  │ - EmpleadoResponseDTO                                    │ │
│  └──────────────────────────────────────────────────────────┘ │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ Mappers                                                  │ │
│  │ - EmpleadoDTOMapper (DTO ↔ Domain)                      │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────┬────────────────────────────────────────┘
                        │ Llamadas a Casos de Uso
                        ↓
┌────────────────────────────────────────────────────────────────┐
│              CAPA DE APLICACIÓN (Casos de Uso)                 │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ CrearEmpleadoUseCase                                     │ │
│  │ ActualizarEmpleadoUseCase                                │ │
│  │ BuscarEmpleadoPorIdUseCase                               │ │
│  │ ListarTodosEmpleadosUseCase                              │ │
│  │ EliminarEmpleadoUseCase                                  │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────┬────────────────────────────────────────┘
                        │ Usa puertos (interfaces)
                        ↓
┌────────────────────────────────────────────────────────────────┐
│                   CAPA DE DOMINIO (Núcleo)                     │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ Modelo                                                   │ │
│  │ - Empleado (POJO puro, sin anotaciones)                 │ │
│  │   · isValid()                                            │ │
│  │   · calcularSalarioAnual()                               │ │
│  └──────────────────────────────────────────────────────────┘ │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ Puertos (Interfaces)                                     │ │
│  │ - EmpleadoRepository                                     │ │
│  │   · save(Empleado)                                       │ │
│  │   · delete(Empleado)                                     │ │
│  │   · findById(Long)                                       │ │
│  │   · findAll()                                            │ │
│  └──────────────────────────────────────────────────────────┘ │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ Excepciones de Dominio                                   │ │
│  │ - EmpleadoNoEncontradoException                          │ │
│  │ - DatosInvalidosException                                │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────┬────────────────────────────────────────┘
                        ↑ Implementa puertos
                        │
┌────────────────────────────────────────────────────────────────┐
│                  ADAPTADORES OUT (Puerto de Salida)            │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ EmpleadoJpaRepository (implementa EmpleadoRepository)    │ │
│  │ - Usa EntityManager de JPA                               │ │
│  │ - Convierte Domain ↔ Entity usando mapper                │ │
│  └──────────────────────────────────────────────────────────┘ │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ EmpleadoEntity (Entidad JPA)                             │ │
│  │ - @Entity, @Id, @Column                                  │ │
│  │ - Mapeo con tabla "empleados"                            │ │
│  └──────────────────────────────────────────────────────────┘ │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ Mappers                                                  │ │
│  │ - EmpleadoEntityMapper (Domain ↔ Entity)                │ │
│  └──────────────────────────────────────────────────────────┘ │
└───────────────────────┬────────────────────────────────────────┘
                        │ Operaciones SQL
                        ↓
┌────────────────────────────────────────────────────────────────┐
│                   BASE DE DATOS (PostgreSQL/MySQL/H2)          │
│  ┌──────────────────────────────────────────────────────────┐ │
│  │ Tabla: empleados                                         │ │
│  │ - id (BIGINT, AUTO_INCREMENT)                            │ │
│  │ - nombre (VARCHAR, NOT NULL)                             │ │
│  │ - cargo (VARCHAR, NOT NULL)                              │ │
│  │ - salario (DOUBLE, NOT NULL)                             │ │
│  └──────────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────┘
```

## 🔄 Flujo de Datos - Ejemplo: Crear Empleado

### Request → Response

```
1. Cliente envía POST /api/empleados
   {
     "nombre": "Juan Pérez",
     "cargo": "Desarrollador",
     "salario": 50000.0
   }

2. EmpleadoResource (Adaptador IN)
   - Recibe EmpleadoRequestDTO
   - Usa EmpleadoDTOMapper para convertir a Empleado (Domain)

3. CrearEmpleadoUseCase (Aplicación)
   - Valida el empleado (isValid())
   - Si es inválido, lanza DatosInvalidosException
   - Llama a empleadoRepository.save(empleado)

4. EmpleadoJpaRepository (Adaptador OUT)
   - Usa EmpleadoEntityMapper para convertir Empleado → EmpleadoEntity
   - Llama a entityManager.persist(entity)
   - Convierte EmpleadoEntity → Empleado y lo devuelve

5. Base de Datos
   - Inserta registro en tabla empleados
   - Genera ID automático (ej: id = 1)

6. Respuesta inversa
   - EmpleadoJpaRepository devuelve Empleado con ID
   - CrearEmpleadoUseCase devuelve Empleado
   - EmpleadoResource convierte Empleado → EmpleadoResponseDTO
   - Responde 201 Created con JSON:
   {
     "id": 1,
     "nombre": "Juan Pérez",
     "cargo": "Desarrollador",
     "salario": 50000.0
   }
```

## 🎯 Principios SOLID Aplicados

### 1. **S** - Single Responsibility Principle
- **Cada caso de uso** tiene UNA responsabilidad
- **EmpleadoResource** solo orquesta (no tiene lógica de negocio)
- **EmpleadoJpaRepository** solo persiste (no valida)

### 2. **O** - Open/Closed Principle
- Puedes agregar nuevos casos de uso sin modificar los existentes
- Puedes cambiar la implementación del repositorio sin tocar el dominio

### 3. **L** - Liskov Substitution Principle
- Cualquier implementación de `EmpleadoRepository` es intercambiable
- Puedes usar JpaRepository, MongoRepository, InMemoryRepository, etc.

### 4. **I** - Interface Segregation Principle
- Los puertos (interfaces) son pequeños y específicos
- `EmpleadoRepository` solo tiene métodos relacionados con Empleado

### 5. **D** - Dependency Inversion Principle
- **La capa de aplicación depende de interfaces (puertos), NO de implementaciones**
- El flujo de dependencias va hacia adentro (hacia el dominio)

## 📂 Estructura de Carpetas

```
src/main/java/org/crud/
├── domain/                          # NÚCLEO (independiente)
│   ├── model/
│   │   └── Empleado.java           # Modelo de dominio (POJO)
│   ├── port/
│   │   └── EmpleadoRepository.java # Puerto (interfaz)
│   └── exception/
│       ├── EmpleadoNoEncontradoException.java
│       └── DatosInvalidosException.java
│
├── application/                     # CASOS DE USO
│   └── usecase/
│       ├── CrearEmpleadoUseCase.java
│       ├── ActualizarEmpleadoUseCase.java
│       ├── BuscarEmpleadoPorIdUseCase.java
│       ├── ListarTodosEmpleadosUseCase.java
│       └── EliminarEmpleadoUseCase.java
│
└── adapters/                        # INFRAESTRUCTURA
    ├── in/                          # Puertos de ENTRADA
    │   ├── EmpleadoResource.java   # REST API (JAX-RS)
    │   ├── dto/
    │   │   ├── EmpleadoRequestDTO.java
    │   │   └── EmpleadoResponseDTO.java
    │   ├── mapper/
    │   │   └── EmpleadoDTOMapper.java
    │   └── exception/
    │       └── GlobalExceptionHandler.java
    │
    └── out/                         # Puertos de SALIDA
        ├── EmpleadoJpaRepository.java  # Implementación JPA
        ├── EmpleadoEntity.java         # Entidad JPA
        └── mapper/
            └── EmpleadoEntityMapper.java
```

## ✅ Ventajas de esta Arquitectura

### 1. **Independencia de Frameworks**
- El dominio NO depende de Quarkus, JPA, JAX-RS, etc.
- Puedes cambiar de framework sin tocar el núcleo

### 2. **Testabilidad**
- Puedes probar el dominio sin levantar BD
- Puedes usar mocks fácilmente
- Tests unitarios rápidos

### 3. **Mantenibilidad**
- Código organizado y fácil de entender
- Responsabilidades claras
- Fácil de navegar

### 4. **Escalabilidad**
- Puedes agregar nuevos adaptadores (GraphQL, gRPC, etc.)
- Puedes agregar nuevos casos de uso sin romper nada
- Múltiples implementaciones del mismo puerto

### 5. **Flexibilidad**
- Cambiar de BD (PostgreSQL → MongoDB) es trivial
- Agregar cache, logging, seguridad es más fácil
- Evolucionas el sistema de forma ordenada

## 🔥 Mejoras Futuras Recomendadas

### 1. **Validación con Bean Validation**
```java
// En EmpleadoRequestDTO
@NotNull
@Size(min = 3, max = 100)
private String nombre;

@Min(0)
private Double salario;
```

### 2. **Paginación en findAll()**
```java
Page<Empleado> findAll(Pageable pageable);
```

### 3. **Auditoría (Quién y cuándo modificó)**
```java
@CreationTimestamp
private LocalDateTime creadoEn;

@UpdateTimestamp
private LocalDateTime actualizadoEn;
```

### 4. **Testing**
- Tests unitarios de casos de uso
- Tests de integración con Testcontainers
- Tests de API con RestAssured

### 5. **Documentación API con OpenAPI**
```java
@Operation(summary = "Crear empleado")
@APIResponse(responseCode = "201", description = "Empleado creado")
```

### 6. **Seguridad**
- JWT authentication
- Role-based access control
- Rate limiting

## 🚀 Cómo Probar la API

### Crear empleado
```bash
curl -X POST http://localhost:8080/api/empleados \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez",
    "cargo": "Desarrollador",
    "salario": 50000.0
  }'
```

### Listar todos
```bash
curl http://localhost:8080/api/empleados
```

### Obtener por ID
```bash
curl http://localhost:8080/api/empleados/1
```

### Actualizar
```bash
curl -X PUT http://localhost:8080/api/empleados/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nombre": "Juan Pérez Updated",
    "cargo": "Senior Developer",
    "salario": 60000.0
  }'
```

### Eliminar
```bash
curl -X DELETE http://localhost:8080/api/empleados/1
```

## 📚 Referencias

- **Hexagonal Architecture**: Alistair Cockburn
- **Clean Architecture**: Robert C. Martin (Uncle Bob)
- **Domain-Driven Design**: Eric Evans
- **SOLID Principles**: Robert C. Martin
