package com.espol.modelo;

public class AccionSostenible {
    private final int id;
    private final String descripcion;
    private final int puntos;

    public AccionSostenible(int id, String descripcion, int puntos) {
        this.id = id;
        this.descripcion = descripcion;
        this.puntos = puntos;
    }

    public int getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getPuntos() {
        return puntos;
    }
}
