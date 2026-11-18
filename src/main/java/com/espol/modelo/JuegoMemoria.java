package com.espol.modelo;

public class JuegoMemoria {
    private final int filas;
    private final int columnas;
    private final Carta[][] tablero;
    private int turnosJugados;
    private int paresEncontrados;

    public JuegoMemoria(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        tablero = new Carta[filas][columnas];
    }

    // La vista usa un tablero de 4x4 con ocho temas distintos.
    public void inicializarTablero(String[] temasSostenibles) {
        Carta[] cartas = new Carta[filas * columnas];
        for (int i = 0; i < cartas.length / 2; i++) {
            cartas[2 * i] = new Carta(i + 1, temasSostenibles[i]);
            cartas[2 * i + 1] = new Carta(i + 1, temasSostenibles[i]);
        }

        // Intercambia cada carta con una posicion al azar entre las que faltan.
        for (int i = cartas.length - 1; i > 0; i--) {
            int otraPosicion = (int) (Math.random() * (i + 1));
            Carta temporal = cartas[i];
            cartas[i] = cartas[otraPosicion];
            cartas[otraPosicion] = temporal;
        }
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                tablero[fila][columna] = cartas[fila * columnas + columna];
            }
        }
        turnosJugados = 0;
        paresEncontrados = 0;
    }

    public Carta seleccionarCarta(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas) {
            return null;
        }
        Carta carta = tablero[fila][columna];
        if (carta == null || carta.isEstaRevelada()) {
            return null;
        }
        carta.setEstaRevelada(true);
        return carta;
    }

    public boolean verificarCoincidencia(Carta primera, Carta segunda) {
        if (primera == null || segunda == null || primera == segunda
                || !primera.isEstaRevelada() || !segunda.isEstaRevelada()
                || primera.isEstaEmparejada() || segunda.isEstaEmparejada()) {
            return false;
        }
        turnosJugados++;
        if (primera.getIdPar() == segunda.getIdPar()) {
            primera.setEstaEmparejada(true);
            segunda.setEstaEmparejada(true);
            paresEncontrados++;
            return true;
        }
        primera.setEstaRevelada(false);
        segunda.setEstaRevelada(false);
        return false;
    }

    public boolean estaCompletado() {
        return paresEncontrados == filas * columnas / 2;
    }

    public int getTurnosJugados() {
        return turnosJugados;
    }

    public int getParesEncontrados() {
        return paresEncontrados;
    }

    public Carta[][] getTablero() {
        Carta[][] copia = new Carta[filas][columnas];
        for (int fila = 0; fila < filas; fila++) {
            for (int columna = 0; columna < columnas; columna++) {
                copia[fila][columna] = tablero[fila][columna];
            }
        }
        return copia;
    }
}
