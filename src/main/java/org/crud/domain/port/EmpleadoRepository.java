package org.crud.domain.port;

import org.crud.domain.model.Empleado;

import java.util.List;
import java.util.Optional;

public interface EmpleadoRepository {

    Empleado save(Empleado empleado);
    void delete(Empleado empleado);
    Optional<Empleado> findById(Long id);
    List<Empleado> findAll();
}
