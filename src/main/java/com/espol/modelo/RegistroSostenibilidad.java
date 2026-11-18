package com.espol.modelo;

import java.time.LocalDate;
import java.util.ArrayList;

public class RegistroSostenibilidad {
    private final LocalDate fecha;
    private final ArrayList<AccionSostenible> accionesRealizadas = new ArrayList<>();

    public RegistroSostenibilidad(LocalDate fecha) {
        this.fecha = fecha;
    }

    // Una accion se cuenta una sola vez por dia.
    public void agregarAccion(AccionSostenible accion) {
        for (AccionSostenible realizada : accionesRealizadas) {
            if (realizada.getId() == accion.getId()) {
                return;
            }
        }
        accionesRealizadas.add(accion);
    }

    public ArrayList<AccionSostenible> getAccionesRealizadas() {
        return new ArrayList<>(accionesRealizadas);
    }

    public LocalDate getFecha() {
        return fecha;
    }
}
