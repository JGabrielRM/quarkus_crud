package org.crud.adapters.out;

import jakarta.persistence.*;

/**
 * Entidad JPA que representa la tabla "empleados" en la base de datos.
 *
 * IMPORTANTE: Esta clase es SOLO infraestructura
 * - Tiene anotaciones JPA (@Entity, @Id, @Column, etc.)
 * - NO debe contener lógica de negocio
 * - NO debe conocer el dominio directamente
 * - El mapper se encarga de la conversión Domain ↔ Entity
 *
 * ¿Por qué separar Entity del Domain?
 * - El dominio es agnóstico de tecnología
 * - Si cambias de BD (JPA → MongoDB), solo cambias la Entity
 * - El dominio permanece intacto
 *
 * @Entity: Marca esta clase como una entidad JPA
 * @Table: Especifica el nombre de la tabla en la BD
 */
@Entity
@Table(name = "empleados")
public class EmpleadoEntity {

    /**
     * @Id: Define este campo como clave primaria
     * @GeneratedValue: La BD genera automáticamente el valor
     * - IDENTITY: Usa AUTO_INCREMENT de MySQL/PostgreSQL
     * - SEQUENCE: Usa secuencias de Oracle
     * - AUTO: JPA elige la estrategia según la BD
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * @Column: Personaliza el mapeo con la columna de la tabla
     * - nullable = false: NO NULL en la BD
     * - unique = true: Crea índice único
     * - length = 100: VARCHAR(100)
     * - name = "nombre_empleado": Nombre diferente en BD
     */
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String cargo;

    @Column(nullable = false)
    private Double salario;

    // Constructor vacío OBLIGATORIO para JPA
    // JPA usa reflexión para crear instancias
    public EmpleadoEntity() {}

    // Constructor con parámetros (opcional, útil para tests)
    public EmpleadoEntity(String nombre, String cargo, Double salario) {
        this.nombre = nombre;
        this.cargo = cargo;
        this.salario = salario;
    }

    // Getters y Setters requeridos por JPA
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
