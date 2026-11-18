package com.espol.vista;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

class Consola {
    static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/uuuu");
    static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/uuuu HH:mm");
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
        if (texto.isEmpty() || texto.length() > 10) {
            return -1;
        }
        long valor = 0;
        for (int i = 0; i < texto.length(); i++) {
            char digito = texto.charAt(i);
            if (digito < '0' || digito > '9') {
                return -1;
            }
            valor = valor * 10 + digito - '0';
        }
        if (valor > Integer.MAX_VALUE) {
            return -1;
        }
        return (int) valor;
    }

    double decimal(String mensaje, double maximo) {
        while (true) {
            String texto = leer(mensaje).replace(',', '.');
            int punto = texto.indexOf('.');
            int entero = leerEntero(texto);
            if (punto >= 0) {
                entero = leerEntero(texto.substring(0, punto));
                String decimales = texto.substring(punto + 1);
                int fraccion = leerEntero(decimales);
                if (entero >= 0 && fraccion >= 0) {
                    double valor = entero + fraccion / Math.pow(10, decimales.length());
                    if (valor <= maximo) {
                        return valor;
                    }
                }
            } else if (entero >= 0 && entero <= maximo) {
                return entero;
            }
            System.out.println("Ingrese un numero valido entre 0 y " + maximo + ".");
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

    // Se comprueban los componentes antes de construir la fecha.
    private LocalDateTime leerFecha(String texto) {
        int longitud = texto.length();
        if ((longitud != 10 && longitud != 16) || texto.charAt(2) != '/' || texto.charAt(5) != '/') {
            return null;
        }
        int dia = leerEntero(texto.substring(0, 2));
        int mes = leerEntero(texto.substring(3, 5));
        int year = leerEntero(texto.substring(6, 10));
        if (year < 1 || mes < 1 || mes > 12 || dia < 1 || dia > YearMonth.of(year, mes).lengthOfMonth()) {
            return null;
        }

        int hora = 23;
        int minuto = 59;
        if (longitud == 16) {
            if (texto.charAt(10) != ' ' || texto.charAt(13) != ':') {
                return null;
            }
            hora = leerEntero(texto.substring(11, 13));
            minuto = leerEntero(texto.substring(14, 16));
            if (hora < 0 || hora > 23 || minuto < 0 || minuto > 59) {
                return null;
            }
        }
        return LocalDateTime.of(year, mes, dia, hora, minuto);
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
