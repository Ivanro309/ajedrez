package com.ajedrez.modelo;

import com.ajedrez.modelo.piezas.Alfil;
import com.ajedrez.modelo.piezas.Caballo;
import com.ajedrez.modelo.piezas.Peon;
import com.ajedrez.modelo.piezas.Reina;
import com.ajedrez.modelo.piezas.Rey;
import com.ajedrez.modelo.piezas.Torre;

/**
 * Representa la lógica interna del tablero de ajedrez utilizando una matriz 2D.
 */
public class Tablero {

    /**
     * Tamaño estándar del tablero de ajedrez (8x8).
     */
    public static final int TAMANO_TABLERO = 8;

    private final Casilla[][] casillas;

    /**
     * Constructor del tablero. Inicializa la matriz de casillas vacías.
     */
    public Tablero() {
        this.casillas = new Casilla[TAMANO_TABLERO][TAMANO_TABLERO];
        inicializarCasillasVacias();
    }

    /**
     * Llena la matriz con objetos Casilla.
     */
    private void inicializarCasillasVacias() {
        for (int fila = 0; fila < TAMANO_TABLERO; fila++) {
            for (int col = 0; col < TAMANO_TABLERO; col++) {
                casillas[fila][col] = new Casilla(fila, col);
            }
        }
    }

    /**
     * Devuelve una casilla específica protegiendo contra desbordamientos.
     *
     * @param fila Índice de la fila.
     * @param columna Índice de la columna.
     * @return La Casilla solicitada o null si está fuera de límites.
     */
    public Casilla obtenerCasilla(final int fila, final int columna) {
        if (fila < 0 || fila >= TAMANO_TABLERO || columna < 0 || columna >= TAMANO_TABLERO) {
            return null;
        }
        return casillas[fila][columna];
    }

    /**
     * Coloca una pieza en una posicion concreta del tablero.
     *
     * @param fila fila donde se colocara la pieza.
     * @param columna columna donde se colocara la pieza.
     * @param pieza pieza que se desea colocar, o null para vaciar la casilla.
     */
    public void colocarPieza(final int fila, final int columna, final Pieza pieza) {
        Casilla casilla = obtenerCasilla(fila, columna);
        if (casilla != null) {
            casilla.setPiezaActual(pieza);
        }
    }

    /**
     * Limpia todas las piezas del tablero.
     */
    public void limpiar() {
        for (int fila = 0; fila < TAMANO_TABLERO; fila++) {
            for (int columna = 0; columna < TAMANO_TABLERO; columna++) {
                casillas[fila][columna].setPiezaActual(null);
            }
        }
    }

    /**
     * Coloca las piezas en la posicion inicial de una partida de ajedrez.
     */
    public void inicializarPiezas() {
        limpiar();
        inicializarPiezasMayores(ColorPieza.NEGRO, 0);
        inicializarPeones(ColorPieza.NEGRO, 1);
        inicializarPeones(ColorPieza.BLANCO, 6);
        inicializarPiezasMayores(ColorPieza.BLANCO, 7);
    }

    /**
     * Mueve una pieza si la casilla de origen contiene una pieza y el movimiento
     * cumple las reglas de dicha pieza.
     *
     * @param origen casilla desde la que se mueve.
     * @param destino casilla a la que se mueve.
     * @return true si se ha aplicado el movimiento.
     */
    public boolean moverPieza(final Casilla origen, final Casilla destino) {
        if (origen == null || destino == null || origen.estaVacia()) {
            return false;
        }

        Pieza pieza = origen.getPiezaActual();
        if (!pieza.esMovimientoValido(origen, destino, this)) {
            return false;
        }

        destino.setPiezaActual(pieza);
        origen.setPiezaActual(null);
        pieza.setHaMovido(true);
        return true;
    }

    private void inicializarPeones(final ColorPieza color, final int fila) {
        for (int columna = 0; columna < TAMANO_TABLERO; columna++) {
            colocarPieza(fila, columna, new Peon(color));
        }
    }

    private void inicializarPiezasMayores(final ColorPieza color, final int fila) {
        colocarPieza(fila, 0, new Torre(color));
        colocarPieza(fila, 1, new Caballo(color));
        colocarPieza(fila, 2, new Alfil(color));
        colocarPieza(fila, 3, new Reina(color));
        colocarPieza(fila, 4, new Rey(color));
        colocarPieza(fila, 5, new Alfil(color));
        colocarPieza(fila, 6, new Caballo(color));
        colocarPieza(fila, 7, new Torre(color));
    }
}
