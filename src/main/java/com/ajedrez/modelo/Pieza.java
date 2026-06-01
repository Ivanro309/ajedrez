package com.ajedrez.modelo;

/**
 * Clase abstracta que define el comportamiento base de cualquier pieza de ajedrez.
 * Aplica el principio Abierto/Cerrado (Open/Closed) de SOLID.
 */
public abstract class Pieza {

    private final ColorPieza color;
    private boolean haMovido;

    /**
     * Constructor principal de la pieza.
     *
     * @param color El color asignado a la pieza (Blanco o Negro).
     */
    protected Pieza(final ColorPieza color) {
        this.color = color;
        this.haMovido = false;
    }

    public boolean haMovido() {
        return haMovido;
    }

    public void setHaMovido(final boolean haMovido) {
        this.haMovido = haMovido;
    }

    /**
     * Obtiene el color de la pieza.
     *
     * @return ColorPieza asociado.
     */
    public ColorPieza getColor() {
        return this.color;
    }

    /**
     * Comprueba que no existan piezas entre la casilla de origen y la de destino.
     *
     * <p>Este metodo se usa para piezas de desplazamiento lineal, como torre,
     * alfil y reina. No debe utilizarse para el caballo, porque esa pieza puede
     * saltar sobre otras.</p>
     *
     * @param origen casilla desde la que parte la pieza.
     * @param destino casilla final del movimiento.
     * @param tablero tablero donde se comprueban las casillas intermedias.
     * @return true si todas las casillas intermedias estan libres.
     */
    protected boolean caminoEstaLibre(final Casilla origen, final Casilla destino, final Tablero tablero) {
        final int avanceFila = Integer.compare(destino.getFila(), origen.getFila());
        final int avanceColumna = Integer.compare(destino.getColumna(), origen.getColumna());
        int filaActual = origen.getFila() + avanceFila;
        int columnaActual = origen.getColumna() + avanceColumna;

        while (filaActual != destino.getFila() || columnaActual != destino.getColumna()) {
            final Casilla casillaIntermedia = tablero.obtenerCasilla(filaActual, columnaActual);
            if (casillaIntermedia == null || !casillaIntermedia.estaVacia()) {
                return false;
            }

            filaActual += avanceFila;
            columnaActual += avanceColumna;
        }

        return true;
    }

    /**
     * Método abstracto que cada pieza específica (Peón, Rey, etc.) debe implementar.
     *
     * @param origen Casilla desde la que se mueve.
     * @param destino Casilla a la que intenta ir.
     * @param tablero Estado actual del tablero para comprobar colisiones.
     * @return true si el movimiento cumple las reglas de la pieza, false en caso contrario.
     */
    public abstract boolean esMovimientoValido(Casilla origen, Casilla destino, Tablero tablero);
}
