package org.crud.notification.infrastructure.messaging;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import org.crud.shared.EmpleadoEvent;

public class EmpleadoEventDeserializer extends ObjectMapperDeserializer<EmpleadoEvent> {

    public EmpleadoEventDeserializer() {
        super(EmpleadoEvent.class);
    }
}
