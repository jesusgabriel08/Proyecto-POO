package com.espol.modelo;

public final class EstadoActividad {
    public static final EstadoActividad PENDIENTE = new EstadoActividad("PENDIENTE");
    public static final EstadoActividad EN_CURSO = new EstadoActividad("EN_CURSO");
    public static final EstadoActividad COMPLETADA = new EstadoActividad("COMPLETADA");
    private final String nombre;

    private EstadoActividad(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
