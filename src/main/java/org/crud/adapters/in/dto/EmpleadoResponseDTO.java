package org.crud.adapters.in.dto;

/**
 * DTO para enviar datos de empleado al cliente.
 * 
 * ¿Por qué separar Request y Response DTOs?
 * - El Request NO incluye el ID (lo genera la BD)
 * - El Response SÍ incluye el ID (para que el cliente lo conozca)
 * - Puedes agregar campos calculados en el Response (ej: salarioAnual)
 * - Mayor flexibilidad para evolucionar la API
 */
public class EmpleadoResponseDTO {

    // El ID solo existe en respuestas, no en requests
    private Long id;
    private String nombre;
    private String cargo;
    private Double salario;

    public EmpleadoResponseDTO() {
    }

    public EmpleadoResponseDTO(Long id, String nombre, String cargo, Double salario) {
        this.id = id;
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public Double getSalario() {
        return salario;
    }

    public void setSalario(Double salario) {
        this.salario = salario;
    }
}
