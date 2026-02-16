package org.crud.core.infrastructure.messaging;

import io.smallrye.reactive.messaging.MutinyEmitter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.crud.shared.EmpleadoEvent;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;

@ApplicationScoped
public class EmpleadoKafkaProducer {

    private static final Logger LOG = Logger.getLogger(EmpleadoKafkaProducer.class);

    @Inject
    @Channel("empleados-out")
    MutinyEmitter<EmpleadoEvent> emitter;

    public void enviar(EmpleadoEvent evento) {
        LOG.infof("Enviando evento a Kafka: %s", evento);
        emitter.sendAndAwait(evento);
        LOG.infof("Evento enviado exitosamente al topic 'empleados'");
    }
}
