package org.crud.adapters.in;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.adapters.in.dto.EmpleadoRequestDTO;
import org.crud.adapters.in.dto.EmpleadoResponseDTO;
import org.crud.adapters.in.mapper.EmpleadoDTOMapper;
import org.crud.application.usecases.*;
import org.crud.domain.model.Empleado;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Adaptador IN tipo REST API para gestionar empleados.
 *
 * ¿Qué es un adaptador IN?
 * - Es el PUNTO DE ENTRADA a la aplicación (REST, GraphQL, CLI, etc.)
 * - Recibe peticiones del exterior y las traduce a llamadas de casos de uso
 * - NO contiene lógica de negocio, solo orquesta
 * - Devuelve respuestas en el formato esperado por el cliente
 *
 * @Path: Define la ruta base del recurso REST
 * @Produces/@Consumes: Especifica que trabaja con JSON
 */
@Path("/api/empleados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {

    // Inyectamos los casos de uso específicos, NO un servicio genérico
    @Inject
    CrearEmpleadoUseCase crearEmpleadoUseCase;

    @Inject
    ActualizarEmpleadoUseCase actualizarEmpleadoUseCase;

    @Inject
    BuscarEmpleadoPorIdUseCase buscarEmpleadoPorIdUseCase;

    @Inject
    ListarTodosEmpleadosUseCase listarTodosEmpleadosUseCase;

    @Inject
    EliminarEmpleadoUseCase eliminarEmpleadoUseCase;

    // Mapper para convertir entre DTO y Domain
    @Inject
    EmpleadoDTOMapper mapper;

    /**
     * GET /api/empleados
     * Lista todos los empleados.
     *
     * @return 200 OK con lista de empleados
     */
    @GET
    public Response listarTodos() {
        // 1. Ejecutar caso de uso (obtiene lista de Domain)
        List<Empleado> empleados = listarTodosEmpleadosUseCase.ejecutar();

        // 2. Convertir de Domain a DTO usando Stream API
        List<EmpleadoResponseDTO> response = empleados.stream()
            .map(mapper::toResponseDTO)
            .collect(Collectors.toList());

        // 3. Devolver respuesta HTTP 200 OK
        return Response.ok(response).build();
    }

    /**
     * GET /api/empleados/{id}
     * Obtiene un empleado por su ID.
     *
     * @PathParam: Extrae el valor del ID desde la URL
     * @param id ID del empleado
     * @return 200 OK con el empleado, o 404 Not Found si no existe
     */
    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            // 1. Ejecutar caso de uso
            Empleado empleado = buscarEmpleadoPorIdUseCase.ejecutar(id);

            // 2. Convertir a DTO
            EmpleadoResponseDTO response = mapper.toResponseDTO(empleado);

            // 3. Devolver 200 OK
            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            // Si no existe, devolver 404 Not Found
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
        }
    }

    /**
     * POST /api/empleados
     * Crea un nuevo empleado.
     *
     * Flujo:
     * 1. Cliente envía JSON → se deserializa a EmpleadoRequestDTO
     * 2. DTO → Domain (usando mapper)
     * 3. Ejecutar caso de uso con objeto Domain
     * 4. Domain → DTO de respuesta
     * 5. Devolver 201 Created con Location header
     *
     * @param requestDTO Datos del empleado a crear
     * @return 201 Created con el empleado creado y header Location
     */
    @POST
    public Response crear(EmpleadoRequestDTO requestDTO) {
        try {
            // 1. Convertir DTO → Domain
            Empleado empleado = mapper.toDomain(requestDTO);

            // 2. Ejecutar caso de uso
            Empleado empleadoCreado = crearEmpleadoUseCase.ejecutar(empleado);

            // 3. Convertir Domain → DTO
            EmpleadoResponseDTO response = mapper.toResponseDTO(empleadoCreado);

            // 4. Devolver 201 Created con Location header
            // Location: /api/empleados/123
            return Response.created(
                URI.create("/api/empleados/" + empleadoCreado.getId())
            ).entity(response).build();

        } catch (IllegalArgumentException e) {
            // Si los datos son inválidos, devolver 400 Bad Request
            return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
        }
    }

    /**
     * PUT /api/empleados/{id}
     * Actualiza un empleado existente completamente.
     *
     * PUT vs PATCH:
     * - PUT: Reemplaza el recurso completo
     * - PATCH: Actualiza solo campos específicos
     *
     * @param id ID del empleado a actualizar
     * @param requestDTO Nuevos datos del empleado
     * @return 200 OK con el empleado actualizado, o 404/400 en error
     */
    @PUT
    @Path("/{id}")
    public Response actualizar(@PathParam("id") Long id, EmpleadoRequestDTO requestDTO) {
        try {
            // 1. Convertir DTO → Domain
            Empleado empleado = mapper.toDomain(requestDTO);

            // 2. Ejecutar caso de uso
            Empleado empleadoActualizado = actualizarEmpleadoUseCase.ejecutar(id, empleado);

            // 3. Convertir Domain → DTO
            EmpleadoResponseDTO response = mapper.toResponseDTO(empleadoActualizado);

            // 4. Devolver 200 OK
            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            // Puede ser 404 (no existe) o 400 (datos inválidos)
            Response.Status status = e.getMessage().contains("no encontrado")
                ? Response.Status.NOT_FOUND
                : Response.Status.BAD_REQUEST;

            return Response.status(status)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
        }
    }

    /**
     * DELETE /api/empleados/{id}
     * Elimina un empleado.
     *
     * @param id ID del empleado a eliminar
     * @return 204 No Content si se eliminó, o 404 si no existe
     */
    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            // Ejecutar caso de uso
            eliminarEmpleadoUseCase.ejecutar(id);

            // 204 No Content: operación exitosa sin cuerpo de respuesta
            return Response.noContent().build();

        } catch (IllegalArgumentException e) {
            // Si no existe, devolver 404
            return Response.status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(e.getMessage()))
                .build();
        }
    }

    /**
     * Clase interna para respuestas de error.
     * Devuelve JSON con formato: {"message": "descripción del error"}
     */
    public static class ErrorResponse {
        private String message;

        public ErrorResponse(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
