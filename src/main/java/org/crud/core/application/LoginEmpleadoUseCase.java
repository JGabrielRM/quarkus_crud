package org.crud.core.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.port.EmpleadoRepository;
import org.crud.core.domain.port.PasswordService;
import org.crud.core.domain.port.TokenService;

@ApplicationScoped
public class LoginEmpleadoUseCase {

    @Inject
    EmpleadoRepository empleadoRepository;

    @Inject
    PasswordService passwordService;

    @Inject
    TokenService tokenService;

    public String ejecutar(String username, String password) {
        if (username == null || password == null) {
            throw new IllegalArgumentException("Username y password son obligatorios");
        }

        Empleado empleado = empleadoRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

        if (!passwordService.verificar(password, empleado.getPassword())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        return tokenService.generarToken(empleado);
    }
}
