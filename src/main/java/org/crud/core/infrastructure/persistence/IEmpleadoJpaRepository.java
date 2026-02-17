package org.crud.core.infrastructure.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.crud.core.infrastructure.persistence.mapper.EmpleadoEntityMapper;
import org.crud.core.domain.model.Empleado;
import org.crud.core.domain.repositories.IEmpleadoRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class IEmpleadoJpaRepository implements IEmpleadoRepository {

    @Inject
    EntityManager entityManager;

    @Inject
    EmpleadoEntityMapper mapper;

    @Override
    public Empleado save(Empleado empleado) {
        EmpleadoEntity entity = mapper.toEntity(empleado);

        if (entity.getId() == null) {
            entityManager.persist(entity);
        }
        // ACTUALIZAR EMPLEADO
        else {
            entity = entityManager.merge(entity);
        }

        entityManager.flush();

        return mapper.toDomain(entity);
    }

    @Override
    public void delete(Empleado empleado) {
        EmpleadoEntity entity = mapper.toEntity(empleado);

        if (!entityManager.contains(entity)) {
            entity = entityManager.merge(entity);
        }

        entityManager.remove(entity);
    }

    @Override
    public Optional<Empleado> findById(Long id) {
        EmpleadoEntity entity = entityManager.find(EmpleadoEntity.class, id);

        return Optional.ofNullable(entity)
                .map(mapper::toDomain);
    }

    @Override
    public List<Empleado> findAll() {
        List<EmpleadoEntity> entities = entityManager
                .createQuery("SELECT e FROM EmpleadoEntity e", EmpleadoEntity.class)
                .getResultList();

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Empleado> findByUsername(String username) {
        List<EmpleadoEntity> results = entityManager
                .createQuery("SELECT e FROM EmpleadoEntity e WHERE e.username = :username", EmpleadoEntity.class)
                .setParameter("username", username)
                .getResultList();

        return results.stream()
                .findFirst()
                .map(mapper::toDomain);
    }

    @Override
    public List<Empleado> findByCreadorId(Long creadorId) {
        List<EmpleadoEntity> entities = entityManager
                .createQuery("SELECT e FROM EmpleadoEntity e WHERE e.creadorId = :creadorId", EmpleadoEntity.class)
                .setParameter("creadorId", creadorId)
                .getResultList();

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
