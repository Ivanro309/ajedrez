package com.ajedrez.modelo.piezas;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.Pieza;
import com.ajedrez.modelo.Tablero;

/**
 * Representa un peon de ajedrez.
 * El peon avanza en vertical y captura una casilla en diagonal hacia delante.
 */
public class Peon extends Pieza {

    /**
     * Crea un peon del color indicado.
     *
     * @param color color de la pieza.
     */
    public Peon(final ColorPieza color) {
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

        if (origen == destino || !esMovimientoRectoPeon(origen, destino)) {
            return esCapturaValida(origen, destino);
        }

        return destino.estaVacia() && caminoEstaLibre(origen, destino, tablero);
    }

    /**
     * Comprueba el avance frontal del peon, incluyendo el doble avance inicial.
     *
     * @param origen casilla inicial del movimiento.
     * @param destino casilla final del movimiento.
     * @return true si el movimiento es un avance frontal valido.
     */
    private boolean esMovimientoRectoPeon(final Casilla origen, final Casilla destino) {
        int avanceFila = destino.getFila() - origen.getFila();
        int avanceColumna = destino.getColumna() - origen.getColumna();
        int direccion = obtenerDireccion();

        if (avanceColumna != 0) {
            return false;
        }

        return avanceFila == direccion || esAvanceInicialDoble(origen, avanceFila);
    }

    /**
     * Comprueba si el peon realiza su doble avance desde la fila inicial.
     *
     * @param origen casilla inicial del movimiento.
     * @param avanceFila desplazamiento vertical solicitado.
     * @return true si el avance doble es legal por posicion y direccion.
     */
    private boolean esAvanceInicialDoble(final Casilla origen, final int avanceFila) {
        return origen.getFila() == obtenerFilaInicial() && avanceFila == obtenerDireccion() * 2;
    }

    /**
     * No contempla captura al paso; solo la captura diagonal ordinaria.
     *
     * @param origen casilla inicial del movimiento.
     * @param destino casilla final del movimiento.
     * @return true si el destino contiene una pieza rival en diagonal.
     */
    private boolean esCapturaValida(final Casilla origen, final Casilla destino) {
        int avanceFila = destino.getFila() - origen.getFila();
        int avanceColumna = Math.abs(destino.getColumna() - origen.getColumna());

        return avanceFila == obtenerDireccion()
                && avanceColumna == 1
                && !destino.estaVacia()
                && destino.getPiezaActual().getColor() != getColor();
    }

    /**
     * Devuelve la direccion vertical en la que avanza el peon.
     *
     * @return -1 para blanco y 1 para negro.
     */
    private int obtenerDireccion() {
        return getColor() == ColorPieza.BLANCO ? -1 : 1;
    }

    /**
     * Devuelve la fila desde la que el peon puede avanzar dos casillas.
     *
     * @return fila inicial correspondiente al color de la pieza.
     */
    private int obtenerFilaInicial() {
        return getColor() == ColorPieza.BLANCO ? 6 : 1;
    }
}
