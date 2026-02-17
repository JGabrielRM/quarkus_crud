package org.crud.core.domain.repositories;

public interface IPasswordService {
    String hashear(String password);

    boolean verificar(String passwordPlano, String passwordHasheado);
}
