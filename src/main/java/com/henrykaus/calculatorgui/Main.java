package com.henrykaus.calculatorgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

// MAIN
public class Main extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("calculator-view.fxml"));
        Scene  scene = new Scene(fxmlLoader.load(), 320, 470);
        String css_file = "calculator_style.css";
        try {
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(css_file)).toExternalForm());
            scene.lookup("#expression_field").requestFocus();
            stage.setTitle("Calculator");
            stage.getIcons().addAll(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("calculator-icon-256x256.png"))),
                                    new Image(Objects.requireNonNull(Main.class.getResourceAsStream("calculator-icon-128x128.png"))),
                                    new Image(Objects.requireNonNull(Main.class.getResourceAsStream("calculator-icon-64x64.png"))),
                                    new Image(Objects.requireNonNull(Main.class.getResourceAsStream("calculator-icon-48x48.png"))),
                                    new Image(Objects.requireNonNull(Main.class.getResourceAsStream("calculator-icon-32x32.png"))));
            stage.setMinWidth(315);
            stage.setMinHeight(500);
            stage.setScene(scene);
            stage.show();
        }
        catch (NullPointerException exception) {
            Alert error_screen = new Alert(Alert.AlertType.ERROR);
            error_screen.setHeaderText("INTERNAL ERROR");
            error_screen.setContentText("No CSS file named: " + css_file);
            error_screen.showAndWait();
        }
    }
}