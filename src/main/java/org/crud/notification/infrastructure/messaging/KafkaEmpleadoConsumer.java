package org.crud.notification.infrastructure.messaging;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.crud.notification.application.ProcesarEventoUseCase;
import org.crud.shared.EmpleadoEvent;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

@ApplicationScoped
public class KafkaEmpleadoConsumer {

    private static final Logger LOG = Logger.getLogger(KafkaEmpleadoConsumer.class);

    @Inject
    ProcesarEventoUseCase procesarEventoUseCase;

    @Incoming("empleados-in")
    public void consumir(EmpleadoEvent evento) {
        LOG.infof("Mensaje recibido de Kafka: %s", evento);
        procesarEventoUseCase.ejecutar(evento);
    }
}
