package com.espol.vista;

import com.espol.controlador.SistemaManager;
import com.espol.modelo.Carta;
import com.espol.modelo.JuegoMemoria;

class VistaMemoria {
    private final SistemaManager sistema;
    private final Consola consola;

    VistaMemoria(SistemaManager sistema, Consola consola) {
        this.sistema = sistema;
        this.consola = consola;
    }

    void jugar() {
        JuegoMemoria juego = sistema.iniciarJuegoMemoria();
        Carta[][] tablero = juego.getTablero();
        int totalPares = tablero.length * tablero[0].length / 2;
        System.out.printf("%n--- JUEGO DE MEMORIA ECOLOGICO ---%nEncuentra los %d pares!%n", totalPares);
        mostrarUbicacionPares(tablero);
        mostrarTablero(tablero);
        marcador(juego, totalPares);
        if (consola.leer("Presione [ENTER] para comenzar o 0 para volver: ").equals("0")) {
            return;
        }

        while (!juego.estaCompletado()) {
            System.out.printf("%n--- TURNO %d | SELECCION ---%n", juego.getTurnosJugados() + 1);
            Carta primera = seleccionar(juego, tablero, "PRIMERA");
            if (primera == null) {
                return;
            }
            Carta segunda = seleccionar(juego, tablero, "SEGUNDA");
            if (segunda == null) {
                return;
            }

            System.out.println("\nVolteando cartas...");
            mostrarTablero(tablero);
            boolean coinciden = juego.verificarCoincidencia(primera, segunda);
            marcador(juego, totalPares);
            if (coinciden) {
                System.out.println("PAR ENCONTRADO! El par " + primera.getContenido() + " se mantiene visible.");
            } else {
                System.out.printf("ERROR! %s y %s no coinciden. Las cartas se cubren de nuevo.%n",
                        primera.getContenido(), segunda.getContenido());
            }
            if (!juego.estaCompletado()) {
                if (consola.leer("Presione [ENTER] para continuar al siguiente intento o 0 para volver: ").equals("0")) {
                    return;
                }
                mostrarTablero(tablero);
            }
        }
        System.out.printf("Juego completado en %d turnos.%n", juego.getTurnosJugados());
        consola.pausa("para volver al menu principal");
    }

    private Carta seleccionar(JuegoMemoria juego, Carta[][] tablero, String orden) {
        int columnas = tablero[0].length;
        while (true) {
            int numero = consola.entero("Ingrese el numero de la " + orden + " carta (0 para volver): ",
                    tablero.length * columnas);
            if (numero == 0) {
                return null;
            }
            int indice = numero - 1;
            Carta carta = juego.seleccionarCarta(indice / columnas, indice % columnas);
            if (carta != null) {
                return carta;
            }
            System.out.println("La carta ya esta revelada. Elija otra.");
        }
    }

    private void marcador(JuegoMemoria juego, int totalPares) {
        System.out.printf("%nTotal de Intentos: %d | Pares Encontrados: %d/%d%n",
                juego.getTurnosJugados(), juego.getParesEncontrados(), totalPares);
    }

    private void mostrarUbicacionPares(Carta[][] tablero) {
        int columnas = tablero[0].length;
        int totalCartas = tablero.length * columnas;
        System.out.println("\n--- UBICACION DE LOS PARES (REVISION) ---");
        for (int primera = 0; primera < totalCartas; primera++) {
            Carta carta = tablero[primera / columnas][primera % columnas];
            for (int segunda = primera + 1; segunda < totalCartas; segunda++) {
                Carta otra = tablero[segunda / columnas][segunda % columnas];
                if (carta.getIdPar() == otra.getIdPar()) {
                    System.out.printf("%s: posiciones %d y %d%n",
                            carta.getContenido(), primera + 1, segunda + 1);
                    break;
                }
            }
        }
    }

    private void mostrarTablero(Carta[][] tablero) {
        String borde = "+";
        for (int i = 0; i < tablero[0].length; i++) {
            borde += "------------+";
        }

        int numero = 1;
        System.out.println("\n" + borde);
        for (Carta[] fila : tablero) {
            System.out.print("|");
            for (Carta carta : fila) {
                if (carta.isEstaRevelada()) {
                    System.out.printf(" %-10s |", carta.getContenido());
                } else {
                    System.out.printf(" %-10d |", numero);
                }
                numero++;
            }
            System.out.println("\n" + borde);
        }
    }
}
