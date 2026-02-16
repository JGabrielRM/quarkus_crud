package org.crud.core.presentation.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.crud.core.presentation.dto.EmpleadoRequestDTO;
import org.crud.core.presentation.dto.EmpleadoResponseDTO;
import org.crud.core.domain.model.Empleado;

@ApplicationScoped
public class EmpleadoDTOMapper {
    // Force recompile

    public Empleado toDomain(EmpleadoRequestDTO dto) {
        if (dto == null) {
            return null;
        }

        // Creamos una nueva instancia del dominio
        // El ID será null porque lo genera la base de datos
        return new Empleado(
                null, // ID se asigna después de guardar
                dto.getNombre(),
                dto.getCargo(),
                dto.getSalario());
    }

    public EmpleadoResponseDTO toResponseDTO(Empleado empleado) {
        if (empleado == null) {
            return null;
        }

        return new EmpleadoResponseDTO(
                empleado.getId(),
                empleado.getNombre(),
                empleado.getCargo(),
                empleado.getSalario());
    }

    public void updateDomainFromDTO(Empleado empleado, EmpleadoRequestDTO dto) {
        if (empleado == null || dto == null) {
            return;
        }

        empleado.setNombre(dto.getNombre());
        empleado.setCargo(dto.getCargo());
        empleado.setSalario(dto.getSalario());
    }
}
