package org.crud.notification.domain.repository;

import org.crud.notification.domain.model.NotificationLog;

import java.util.List;

public interface INotificationLogRepository {

    NotificationLog save(NotificationLog log);

    List<NotificationLog> findAll();

    List<NotificationLog> findByCreadorId(Long creadorId);
}
