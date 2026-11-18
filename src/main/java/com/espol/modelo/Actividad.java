package com.espol.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class Actividad {
    private final int id;
    private final String nombre;
    private final String descripcion;
    private final LocalDateTime fechaVencimiento;
    private final Prioridad prioridad;
    private final int tiempoEstimadoMinutos;
    private EstadoActividad estado = EstadoActividad.PENDIENTE;
    private double porcentajeAvance;
    private final ArrayList<SesionEnfoque> historialEnfoque = new ArrayList<>();

    public Actividad(int id, String nombre, String descripcion, LocalDateTime fechaVencimiento,
                     Prioridad prioridad, int tiempoEstimadoMinutos) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaVencimiento = fechaVencimiento;
        this.prioridad = prioridad;
        this.tiempoEstimadoMinutos = tiempoEstimadoMinutos;
    }

    protected Actividad(int id, String nombre, String descripcion) {
        this(id, nombre, descripcion, LocalDate.now().atTime(23, 59), Prioridad.MEDIA, 0);
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public LocalDateTime getFechaVencimiento() {
        return fechaVencimiento;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public EstadoActividad getEstado() {
        return estado;
    }

    public int getTiempoEstimadoMinutos() {
        return tiempoEstimadoMinutos;
    }

    public double getPorcentajeAvance() {
        return porcentajeAvance;
    }

    // El estado cambia junto con el porcentaje registrado.
    public void setPorcentajeAvance(double avance) {
        if (!(avance >= 0 && avance <= 100)) {
            return;
        }
        porcentajeAvance = avance;
        if (avance == 100) {
            estado = EstadoActividad.COMPLETADA;
        } else if (avance > 0) {
            estado = EstadoActividad.EN_CURSO;
        } else {
            estado = EstadoActividad.PENDIENTE;
        }
    }

    public void registrarSesionEnfoque(SesionEnfoque sesion) {
        historialEnfoque.add(sesion);
    }

    public ArrayList<SesionEnfoque> getHistorialEnfoque() {
        return new ArrayList<>(historialEnfoque);
    }

    public abstract String getTipo();
}
