package org.crud.core.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.repositories.IEmpleadoRepository;
import org.crud.core.domain.repositories.IPasswordService;

@ApplicationScoped
public class RegistrarEmpleadoUseCase {

    @Inject
    IEmpleadoRepository empleadoRepository;

    @Inject
    IPasswordService IPasswordService;

    @Transactional
    public Empleado ejecutar(String username, String password, String nombre, String cargo, Double salario,
            String rol) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("El username es obligatorio");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 4 caracteres");
        }

        // Verificar que no exista
        if (empleadoRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("El username '" + username + "' ya está registrado");
        }

        Empleado empleado = new Empleado();
        empleado.setUsername(username);
        empleado.setPassword(IPasswordService.hashear(password));
        empleado.setNombre(nombre);
        empleado.setCargo(cargo);
        empleado.setSalario(salario);
        empleado.setRol(rol != null ? rol : "USER");

        return empleadoRepository.save(empleado);
    }
}
