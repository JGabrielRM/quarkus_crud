package org.crud.notification.infrastructure.persistence;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notification_logs")
public class NotificationLogEntity {

    @Id
    @SequenceGenerator(name = "notificationLogSeq", sequenceName = "notification_logs_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificationLogSeq")
    private Long id;

    @Column(nullable = false)
    private String evento;

    @Column(nullable = false)
    private String payload;

    @Column(name = "creador_id")
    private Long creadorId;

    private LocalDateTime fecha;

    public NotificationLogEntity() {
    }

    public NotificationLogEntity(String evento, String payload, Long creadorId, LocalDateTime fecha) {
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
