package com.ajedrez;

import com.ajedrez.vista.VistaTablero;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Punto de entrada de la aplicacion JavaFX.
 *
 * <p>Crea la ventana principal y carga la vista del tablero de ajedrez.</p>
 */
public class App extends Application {

    private static final int ANCHO_INICIAL = 1000;
    private static final int ALTO_INICIAL = 880;

    /**
     * Crea la aplicacion JavaFX.
     */
    public App() {
    }

    @Override
    public void start(final Stage escenario) {
        VistaTablero vistaTablero = new VistaTablero();
        Scene escena = new Scene(vistaTablero, ANCHO_INICIAL, ALTO_INICIAL);
        escena.getStylesheets().add(obtenerHojaEstilos());

        escenario.setTitle("Ajedrez");
        escenario.setMinWidth(720);
        escenario.setMinHeight(640);
        escenario.setScene(escena);
        escenario.show();
    }

    /**
     * Lanza la aplicacion.
     *
     * @param args argumentos recibidos por linea de comandos.
     */
    public static void main(final String[] args) {
        launch(args);
    }

    private String obtenerHojaEstilos() {
        return App.class.getResource("/com/ajedrez/ajedrez.css").toExternalForm();
    }
}
