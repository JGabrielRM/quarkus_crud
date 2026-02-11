package org.crud.adapters.in.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.crud.adapters.in.dto.EmpleadoRequestDTO;
import org.crud.adapters.in.dto.EmpleadoResponseDTO;
import org.crud.domain.model.Empleado;

/**
 * Mapper para convertir entre DTOs y modelo de dominio.
 * 
 * ¿Por qué usar un Mapper separado?
 * - Principio de Responsabilidad Única: cada clase tiene una sola razón de cambio
 * - Los DTOs solo transportan datos, el Mapper se encarga de la lógica de conversión
 * - Facilita testing: puedes probar el mapper independientemente
 * - Reutilizable: centraliza la lógica de conversión
 * 
 * @ApplicationScoped: CDI (Context Dependency Injection) crea una única instancia
 * compartida en toda la aplicación. Es ideal para mappers porque no tienen estado.
 */
@ApplicationScoped
public class EmpleadoDTOMapper {

    /**
     * Convierte de DTO de Request a modelo de dominio.
     * Se usa cuando el cliente envía datos para crear/actualizar.
     * 
     * @param dto Datos recibidos del cliente
     * @return Objeto de dominio listo para la lógica de negocio
     */
    public Empleado toDomain(EmpleadoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        
        // Creamos una nueva instancia del dominio
        // El ID será null porque lo genera la base de datos
        return new Empleado(
            null,  // ID se asigna después de guardar
            dto.getNombre(),
            dto.getCargo(),
            dto.getSalario()
        );
    }

    /**
     * Convierte de modelo de dominio a DTO de Response.
     * Se usa cuando devolvemos datos al cliente.
     * 
     * @param empleado Objeto de dominio desde la lógica de negocio
     * @return DTO listo para serializar como JSON
     */
    public EmpleadoResponseDTO toResponseDTO(Empleado empleado) {
        if (empleado == null) {
            return null;
        }
        
        return new EmpleadoResponseDTO(
            empleado.getId(),
            empleado.getNombre(),
            empleado.getCargo(),
            empleado.getSalario()
        );
    }

    /**
     * Actualiza un objeto de dominio existente con datos del DTO.
     * Se usa en operaciones de actualización (PUT/PATCH).
     * 
     * @param empleado Objeto de dominio existente
     * @param dto Nuevos datos del cliente
     */
    public void updateDomainFromDTO(Empleado empleado, EmpleadoRequestDTO dto) {
        if (empleado == null || dto == null) {
            return;
        }
        
        empleado.setNombre(dto.getNombre());
        empleado.setCargo(dto.getCargo());
        empleado.setSalario(dto.getSalario());
    }
}
