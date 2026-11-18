package com.espol.modelo;

import java.time.LocalDateTime;

public class ActividadPersonal extends Actividad {
    private String lugar = "";

    public ActividadPersonal(int id, String nombre, String descripcion) {
        super(id, nombre, descripcion);
    }

    public ActividadPersonal(int id, String nombre, String descripcion, LocalDateTime fechaVencimiento,
                             Prioridad prioridad, int tiempoEstimadoMinutos, String lugar) {
        super(id, nombre, descripcion, fechaVencimiento, prioridad, tiempoEstimadoMinutos);
        setLugar(lugar);
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    @Override
    public String getTipo() {
        return "PERSONAL";
    }
}
