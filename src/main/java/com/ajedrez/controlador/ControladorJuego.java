package com.ajedrez.controlador;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.EstadoJuego;
import com.ajedrez.modelo.Pieza;
import com.ajedrez.modelo.Tablero;
import com.ajedrez.modelo.piezas.Rey;

/**
 * Controlador principal del flujo de una partida de ajedrez.
 *
 * <p>Coordina el estado del juego, el turno activo, la validacion de
 * movimientos incluyendo jaque y jaque mate.</p>
 */
public class ControladorJuego {

    private final Tablero tablero;
    private final EstadoJuego estado;
    private Casilla casillaSeleccionada;

    /**
     * Crea un controlador de juego con un tablero y estado en posicion inicial.
     */
    public ControladorJuego() {
        this.tablero = new Tablero();
        this.estado = new EstadoJuego();
        reiniciarPartida();
    }

    /**
     * Devuelve el tablero gestionado por el controlador.
     *
     * @return tablero actual de la partida.
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Devuelve el estado de la partida.
     *
     * @return estado actual.
     */
    public EstadoJuego getEstado() {
        return estado;
    }

    /**
     * Devuelve el color que tiene el turno.
     *
     * @return color de la pieza que debe mover.
     */
    public ColorPieza getTurnoActual() {
        return estado.getTurnoActual();
    }

    /**
     * Devuelve la casilla seleccionada como origen de movimiento.
     *
     * @return casilla seleccionada, o null si no hay seleccion.
     */
    public Casilla getCasillaSeleccionada() {
        return casillaSeleccionada;
    }

    /**
     * Reinicia la partida con la posicion inicial y turno de blancas.
     */
    public final void reiniciarPartida() {
        tablero.inicializarPiezas();
        estado.setTurnoActual(ColorPieza.BLANCO);
        estado.setJaque(false);
        estado.setJaqueMate(false);
        estado.setTablas(false);
        estado.setFinalizada(false);
        casillaSeleccionada = null;
    }

    /**
     * Procesa la seleccion de una casilla desde la interfaz.
     *
     * @param fila fila pulsada.
     * @param columna columna pulsada.
     * @return resultado de la accion para mostrarlo en la vista.
     */
    public ResultadoMovimiento seleccionarCasilla(final int fila, final int columna) {
        if (estado.isFinalizada()) {
            return ResultadoMovimiento.error("La partida ha terminado.");
        }

        final Casilla casilla = tablero.obtenerCasilla(fila, columna);
        if (casilla == null) {
            return ResultadoMovimiento.error("Casilla fuera del tablero.");
        }

        if (casillaSeleccionada == null) {
            return seleccionarOrigen(casilla);
        }

        return moverDesdeSeleccion(casilla);
    }

    /**
     * Selecciona una casilla de origen que contenga una pieza del turno actual.
     *
     * @param casilla la casilla seleccionada.
     * @return resultado de la validacion.
     */
    private ResultadoMovimiento seleccionarOrigen(final Casilla casilla) {
        if (casilla.estaVacia()) {
            return ResultadoMovimiento.error("Selecciona una pieza " + textoColor(estado.getTurnoActual()) + ".");
        }

        final Pieza pieza = casilla.getPiezaActual();
        if (!pieza.getColor().equals(estado.getTurnoActual())) {
            return ResultadoMovimiento.error("Es el turno de " + textoColor(estado.getTurnoActual()) + ".");
        }

        casillaSeleccionada = casilla;
        return ResultadoMovimiento.seleccion("Origen seleccionado. Elige destino.");
    }

    /**
     * Intenta mover la pieza seleccionada a una casilla destino.
     *
     * @param destino la casilla de destino.
     * @return resultado del intento de movimiento.
     */
    private ResultadoMovimiento moverDesdeSeleccion(final Casilla destino) {
        if (destino == casillaSeleccionada) {
            casillaSeleccionada = null;
            return ResultadoMovimiento.seleccion("Seleccion cancelada.");
        }

        if (!destino.estaVacia() && destino.getPiezaActual().getColor().equals(estado.getTurnoActual())) {
            casillaSeleccionada = destino;
            return ResultadoMovimiento.seleccion("Origen cambiado. Elige destino.");
        }

        final Pieza pieza = casillaSeleccionada.getPiezaActual();
        if (!pieza.esMovimientoValido(casillaSeleccionada, destino, tablero)) {
            return ResultadoMovimiento.error("Movimiento no valido para esa pieza.");
        }

        if (pieza instanceof com.ajedrez.modelo.piezas.Rey && Math.abs(destino.getColumna() - casillaSeleccionada.getColumna()) == 2) {
            // Verificar que no está en jaque ni pasa por jaque
            if (estado.isJaque()) {
                return ResultadoMovimiento.error("No puedes enrocar estando en jaque.");
            }
            int direccion = destino.getColumna() > casillaSeleccionada.getColumna() ? 1 : -1;
            Casilla intermedia = tablero.obtenerCasilla(destino.getFila(), casillaSeleccionada.getColumna() + direccion);
            if (movimientoDejaEnJaque(casillaSeleccionada, intermedia, estado.getTurnoActual())) {
                return ResultadoMovimiento.error("No puedes enrocar a través de una casilla atacada.");
            }
            if (movimientoDejaEnJaque(casillaSeleccionada, destino, estado.getTurnoActual())) {
                return ResultadoMovimiento.error("No puedes enrocar si la casilla de destino está bajo ataque.");
            }
        } else {
            if (movimientoDejaEnJaque(casillaSeleccionada, destino, estado.getTurnoActual())) {
                return ResultadoMovimiento.error("Movimiento invalido: dejas a tu rey en jaque.");
            }
        }

        int diferenciaColumnas = destino.getColumna() - casillaSeleccionada.getColumna();
        tablero.moverPieza(casillaSeleccionada, destino);
        
        // Mover la torre si es un enroque
        if (pieza instanceof com.ajedrez.modelo.piezas.Rey && Math.abs(diferenciaColumnas) == 2) {
            if (diferenciaColumnas > 0) { // Corto
                Casilla origenTorre = tablero.obtenerCasilla(destino.getFila(), 7);
                Casilla destinoTorre = tablero.obtenerCasilla(destino.getFila(), 5);
                tablero.moverPieza(origenTorre, destinoTorre);
            } else { // Largo
                Casilla origenTorre = tablero.obtenerCasilla(destino.getFila(), 0);
                Casilla destinoTorre = tablero.obtenerCasilla(destino.getFila(), 3);
                tablero.moverPieza(origenTorre, destinoTorre);
            }
        }
        estado.cambiarTurno();
        casillaSeleccionada = null;

        return recalcularEstado();
    }

    /**
     * Promociona un peón en la casilla indicada a una nueva pieza.
     *
     * @param fila fila de la casilla.
     * @param columna columna de la casilla.
     * @param tipoPieza tipo de pieza a crear (Reina, Torre, Alfil, Caballo).
     */
    public void promocionarPeon(int fila, int columna, String tipoPieza) {
        Casilla casilla = tablero.obtenerCasilla(fila, columna);
        if (casilla != null && !casilla.estaVacia() && casilla.getPiezaActual() instanceof com.ajedrez.modelo.piezas.Peon) {
            ColorPieza color = casilla.getPiezaActual().getColor();
            Pieza nuevaPieza;
            switch (tipoPieza) {
                case "Caballo" -> nuevaPieza = new com.ajedrez.modelo.piezas.Caballo(color);
                case "Torre" -> nuevaPieza = new com.ajedrez.modelo.piezas.Torre(color);
                case "Alfil" -> nuevaPieza = new com.ajedrez.modelo.piezas.Alfil(color);
                default -> nuevaPieza = new com.ajedrez.modelo.piezas.Reina(color);
            }
            casilla.setPiezaActual(nuevaPieza);
        }
    }

    /**
     * Recalcula el estado de jaque o jaque mate del tablero y devuelve el resultado.
     *
     * @return el resultado de la evaluacion del estado actual.
     */
    public ResultadoMovimiento recalcularEstado() {
        final ColorPieza nuevoTurno = estado.getTurnoActual();
        if (estaEnJaqueMate(nuevoTurno)) {
            estado.setJaqueMate(true);
            estado.setFinalizada(true);
            final ColorPieza ganador = nuevoTurno.equals(ColorPieza.BLANCO) ? ColorPieza.NEGRO : ColorPieza.BLANCO;
            return ResultadoMovimiento.movimiento("¡Jaque Mate! Ganan las " + textoColor(ganador) + ".");
        } else if (estaEnJaque(nuevoTurno)) {
            estado.setJaque(true);
            return ResultadoMovimiento.movimiento("Movimiento realizado. ¡" + textoColor(nuevoTurno).toUpperCase() + " EN JAQUE! Turno de " + textoColor(nuevoTurno) + ".");
        } else {
            estado.setJaque(false);
            return ResultadoMovimiento.movimiento("Movimiento realizado. Turno de " + textoColor(nuevoTurno) + ".");
        }
    }

    /**
     * Simula un movimiento para verificar si dejaria al propio rey en jaque.
     *
     * @param origen casilla inicial.
     * @param destino casilla final.
     * @param color color del rey a proteger.
     * @return true si el movimiento deja al rey en jaque.
     */
    private boolean movimientoDejaEnJaque(final Casilla origen, final Casilla destino, final ColorPieza color) {
        final Pieza piezaOrigen = origen.getPiezaActual();
        final Pieza piezaDestino = destino.getPiezaActual();

        origen.setPiezaActual(null);
        destino.setPiezaActual(piezaOrigen);

        final boolean jaque = estaEnJaque(color);

        origen.setPiezaActual(piezaOrigen);
        destino.setPiezaActual(piezaDestino);

        return jaque;
    }

    /**
     * Comprueba si el rey del color indicado esta bajo la amenaza de alguna pieza rival.
     *
     * @param color color del rey a evaluar.
     * @return true si esta en jaque.
     */
    private boolean estaEnJaque(final ColorPieza color) {
        Casilla reyCasilla = null;
        for (int i = 0; i < Tablero.TAMANO_TABLERO; i++) {
            for (int j = 0; j < Tablero.TAMANO_TABLERO; j++) {
                final Casilla c = tablero.obtenerCasilla(i, j);
                if (!c.estaVacia() && c.getPiezaActual() instanceof Rey && c.getPiezaActual().getColor().equals(color)) {
                    reyCasilla = c;
                    break;
                }
            }
        }
        
        if (reyCasilla == null) {
            return false;
        }

        for (int i = 0; i < Tablero.TAMANO_TABLERO; i++) {
            for (int j = 0; j < Tablero.TAMANO_TABLERO; j++) {
                final Casilla c = tablero.obtenerCasilla(i, j);
                if (!c.estaVacia() && !c.getPiezaActual().getColor().equals(color) 
                        && c.getPiezaActual().esMovimientoValido(c, reyCasilla, tablero)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Comprueba si el color indicado no tiene ningun movimiento legal y esta en jaque.
     *
     * @param color color a evaluar.
     * @return true si el color esta en jaque mate.
     */
    private boolean estaEnJaqueMate(final ColorPieza color) {
        if (!estaEnJaque(color)) {
            return false;
        }

        for (int i = 0; i < Tablero.TAMANO_TABLERO; i++) {
            for (int j = 0; j < Tablero.TAMANO_TABLERO; j++) {
                final Casilla origen = tablero.obtenerCasilla(i, j);
                if (!origen.estaVacia() && origen.getPiezaActual().getColor().equals(color) 
                        && puedeEvitarJaqueDesdeOrigen(origen, color)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Comprueba si existe al menos un movimiento legal para la pieza en la casilla de origen.
     *
     * @param origen casilla de la pieza a evaluar.
     * @param color color de la pieza.
     * @return true si puede evitar el jaque realizando un movimiento valido.
     */
    private boolean puedeEvitarJaqueDesdeOrigen(final Casilla origen, final ColorPieza color) {
        for (int m = 0; m < Tablero.TAMANO_TABLERO; m++) {
            for (int n = 0; n < Tablero.TAMANO_TABLERO; n++) {
                final Casilla destino = tablero.obtenerCasilla(m, n);
                if (origen.getPiezaActual().esMovimientoValido(origen, destino, tablero) 
                        && !movimientoDejaEnJaque(origen, destino, color)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Traduce el enumerado de color a su equivalente en texto para mostrarlo.
     *
     * @param color color a traducir.
     * @return cadena con el nombre del color en minusculas.
     */
    private String textoColor(final ColorPieza color) {
        return color.equals(ColorPieza.BLANCO) ? "blancas" : "negras";
    }

    /**
     * Resultado de una accion realizada sobre el tablero.
     *
     * @param mensaje texto para mostrar al usuario.
     * @param movimientoRealizado indica si se ha ejecutado un movimiento.
     * @param error indica si la accion ha sido rechazada.
     */
    public record ResultadoMovimiento(String mensaje, boolean movimientoRealizado, boolean error) {

        private static ResultadoMovimiento seleccion(final String mensaje) {
            return new ResultadoMovimiento(mensaje, false, false);
        }

        private static ResultadoMovimiento movimiento(final String mensaje) {
            return new ResultadoMovimiento(mensaje, true, false);
        }

        private static ResultadoMovimiento error(final String mensaje) {
            return new ResultadoMovimiento(mensaje, false, true);
        }
    }
}
