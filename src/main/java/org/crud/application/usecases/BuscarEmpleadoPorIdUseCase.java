package org.crud.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.crud.domain.exception.DatosInvalidosException;
import org.crud.domain.exception.EmpleadoNoEncontradoException;
import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

/**
 * Caso de uso: Buscar un empleado por su ID
 */
@ApplicationScoped
public class BuscarEmpleadoPorIdUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    /**
     * Ejecuta el caso de uso de buscar empleado por ID.
     *
     * @param id ID del empleado a buscar
     * @return Empleado encontrado
     * @throws IllegalArgumentException si no existe
     */
    public Empleado ejecutar(Long id) {
        if (id == null) {
            throw new DatosInvalidosException("El ID no puede ser nulo");
        }

        return empleadoRepository.findById(id)
            .orElseThrow(() -> new EmpleadoNoEncontradoException(id));
    }
}
