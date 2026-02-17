package org.crud.core.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.repositories.IEmpleadoRepository;

@ApplicationScoped
public class CrearEmpleadoUseCase {

    @Inject
    IEmpleadoRepository empleadoRepository;

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
