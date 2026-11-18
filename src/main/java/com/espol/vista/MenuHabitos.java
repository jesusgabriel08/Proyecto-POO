package com.espol.vista;

import com.espol.controlador.SistemaManager;
import com.espol.modelo.AccionSostenible;
import com.espol.modelo.DetalleHidratacion;
import com.espol.modelo.RegistroDiarioHidratacion;
import com.espol.modelo.RegistroSostenibilidad;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Locale;

class MenuHabitos {
    private static final String LINEA = "----------------------------------------------------------";
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("hh:mm a", Locale.US);
    private final SistemaManager sistema;
    private final Consola consola;

    MenuHabitos(SistemaManager sistema, Consola consola) {
        this.sistema = sistema;
        this.consola = consola;
    }

    void hidratacion() {
        while (true) {
            System.out.println("\n--- CONTROL DE HIDRATACION ---");
            System.out.println("1. Registrar Ingesta de Agua");
            System.out.println("2. Establecer meta diaria");
            System.out.println("3. Ver Progreso Diario y Meta");
            System.out.println("4. Volver al Menu Principal");
            int opcion = consola.entero("Ingrese su opcion: ", 4);
            if (opcion == 0 || opcion == 4) {
                return;
            }
            LocalDateTime ahora = LocalDateTime.now();
            LocalDate hoy = ahora.toLocalDate();
            RegistroDiarioHidratacion registro = sistema.getHidratacion(hoy);
            if (opcion == 1) {
                registrarAgua(registro, ahora);
            } else if (opcion == 2) {
                establecerMeta(registro);
            } else {
                mostrarHidratacion(registro);
            }
        }
    }

    private void registrarAgua(RegistroDiarioHidratacion registro, LocalDateTime ahora) {
        System.out.println("\n--- REGISTRAR INGESTA DE AGUA ---");
        int anterior = registro.getAcumuladoHoy(registro.getFecha());
        int ml = consola.entero("Ingrese la cantidad de agua (mililitros, 0 para volver): ",
                Integer.MAX_VALUE - anterior);
        if (ml == 0) {
            return;
        }
        registro.registrarIngesta(ml, ahora);
        System.out.println("\nRegistro de " + ml + " ml agregado.\n");
        System.out.println("\n--- PROGRESO RAPIDO ---");
        System.out.println("Meta Diaria: " + registro.getMetaDiariaMl() + " ml");
        System.out.println("Acumulado Hoy: " + (anterior + ml) + " ml (Antes eran " + anterior + " ml)");
        mostrarBarra(registro);
        consola.pausa("para continuar");
    }

    private void establecerMeta(RegistroDiarioHidratacion registro) {
        System.out.println("\n--- ESTABLECER META DIARIA DE HIDRATACION ---");
        System.out.println("Meta diaria actual: " + registro.getMetaDiariaMl() + " ml.");
        int meta = consola.entero("Ingrese la nueva meta diaria (mililitros, 0 para volver): ", Integer.MAX_VALUE);
        if (meta == 0 || !consola.confirmar("Confirma que la nueva meta diaria es " + meta + " ml?")) {
            return;
        }
        LocalDate fecha = registro.getFecha();
        sistema.establecerMetaDiaria(meta, fecha);
        System.out.println("\nMeta diaria de hidratacion actualizada a " + meta + " ml con exito.");
        System.out.println("\n--- PROGRESO ACTUALIZADO ---");
        System.out.println("Acumulado Hoy: " + registro.getAcumuladoHoy(fecha) + " ml");
        System.out.println("Nueva Meta: " + meta + " ml");
        mostrarBarra(registro);
        consola.pausa("para volver al menu");
    }

    private void mostrarHidratacion(RegistroDiarioHidratacion registro) {
        System.out.println("\n--- PROGRESO DE HIDRATACION DIARIA ---");
        LocalDate fecha = registro.getFecha();
        int acumulado = registro.getAcumuladoHoy(fecha);
        int meta = registro.getMetaDiariaMl();
        System.out.println("Fecha: " + fecha.format(Consola.FECHA));
        System.out.println("\nMeta Diaria (ML): " + meta + " ml");
        System.out.println("Ingesta Acumulada: " + acumulado + " ml");
        System.out.println("\nFalta: " + Math.max(0, meta - acumulado) + " ml");
        mostrarBarra(registro);

        System.out.println("\nHistorial de Registros de Hoy:");
        ArrayList<DetalleHidratacion> detalles = registro.getRegistrosPorFecha(fecha);
        if (detalles.isEmpty()) {
            System.out.println("Sin ingestas registradas.");
        }
        for (DetalleHidratacion detalle : detalles) {
        System.out.println("- " + detalle.getFechaHora().format(HORA) + ": " + detalle.getCantidadMl() + " ml");
        }
        System.out.println(LINEA);
        System.out.println("Recuerda mantenerte hidratado!");
        consola.pausa("para volver al menu de Hidratacion");
    }

    private void mostrarBarra(RegistroDiarioHidratacion registro) {
        double porcentaje = registro.calcularPorcentajeProgreso(registro.getFecha());
        int bloques = (int) Math.min(20, Math.round(porcentaje / 5));
        System.out.print("Progreso: [");
        for (int i = 0; i < 20; i++) {
            if (i < bloques) {
                System.out.print("=");
            } else {
                System.out.print("-");
            }
        }
        System.out.printf("] %.0f%% (%d/%d)%n", porcentaje, registro.getAcumuladoHoy(registro.getFecha()),
                registro.getMetaDiariaMl());
    }

    void sostenibilidad() {
        LocalDate hoy = LocalDate.now();
        AccionSostenible[] catalogo = sistema.getCatalogoAcciones();
        System.out.printf("%n--- REGISTRO DIARIO DE SOSTENIBILIDAD (%s) ---%n", hoy.format(Consola.FECHA));
        System.out.println("Marque las acciones que realizo hoy (ej. 1, 3).");
        for (AccionSostenible accion : catalogo) {
            System.out.printf("%d. %s%n", accion.getId(), accion.getDescripcion());
        }
        while (true) {
            String entrada = consola.leer("\nIngrese sus selecciones (ENTER para resumen, 0 para volver): ");
            if (entrada.equals("0")) {
                return;
            }
            if (entrada.isEmpty()) {
                break;
            }

            ArrayList<Integer> seleccionadas = new ArrayList<>();
            boolean valida = true;
            for (String parte : entrada.split(",", -1)) {
                String opcion = parte.trim();
                if (opcion.length() != 1 || opcion.charAt(0) < '1' || opcion.charAt(0) > '0' + catalogo.length) {
                    valida = false;
                    break;
                }
                int id = opcion.charAt(0) - '0';
                if (!seleccionadas.contains(id)) {
                    seleccionadas.add(id);
                }
            }
            if (!valida) {
                System.out.println("Use numeros del 1 al " + catalogo.length + " separados por coma.");
                continue;
            }

            sistema.registrarAcciones(hoy, seleccionadas);
            System.out.println("\n" + LINEA);
            System.out.println("Acciones de sostenibilidad registradas:");
            for (int i = 0; i < catalogo.length; i++) {
                if (seleccionadas.contains(catalogo[i].getId())) {
                    System.out.println("- " + catalogo[i].getDescripcion());
                }
            }
            System.out.println("\nExcelente contribucion al planeta hoy!");
            consola.pausa("para ver el resumen semanal");
            break;
        }
        mostrarResumen(hoy, catalogo);
        consola.pausa("para volver al menu principal");
    }

    private void mostrarResumen(LocalDate hoy, AccionSostenible[] catalogo) {
        int dias = SistemaManager.DIAS_RESUMEN;
        int[] frecuencia = new int[catalogo.length];
        int conAcciones = 0;
        int completos = 0;
        for (RegistroSostenibilidad registro : sistema.getRegistrosSemana(hoy)) {
            ArrayList<AccionSostenible> acciones = registro.getAccionesRealizadas();
            if (!acciones.isEmpty()) {
                conAcciones++;
            }
            if (acciones.size() == catalogo.length) {
                completos++;
            }
            for (AccionSostenible accion : acciones) {
                frecuencia[accion.getId() - 1]++;
            }
        }

        System.out.printf("%n--- RESUMEN SEMANAL DE SOSTENIBILIDAD (%s - %s) ---%n",
                hoy.minusDays(dias - 1).format(Consola.FECHA), hoy.format(Consola.FECHA));
        System.out.println("\n" + LINEA + "\nFRECUENCIA DE ACCIONES\n" + LINEA);
        System.out.printf("%-53s | %-15s | %s%n", "ACCION", "VECES REALIZADA", "LOGRO");
        int menosFrecuente = 0;
        for (int i = 0; i < catalogo.length; i++) {
            int total = frecuencia[i];
            String logro = "Necesita mejorar";
            if (total == dias) {
                logro = "Excelente";
            } else if (total >= 5) {
                logro = "Muy bien";
            }
            System.out.printf("%-53s | %d / %d Dias      | %s%n", catalogo[i].getDescripcion(), total, dias, logro);
            if (total < frecuencia[menosFrecuente]) {
                menosFrecuente = i;
            }
        }
        System.out.println("\n" + LINEA + "\nANALISIS ECOLOGICO\n" + LINEA);
        System.out.printf("Dias con al menos 1 accion de sostenibilidad: %d de %d (%.0f%%)%n",
                conAcciones, dias, 100.0 * conAcciones / dias);
        System.out.printf("Dias con las %d acciones completas: %d de %d (%.0f%%)%n",
                catalogo.length, completos, dias, 100.0 * completos / dias);

        String[] consejos = {
            "Planea un trayecto a pie, en bici o en transporte publico.",
            "Guarda tus apuntes en formato digital.",
            "Lleva una botella o una taza reutilizable.",
            "Separa el papel, el plastico y el vidrio."
        };
        System.out.println("\nTip Ecologico de la Semana: " + consejos[menosFrecuente]);
        System.out.println(LINEA);
    }
}
