package org.crud.notification.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.crud.notification.domain.model.NotificationLog;
import org.crud.notification.domain.repository.INotificationLogRepository;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class NotificationService {

    @Inject
    INotificationLogRepository repository;

    /**
     * Lista logs de notificación según el rol del usuario.
     * ADMIN ve todos; USER ve solo los suyos.
     */
    public List<NotificationLog> listarLogs(Set<String> roles, Long creadorId) {
        if (roles.contains("ADMIN")) {
            return repository.findAll();
        }
        return repository.findByCreadorId(creadorId);
    }
}
