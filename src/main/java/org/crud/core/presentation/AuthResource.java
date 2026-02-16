package org.crud.core.presentation;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.crud.core.application.LoginEmpleadoUseCase;
import org.crud.core.application.RegistrarEmpleadoUseCase;

import org.crud.core.domain.model.Empleado;
import org.crud.core.presentation.dto.AuthRequestDTO;
import org.crud.core.presentation.dto.AuthResponseDTO;

@Path("/auth")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AuthResource {

    @Inject
    RegistrarEmpleadoUseCase registrarEmpleadoUseCase;

    @Inject
    LoginEmpleadoUseCase loginEmpleadoUseCase;

    @POST
    @Path("/register")
    public Response register(AuthRequestDTO request) {
        try {
            Empleado empleado = registrarEmpleadoUseCase.ejecutar(
                    request.getUsername(),
                    request.getPassword(),
                    request.getNombre(),
                    request.getCargo(),
                    request.getSalario(),
                    request.getRol() != null ? request.getRol() : "USER");

            return Response.status(Response.Status.CREATED)
                    .entity(new AuthResponseDTO(null, empleado.getId(), empleado.getUsername(), empleado.getRol()))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorMessage(e.getMessage()))
                    .build();
        }
    }

    @POST
    @Path("/login")
    public Response login(AuthRequestDTO request) {
        try {
            String token = loginEmpleadoUseCase.ejecutar(
                    request.getUsername(),
                    request.getPassword());

            return Response.ok(new AuthResponseDTO(token, null, request.getUsername(), null))
                    .build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(new ErrorMessage(e.getMessage()))
                    .build();
        }
    }

    public static class ErrorMessage {
        private String message;

        public ErrorMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }
}
