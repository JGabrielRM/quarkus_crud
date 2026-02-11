package org.crud.domain.model;

import java.util.Objects;

/**
 * Entidad de dominio que representa un Empleado.
 *
 * IMPORTANTE: Esta clase es AGNÓSTICA de infraestructura
 * - No tiene anotaciones JPA (@Entity, @Id, etc.)
 * - No tiene anotaciones de Jackson (@JsonProperty, etc.)
 * - No tiene anotaciones de validación (@NotNull, etc.)
 *
 * ¿Por qué?
 * - El dominio es el CORAZÓN de la aplicación y debe ser independiente
 * - Puedes cambiar de BD, framework REST, etc. sin tocar el dominio
 * - Facilita testing: no necesitas levantar BD o servidor
 * - Sigue el principio de Inversión de Dependencias (SOLID)
 */
public class Empleado {

    private Long id;
    private String nombre;
    private String cargo;
    private Double salario;

    // Constructor vacío para frameworks que lo requieran
    public Empleado() {
    }

    // Constructor completo
    public Empleado(Long id, String nombre, String cargo, Double salario) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public Double getSalario() {
        return salario;
    }

    // Setters (necesarios para actualización)
    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }

    /**
     * Método de dominio para validar si el empleado tiene datos completos.
     * Este tipo de lógica de negocio DEBE estar en el dominio.
     *
     * @return true si todos los campos obligatorios están presentes
     */
    public boolean isValid() {
        return nombre != null && !nombre.trim().isEmpty()
            && cargo != null && !cargo.trim().isEmpty()
            && salario != null && salario > 0;
    }

    /**
     * Método de dominio para calcular salario anual.
     * Ejemplo de lógica de negocio que pertenece al dominio.
     *
     * @return salario multiplicado por 12 meses
     */
    public Double calcularSalarioAnual() {
        return salario != null ? salario * 12 : 0.0;
    }

    // Métodos equals y hashCode para comparación correcta
    // Útiles en colecciones y tests
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Empleado empleado = (Empleado) o;
        return Objects.equals(id, empleado.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString para debugging
    @Override
    public String toString() {
        return "Empleado{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", cargo='" + cargo + '\'' +
                ", salario=" + salario +
                '}';
    }
}
