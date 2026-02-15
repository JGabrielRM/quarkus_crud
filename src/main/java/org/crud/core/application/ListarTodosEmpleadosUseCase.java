package org.crud.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

import java.util.List;


@ApplicationScoped
public class ListarTodosEmpleadosUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;


    public List<Empleado> ejecutar() {
        return empleadoRepository.findAll();
    }
}
