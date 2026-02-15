package org.crud.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

@ApplicationScoped
public class CrearEmpleadoUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    @Transactional
    public Empleado ejecutar(Empleado empleado) {
        // Validación de negocio ANTES de persistir
        if (empleado == null) {
            throw new IllegalArgumentException("El empleado no puede ser nulo");
        }

        if (!empleado.isValid()) {
            throw new IllegalArgumentException(
                    "Datos del empleado incompletos o inválidos");
        }

        // Validación: no debe tener ID (es creación, no actualización)
        if (empleado.getId() != null) {
            throw new IllegalArgumentException(
                    "El empleado no debe tener ID al crearlo");
        }

        // Delegar la persistencia al adaptador
        return empleadoRepository.save(empleado);
    }
}
