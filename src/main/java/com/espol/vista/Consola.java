package com.espol.vista;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Scanner;

class Consola {
    static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);
    static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm")
            .withResolverStyle(ResolverStyle.STRICT);
    private final Scanner entrada = new Scanner(System.in);

    String leer(String mensaje) {
        System.out.print(mensaje);
        if (entrada.hasNextLine()) {
            return entrada.nextLine().trim();
        }
        return "0";
    }

    int entero(String mensaje, int maximo) {
        while (true) {
            int numero = leerEntero(leer(mensaje));
            if (numero >= 0 && numero <= maximo) {
                return numero;
            }
            System.out.println("Ingrese un entero entre 0 y " + maximo + ".");
        }
    }

    // Devuelve -1 cuando el texto no representa un entero positivo o cero.
    private int leerEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    double decimal(String mensaje, double maximo) {
        while (true) {
            double valor = leerDecimal(leer(mensaje));
            if (Double.isFinite(valor) && valor >= 0 && valor <= maximo) {
                return valor;
            }
            System.out.println("Ingrese un numero valido entre 0 y " + maximo + ".");
        }
    }

    private double leerDecimal(String texto) {
        try {
            return Double.parseDouble(texto.replace(',', '.'));
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    LocalDateTime vencimiento() {
        while (true) {
            String texto = leer("Ingrese la fecha de vencimiento (DD/MM/YYYY HH:MM, 0 para volver): ");
            if (texto.equals("0")) {
                return null;
            }
            LocalDateTime fecha = leerFecha(texto);
            if (fecha != null) {
                return fecha;
            }
            System.out.println("Ingrese una fecha valida, por ejemplo 30/11/2026 17:00. La hora es opcional.");
        }
    }

    private LocalDateTime leerFecha(String texto) {
        try {
            LocalDateTime fecha;
            if (texto.length() == 10) {
                fecha = LocalDate.parse(texto, FECHA).atTime(23, 59);
            } else {
                fecha = LocalDateTime.parse(texto, FECHA_HORA);
            }
            if (fecha.getYear() >= 1 && fecha.getYear() <= 9999) {
                return fecha;
            }
        } catch (DateTimeParseException ex) {
            return null;
        }
        return null;
    }

    boolean confirmar(String mensaje) {
        while (true) {
            String respuesta = leer(mensaje + " (S/N): ");
            if (respuesta.equalsIgnoreCase("S")) {
                return true;
            }
            if (respuesta.equalsIgnoreCase("N") || respuesta.equals("0")) {
                return false;
            }
            System.out.println("Ingrese S o N.");
        }
    }

    void pausa(String destino) {
        leer("Presione [ENTER] " + destino + "...");
    }
}
