package com.espol.modelo;

import java.time.LocalDate;

public class SesionEnfoque {
    private final LocalDate fecha;
    private final TipoTecnica tecnica;
    private final int duracionMinutos;

    public SesionEnfoque(LocalDate fecha, TipoTecnica tecnica, int duracionMinutos) {
        this.fecha = fecha;
        this.tecnica = tecnica;
        this.duracionMinutos = duracionMinutos;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public TipoTecnica getTecnica() {
        return tecnica;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }
}
