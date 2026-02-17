package org.crud.core.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.repositories.IEmpleadoRepository;
import org.crud.core.domain.repositories.IPasswordService;
import org.crud.core.domain.repositories.ITokenService;

@ApplicationScoped
public class LoginEmpleadoUseCase {

    @Inject
    IEmpleadoRepository empleadoRepository;

    @Inject
    IPasswordService IPasswordService;

    @Inject
    ITokenService ITokenService;

    public String ejecutar(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username y password son obligatorios");
        }

        Empleado empleado = empleadoRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!IPasswordService.verificar(password, empleado.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return ITokenService.generarToken(empleado);
    }
}
