module com.henrykaus.calculatorgui {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.graphics;

    opens com.henrykaus.calculatorgui to javafx.fxml;
    exports com.henrykaus.calculatorgui;
}