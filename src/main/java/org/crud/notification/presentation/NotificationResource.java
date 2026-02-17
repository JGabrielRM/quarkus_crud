package org.crud.notification.presentation;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.crud.notification.application.service.NotificationService;
import org.crud.notification.domain.model.NotificationLog;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/api/notifications")
@Produces(MediaType.APPLICATION_JSON)
public class NotificationResource {

    @Inject
    NotificationService notificationService;

    @Inject
    JsonWebToken jwt;

    @GET
    public List<NotificationLog> listarLogs() {
        return notificationService.listarLogs(
                jwt.getGroups(), getUsuarioIdFromToken());
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
