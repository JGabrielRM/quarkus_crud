package org.crud.core.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.port.EmpleadoRepository;

@ApplicationScoped
public class BuscarEmpleadoPorIdUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    public Empleado ejecutar(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("El ID no puede ser nulo");
        }

        return empleadoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));
    }
}
