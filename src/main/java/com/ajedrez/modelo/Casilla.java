package com.ajedrez.modelo;

/**
 * Representa una coordenada individual dentro del tablero de ajedrez.
 * Mantiene el estado de si está vacía o contiene una pieza.
 */
public class Casilla {

    private final int fila;
    private final int columna;
    private Pieza piezaActual;

    /**
     * Inicializa una casilla en una coordenada específica.
     *
     * @param fila Fila del 0 al 7.
     * @param columna Columna del 0 al 7.
     */
    public Casilla(final int fila, final int columna) {
        this.fila = fila;
        this.columna = columna;
    }

    /**
     * Devuelve la fila de la casilla dentro del tablero.
     *
     * @return indice de fila, entre 0 y 7.
     */
    public int getFila() {
        return fila;
    }

    /**
     * Devuelve la columna de la casilla dentro del tablero.
     *
     * @return indice de columna, entre 0 y 7.
     */
    public int getColumna() {
        return columna;
    }

    /**
     * Devuelve la pieza ubicada en la casilla.
     *
     * @return pieza actual, o null si la casilla esta vacia.
     */
    public Pieza getPiezaActual() {
        return piezaActual;
    }

    /**
     * Coloca o retira una pieza de la casilla.
     *
     * @param piezaActual pieza que pasa a ocupar la casilla, o null para dejarla vacia.
     */
    public void setPiezaActual(final Pieza piezaActual) {
        this.piezaActual = piezaActual;
    }

    /**
     * Comprueba de forma segura si hay una pieza en la casilla.
     *
     * @return true si no hay ninguna pieza.
     */
    public boolean estaVacia() {
        return this.piezaActual == null;
    }
}
