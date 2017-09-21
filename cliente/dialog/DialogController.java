/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.apache.commons.lang3.StringUtils;
import usuario.bean.UsuarioBean;

/**
 *
 * @author Galvani Júnior
 */
public class DialogController {
    
    private static DialogController instancia = null;
    private static FXMLLoader mLoader;
    @FXML private static AnchorPane modal;
    @FXML private static VBox box;
    @FXML private static Label labelNome;
    @FXML private static Label labelEmail;
    @FXML private static Label labelSexo;
    @FXML private static Label labelDataNascimento;
    
    private DialogController (UsuarioBean usuario) {
        FXMLLoader mLoader = new FXMLLoader(getClass().getResource("DialogFXML.fxml"));
        mLoader.setController(this);
        //labelNome.setText(labelNome.getText() + StringUtils.capitalize(usuario.getPessoa().getNome().toLowerCase()));
        labelEmail.setText(labelEmail.getText() + usuario.getEmail());
        if (usuario.getPessoa().getSexo().equals("M")) 
            labelSexo.setText(labelSexo.getText() + "Masculino");
        else
            labelSexo.setText(labelSexo.getText() + "Feminino");
        labelDataNascimento.setText(usuario.getPessoa().getDataNascimento().format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
    
    public static DialogController getInstance(UsuarioBean usuario) {
        if (instancia != null)
            return instancia;
        instancia = new DialogController(usuario);
        return instancia;       
    }
    
    public static Node getModal() {
        return modal;
    }
      
}
