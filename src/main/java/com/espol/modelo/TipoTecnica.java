package com.espol.modelo;

public final class TipoTecnica {
    public static final TipoTecnica POMODORO = new TipoTecnica("POMODORO");
    public static final TipoTecnica DEEP_WORK = new TipoTecnica("DEEP_WORK");
    private final String nombre;

    private TipoTecnica(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
