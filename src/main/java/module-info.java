/**
 * Modulo principal de la aplicacion de ajedrez.
 *
 * <p>Declara las dependencias de JavaFX y expone el paquete base de la
 * aplicacion. El resto de paquetes forman parte de la implementacion interna
 * del modelo, la vista y los controladores.</p>
 */
module com.proyecto.base {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;

    opens com.ajedrez to javafx.fxml;
    exports com.ajedrez;
}
