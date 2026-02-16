package org.crud.core.infrastructure.persistence.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.crud.core.infrastructure.persistence.EmpleadoEntity;
import org.crud.core.domain.model.Empleado;

@ApplicationScoped
public class EmpleadoEntityMapper {

    public EmpleadoEntity toEntity(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        EmpleadoEntity entity = new EmpleadoEntity();
        entity.setId(empleado.getId());
        entity.setNombre(empleado.getNombre());
        entity.setCargo(empleado.getCargo());
        entity.setSalario(empleado.getSalario());
        entity.setUsername(empleado.getUsername());
        entity.setPassword(empleado.getPassword());
        entity.setRol(empleado.getRol());
        entity.setCreadorId(empleado.getCreadorId());

        return entity;
    }

    public Empleado toDomain(EmpleadoEntity entity) {
        if (entity == null) {
            return null;
        }

        Empleado empleado = new Empleado();
        empleado.setId(entity.getId());
        empleado.setNombre(entity.getNombre());
        empleado.setCargo(entity.getCargo());
        empleado.setSalario(entity.getSalario());
        empleado.setUsername(entity.getUsername());
        empleado.setPassword(entity.getPassword());
        empleado.setRol(entity.getRol());
        empleado.setCreadorId(entity.getCreadorId());

        return empleado;
    }

    public void updateEntityFromDomain(EmpleadoEntity entity, Empleado empleado) {
        if (entity == null || empleado == null) {
            return;
        }

        entity.setNombre(empleado.getNombre());
        entity.setCargo(empleado.getCargo());
        entity.setSalario(empleado.getSalario());
    }
}
