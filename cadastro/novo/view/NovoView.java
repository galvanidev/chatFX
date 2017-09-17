/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro.novo.view;

import login.view.*;
import conexao.ConexaoController;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Galvani Júnior
 */
public class NovoView extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("NovoFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setMinWidth(1000);
            stage.setMinHeight(675);
            stage.setScene(scene);
            primaryStage = stage;
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(NovoView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
