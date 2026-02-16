package org.crud.notification.presentation;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.crud.notification.domain.model.NotificationLog;
import org.crud.notification.domain.port.NotificationLogRepository;

import java.util.List;

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    NotificationLogRepository repository;

    @Inject
    org.eclipse.microprofile.jwt.JsonWebToken jwt;

    @GET
    public List<NotificationLog> listarLogs() {
        // ADMIN ve todo
        if (jwt.getGroups().contains("ADMIN")) {
            return repository.findAll();
        }

        // USER ve solo sus logs
        Long myId = getUsuarioIdFromToken();
        return repository.findByCreadorId(myId);
    }

    private Long getUsuarioIdFromToken() {
        Object idClaim = jwt.getClaim("id");
        if (idClaim == null)
            return null;
        if (idClaim instanceof Number)
            return ((Number) idClaim).longValue();
        return Long.parseLong(idClaim.toString());
    }
}
