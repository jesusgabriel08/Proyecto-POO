package com.espol.modelo;

public class Carta {
    private final int idPar;
    private final String contenido;
    private boolean estaRevelada;
    private boolean estaEmparejada;

    public Carta(int idPar, String contenido) {
        this.idPar = idPar;
        this.contenido = contenido;
    }

    public int getIdPar() {
        return idPar;
    }

    public String getContenido() {
        return contenido;
    }

    public boolean isEstaRevelada() {
        return estaRevelada;
    }

    public void setEstaRevelada(boolean revelada) {
        estaRevelada = revelada || estaEmparejada;
    }

    public boolean isEstaEmparejada() {
        return estaEmparejada;
    }

    public void setEstaEmparejada(boolean emparejada) {
        estaEmparejada = emparejada;
        if (emparejada) {
            estaRevelada = true;
        }
    }
}
