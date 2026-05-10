package com.ajedrez.modelo;

/**
 * Representa los dos colores posibles de las piezas y el turno.
 * Se utiliza un enum para garantizar la seguridad de tipos (Type Safety).
 */
public enum ColorPieza {
    /**
     * Piezas blancas. En la representacion interna actual avanzan hacia filas
     * decrecientes.
     */
    BLANCO,

    /**
     * Piezas negras. En la representacion interna actual avanzan hacia filas
     * crecientes.
     */
    NEGRO
}
