package com.espol.modelo;

public final class Prioridad {
    public static final Prioridad ALTA = new Prioridad("ALTA");
    public static final Prioridad MEDIA = new Prioridad("MEDIA");
    public static final Prioridad BAJA = new Prioridad("BAJA");
    private final String nombre;

    private Prioridad(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
