package org.crud.domain.exception;

/**
 * Excepción de dominio cuando los datos son inválidos.
 *
 * Ejemplos:
 * - Empleado sin nombre
 * - Salario negativo
 * - Campos obligatorios vacíos
 */
public class DatosInvalidosException extends RuntimeException {

    public DatosInvalidosException(String message) {
        super(message);
    }

    public DatosInvalidosException(String message, Throwable cause) {
        super(message, cause);
    }
}
