package org.crud.notification.infrastructure.persistence;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.crud.notification.domain.model.NotificationLog;
import org.crud.notification.domain.port.NotificationLogRepository;
import org.crud.notification.infrastructure.persistence.mapper.NotificationLogEntityMapper;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class NotificationLogJpaRepository implements NotificationLogRepository {

    @Inject
    EntityManager entityManager;

    @Inject
    NotificationLogEntityMapper mapper;

    @Override
    public NotificationLog save(NotificationLog log) {
        NotificationLogEntity entity = mapper.toEntity(log);

        if (entity.getId() == null) {
            entityManager.persist(entity);
        } else {
            entity = entityManager.merge(entity);
        }

        entityManager.flush();
        return mapper.toDomain(entity);
    }

    @Override
    public List<NotificationLog> findAll() {
        List<NotificationLogEntity> entities = entityManager
                .createQuery("SELECT n FROM NotificationLogEntity n", NotificationLogEntity.class)
                .getResultList();

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<NotificationLog> findByCreadorId(Long creadorId) {
        List<NotificationLogEntity> entities = entityManager
                .createQuery("SELECT n FROM NotificationLogEntity n WHERE n.creadorId = :creadorId",
                        NotificationLogEntity.class)
                .setParameter("creadorId", creadorId)
                .getResultList();

        return entities.stream()
                .map(mapper::toDomain)
                .collect(Collectors.toList());
    }
}
