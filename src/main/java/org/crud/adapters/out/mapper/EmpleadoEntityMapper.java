package org.crud.adapters.out.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.crud.adapters.out.EmpleadoEntity;
import org.crud.domain.model.Empleado;

/**
 * Mapper para convertir entre Entity (JPA) y modelo de dominio.
 * 
 * ¿Por qué necesitamos este mapper?
 * - Separa la persistencia (cómo guardamos) del dominio (qué guardamos)
 * - El Entity tiene anotaciones JPA (@Entity, @Id, etc.)
 * - El Domain es POJO puro sin dependencias de frameworks
 * - Si cambias de BD (ej: JPA → MongoDB), solo cambias el adaptador
 * 
 * Este mapper vive en el adaptador OUT porque es parte de la infraestructura.
 */
@ApplicationScoped
public class EmpleadoEntityMapper {

    /**
     * Convierte de modelo de dominio a Entity para persistir.
     * Se usa cuando guardamos en la base de datos.
     * 
     * @param empleado Objeto de dominio
     * @return Entity listo para JPA
     */
    public EmpleadoEntity toEntity(Empleado empleado) {
        if (empleado == null) {
            return null;
        }
        
        EmpleadoEntity entity = new EmpleadoEntity();
        entity.setId(empleado.getId());
        entity.setNombre(empleado.getNombre());
        entity.setCargo(empleado.getCargo());
        entity.setSalario(empleado.getSalario());
        
        return entity;
    }

    /**
     * Convierte de Entity a modelo de dominio.
     * Se usa cuando leemos desde la base de datos.
     * 
     * @param entity Entity obtenido de JPA
     * @return Objeto de dominio para la lógica de negocio
     */
    public Empleado toDomain(EmpleadoEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new Empleado(
            entity.getId(),
            entity.getNombre(),
            entity.getCargo(),
            entity.getSalario()
        );
    }

    /**
     * Actualiza un Entity existente con datos del dominio.
     * Se usa en operaciones de actualización para no perder el contexto JPA.
     * 
     * @param entity Entity existente (managed por JPA)
     * @param empleado Datos actualizados del dominio
     */
    public void updateEntityFromDomain(EmpleadoEntity entity, Empleado empleado) {
        if (entity == null || empleado == null) {
            return;
        }
        
        entity.setNombre(empleado.getNombre());
        entity.setCargo(empleado.getCargo());
        entity.setSalario(empleado.getSalario());
    }
}
