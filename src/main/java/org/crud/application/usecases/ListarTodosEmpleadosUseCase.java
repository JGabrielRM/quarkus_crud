package org.crud.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

import java.util.List;

/**
 * Caso de uso: Listar todos los empleados
 */
@ApplicationScoped
public class ListarTodosEmpleadosUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    /**
     * Ejecuta el caso de uso de listar todos los empleados.
     *
     * @return Lista de todos los empleados
     */
    public List<Empleado> ejecutar() {
        return empleadoRepository.findAll();
    }
}
