package org.crud.adapters.out;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.crud.adapters.out.mapper.EmpleadoEntityMapper;
import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adaptador OUT que implementa el puerto EmpleadoRepository usando JPA.
 *
 * ¿Qué es un adaptador OUT?
 * - Implementa un puerto (interfaz) definido en el dominio
 * - Se encarga de la persistencia de datos (BD, archivos, APIs externas, etc.)
 * - Depende del dominio, pero el dominio NO depende de él (Inversión de dependencias)
 *
 * ¿Por qué separar el adaptador del dominio?
 * - Si cambias de BD (JPA → MongoDB), solo cambias este adaptador
 * - El dominio y los casos de uso NO se enteran del cambio
 * - Puedes crear múltiples adaptadores (MemoryRepository para tests, etc.)
 *
 * @ApplicationScoped: Es un singleton manejado por CDI
 */
@ApplicationScoped
public class EmpleadoJpaRepository implements EmpleadoRepository {

    /**
     * EntityManager es la interfaz principal de JPA para interactuar con la BD.
     * Quarkus lo inyecta automáticamente con la configuración de application.properties
     */
    @Inject
    EntityManager entityManager;

    /**
     * Mapper para convertir entre Entity (JPA) y Domain (POJO)
     */
    @Inject
    EmpleadoEntityMapper mapper;

    /**
     * Guarda un empleado en la base de datos.
     *
     * Flujo:
     * 1. Domain → Entity (usando mapper)
     * 2. Persistir Entity con JPA
     * 3. Entity → Domain (devolver resultado)
     *
     * persist() vs merge():
     * - persist(): Solo para entidades NUEVAS (sin ID)
     * - merge(): Para entidades existentes O nuevas (más flexible)
     *
     * @param empleado Objeto de dominio a guardar
     * @return Empleado guardado con ID generado
     */
    @Override
    public Empleado save(Empleado empleado) {
        // Convertir de Domain a Entity
        EmpleadoEntity entity = mapper.toEntity(empleado);

        // Si tiene ID, es actualización; si no, es creación
        if (entity.getId() == null) {
            // Crear: persist hace que JPA rastree la entidad
            entityManager.persist(entity);
        } else {
            // Actualizar: merge sincroniza los cambios con la BD
            entity = entityManager.merge(entity);
        }

        // flush(): Fuerza la escritura inmediata a la BD
        // Útil para obtener el ID generado inmediatamente
        entityManager.flush();

        // Convertir de Entity a Domain antes de devolver
        return mapper.toDomain(entity);
    }

    /**
     * Elimina un empleado de la base de datos.
     *
     * Importante: JPA solo puede eliminar entidades "managed" (rastreadas)
     * Por eso hacemos merge() antes de remove()
     *
     * @param empleado Empleado a eliminar
     */
    @Override
    public void delete(Empleado empleado) {
        // Convertir a Entity
        EmpleadoEntity entity = mapper.toEntity(empleado);

        // Si la entidad no está managed, hacemos merge para que JPA la rastree
        if (!entityManager.contains(entity)) {
            entity = entityManager.merge(entity);
        }

        // Eliminar
        entityManager.remove(entity);
    }

    /**
     * Busca un empleado por su ID.
     *
     * find() vs getReference():
     * - find(): Carga la entidad completa de inmediato
     * - getReference(): Devuelve un proxy, carga lazy
     *
     * @param id ID del empleado
     * @return Optional con el empleado si existe, vacío si no
     */
    @Override
    public Optional<Empleado> findById(Long id) {
        // find() devuelve null si no encuentra
        EmpleadoEntity entity = entityManager.find(EmpleadoEntity.class, id);

        // Convertir a Domain y envolver en Optional
        return Optional.ofNullable(entity)
            .map(mapper::toDomain);
    }

    /**
     * Obtiene todos los empleados.
     *
     * Usa JPQL (Java Persistence Query Language), similar a SQL pero orientado a objetos
     * - Usamos nombres de clases Java, no tablas SQL
     * - "SELECT e FROM EmpleadoEntity e" no "SELECT * FROM empleados"
     *
     * @return Lista de todos los empleados
     */
    @Override
    public List<Empleado> findAll() {
        // JPQL: Consulta sobre entidades, no sobre tablas
        List<EmpleadoEntity> entities = entityManager
            .createQuery("SELECT e FROM EmpleadoEntity e", EmpleadoEntity.class)
            .getResultList();

        // Convertir cada Entity a Domain usando Stream API
        return entities.stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }
}
