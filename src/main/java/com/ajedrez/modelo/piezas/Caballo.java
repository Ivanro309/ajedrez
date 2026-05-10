package com.ajedrez.modelo.piezas;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.Pieza;
import com.ajedrez.modelo.Tablero;

/**
 * Representa un caballo de ajedrez.
 * El caballo se mueve en forma de L y puede saltar por encima de otras piezas.
 */
public class Caballo extends Pieza {

    /**
     * Crea un caballo del color indicado.
     *
     * @param color color de la pieza.
     */
    public Caballo(final ColorPieza color) {
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

        if (origen == destino || !esMovimientoCaballo(origen, destino)) {
            return false;
        }

        return destino.estaVacia() || destino.getPiezaActual().getColor() != getColor();
    }

    /**
     * Comprueba el patron de movimiento del caballo.
     *
     * @param origen casilla inicial del movimiento.
     * @param destino casilla final del movimiento.
     * @return true si el movimiento tiene forma de L.
     */
    private boolean esMovimientoCaballo(final Casilla origen, final Casilla destino) {
        int diferenciaFilas = Math.abs(destino.getFila() - origen.getFila());
        int diferenciaColumnas = Math.abs(destino.getColumna() - origen.getColumna());

        return diferenciaFilas == 2 && diferenciaColumnas == 1
                || diferenciaFilas == 1 && diferenciaColumnas == 2;
    }
}
