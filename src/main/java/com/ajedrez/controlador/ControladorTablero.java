package com.ajedrez.controlador;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.Tablero;
import com.ajedrez.vista.VistaTablero;

/**
 * Controlador responsable de las acciones realizadas sobre el tablero.
 *
 * <p>Su responsabilidad prevista es traducir las acciones de la interfaz en
 * operaciones sobre el modelo, por ejemplo seleccionar una casilla, mover una
 * pieza o refrescar la vista tras una jugada valida.</p>
 */
public class ControladorTablero {

    private Tablero tablero;
    private VistaTablero vista;

    /**
     * Crea un controlador de tablero sin vista ni modelo asociados todavia.
     */
    public ControladorTablero() {
        // PMD prefiere evitar this.tablero = null;
    }

    /**
     * Devuelve el tablero asociado.
     *
     * @return el modelo del tablero actual.
     */
    public Tablero getTablero() {
        return tablero;
    }

    /**
     * Asocia un modelo de tablero a este controlador.
     *
     * @param tablero el modelo de tablero a controlar.
     */
    public void setTablero(final Tablero tablero) {
        this.tablero = tablero;
    }

    /**
     * Devuelve la vista asociada.
     *
     * @return la vista del tablero actual.
     */
    public VistaTablero getVista() {
        return vista;
    }

    /**
     * Asocia una vista a este controlador.
     *
     * @param vista la vista de tablero a refrescar.
     */
    public void setVista(final VistaTablero vista) {
        this.vista = vista;
    }

    /**
     * Procesa la seleccion de una casilla en la interfaz.
     *
     * @param fila fila seleccionada.
     * @param columna columna seleccionada.
     */
    public void seleccionarCasilla(final int fila, final int columna) {
        // La logica especifica de seleccion delegara en ControladorJuego
        // o se implementara aqui dependiendo de la refactorizacion.
    }

    /**
     * Intenta mover una pieza entre dos casillas y refresca la vista si tiene exito.
     *
     * @param origen casilla inicial.
     * @param destino casilla final.
     * @return true si el movimiento ha sido valido y se ha refrescado la vista.
     */
    public boolean moverPieza(final Casilla origen, final Casilla destino) {
        if (tablero != null && tablero.moverPieza(origen, destino)) {
            refrescarVista();
            return true;
        }
        return false;
    }

    /**
     * Indica a la vista que debe actualizar su representacion visual.
     */
    public void refrescarVista() {
        // Asumiendo que VistaTablero implementara en el futuro o delegara un metodo renderizar.
    }
}
