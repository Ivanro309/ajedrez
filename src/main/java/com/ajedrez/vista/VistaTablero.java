package com.ajedrez.vista;

import com.ajedrez.controlador.ControladorJuego;
import com.ajedrez.controlador.ControladorJuego.ResultadoMovimiento;
import com.ajedrez.modelo.Casilla;
import com.ajedrez.modelo.ColorPieza;
import com.ajedrez.modelo.Pieza;
import com.ajedrez.modelo.Tablero;
import com.ajedrez.modelo.piezas.Alfil;
import com.ajedrez.modelo.piezas.Caballo;
import com.ajedrez.modelo.piezas.Peon;
import com.ajedrez.modelo.piezas.Reina;
import com.ajedrez.modelo.piezas.Rey;
import com.ajedrez.modelo.piezas.Torre;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

/**
 * Vista principal del tablero de ajedrez.
 *
 * <p>Esta clase extiende {@link BorderPane} y se encarga de representar gráficamente
 * la interfaz de usuario del juego, incluyendo el tablero, las piezas, el estado
 * de la partida y los controles básicos utilizando JavaFX.</p>
 *
 * <p>Se comunica con el {@link ControladorJuego} para delegar la lógica de negocio
 * y actualizar la vista en base al estado del modelo.</p>
 *
 * @author [Tu Nombre/Proyecto]
 * @version 1.0
 */
public class VistaTablero extends BorderPane {

    /** Tamaño en píxeles de cada lado de una casilla cuadrada. */
    private static final int TAMANO_CASILLA = 84;

    /** Cantidad de casillas por fila y columna (por defecto 8). */
    private static final int TOTAL_CASILLAS = Tablero.TAMANO_TABLERO;

    /** Controlador que gestiona la lógica del juego y las reglas de movimiento. */
    private final ControladorJuego controladorJuego;

    /** Contenedor gráfico que organiza las casillas en una cuadrícula. */
    private final GridPane tableroGrafico;

    /** Matriz que almacena las referencias visuales de cada casilla del tablero. */
    private final StackPane[][] casillasGraficas;

    /** Etiqueta para mostrar mensajes sobre el estado actual de la partida. */
    private final Label etiquetaEstado;

    /** Etiqueta para mostrar qué jugador tiene el turno actual. */
    private final Label etiquetaTurno;

    /**
     * Constructor por defecto.
     * Crea e inicializa la vista del tablero, configurando el controlador,
     * los componentes gráficos principales y renderizando el estado inicial.
     */
    public VistaTablero() {
        this.controladorJuego = new ControladorJuego();
        this.tableroGrafico = new GridPane();
        this.casillasGraficas = new StackPane[TOTAL_CASILLAS][TOTAL_CASILLAS];
        this.etiquetaEstado = new Label("Partida iniciada.");
        this.etiquetaTurno = new Label();

        configurarVista();
        renderizarTablero();
    }

    /**
     * Configura la disposición general de la vista, asignando las clases CSS
     * y distribuyendo las barras superior, central e inferior.
     */
    private void configurarVista() {
        getStyleClass().add("app-root");
        setTop(crearBarraSuperior());
        setCenter(crearContenedorTablero());
        setBottom(crearBarraEstado());
    }

    /**
     * Crea y configura la barra superior de la interfaz.
     * Incluye el título del juego, el indicador de turno y el botón de reinicio.
     *
     * @return Un {@link HBox} que contiene los elementos de la barra superior.
     */
    private HBox crearBarraSuperior() {
        Label titulo = new Label("Ajedrez");
        titulo.getStyleClass().add("app-title");

        etiquetaTurno.getStyleClass().add("turn-label");

        Button botonReiniciar = new Button("Reiniciar");
        botonReiniciar.getStyleClass().add("primary-button");
        botonReiniciar.setOnAction(evento -> reiniciarPartida());

        HBox separador = new HBox();
        HBox.setHgrow(separador, Priority.ALWAYS);

        HBox barra = new HBox(16, titulo, separador, etiquetaTurno, botonReiniciar);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.getStyleClass().add("top-bar");
        return barra;
    }

    /**
     * Crea el contenedor central que envuelve al tablero de ajedrez,
     * centrando la cuadrícula y aplicando márgenes.
     *
     * @return Un {@link VBox} que contiene el tablero gráfico.
     */
    private VBox crearContenedorTablero() {
        tableroGrafico.getStyleClass().add("board");
        tableroGrafico.setAlignment(Pos.CENTER);
        tableroGrafico.setMaxSize(TAMANO_CASILLA * TOTAL_CASILLAS, TAMANO_CASILLA * TOTAL_CASILLAS);

        VBox contenedor = new VBox(tableroGrafico);
        contenedor.setAlignment(Pos.CENTER);
        contenedor.setPadding(new Insets(24));
        return contenedor;
    }

    /**
     * Crea la barra inferior de estado donde se muestran los mensajes de error
     * o información sobre los movimientos.
     *
     * @return Un {@link HBox} que contiene la etiqueta de estado.
     */
    private HBox crearBarraEstado() {
        etiquetaEstado.getStyleClass().add("status-label");

        HBox barra = new HBox(etiquetaEstado);
        barra.setAlignment(Pos.CENTER_LEFT);
        barra.getStyleClass().add("status-bar");
        return barra;
    }

    /**
     * Limpia y vuelve a dibujar completamente el tablero basándose en el
     * estado actual del modelo (posiciones de piezas, turnos, selecciones).
     */
    private void renderizarTablero() {
        tableroGrafico.getChildren().clear();
        actualizarTurno();

        for (int fila = 0; fila < TOTAL_CASILLAS; fila++) {
            for (int columna = 0; columna < TOTAL_CASILLAS; columna++) {
                StackPane casillaGrafica = crearCasillaGrafica(fila, columna);
                casillasGraficas[fila][columna] = casillaGrafica;
                tableroGrafico.add(casillaGrafica, columna, fila);
            }
        }
    }

    /**
     * Construye la representación gráfica individual de una casilla específica.
     * Asigna colores, añade la imagen de la pieza (si existe), marca la casilla
     * si está seleccionada y registra los eventos de clic.
     *
     * @param fila    La fila de la casilla en el tablero (0 a 7).
     * @param columna La columna de la casilla en el tablero (0 a 7).
     * @return Un {@link StackPane} configurado visualmente para representar la casilla.
     */
    private StackPane crearCasillaGrafica(final int fila, final int columna) {
        StackPane casillaGrafica = new StackPane();
        casillaGrafica.setMinSize(TAMANO_CASILLA, TAMANO_CASILLA);
        casillaGrafica.setPrefSize(TAMANO_CASILLA, TAMANO_CASILLA);
        casillaGrafica.setMaxSize(TAMANO_CASILLA, TAMANO_CASILLA);
        casillaGrafica.getStyleClass().add("square");
        casillaGrafica.getStyleClass().add(esCasillaClara(fila, columna) ? "square-light" : "square-dark");

        Casilla casillaModelo = controladorJuego.getTablero().obtenerCasilla(fila, columna);
        ImageView imagenPieza = obtenerSimboloPieza(casillaModelo);

        if (imagenPieza != null) {
            imagenPieza.getStyleClass().add("piece");
            casillaGrafica.getChildren().add(imagenPieza);
        }

        if (controladorJuego.getCasillaSeleccionada() == casillaModelo) {
            casillaGrafica.getStyleClass().add("square-selected");
        }

        Pieza pieza = casillaModelo.getPiezaActual();
        if (pieza instanceof Rey && pieza.getColor() == controladorJuego.getTurnoActual()) {
            if (controladorJuego.getEstado().isJaqueMate()) {
                casillaGrafica.getStyleClass().add("square-mate");
            } else if (controladorJuego.getEstado().isJaque()) {
                casillaGrafica.getStyleClass().add("square-check");
            }
        }

        casillaGrafica.setOnMouseClicked(evento -> manejarClickCasilla(fila, columna));
        return casillaGrafica;
    }

    /**
     * Gestiona el evento de clic sobre una casilla del tablero.
     * Delega la acción al controlador y actualiza la vista con el resultado.
     *
     * @param fila    La fila de la casilla clicada.
     * @param columna La columna de la casilla clicada.
     */
    private void manejarClickCasilla(final int fila, final int columna) {
        ResultadoMovimiento resultado = controladorJuego.seleccionarCasilla(fila, columna);
        
        // Detectar si fue un movimiento válido y si hay que promocionar
        if (resultado.movimientoRealizado()) {
            Casilla c = controladorJuego.getTablero().obtenerCasilla(fila, columna);
            if (c.getPiezaActual() instanceof com.ajedrez.modelo.piezas.Peon) {
                if (c.getFila() == 0 || c.getFila() == Tablero.TAMANO_TABLERO - 1) {
                    ChoiceDialog<String> dialog = new ChoiceDialog<>("Reina", "Reina", "Torre", "Alfil", "Caballo");
                    dialog.setTitle("Promoción de Peón");
                    dialog.setHeaderText("¡Tu peón ha llegado al final!");
                    dialog.setContentText("Elige a qué pieza quieres promocionarlo:");
                    
                    String eleccion = dialog.showAndWait().orElse("Reina");
                    controladorJuego.promocionarPeon(fila, columna, eleccion);
                    resultado = controladorJuego.recalcularEstado();
                }
            }
        }

        etiquetaEstado.setText(resultado.mensaje());
        etiquetaEstado.pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("error"), resultado.error());
        renderizarTablero();
    }

    /**
     * Reinicia el estado de la partida a través del controlador y actualiza
     * la interfaz gráfica para reflejar el tablero inicial.
     */
    private void reiniciarPartida() {
        controladorJuego.reiniciarPartida();
        etiquetaEstado.setText("Partida reiniciada.");
        etiquetaEstado.pseudoClassStateChanged(javafx.css.PseudoClass.getPseudoClass("error"), false);
        renderizarTablero();
    }

    /**
     * Actualiza la etiqueta visual que indica el turno del jugador actual.
     */
    private void actualizarTurno() {
        etiquetaTurno.setText("Turno: " + textoColor(controladorJuego.getTurnoActual()));
    }

    /**
     * Determina si una casilla debe tener el color claro u oscuro basado en sus coordenadas.
     *
     * @param fila    La fila de la casilla.
     * @param columna La columna de la casilla.
     * @return {@code true} si la casilla es de color claro, {@code false} si es oscura.
     */
    private boolean esCasillaClara(final int fila, final int columna) {
        return (fila + columna) % 2 == 0;
    }

    /**
     * Obtiene la imagen correspondiente a la pieza ubicada en una casilla concreta.
     * Identifica el tipo y color de la pieza para cargar el recurso adecuado.
     *
     * @param casilla La {@link Casilla} del modelo de la cual extraer la pieza.
     * @return Un {@link ImageView} configurado con la imagen de la pieza, o {@code null}
     *         si la casilla está vacía o la imagen no se encuentra.
     */
    private ImageView obtenerSimboloPieza(final Casilla casilla) {
        if (casilla == null || casilla.estaVacia()) {
            return null;
        }

        Pieza pieza = casilla.getPiezaActual();
        boolean blanca = pieza.getColor() == ColorPieza.BLANCO;
        String rutaImagen = "";

        // Se usa la barra '/' inicial para buscar desde la raíz de resources
        if (pieza instanceof Rey) {
            rutaImagen = blanca ? "/com/ajedrez/piezas/blancas/c_rey.png" : "/com/ajedrez/piezas/negras/rey.png";
        } else if (pieza instanceof Reina) {
            rutaImagen = blanca ? "/com/ajedrez/piezas/blancas/c_reina.png" : "/com/ajedrez/piezas/negras/reina.png";
        } else if (pieza instanceof Torre) {
            rutaImagen = blanca ? "/com/ajedrez/piezas/blancas/c_torre.png" : "/com/ajedrez/piezas/negras/torre.png";
        } else if (pieza instanceof Alfil) {
            rutaImagen = blanca ? "/com/ajedrez/piezas/blancas/c_alfil.png" : "/com/ajedrez/piezas/negras/alfil.png";
        } else if (pieza instanceof Caballo) {
            rutaImagen = blanca ? "/com/ajedrez/piezas/blancas/c_caballo.png" : "/com/ajedrez/piezas/negras/caballo.png";
        } else if (pieza instanceof Peon) {
            rutaImagen = blanca ? "/com/ajedrez/piezas/blancas/c_peon.png" : "/com/ajedrez/piezas/negras/peon.png";
        }

        if (rutaImagen.isEmpty()) {
            return null;
        }

        try {
            Image imagen = new Image(Objects.requireNonNull(getClass().getResourceAsStream(rutaImagen)));
            ImageView imageView = new ImageView(imagen);

            // Ajustamos el tamaño de la imagen para que encaje con margen dentro de la casilla
            imageView.setFitWidth(TAMANO_CASILLA - 10);
            imageView.setFitHeight(TAMANO_CASILLA - 10);
            imageView.setPreserveRatio(true);

            return imageView;
        } catch (NullPointerException e) {
            System.err.println("Error: No se encontró la imagen en la ruta: " + rutaImagen);
            return null;
        }
    }

    /**
     * Convierte el enumerador de color en una cadena de texto legible para la interfaz.
     *
     * @param color El {@link ColorPieza} a convertir.
     * @return Una cadena indicando "blancas" o "negras".
     */
    private String textoColor(final ColorPieza color) {
        return color == ColorPieza.BLANCO ? "blancas" : "negras";
    }
}