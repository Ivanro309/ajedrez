package com.ajedrez.modelo;

/**
 * Representa el estado global de una partida.
 *
 * <p>Almacena informacion como el turno actual, si la partida esta en jaque,
 * jaque mate, tablas o finalizada.</p>
 */
public class EstadoJuego {

    private ColorPieza turnoActual;
    private boolean jaque;
    private boolean jaqueMate;
    private boolean tablas;
    private boolean finalizada;

    /**
     * Crea un estado de juego inicial (turno de blancas y partida sin finalizar).
     */
    public EstadoJuego() {
        this.turnoActual = ColorPieza.BLANCO;
        this.jaque = false;
        this.jaqueMate = false;
        this.tablas = false;
        this.finalizada = false;
    }

    /**
     * Devuelve el turno actual.
     *
     * @return color de la pieza a la que le toca mover.
     */
    public ColorPieza getTurnoActual() {
        return turnoActual;
    }

    /**
     * Establece el turno actual.
     *
     * @param turnoActual color de la pieza.
     */
    public void setTurnoActual(final ColorPieza turnoActual) {
        this.turnoActual = turnoActual;
    }

    /**
     * Indica si la partida esta en jaque.
     *
     * @return true si hay jaque, false en caso contrario.
     */
    public boolean isJaque() {
        return jaque;
    }

    /**
     * Establece si la partida esta en jaque.
     *
     * @param jaque true para indicar jaque.
     */
    public void setJaque(final boolean jaque) {
        this.jaque = jaque;
    }

    /**
     * Indica si la partida ha acabado en jaque mate.
     *
     * @return true si hay jaque mate.
     */
    public boolean isJaqueMate() {
        return jaqueMate;
    }

    /**
     * Establece si la partida ha acabado en jaque mate.
     *
     * @param jaqueMate true para indicar jaque mate.
     */
    public void setJaqueMate(final boolean jaqueMate) {
        this.jaqueMate = jaqueMate;
    }

    /**
     * Indica si la partida ha terminado en tablas.
     *
     * @return true si hay tablas.
     */
    public boolean isTablas() {
        return tablas;
    }

    /**
     * Establece si la partida ha terminado en tablas.
     *
     * @param tablas true para indicar tablas.
     */
    public void setTablas(final boolean tablas) {
        this.tablas = tablas;
    }

    /**
     * Indica si la partida ha finalizado por cualquier motivo.
     *
     * @return true si esta finalizada.
     */
    public boolean isFinalizada() {
        return finalizada;
    }

    /**
     * Establece el estado de finalizacion de la partida.
     *
     * @param finalizada true si la partida ha terminado.
     */
    public void setFinalizada(final boolean finalizada) {
        this.finalizada = finalizada;
    }

    /**
     * Cambia el turno al color contrario.
     */
    public void cambiarTurno() {
        this.turnoActual = this.turnoActual == ColorPieza.BLANCO ? ColorPieza.NEGRO : ColorPieza.BLANCO;
    }
}
