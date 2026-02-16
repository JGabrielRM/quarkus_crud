package org.crud.notification.domain.port;

import org.crud.notification.domain.model.NotificationLog;

import java.util.List;

public interface NotificationLogRepository {

    NotificationLog save(NotificationLog log);

    List<NotificationLog> findAll();

    List<NotificationLog> findByCreadorId(Long creadorId);
}
