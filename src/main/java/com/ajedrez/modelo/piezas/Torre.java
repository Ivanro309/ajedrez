package com.ajedrez.modelo.piezas;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.Pieza;
import com.ajedrez.modelo.Tablero;

/**
 * Representa una torre de ajedrez.
 * La torre se mueve cualquier numero de casillas en horizontal o vertical.
 */
public class Torre extends Pieza {

    /**
     * Crea una torre del color indicado.
     *
     * @param color color de la pieza.
     */
    public Torre(final ColorPieza color) {
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

        if (origen == destino || !esMovimientoRecto(origen, destino)) {
            return false;
        }

        if (!caminoEstaLibre(origen, destino, tablero)) {
            return false;
        }

        return destino.estaVacia() || destino.getPiezaActual().getColor() != getColor();
    }

    /**
     * Comprueba si la torre se desplaza en horizontal o vertical.
     *
     * @param origen casilla inicial del movimiento.
     * @param destino casilla final del movimiento.
     * @return true si no cambia la fila o no cambia la columna.
     */
    private boolean esMovimientoRecto(final Casilla origen, final Casilla destino) {
        return origen.getFila() == destino.getFila() || origen.getColumna() == destino.getColumna();
    }
}
