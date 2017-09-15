/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.controller;

import conexao.ConexaoController;
import java.io.IOException;
import usuario.model.UsuarioModel;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import login.view.LoginView;
import mensagem.model.MensagemModel;
import usuario.bean.UsuarioBean;

/**
 *
 * @author Galvani Júnior
 */
public class ClienteController {

    @FXML
    private VBox campoMensagens;
    @FXML
    private TextField tfMensagem;
    @FXML
    private ScrollPane scrollMensagens;
    @FXML
    private ListView<UsuarioBean> listaUsuarios;

    @FXML
    private void initialize() {
        listaUsuarios.setItems(UsuarioModel.getLista());
        tfMensagem.requestFocus();
        iniciaEscutas();
    }

    @FXML
    public void sair() {
        ConexaoController.logout();
        new LoginView().start(LoginView.primaryStage);
    }

    @FXML
    public void enterMensagem(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            enviarMensagem();
        }
    }

    @FXML
    private void enviarMensagem() {
        try {
            ConexaoController.enviaMensagem(tfMensagem.getText());
            tfMensagem.setText("");
            tfMensagem.requestFocus();
        } catch (IOException ex) {
            Logger.getLogger(ClienteController.class.getName()).log(Level.SEVERE, null, ex);
            ConexaoController.logout();
        }
    }

    @FXML
    public void selecionaItem(MouseEvent ev) {
        if (ev.getClickCount() == 2 && ev.getButton() == MouseButton.PRIMARY) {
            
        }
    }
        
    public void recebeMensagens(List<? extends Node> nos) {
        campoMensagens.getChildren().addAll(nos);
        scrollMensagens.vvalueProperty().bind(campoMensagens.heightProperty());
    }

    public void atualizaListaUsuarios() {
        listaUsuarios.setItems(UsuarioModel.getLista());
    }
    
    private void iniciaEscutas() {
        
        // Escuta para os usuários
        UsuarioModel.getLista().addListener((ListChangeListener.Change<? extends UsuarioBean> change) -> {
            while (change.next()) {
                atualizaListaUsuarios();
            }
        });

        // Escuta para as mensagens
        MensagemModel.getLista().addListener((ListChangeListener.Change<? extends Node> change) -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    recebeMensagens(change.getAddedSubList());
                }
            }
        });
    }
}
