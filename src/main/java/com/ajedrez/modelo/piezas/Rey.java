package com.ajedrez.modelo.piezas;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.Pieza;
import com.ajedrez.modelo.Tablero;

/**
 * Representa un rey de ajedrez.
 * El rey se mueve una casilla en cualquier direccion.
 */
public class Rey extends Pieza {

    /**
     * Crea un rey del color indicado.
     *
     * @param color color de la pieza.
     */
    public Rey(final ColorPieza color) {
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

        if (origen.equals(destino)) {
            return false;
        }

        final boolean esDestinoValido = destino.estaVacia() || !destino.getPiezaActual().getColor().equals(getColor());

        if (esMovimientoDeRey(origen, destino)) {
            return esDestinoValido;
        }

        // Enroque (Castling)
        final int diferenciaFilas = Math.abs(destino.getFila() - origen.getFila());
        final int diferenciaColumnas = Math.abs(destino.getColumna() - origen.getColumna());

        if (diferenciaFilas == 0 && diferenciaColumnas == 2 && !this.haMovido()) {
            if (destino.getColumna() == 6) { // Enroque corto
                final Casilla torreCasilla = tablero.obtenerCasilla(origen.getFila(), 7);
                if (torreCasilla != null && !torreCasilla.estaVacia() && torreCasilla.getPiezaActual() instanceof Torre) {
                    final Pieza torre = torreCasilla.getPiezaActual();
                    if (!torre.haMovido() && torre.getColor().equals(this.getColor())) {
                        if (tablero.obtenerCasilla(origen.getFila(), 5).estaVacia() && tablero.obtenerCasilla(origen.getFila(), 6).estaVacia()) {
                            return true;
                        }
                    }
                }
            } else if (destino.getColumna() == 2) { // Enroque largo
                final Casilla torreCasilla = tablero.obtenerCasilla(origen.getFila(), 0);
                if (torreCasilla != null && !torreCasilla.estaVacia() && torreCasilla.getPiezaActual() instanceof Torre) {
                    final Pieza torre = torreCasilla.getPiezaActual();
                    if (!torre.haMovido() && torre.getColor().equals(this.getColor())) {
                        if (tablero.obtenerCasilla(origen.getFila(), 1).estaVacia() && 
                            tablero.obtenerCasilla(origen.getFila(), 2).estaVacia() && 
                            tablero.obtenerCasilla(origen.getFila(), 3).estaVacia()) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    /**
     * Comprueba que el rey se desplace como maximo una casilla.
     *
     * @param origen casilla inicial del movimiento.
     * @param destino casilla final del movimiento.
     * @return true si el desplazamiento es valido para un rey.
     */
    private boolean esMovimientoDeRey(final Casilla origen, final Casilla destino) {
        final int diferenciaFilas = Math.abs(destino.getFila() - origen.getFila());
        final int diferenciaColumnas = Math.abs(destino.getColumna() - origen.getColumna());

        return diferenciaFilas <= 1 && diferenciaColumnas <= 1;
    }
}
