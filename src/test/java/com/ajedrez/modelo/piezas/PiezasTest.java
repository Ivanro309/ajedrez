package com.ajedrez.modelo.piezas;

import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.Tablero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Suite de pruebas unitarias para validar las reglas de movimiento de las piezas de ajedrez.
 * Cumple con los requisitos de verificacion dinamica de calidad descritos en la practica 4.
 */
public class PiezasTest {

    private Tablero tablero;

    @BeforeEach
    public void setUp() {
        tablero = new Tablero();
    }

    @Test
    public void testMovimientoAlfil() {
        final Alfil alfilBlanco = new Alfil(ColorPieza.BLANCO);
        tablero.colocarPieza(4, 4, alfilBlanco);

        final Casilla origen = tablero.obtenerCasilla(4, 4);
        final Casilla destinoValido = tablero.obtenerCasilla(6, 6);
        final Casilla destinoInvalido = tablero.obtenerCasilla(4, 6);

        // Movimiento diagonal legal
        assertTrue(alfilBlanco.esMovimientoValido(origen, destinoValido, tablero));
        // Movimiento recto ilegal para el alfil
        assertFalse(alfilBlanco.esMovimientoValido(origen, destinoInvalido, tablero));
    }

    @Test
    public void testMovimientoTorre() {
        final Torre torreBlanca = new Torre(ColorPieza.BLANCO);
        tablero.colocarPieza(4, 4, torreBlanca);

        final Casilla origen = tablero.obtenerCasilla(4, 4);
        final Casilla destinoValido = tablero.obtenerCasilla(4, 7);
        final Casilla destinoInvalido = tablero.obtenerCasilla(5, 5);

        // Movimiento recto legal
        assertTrue(torreBlanca.esMovimientoValido(origen, destinoValido, tablero));
        // Movimiento diagonal ilegal para la torre
        assertFalse(torreBlanca.esMovimientoValido(origen, destinoInvalido, tablero));
    }

    @Test
    public void testMovimientoReina() {
        final Reina reinaNegra = new Reina(ColorPieza.NEGRO);
        tablero.colocarPieza(3, 3, reinaNegra);

        final Casilla origen = tablero.obtenerCasilla(3, 3);
        final Casilla destinoRecto = tablero.obtenerCasilla(3, 6);
        final Casilla destinoDiagonal = tablero.obtenerCasilla(5, 5);
        final Casilla destinoInvalido = tablero.obtenerCasilla(5, 6);

        // La reina puede moverse en diagonal y en linea recta
        assertTrue(reinaNegra.esMovimientoValido(origen, destinoRecto, tablero));
        assertTrue(reinaNegra.esMovimientoValido(origen, destinoDiagonal, tablero));
        // No puede hacer un movimiento en L
        assertFalse(reinaNegra.esMovimientoValido(origen, destinoInvalido, tablero));
    }

    @Test
    public void testMovimientoCaballo() {
        final Caballo caballoBlanco = new Caballo(ColorPieza.BLANCO);
        tablero.colocarPieza(4, 4, caballoBlanco);

        final Casilla origen = tablero.obtenerCasilla(4, 4);
        final Casilla destinoValido = tablero.obtenerCasilla(6, 5); // Movimiento en L (2 filas, 1 col)
        final Casilla destinoInvalido = tablero.obtenerCasilla(5, 5);

        assertTrue(caballoBlanco.esMovimientoValido(origen, destinoValido, tablero));
        assertFalse(caballoBlanco.esMovimientoValido(origen, destinoInvalido, tablero));
    }

    @Test
    public void testMovimientoPeon() {
        final Peon peonBlanco = new Peon(ColorPieza.BLANCO);
        // Los peones blancos inician en la fila 6
        tablero.colocarPieza(6, 4, peonBlanco);

        final Casilla origen = tablero.obtenerCasilla(6, 4);
        final Casilla avanceSimple = tablero.obtenerCasilla(5, 4);
        final Casilla avanceDoble = tablero.obtenerCasilla(4, 4);
        final Casilla avanceInvalido = tablero.obtenerCasilla(3, 4); // Triple avance ilegal

        assertTrue(peonBlanco.esMovimientoValido(origen, avanceSimple, tablero));
        assertTrue(peonBlanco.esMovimientoValido(origen, avanceDoble, tablero));
        assertFalse(peonBlanco.esMovimientoValido(origen, avanceInvalido, tablero));
    }

    @Test
    public void testMovimientoRey() {
        final Rey reyNegro = new Rey(ColorPieza.NEGRO);
        tablero.colocarPieza(4, 4, reyNegro);

        final Casilla origen = tablero.obtenerCasilla(4, 4);
        final Casilla unPaso = tablero.obtenerCasilla(4, 5);
        final Casilla dosPasos = tablero.obtenerCasilla(4, 6);

        assertTrue(reyNegro.esMovimientoValido(origen, unPaso, tablero));
        assertFalse(reyNegro.esMovimientoValido(origen, dosPasos, tablero));
    }
}
