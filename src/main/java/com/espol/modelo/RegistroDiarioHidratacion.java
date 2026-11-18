package com.espol.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class RegistroDiarioHidratacion {
    public static final int META_PREDETERMINADA_ML = 2500;

    private final LocalDate fecha;
    private int metaDiariaMl = META_PREDETERMINADA_ML;
    private final ArrayList<DetalleHidratacion> registros = new ArrayList<>();

    public RegistroDiarioHidratacion(int metaDiariaMl) {
        this(LocalDate.now(), metaDiariaMl);
    }

    public RegistroDiarioHidratacion(LocalDate fecha, int metaDiariaMl) {
        this.fecha = fecha;
        setMetaDiariaMl(metaDiariaMl);
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getMetaDiariaMl() {
        return metaDiariaMl;
    }

    public void setMetaDiariaMl(int meta) {
        if (meta > 0) {
            metaDiariaMl = meta;
        }
    }

    public void registrarIngesta(int ml, LocalDateTime fechaHora) {
        if (fecha.equals(fechaHora.toLocalDate()) && ml > 0 && ml <= Integer.MAX_VALUE - getAcumuladoHoy(fecha)) {
            registros.add(new DetalleHidratacion(fechaHora, ml));
        }
    }

    public ArrayList<DetalleHidratacion> getRegistrosPorFecha(LocalDate dia) {
        if (fecha.equals(dia)) {
            return new ArrayList<>(registros);
        }
        return new ArrayList<>();
    }

    public int getAcumuladoHoy(LocalDate dia) {
        int total = 0;
        if (fecha.equals(dia)) {
            for (DetalleHidratacion registro : registros) {
                total += registro.getCantidadMl();
            }
        }
        return total;
    }

    public double calcularPorcentajeProgreso(LocalDate dia) {
        return 100.0 * getAcumuladoHoy(dia) / metaDiariaMl;
    }
}
