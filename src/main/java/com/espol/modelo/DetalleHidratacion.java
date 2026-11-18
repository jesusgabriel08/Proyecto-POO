package com.espol.modelo;

import java.time.LocalDateTime;

public class DetalleHidratacion {
    private final LocalDateTime fechaHora;
    private final int cantidadMl;

    public DetalleHidratacion(LocalDateTime fechaHora, int cantidadMl) {
        this.fechaHora = fechaHora;
        this.cantidadMl = cantidadMl;
    }

    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    public int getCantidadMl() {
        return cantidadMl;
    }
}
