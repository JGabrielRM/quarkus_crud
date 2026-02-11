package org.crud.application.usecases;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.crud.domain.exception.DatosInvalidosException;
import org.crud.domain.model.Empleado;
import org.crud.domain.port.EmpleadoRepository;

/**
 * Caso de uso: Crear un nuevo empleado
 *
 * ¿Qué es un Caso de Uso?
 * - Representa una acción específica que un usuario puede realizar
 * - Contiene la LÓGICA DE NEGOCIO de esa acción
 * - Es independiente de la infraestructura (REST, BD, etc.)
 *
 * ¿Por qué separar casos de uso en lugar de un servicio genérico?
 * - Cada caso de uso tiene responsabilidad única (SRP - SOLID)
 * - Facilita entender qué hace cada operación
 * - Permite agregar validaciones específicas por caso de uso
 * - Más fácil de testear y mantener
 * - Sigue el patrón Use Case de Clean Architecture
 *
 * @ApplicationScoped: CDI maneja el ciclo de vida (singleton)
 */
@ApplicationScoped
public class CrearEmpleadoUseCase {

    // Inyección de dependencias del puerto (interfaz)
    // NO inyectamos la implementación directamente
    @Inject
    EmpleadoRepository empleadoRepository;

    /**
     * Ejecuta el caso de uso de crear un empleado.
     *
     * @Transactional: Garantiza que la operación sea atómica
     * - Si algo falla, se hace rollback automático
     * - Si todo va bien, se hace commit al finalizar
     *
     * @param empleado Empleado a crear (sin ID)
     * @return Empleado creado (con ID generado)
     * @throws IllegalArgumentException si el empleado no es válido
     */
    @Transactional
    public Empleado ejecutar(Empleado empleado) {
        // Validación de negocio ANTES de persistir
        if (empleado == null) {
            throw new DatosInvalidosException("El empleado no puede ser nulo");
        }

        if (!empleado.isValid()) {
            throw new DatosInvalidosException(
                "Datos del empleado incompletos o inválidos"
            );
        }

        // Validación: no debe tener ID (es creación, no actualización)
        if (empleado.getId() != null) {
            throw new DatosInvalidosException(
                "El empleado no debe tener ID al crearlo"
            );
        }

        // Lógica de negocio adicional (ejemplo)
        // Podrías validar que no exista otro empleado con el mismo nombre, etc.

        // Delegar la persistencia al adaptador
        return empleadoRepository.save(empleado);
    }
}
