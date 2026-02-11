package org.crud.adapters.in.dto;

public class EmpleadoRequestDTO {

    // Atributos que el cliente enviará para crear/actualizar un empleado
    private String nombre;
    private String cargo;
    private Double salario;

    // Constructor vacío requerido para deserialización JSON
    public EmpleadoRequestDTO() {
    }

    // Constructor con parámetros para facilitar creación en tests
    public EmpleadoRequestDTO(String nombre, String cargo, Double salario) {
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
    }

    // Getters y Setters
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
