package com.espol.vista;

import com.espol.controlador.SistemaManager;
import com.espol.modelo.Actividad;
import com.espol.modelo.ActividadAcademica;
import com.espol.modelo.ActividadPersonal;
import com.espol.modelo.Examen;
import com.espol.modelo.Prioridad;
import com.espol.modelo.Proyecto;
import com.espol.modelo.SesionEnfoque;
import com.espol.modelo.Tarea;
import com.espol.modelo.TipoTecnica;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

class MenuActividades {
    private static final String LINEA = "======================================================";
    private static final int RESUMEN = 0;
    private static final int AVANCE = 1;
    private static final int COMPLETO = 2;
    private final SistemaManager sistema;
    private final Consola consola;

    MenuActividades(SistemaManager sistema, Consola consola) {
        this.sistema = sistema;
        this.consola = consola;
    }

    void gestionar() {
        int opcion;
        do {
            System.out.println("\n--- GESTION DE ACTIVIDADES ---");
            System.out.println("1. Visualizar actividades");
            System.out.println("2. Crear actividad");
            System.out.println("3. Registrar avance de actividad");
            System.out.println("4. Eliminar actividad");
            System.out.println("5. Volver al menu");
            opcion = consola.entero("Ingrese su opcion: ", 5);

            switch (opcion) {
                case 1:
                    visualizar();
                    return;
                case 2:
                    crear();
                    return;
                case 3:
                    registrarAvance();
                    break;
                case 4:
                    eliminar();
                    break;
                default:
                    break;
            }
        } while (opcion != 0 && opcion != 5);
    }

    private void visualizar() {
        ArrayList<Actividad> vigentes = sistema.filtrarActividadesVigentes(LocalDateTime.now());
        while (true) {
            System.out.println("\n--- LISTADO DE ACTIVIDADES PENDIENTES ---");
            Actividad actividad = seleccionar(vigentes,
                    "Ingrese el ID para ver DETALLES o '0' para volver: ", COMPLETO);
            if (actividad == null) {
                return;
            }
            mostrarDetalle(actividad);
            consola.pausa("para volver a la lista");
        }
    }

    private void mostrarDetalle(Actividad actividad) {
        System.out.println("\n" + LINEA);
        System.out.printf("    DETALLES DE %s (ID %d)%n", actividad.getTipo(), actividad.getId());
        System.out.println(LINEA);
        System.out.println("Nombre: " + actividad.getNombre());
        System.out.println("Tipo: " + actividad.getTipo());
        System.out.println("Descripcion: " + actividad.getDescripcion());

        String tiempo = actividad.getTiempoEstimadoMinutos() + " min";

        if (actividad instanceof ActividadAcademica) {
            ActividadAcademica academica = (ActividadAcademica) actividad;
            System.out.println("Asignatura: " + academica.getAsignatura());
            tiempo = String.format("%.1f horas", actividad.getTiempoEstimadoMinutos() / 60.0);
        } else {
            ActividadPersonal personal = (ActividadPersonal) actividad;
            if (!personal.getLugar().isEmpty()) {
                System.out.println("Lugar: " + personal.getLugar());
            }
        }
        System.out.println("Prioridad: " + actividad.getPrioridad());
        System.out.println("Estado: " + actividad.getEstado());
        System.out.println("Fecha Limite: " + actividad.getFechaVencimiento().format(Consola.FECHA_HORA));
        System.out.println("Tiempo Estimado Total: " + tiempo);
        System.out.printf("Avance Actual: %.1f%%%n", actividad.getPorcentajeAvance());
        System.out.println("---------------------------------------------");
        System.out.println("HISTORIAL DE GESTION DEL TIEMPO");
        System.out.println("| Fecha Sesion | Tecnica Aplicada | Duracion (min) |");
        ArrayList<SesionEnfoque> sesiones = actividad.getHistorialEnfoque();
        if (sesiones.isEmpty()) {
            System.out.println("Sin sesiones registradas.");
        }
        for (SesionEnfoque sesion : sesiones) {
            String tecnica = "Deep Work";
            if (sesion.getTecnica() == TipoTecnica.POMODORO) {
                tecnica = "Pomodoro";
            }
            System.out.printf("| %-12s | %-16s | %-14d |%n",
                    sesion.getFecha().format(Consola.FECHA), tecnica, sesion.getDuracionMinutos());
        }
        System.out.println("---------------------------------------------");
    }

    private void crear() {
        System.out.println("\n" + LINEA + "\n    F O C U S F L O W | C R E A R\n" + LINEA);
        System.out.println("--- PASO 1: CATEGORIA ---");
        System.out.println("1. ACADEMICA (Tarea, Examen, Proyecto)");
        System.out.println("2. PERSONAL (Citas, Ejercicio, Hobbies)");
        int categoria = consola.entero("Ingrese opcion (0 para volver): ", 2);
        if (categoria == 0) {
            return;
        }

        boolean academica = categoria == 1;
        int tipo = 0;
        if (academica) {
            System.out.println("\n--- PASO 2: TIPO (Academico) ---");
            System.out.println("1. TAREA\n2. EXAMEN\n3. PROYECTO");
            tipo = consola.entero("Ingrese opcion (0 para volver): ", 3);
            if (tipo == 0) {
                return;
            }
        } else {
            System.out.println("\n--- PASO 2: TIPO (Personal) ---");
            System.out.println("Ha seleccionado: PERSONAL.");
        }

        System.out.println("\n--- PASO 3: DETALLES ---");
        String nombre = consola.leer("Ingrese el nombre (0 para volver): ");
        if (nombre.isEmpty() || nombre.equals("0")) {
            return;
        }
        String descripcion = consola.leer("Ingrese la descripcion: ");
        String asignatura = "";
        if (academica) {
            asignatura = consola.leer("Ingrese la asignatura (0 para volver): ");
            if (asignatura.isEmpty() || asignatura.equals("0")) {
                return;
            }
        }

        LocalDateTime vencimiento = consola.vencimiento();
        if (vencimiento == null) {
            return;
        }
        Prioridad prioridad = leerPrioridad();
        if (prioridad == null) {
            return;
        }

        int minutos;
        if (academica) {
            double horas = consola.decimal("Ingrese el tiempo estimado (en horas, ej. 1,5, 0 para volver): ",
                    Integer.MAX_VALUE / 60);
            if (horas == 0) {
                return;
            }
            minutos = (int) Math.max(1, Math.round(horas * 60));
        } else {
            minutos = consola.entero("Ingrese el tiempo estimado (en minutos, 0 para volver): ", Integer.MAX_VALUE);
            if (minutos == 0) {
                return;
            }
        }

        String lugar = "";
        if (!academica) {
            lugar = consola.leer("Ingrese el lugar (opcional, 0 para volver): ");
            if (lugar.equals("0")) {
                return;
            }
        }

        int id = sistema.generarIdActividad();
        Actividad actividad;
        if (tipo == 1) {
            actividad = new Tarea(id, nombre, descripcion, vencimiento, prioridad, minutos, asignatura);
        } else if (tipo == 2) {
            actividad = new Examen(id, nombre, descripcion, vencimiento, prioridad, minutos, asignatura);
        } else if (tipo == 3) {
            actividad = new Proyecto(id, nombre, descripcion, vencimiento, prioridad, minutos, asignatura);
        } else {
            actividad = new ActividadPersonal(id, nombre, descripcion, vencimiento, prioridad, minutos, lugar);
        }
        sistema.agregarActividad(actividad);
        System.out.printf("%n%s '%s' creada con exito (ID %d).%n", actividad.getTipo(), nombre, id);
        consola.pausa("para volver al menu principal");
    }

    private Prioridad leerPrioridad() {
        while (true) {
            String texto = consola.leer("Ingrese prioridad (ALTA, MEDIA, BAJA, 0 para volver): ");
            if (texto.equalsIgnoreCase("ALTA")) {
                return Prioridad.ALTA;
            }
            if (texto.equalsIgnoreCase("MEDIA")) {
                return Prioridad.MEDIA;
            }
            if (texto.equalsIgnoreCase("BAJA")) {
                return Prioridad.BAJA;
            }
            if (texto.equals("0")) {
                return null;
            }
            System.out.println("Ingrese ALTA, MEDIA o BAJA.");
        }
    }

    private void registrarAvance() {
        System.out.println("\n--- REGISTRAR AVANCE ---");
        System.out.println("Actividades pendientes:");
        Actividad actividad = seleccionar(sistema.filtrarActividadesPendientes(),
                "Ingrese el ID de la actividad a actualizar (o 0 para salir): ", AVANCE);
        if (actividad == null) {
            return;
        }
        System.out.printf("Ha seleccionado: %s '%s'.%n", actividad.getTipo(), actividad.getNombre());
        System.out.printf("Avance actual: %.1f%%.%n", actividad.getPorcentajeAvance());
        double avance = consola.decimal("Ingrese el nuevo porcentaje de avance (0 - 100): ", 100);
        if (consola.confirmar("Confirma que el nuevo avance para el ID " + actividad.getId()
                + " es " + avance + "%?")) {
            actividad.setPorcentajeAvance(avance);
            System.out.println("Avance registrado.");
        }
        consola.pausa("para volver a Gestion de Actividades");
    }

    private void eliminar() {
        System.out.println("\n--- ELIMINAR ACTIVIDAD ---");
        System.out.println("Seleccione el ID de la actividad a eliminar:");
        Actividad actividad = seleccionar(sistema.getActividades(),
                "Ingrese el ID de la actividad a eliminar (o 0 para cancelar): ", RESUMEN);
        if (actividad == null) {
            return;
        }
        System.out.printf("Usted selecciono la actividad ID %d: '%s' (TIPO: %s).%n",
                actividad.getId(), actividad.getNombre(), actividad.getTipo());
        if (consola.confirmar("Esta seguro que desea ELIMINAR PERMANENTEMENTE esta actividad?")) {
            sistema.eliminarActividad(actividad);
            System.out.println("Actividad eliminada.");
        }
    }

    void enfoque() {
        while (true) {
            System.out.println("\n--- T E C N I C A S  D E  E N F O Q U E ---");
            System.out.println("1. Iniciar Pomodoro (" + SistemaManager.MINUTOS_POMODORO + " min Trabajo / " + SistemaManager.MINUTOS_DESCANSO_POMODORO + " min Descanso)");
            System.out.println("2. Iniciar Deep Work (Sesion Larga de " + SistemaManager.MINUTOS_DEEP_WORK + " min)");
            System.out.println("3. Volver al Menu Principal");
            int opcion = consola.entero("Ingrese su opcion: ", 3);
            if (opcion == 0 || opcion == 3) {
                return;
            }

            String nombre = "Deep Work";
            TipoTecnica tecnica = TipoTecnica.DEEP_WORK;
            int minutos = SistemaManager.MINUTOS_DEEP_WORK;
            if (opcion == 1) {
                nombre = "Pomodoro";
                tecnica = TipoTecnica.POMODORO;
                minutos = SistemaManager.MINUTOS_POMODORO;
            }
            System.out.println("\n--- INICIAR " + nombre.toUpperCase() + " ---");
            System.out.println("Seleccione la actividad a trabajar:");
            Actividad actividad = seleccionar(sistema.filtrarActividadesPendientes(),
                    "Ingrese ID de la actividad (o 0 para salir): ", RESUMEN);
            if (actividad == null) {
                continue;
            }
            int totalCiclos = (opcion == 1) ? 4 : 1; // 4 vueltas para Pomodoro, 1 para Deep Work

            for (int ciclo = 1; ciclo <= totalCiclos; ciclo++) {
    
              System.out.println("\n>>> INICIANDO TRABAJO EN '" + actividad.getNombre() + "' <<<");
    
            // Si es Pomodoro imprime el ciclo, si es Deep Work no imprime esa parte
              if (opcion == 1) {
                 System.out.println("Tecnica: Pomodoro | Ciclo: " + ciclo + "/4");
              } else {
                 System.out.println("Tecnica: Deep Work");
              }
    
              System.out.println("Tiempo de Trabajo: " + minutos + ":00 minutos restantes");
    
             // La simulación de tiempo esperando el ENTER
              String accion = consola.leer("[Simulacion] Presione [ENTER] para terminar el tiempo o 0 para salir: ");
              if (accion.equals("0")) {
                break; // Rompe el bucle for y termina anticipadamente
              }
    
            // Registramos la sesión solo si presionó ENTER
              sistema.registrarSesionEnfoque(actividad, tecnica, LocalDate.now());
    
              System.out.println("\n--- TIEMPO DE TRABAJO TERMINADO ---");
              System.out.println("Sesion registrada. Avance de la actividad ID " + actividad.getId() + " actualizado a: " + String.format("%.1f", actividad.getPorcentajeAvance()) + "%");
              if (actividad.getPorcentajeAvance() >= 100.0) {
                System.out.println("¡Excelente! Has completado la actividad al 100%.");
                break; // La palabra clave 'break' rompe el ciclo for inmediatamente
              }
            // Si es Pomodoro Y NO es el último ciclo, le damos el descanso
              if (opcion == 1 && ciclo < 4) {
                System.out.println("Ahora toma un DESCANSO: 05:00 minutos restantes");
                String descanso = consola.leer("[Simulacion] Presione [ENTER] para terminar el descanso o 0 para salir: ");
              if (descanso.equals("0")) {
                break; // Rompe el bucle si no quiere hacer más descansos
              }
              } else if (opcion == 1 && ciclo == 4) {
              System.out.println("¡Felicidades! Completaste los 4 ciclos Pomodoro.");
              consola.leer("Presione [ENTER] para volver al menu...");
              }
            }
        }
    }

    // El listado completo, el de avance y el de seleccion usan las mismas filas.
    private Actividad seleccionar(ArrayList<Actividad> actividades, String mensaje, int formato) {
        if (actividades.isEmpty()) {
            System.out.println("No hay actividades disponibles.");
            return null;
        }
        System.out.printf("%-4s | %-8s | %-36s", "ID", "TIPO", "NOMBRE");
        if (formato == COMPLETO) {
            System.out.print(" | VENCIMIENTO | PRIORIDAD");
        }
        if (formato != RESUMEN) {
            System.out.print(" | AVANCE (%)");
        }
        System.out.println();
        System.out.println("-----+----------+--------------------------------------"
                + (formato == COMPLETO ? "+-------------+-----------" : "") + (formato != RESUMEN ? "+------------" : ""));
        for (Actividad actividad : actividades) {
            System.out.printf("%-4d | %-8s | %-36s", actividad.getId(), actividad.getTipo(), actividad.getNombre());
            if (formato == COMPLETO) {
                System.out.printf(" | %-11s | %-9s", actividad.getFechaVencimiento().format(Consola.FECHA),
                        actividad.getPrioridad());
            }
            if (formato != RESUMEN) {
                System.out.printf(" | %.1f%%", actividad.getPorcentajeAvance());
            }
            System.out.println();
        }
        while (true) {
            int id = consola.entero(mensaje, Integer.MAX_VALUE);
            if (id == 0) {
                return null;
            }
            for (Actividad actividad : actividades) {
                if (actividad.getId() == id) {
                    return actividad;
                }
            }
            System.out.println("El ID debe estar en el listado.");
        }
    }
}
