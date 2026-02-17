package org.crud.core.presentation;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.core.application.service.EmpleadoService;
import org.crud.core.presentation.dto.EmpleadoRequestDTO;
import org.crud.core.presentation.dto.EmpleadoResponseDTO;
import org.crud.core.presentation.mapper.EmpleadoDTOMapper;
import org.crud.core.domain.model.Empleado;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;
import java.util.stream.Collectors;

@Path("/api/empleados")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EmpleadoResource {

    @Inject
    EmpleadoService EmpleadoService;

    @Inject
    EmpleadoDTOMapper mapper;

    @Inject
    JsonWebToken jwt;

    @GET
    public Response listarTodos() {
        List<Empleado> empleados = EmpleadoService.listarEmpleados(
                jwt.getGroups(), getUsuarioIdFromToken());

        List<EmpleadoResponseDTO> response = empleados.stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());

        return Response.ok(response).build();
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        try {
            Empleado empleado = EmpleadoService.buscarPorId(id);
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
            Empleado empleado = mapper.toDomain(requestDTO);
            Long creadorId = getUsuarioIdFromToken();
            Empleado guardado = EmpleadoService.crear(empleado, creadorId);

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
    public Response actualizar(@PathParam("id") Long id,
            EmpleadoRequestDTO requestDTO) {
        try {
            Empleado empleado = mapper.toDomain(requestDTO);
            Long creadorId = getUsuarioIdFromToken();
            Empleado actualizado = EmpleadoService.actualizar(id, empleado, creadorId);

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
            Long creadorId = getUsuarioIdFromToken();
            EmpleadoService.eliminar(id, creadorId);
            return Response.noContent().build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(new ErrorResponse(e.getMessage()))
                    .build();
        }
    }

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
        return Long.parseLong(idClaim.toString());
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
