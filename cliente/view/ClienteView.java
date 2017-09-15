/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.view;

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
public class ClienteView extends Application {
    
    @Override
    public void start(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("ClienteFXML.fxml"));
            Scene scene = new Scene(root);
            stage.setMinWidth(1000);
            stage.setMinHeight(675);
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(ClienteView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void stop() throws Exception {
        System.out.println("foi fechado");
        super.stop();   
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
