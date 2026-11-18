package com.espol.controlador;

import com.espol.modelo.AccionSostenible;
import com.espol.modelo.Actividad;
import com.espol.modelo.ActividadPersonal;
import com.espol.modelo.Examen;
import com.espol.modelo.JuegoMemoria;
import com.espol.modelo.Prioridad;
import com.espol.modelo.Proyecto;
import com.espol.modelo.RegistroDiarioHidratacion;
import com.espol.modelo.RegistroSostenibilidad;
import com.espol.modelo.SesionEnfoque;
import com.espol.modelo.Tarea;
import com.espol.modelo.TipoTecnica;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SistemaManager {
    public static final int MINUTOS_POMODORO = 25;
    public static final int MINUTOS_DESCANSO_POMODORO = 5;
    public static final int MINUTOS_DEEP_WORK = 90;
    public static final int DIAS_RESUMEN = 7;

    private final List<Actividad> actividades = new ArrayList<>();
    private final ArrayList<RegistroDiarioHidratacion> historialHidratacion = new ArrayList<>();
    private final ArrayList<RegistroSostenibilidad> historialSostenibilidad = new ArrayList<>();
    private final AccionSostenible[] catalogo = {
        new AccionSostenible(1, "Use transporte publico, bicicleta o camine.", 1),
        new AccionSostenible(2, "No realice impresiones.", 1),
        new AccionSostenible(3, "No utilice envases descartables (use mi termo/taza).", 1),
        new AccionSostenible(4, "Separe y recicle materiales (vidrio, plastico, papel).", 1)
    };
    private int metaDiariaMl = RegistroDiarioHidratacion.META_PREDETERMINADA_ML;
    private int siguienteId = 1;
    private boolean inicializado;

    // Datos que pide el enunciado para el arranque.
    public void inicializarApp() {
        if (inicializado) {
            return;
        }
        int year = 2026;
        LocalDate noviembre18 = LocalDate.of(year, 11, 18);
        LocalDate noviembre19 = LocalDate.of(year, 11, 19);
        LocalDate noviembre30 = LocalDate.of(year, 11, 30);
        String poo = "Programacion Orientada a Objetos";

        agregarActividad(new ActividadPersonal(generarIdActividad(), "Cita medica",
                "Control medico de rutina", noviembre30.atTime(10, 0), Prioridad.ALTA, 45, "Centro medico"));
        Proyecto proyecto = new Proyecto(generarIdActividad(), "Sistema de gestion del tiempo",
                "Implementar la aplicacion de consola", noviembre30.atTime(23, 59), Prioridad.ALTA, 3600, poo);
        proyecto.setPorcentajeAvance(70);
        proyecto.registrarSesionEnfoque(new SesionEnfoque(noviembre18, TipoTecnica.POMODORO, MINUTOS_POMODORO));
        proyecto.registrarSesionEnfoque(new SesionEnfoque(noviembre19, TipoTecnica.POMODORO, MINUTOS_POMODORO));
        agregarActividad(proyecto);
        agregarActividad(new Tarea(generarIdActividad(), "Cuestionario de la unidad 2",
                "Resolver los ejercicios de la unidad", LocalDate.of(year, 12, 3).atTime(23, 59),
                Prioridad.MEDIA, 120, poo));
        agregarActividad(new Examen(generarIdActividad(), "Examen parcial de calculo",
                "Repasar los temas del primer parcial", LocalDate.of(year, 12, 10).atTime(9, 0),
                Prioridad.ALTA, 180, "Calculo"));

        getHidratacion(noviembre18).registrarIngesta(400, noviembre18.atTime(9, 30));
        getHidratacion(noviembre19).registrarIngesta(300, noviembre19.atTime(12, 15));
        registrarAcciones(noviembre18, 1, 4);
        registrarAcciones(noviembre19, 2, 3);
        inicializado = true;
    }

    private void registrarAcciones(LocalDate fecha, int primera, int segunda) {
        ArrayList<Integer> acciones = new ArrayList<>();
        acciones.add(primera);
        acciones.add(segunda);
        registrarAcciones(fecha, acciones);
    }

    public int generarIdActividad() {
        return siguienteId++;
    }

    public void agregarActividad(Actividad actividad) {
        actividades.add(actividad);
    }

    public ArrayList<Actividad> getActividades() {
        return new ArrayList<>(actividades);
    }

    public ArrayList<Actividad> filtrarActividadesVigentes(LocalDateTime ahora) {
        ArrayList<Actividad> vigentes = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (!actividad.getFechaVencimiento().isBefore(ahora)) {
                vigentes.add(actividad);
            }
        }
        return vigentes;
    }

    public ArrayList<Actividad> filtrarActividadesPendientes() {
        ArrayList<Actividad> pendientes = new ArrayList<>();
        for (Actividad actividad : actividades) {
            if (actividad.getPorcentajeAvance() < 100) {
                pendientes.add(actividad);
            }
        }
        return pendientes;
    }

    public void eliminarActividad(Actividad actividad) {
        actividades.remove(actividad);
    }

    public void registrarSesionEnfoque(Actividad actividad, TipoTecnica tecnica, LocalDate fecha) {
        int minutos = MINUTOS_DEEP_WORK;
        if (tecnica == TipoTecnica.POMODORO) {
            minutos = MINUTOS_POMODORO;
        }
        actividad.registrarSesionEnfoque(new SesionEnfoque(fecha, tecnica, minutos));

        int estimado = actividad.getTiempoEstimadoMinutos();
        if (estimado > 0) {
            double avance = actividad.getPorcentajeAvance() + 100.0 * minutos / estimado;
            actividad.setPorcentajeAvance(Math.min(100, avance));
        }
    }

    public RegistroDiarioHidratacion getHidratacion(LocalDate fecha) {
        for (RegistroDiarioHidratacion registro : historialHidratacion) {
            if (registro.getFecha().equals(fecha)) {
                return registro;
            }
        }
        RegistroDiarioHidratacion registro = new RegistroDiarioHidratacion(fecha, metaDiariaMl);
        historialHidratacion.add(registro);
        return registro;
    }

    public void establecerMetaDiaria(int meta, LocalDate fecha) {
        if (meta > 0) {
            getHidratacion(fecha).setMetaDiariaMl(meta);
            metaDiariaMl = meta;
        }
    }

    public AccionSostenible[] getCatalogoAcciones() {
        return catalogo.clone();
    }

    public void registrarAcciones(LocalDate fecha, ArrayList<Integer> seleccionadas) {
        RegistroSostenibilidad registro = null;
        for (RegistroSostenibilidad entrada : historialSostenibilidad) {
            if (entrada.getFecha().equals(fecha)) {
                registro = entrada;
                break;
            }
        }
        if (registro == null) {
            registro = new RegistroSostenibilidad(fecha);
            historialSostenibilidad.add(registro);
        }
        for (int id : seleccionadas) {
            registro.agregarAccion(catalogo[id - 1]);
        }
    }

    public ArrayList<RegistroSostenibilidad> getRegistrosSemana(LocalDate hasta) {
        LocalDate desde = hasta.minusDays(DIAS_RESUMEN - 1);
        ArrayList<RegistroSostenibilidad> semana = new ArrayList<>();
        for (RegistroSostenibilidad registro : historialSostenibilidad) {
            LocalDate fecha = registro.getFecha();
            if (!fecha.isBefore(desde) && !fecha.isAfter(hasta)) {
                semana.add(registro);
            }
        }
        return semana;
    }

    public JuegoMemoria iniciarJuegoMemoria() {
        JuegoMemoria juego = new JuegoMemoria(4, 4);
        String[] temas = {
                "RECICLAJE", "BICICLETA", "AGUA", "SOLAR", "EOLICA", "COMPOST", "BOSQUES", "REUSO"};
        juego.inicializarTablero(temas);
        return juego;
    }
}
