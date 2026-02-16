package org.crud.notification.application;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import org.crud.notification.domain.model.NotificationLog;
import org.crud.notification.domain.port.NotificationLogRepository;
import org.crud.shared.EmpleadoEvent;
import org.jboss.logging.Logger;

@ApplicationScoped
public class ProcesarEventoUseCase {

    private static final Logger LOG = Logger.getLogger(ProcesarEventoUseCase.class);

    @Inject
    NotificationLogRepository repository;

    @Transactional
    public void ejecutar(EmpleadoEvent evento) {
        LOG.infof("Procesando evento: %s", evento);

        String payload = String.format("Accion: %s, EmpleadoId: %s, Nombre: %s, Cargo: %s, Salario: %s",
                evento.getAccion(), evento.getId(), evento.getNombre(), evento.getCargo(), evento.getSalario());

        NotificationLog log = new NotificationLog(evento.getAccion(), payload, evento.getCreadorId());
        repository.save(log);

        LOG.infof("✅ Evento guardado en NotificationLog: %s", evento.getAccion());
    }
}
