/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebaservidor;

import Utiles.FX.VentanasFX;
import Utiles.FX.VisualFX;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author Rene
 */
public class PruebaServidor extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        try {
            VentanasFX.load(this, "Ventana_Principal.fxml").show();
        } catch (Exception ex) {
            VisualFX.responerException(ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
