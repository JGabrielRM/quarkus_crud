package org.crud.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

@ApplicationScoped
public class EliminarEmpleadoUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    @Transactional
    public void ejecutar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        // Verificar que existe antes de eliminar
        Empleado empleado = empleadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));

        // Eliminar
        empleadoRepository.delete(empleado);
    }
}
