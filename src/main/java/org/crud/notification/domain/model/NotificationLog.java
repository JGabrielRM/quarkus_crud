package org.crud.notification.domain.model;

import java.time.LocalDateTime;

public class NotificationLog {

    private Long id;
    private String evento;
    private String payload;
    private Long creadorId;
    private LocalDateTime fecha;

    public NotificationLog() {
    }

    public NotificationLog(String evento, String payload, Long creadorId) {
        this.evento = evento;
        this.payload = payload;
        this.creadorId = creadorId;
        this.fecha = LocalDateTime.now();
    }

    public NotificationLog(Long id, String evento, String payload, Long creadorId, LocalDateTime fecha) {
        this.id = id;
        this.evento = evento;
        this.payload = payload;
        this.creadorId = creadorId;
        this.fecha = fecha;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public Long getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(Long creadorId) {
        this.creadorId = creadorId;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
