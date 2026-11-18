package com.espol.modelo;

import java.time.LocalDateTime;

public abstract class ActividadAcademica extends Actividad {
    private String asignatura = "";

    public ActividadAcademica(int id, String nombre, String descripcion) {
        super(id, nombre, descripcion);
    }

    protected ActividadAcademica(int id, String nombre, String descripcion,
                                 LocalDateTime fechaVencimiento, Prioridad prioridad,
                                 int tiempoEstimadoMinutos, String asignatura) {
        super(id, nombre, descripcion, fechaVencimiento, prioridad, tiempoEstimadoMinutos);
        setAsignatura(asignatura);
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }
}
