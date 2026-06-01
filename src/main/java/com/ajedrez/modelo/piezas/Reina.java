package com.ajedrez.modelo.piezas;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.Pieza;
import com.ajedrez.modelo.Tablero;

/**
 * Representa una reina de ajedrez.
 * La reina combina los movimientos de la torre y el alfil.
 */
public class Reina extends Pieza {

    /**
     * Crea una reina del color indicado.
     *
     * @param color color de la pieza.
     */
    public Reina(final ColorPieza color) {
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

        if (origen.equals(destino) || !esMovimientoDeReina(origen, destino)) {
            return false;
        }

        if (!caminoEstaLibre(origen, destino, tablero)) {
            return false;
        }

        return destino.estaVacia() || !destino.getPiezaActual().getColor().equals(getColor());
    }

    /**
     * Comprueba si el movimiento coincide con los patrones de torre o alfil.
     *
     * @param origen casilla inicial del movimiento.
     * @param destino casilla final del movimiento.
     * @return true si el movimiento es recto o diagonal.
     */
    private boolean esMovimientoDeReina(final Casilla origen, final Casilla destino) {
        final int diferenciaFilas = Math.abs(destino.getFila() - origen.getFila());
        final int diferenciaColumnas = Math.abs(destino.getColumna() - origen.getColumna());
        final boolean movimientoRecto = origen.getFila() == destino.getFila()
                || origen.getColumna() == destino.getColumna();
        final boolean movimientoDiagonal = diferenciaFilas == diferenciaColumnas;

        return movimientoRecto || movimientoDiagonal;
    }
}
