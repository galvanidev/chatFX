/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuario.view;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import usuario.bean.UsuarioBean;

/**
 *
 * @author Galvani Júnior
 */
public class ListCellUsuario extends ListCell<UsuarioBean> {
    
    @FXML private BorderPane celula;
    @FXML private BorderPane border;
    @FXML private AnchorPane anchor;
    @FXML private HBox hbox;
    @FXML private VBox conteudo;
    @FXML private Label nome;
    @FXML private Label login;
    @FXML private Label hora;
    @FXML private Label letra;

    private FXMLLoader mLoader;
    
    @Override
    protected void updateItem(UsuarioBean usuario, boolean empty) {
        super.updateItem(usuario, empty);
        if(empty || usuario == null) {
            setText(null);
            setGraphic(null);
        } else {
            if (mLoader == null) {
                mLoader = new FXMLLoader(getClass().getResource("ListCellUsuarioFXML.fxml"));
                mLoader.setController(this);
                try {
                    mLoader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            nome.setText(usuario.getPessoa().nomeFormatado());
            login.setText(usuario.getLogin());
            letra.setText(Character.toString(usuario.getPessoa().getNome().charAt(0)));
            setText(null);
            setGraphic(celula);
        }

    }
    
}
