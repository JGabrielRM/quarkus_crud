package org.crud.core.domain.port;

import org.crud.core.domain.model.Empleado;

public interface TokenService {
    String generarToken(Empleado empleado);
}
