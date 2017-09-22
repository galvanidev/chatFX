/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.dialog;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
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
public class DialogController extends AnchorPane {

    // private  FXMLLoader mLoader;
    @FXML
    private VBox box;
    @FXML
    private Text nome;
    @FXML
    private Text email;
    @FXML
    private Text login;
    @FXML
    private Text sexo;
    @FXML
    private Text dataNascimento;

    public DialogController(UsuarioBean usuario) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DialogFXML.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        nome.setText(usuario.getPessoa().getNome().toUpperCase());
        email.setText(usuario.getEmail());
        login.setText(usuario.getLogin());
        if (usuario.getPessoa().getSexo().equals("M")) 
            sexo.setText("Masculino");
        else
            sexo.setText("Feminino");
        dataNascimento.setText(dataNascimento.getText() + usuario.getPessoa().getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    public Node getModal() {
        return this;
    }
}
