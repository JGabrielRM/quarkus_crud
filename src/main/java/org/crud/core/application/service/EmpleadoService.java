package org.crud.core.application.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.crud.core.application.*;
import org.crud.core.commons.Constants;
import org.crud.core.domain.model.Empleado;
import org.crud.core.infrastructure.messaging.EmpleadoKafkaProducer;
import org.crud.shared.EmpleadoEvent;

import java.util.List;
import java.util.Set;

@ApplicationScoped
public class EmpleadoService {

    @Inject
    CrearEmpleadoUseCase crearEmpleadoUseCase;

    @Inject
    ActualizarEmpleadoUseCase actualizarEmpleadoUseCase;

    @Inject
    BuscarEmpleadoPorIdUseCase buscarEmpleadoPorIdUseCase;

    @Inject
    ListarTodosEmpleadosUseCase listarTodosEmpleadosUseCase;

    @Inject
    ListarEmpleadosPorCreadorUseCase listarEmpleadosPorCreadorUseCase;

    @Inject
    EliminarEmpleadoUseCase eliminarEmpleadoUseCase;

    @Inject
    EmpleadoKafkaProducer kafkaProducer;

    /**
     * Lista empleados según el rol del usuario autenticado.
     * ADMIN ve todos; USER ve solo los que creó.
     */
    public List<Empleado> listarEmpleados(Set<String> roles, Long creadorId) {
        if (roles.contains(Constants.PROJECT_ROL_ADMIN)) {
            return listarTodosEmpleadosUseCase.ejecutar();
        }
        return listarEmpleadosPorCreadorUseCase.ejecutar(creadorId);
    }

    /**
     * Busca un empleado por su ID.
     */
    public Empleado buscarPorId(Long id) {
        return buscarEmpleadoPorIdUseCase.ejecutar(id);
    }

    /**
     * Crea un empleado, asigna el creadorId y notifica a Kafka.
     */
    public Empleado crear(Empleado empleado, Long creadorId) {
        empleado.setCreadorId(creadorId);
        Empleado guardado = crearEmpleadoUseCase.ejecutar(empleado);

        EmpleadoEvent evento = new EmpleadoEvent(
                guardado.getId(),
                Constants.PROJECT_ACCION_CREAR,
                guardado.getNombre(),
                guardado.getCargo(),
                guardado.getSalario(),
                creadorId);
        kafkaProducer.enviar(evento);

        return guardado;
    }

    /**
     * Actualiza un empleado y notifica a Kafka.
     */
    public Empleado actualizar(Long id, Empleado empleado, Long creadorId) {
        Empleado actualizado = actualizarEmpleadoUseCase.ejecutar(id, empleado);

        EmpleadoEvent evento = new EmpleadoEvent(
                actualizado.getId(),
                Constants.PROJECT_ACCION_ACTUALIZAR,
                actualizado.getNombre(),
                actualizado.getCargo(),
                actualizado.getSalario(),
                creadorId);
        kafkaProducer.enviar(evento);

        return actualizado;
    }

    /**
     * Elimina un empleado y notifica a Kafka.
     */
    public void eliminar(Long id, Long creadorId) {
        eliminarEmpleadoUseCase.ejecutar(id);

        EmpleadoEvent evento = new EmpleadoEvent(
                id,
                Constants.PROJECT_ACCION_ELIMINAR,
                creadorId);
        kafkaProducer.enviar(evento);
    }
}
