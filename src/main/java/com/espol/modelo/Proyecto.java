package com.espol.modelo;

import java.time.LocalDateTime;

public class Proyecto extends ActividadAcademica {
    public Proyecto(int id, String nombre, String descripcion, LocalDateTime fechaVencimiento,
                  Prioridad prioridad, int tiempoEstimadoMinutos, String asignatura) {
        super(id, nombre, descripcion, fechaVencimiento, prioridad, tiempoEstimadoMinutos, asignatura);
    }

    @Override
    public String getTipo() {
        return "PROYECTO";
    }
}
