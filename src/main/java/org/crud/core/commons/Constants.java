package org.crud.core.commons;

public final class Constants {

    public static final String KAFKA_TOPIC_EMPLEADOS = "empleados";
    public static final String PROJECT_ACCION_CREAR = "CREAR";
    public static final String PROJECT_ACCION_ACTUALIZAR = "ACTUALIZAR";
    public static final String PROJECT_ACCION_ELIMINAR = "ELIMINAR";
    public static final String PROJECT_ROL_ADMIN = "ADMIN";
    public static final String PROJECT_ROL_USER = "USER";
    public static final String JSON_JWT_ISSUER = "crud-quarkus";

    // Constructor privado para prevenir instanciación
    private Constants() {
    }
}
