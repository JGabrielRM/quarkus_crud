package org.crud.core.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.port.EmpleadoRepository;

@ApplicationScoped
public class ActualizarEmpleadoUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    @Transactional
    public Empleado ejecutar(Long id, Empleado empleadoActualizado) {
        // Validaciones
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        if (empleadoActualizado == null || !empleadoActualizado.isValid()) {
            throw new IllegalArgumentException("Datos del empleado inválidos");
        }

        // Buscar el empleado existente
        Empleado empleadoExistente = empleadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));

        // Actualizar los datos (manteniendo el ID original)
        empleadoExistente.setNombre(empleadoActualizado.getNombre());
        empleadoExistente.setCargo(empleadoActualizado.getCargo());
        empleadoExistente.setSalario(empleadoActualizado.getSalario());

        // Guardar cambios
        return empleadoRepository.save(empleadoExistente);
    }
}
