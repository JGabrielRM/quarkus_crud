package org.crud.core.domain.port;

public interface PasswordService {
    String hashear(String password);

    boolean verificar(String passwordPlano, String passwordHasheado);
}
