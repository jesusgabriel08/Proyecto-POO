package com.espol.modelo;

import java.time.LocalDateTime;

public class Examen extends ActividadAcademica {
    public Examen(int id, String nombre, String descripcion, LocalDateTime fechaVencimiento,
                  Prioridad prioridad, int tiempoEstimadoMinutos, String asignatura) {
        super(id, nombre, descripcion, fechaVencimiento, prioridad, tiempoEstimadoMinutos, asignatura);
    }

    @Override
    public String getTipo() {
        return "EXAMEN";
    }
}
