package org.crud.core.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.port.EmpleadoRepository;

import java.util.List;

@ApplicationScoped
public class ListarEmpleadosPorCreadorUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    public List<Empleado> ejecutar(Long creadorId) {
        return empleadoRepository.findByCreadorId(creadorId);
    }
}
