package org.crud.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.crud.domain.exception.DatosInvalidosException;
import org.crud.domain.exception.EmpleadoNoEncontradoException;
import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

/**
 * Caso de uso: Actualizar un empleado existente
 */
@ApplicationScoped
public class ActualizarEmpleadoUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    /**
     * Ejecuta el caso de uso de actualizar un empleado.
     *
     * @param id ID del empleado a actualizar
     * @param empleadoActualizado Datos nuevos del empleado
     * @return Empleado actualizado
     * @throws IllegalArgumentException si los datos son inválidos
     * @throws jakarta.ws.rs.NotFoundException si no existe el empleado
     */
    @Transactional
    public Empleado ejecutar(Long id, Empleado empleadoActualizado) {
        // Validaciones
        if (id == null) {
            throw new DatosInvalidosException("El ID no puede ser nulo");
        }

        if (empleadoActualizado == null || !empleadoActualizado.isValid()) {
            throw new DatosInvalidosException("Datos del empleado inválidos");
        }

        // Buscar el empleado existente
        Empleado empleadoExistente = empleadoRepository.findById(id)
            .orElseThrow(() -> new EmpleadoNoEncontradoException(id));

        // Actualizar los datos (manteniendo el ID original)
        empleadoExistente.setNombre(empleadoActualizado.getNombre());
        empleadoExistente.setCargo(empleadoActualizado.getCargo());
        empleadoExistente.setSalario(empleadoActualizado.getSalario());

        // Guardar cambios
        return empleadoRepository.save(empleadoExistente);
    }
}
