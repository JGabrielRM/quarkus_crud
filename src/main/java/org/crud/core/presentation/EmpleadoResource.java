package org.crud.core.presentation;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.core.commons.Constants;
import org.crud.core.presentation.dto.EmpleadoRequestDTO;
import org.crud.core.presentation.dto.EmpleadoResponseDTO;
import org.crud.core.presentation.mapper.EmpleadoDTOMapper;
import org.crud.core.application.*;
import org.crud.shared.EmpleadoEvent;
import org.crud.core.infrastructure.messaging.EmpleadoKafkaProducer;
import org.crud.core.domain.model.Empleado;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/empleados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {
    // Force recompile

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

    @Inject
    ListarEmpleadosPorCreadorUseCase listarEmpleadosPorCreadorUseCase;

    @Inject
    EmpleadoDTOMapper mapper;

    @Inject
    EmpleadoKafkaProducer kafkaProducer;

    @Inject
    JsonWebToken jwt;

    private Long getUsuarioIdFromToken() {
        Object idClaim = jwt.getClaim("id");
        if (idClaim == null) {
            throw new IllegalStateException("Token JWT no contiene claim 'id'");
        }
        if (idClaim instanceof Number) {
            return ((Number) idClaim).longValue();
        }
        if (idClaim instanceof jakarta.json.JsonNumber) {
            return ((jakarta.json.JsonNumber) idClaim).longValue();
        }
        // Fallback: intentar parsear como string
        return Long.parseLong(idClaim.toString());
    }

    @GET
    public Response listarTodos() {
        // Si es ADMIN, retorna TODOS los empleados
        if (jwt.getGroups().contains("ADMIN")) {
            List<Empleado> todos = listarTodosEmpleadosUseCase.ejecutar();
            List<EmpleadoResponseDTO> response = todos.stream()
                    .map(mapper::toResponseDTO)
                    .collect(Collectors.toList());
            return Response.ok(response).build();
        }

        // Si es USER, retorna solo los suyos
        Long creadorId = getUsuarioIdFromToken();
        List<Empleado> empleados = listarEmpleadosPorCreadorUseCase.ejecutar(creadorId);

        List<EmpleadoResponseDTO> response = empleados.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Empleado empleado = buscarEmpleadoPorIdUseCase.ejecutar(id);
            EmpleadoResponseDTO response = mapper.toResponseDTO(empleado);
            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @POST
    public Response crear(EmpleadoRequestDTO requestDTO) {
        try {
            // 1. Guardar en BD
            Empleado empleado = mapper.toDomain(requestDTO);
            Long creadorId = getUsuarioIdFromToken();
            empleado.setCreadorId(creadorId);
            Empleado guardado = crearEmpleadoUseCase.ejecutar(empleado);

            // 2. Notificar a Kafka
            EmpleadoEvent evento = new EmpleadoEvent(
                    guardado.getId(),
                    Constants.PROJECT_ACCION_CREAR,
                    guardado.getNombre(),
                    guardado.getCargo(),
                    guardado.getSalario(),
                    creadorId);
            kafkaProducer.enviar(evento);

            // 3. Retornar solo el empleado recién creado
            EmpleadoResponseDTO response = mapper.toResponseDTO(guardado);
            return Response.status(Response.Status.CREATED).entity(response).build();

        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity(new ErrorResponse("Error: " + e.getMessage()))
                    .build();
        }
    }

    @PUT
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("id") Long id, EmpleadoRequestDTO requestDTO) {
        try {
            // 1. Actualizar en BD
            Empleado empleado = mapper.toDomain(requestDTO);
            Empleado actualizado = actualizarEmpleadoUseCase.ejecutar(id, empleado);

            // 2. Notificar a Kafka
            Long creadorId = getUsuarioIdFromToken();
            EmpleadoEvent evento = new EmpleadoEvent(
                    actualizado.getId(),
                    Constants.PROJECT_ACCION_ACTUALIZAR,
                    actualizado.getNombre(),
                    actualizado.getCargo(),
                    actualizado.getSalario(),
                    creadorId);
            kafkaProducer.enviar(evento);

            // 3. Retornar solo el empleado actualizado
            EmpleadoResponseDTO response = mapper.toResponseDTO(actualizado);
            return Response.ok(response).build();

        } catch (IllegalArgumentException e) {
            Response.Status status = e.getMessage().contains("no encontrado")
                    ? Response.Status.NOT_FOUND
                    : Response.Status.BAD_REQUEST;

            return Response.status(status)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response eliminar(@PathParam("id") Long id) {
        try {
            // 1. Eliminar de BD
            eliminarEmpleadoUseCase.ejecutar(id);

            // 2. Notificar a Kafka
            Long creadorId = getUsuarioIdFromToken();
            EmpleadoEvent evento = new EmpleadoEvent(id, Constants.PROJECT_ACCION_ELIMINAR, creadorId);
            kafkaProducer.enviar(evento);

            // 3. Retornar 204 No Content
            return Response.noContent().build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

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
