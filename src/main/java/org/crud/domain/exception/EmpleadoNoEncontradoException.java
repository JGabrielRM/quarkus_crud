package org.crud.domain.exception;

/**
 * Excepción de dominio cuando no se encuentra un empleado.
 *
 * ¿Por qué crear excepciones personalizadas?
 * - Más expresivas que IllegalArgumentException genérico
 * - Permiten manejo diferenciado (404 vs 400 vs 500)
 * - Documentan los errores que puede lanzar tu dominio
 * - Facilitan debugging con mensajes claros
 *
 * RuntimeException vs Exception:
 * - RuntimeException: NO necesitas declararla en throws (unchecked)
 * - Exception: Debes declararla en throws (checked)
 * - Para errores de negocio, RuntimeException es más conveniente
 */
public class EmpleadoNoEncontradoException extends RuntimeException {

    public EmpleadoNoEncontradoException(Long id) {
        super("Empleado con ID " + id + " no encontrado");
    }

    public EmpleadoNoEncontradoException(String message) {
        super(message);
    }
}
