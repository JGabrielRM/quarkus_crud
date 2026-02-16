package org.crud.notification.infrastructure.persistence.mapper;

import jakarta.enterprise.context.ApplicationScoped;
import org.crud.notification.domain.model.NotificationLog;
import org.crud.notification.infrastructure.persistence.NotificationLogEntity;

@ApplicationScoped
public class NotificationLogEntityMapper {

    public NotificationLogEntity toEntity(NotificationLog domain) {
        if (domain == null) {
            return null;
        }

        NotificationLogEntity entity = new NotificationLogEntity(
                domain.getEvento(),
                domain.getPayload(),
                domain.getCreadorId(),
                domain.getFecha());
        entity.setId(domain.getId());
        return entity;
    }

    public NotificationLog toDomain(NotificationLogEntity entity) {
        if (entity == null) {
            return null;
        }

        return new NotificationLog(
                entity.getId(),
                entity.getEvento(),
                entity.getPayload(),
                entity.getCreadorId(),
                entity.getFecha());
    }
}
