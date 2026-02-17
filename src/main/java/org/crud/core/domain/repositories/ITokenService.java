package org.crud.core.domain.repositories;

import org.crud.core.domain.model.Empleado;

public interface ITokenService {
    String generarToken(Empleado empleado);
}
