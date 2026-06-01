package com.ajedrez.modelo.piezas;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.Pieza;
import com.ajedrez.modelo.Tablero;

/**
 * Representa un alfil de ajedrez.
 * El alfil se mueve cualquier numero de casillas en diagonal, sin saltar piezas.
 */
public class Alfil extends Pieza {

    /**
     * Crea un alfil del color indicado.
     *
     * @param color color de la pieza.
     */
    public Alfil(final ColorPieza color) {
        super(color);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean esMovimientoValido(final Casilla origen, final Casilla destino, final Tablero tablero) {
        if (origen == null || destino == null || tablero == null) {
            return false;
        }

        if (origen.equals(destino) || !esMovimientoDiagonal(origen, destino)) {
            return false;
        }

        if (!caminoEstaLibre(origen, destino, tablero)) {
            return false;
        }

        return destino.estaVacia() || !destino.getPiezaActual().getColor().equals(getColor());
    }

    /**
     * Comprueba si el desplazamiento mantiene la misma distancia en filas y columnas.
     *
     * @param origen casilla inicial del movimiento.
     * @param destino casilla final del movimiento.
     * @return true si el movimiento es diagonal.
     */
    private boolean esMovimientoDiagonal(final Casilla origen, final Casilla destino) {
        final int diferenciaFilas = Math.abs(destino.getFila() - origen.getFila());
        final int diferenciaColumnas = Math.abs(destino.getColumna() - origen.getColumna());

        return diferenciaFilas == diferenciaColumnas;
    }
}
