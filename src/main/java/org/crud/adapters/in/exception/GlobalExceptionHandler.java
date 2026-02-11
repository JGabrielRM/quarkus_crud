package org.crud.adapters.in.exception;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.crud.domain.exception.DatosInvalidosException;
import org.crud.domain.exception.EmpleadoNoEncontradoException;

/**
 * Manejador global de excepciones para la API REST.
 *
 * ¿Qué es un ExceptionMapper?
 * - Intercepta excepciones lanzadas en los endpoints REST
 * - Las convierte en respuestas HTTP apropiadas
 * - Evita exponer stacktraces al cliente (seguridad)
 * - Centraliza el manejo de errores (DRY)
 *
 * @Provider: Registra esta clase como un proveedor JAX-RS
 * Quarkus lo detecta automáticamente y lo usa para todas las excepciones
 */
@Provider
public class GlobalExceptionHandler {

    /**
     * Mapper para EmpleadoNoEncontradoException → 404 Not Found
     */
    @Provider
    public static class EmpleadoNoEncontradoExceptionMapper
        implements ExceptionMapper<EmpleadoNoEncontradoException> {

        @Override
        public Response toResponse(EmpleadoNoEncontradoException exception) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity(new ErrorResponse(
                    exception.getMessage(),
                    Response.Status.NOT_FOUND.getStatusCode()
                ))
                .build();
        }
    }

    /**
     * Mapper para DatosInvalidosException → 400 Bad Request
     */
    @Provider
    public static class DatosInvalidosExceptionMapper
        implements ExceptionMapper<DatosInvalidosException> {

        @Override
        public Response toResponse(DatosInvalidosException exception) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(new ErrorResponse(
                    exception.getMessage(),
                    Response.Status.BAD_REQUEST.getStatusCode()
                ))
                .build();
        }
    }

    /**
     * Mapper para excepciones genéricas → 500 Internal Server Error
     * IMPORTANTE: Solo para errores inesperados, NO para errores de negocio
     */
    @Provider
    public static class GenericExceptionMapper
        implements ExceptionMapper<Exception> {

        @Override
        public Response toResponse(Exception exception) {
            // En producción, NO expongas el mensaje real (puede contener info sensible)
            // En desarrollo, puedes mostrar más detalles
            String message = "Error interno del servidor";

            // Log del error real (para debugging)
            exception.printStackTrace();

            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse(
                    message,
                    Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()
                ))
                .build();
        }
    }

    /**
     * Clase para respuestas de error estandarizadas.
     *
     * Formato JSON:
     * {
     *   "message": "Descripción del error",
     *   "status": 404
     * }
     */
    public static class ErrorResponse {
        private String message;
        private int status;

        public ErrorResponse(String message, int status) {
            this.message = message;
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
