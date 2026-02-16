package org.crud.shared;

public class EmpleadoEvent {
    private Long id;
    private String accion; // "CREAR", "ACTUALIZAR", "ELIMINAR"
    private String nombre;
    private String cargo;
    private Double salario;

    private Long creadorId;

    // Constructor vacío requerido por Jackson para deserialización
    public EmpleadoEvent() {
    }

    public EmpleadoEvent(Long id, String accion, String nombre, String cargo, Double salario, Long creadorId) {
        this.id = id;
        this.accion = accion;
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
        this.creadorId = creadorId;
    }

    public EmpleadoEvent(Long id, String accion, Long creadorId) {
        this.id = id;
        this.accion = accion;
        this.creadorId = creadorId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
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

    public Long getCreadorId() {
        return creadorId;
    }

    public void setCreadorId(Long creadorId) {
        this.creadorId = creadorId;
    }

    @Override
    public String toString() {
        return "EmpleadoEvent{" +
                "id=" + id +
                ", accion='" + accion + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cargo='" + cargo + '\'' +
                ", salario=" + salario +
                ", creadorId=" + creadorId +
                '}';
    }
}
