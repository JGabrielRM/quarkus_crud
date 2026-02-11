package org.crud.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.crud.domain.exception.DatosInvalidosException;
import org.crud.domain.exception.EmpleadoNoEncontradoException;
import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

/**
 * Caso de uso: Eliminar un empleado
 */
@ApplicationScoped
public class EliminarEmpleadoUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    /**
     * Ejecuta el caso de uso de eliminar un empleado.
     *
     * @param id ID del empleado a eliminar
     * @throws IllegalArgumentException si no existe
     */
    @Transactional
    public void ejecutar(Long id) {
        if (id == null) {
            throw new DatosInvalidosException("El ID no puede ser nulo");
        }

        // Verificar que existe antes de eliminar
        Empleado empleado = empleadoRepository.findById(id)
            .orElseThrow(() -> new EmpleadoNoEncontradoException(id));

        // Eliminar
        empleadoRepository.delete(empleado);
    }
}
