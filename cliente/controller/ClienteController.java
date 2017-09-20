/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import conexao.ConexaoController;
import java.awt.event.ActionEvent;
import usuario.model.UsuarioModel;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import main.view.MainView;
import mensagem.model.MensagemModel;
import usuario.bean.UsuarioBean;
import usuario.model.ListCellUsuario;

/**
 *
 * @author Galvani Júnior
 */
public class ClienteController {
    
    @FXML private StackPane dialogPane;
    @FXML private BorderPane principal;
    @FXML private VBox campoMensagens;
    @FXML private TextField tfMensagem;
    @FXML private ScrollPane scrollMensagens;
    @FXML private ListView<UsuarioBean> listaUsuarios;

    @FXML
    private void initialize() {
        tfMensagem.requestFocus();
        inicializaLista();
        iniciaEscutas();
    }

    @FXML
    public void sair() {
        ConexaoController.logout();
        UsuarioModel.clear();
        new MainView().start(MainView.primaryStage);
    }

    @FXML
    public void enterMensagem(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            enviarMensagem();
        }
    }

    @FXML
    private void enviarMensagem() {
        if (!tfMensagem.getText().isEmpty()) {
            ConexaoController.enviaMensagem(tfMensagem.getText());
            tfMensagem.clear();
            tfMensagem.requestFocus();
        }
    }

    @FXML
    public void selecionaItem(MouseEvent ev) {
        if (ev.getButton() == MouseButton.PRIMARY) 
            openDialog(listaUsuarios.getSelectionModel().getSelectedItem());   
    }

    public void recebeMensagens(List<? extends Node> nos) {
        campoMensagens.getChildren().addAll(nos);
        scrollMensagens.vvalueProperty().bind(campoMensagens.heightProperty());
    }

    public void atualizaListaUsuarios(List<? extends UsuarioBean> usuarios) {
        listaUsuarios.getItems().addAll(usuarios);
    }

    private void iniciaEscutas() {
            // Escuta para os usuários
            // UsuarioModel.getLista().addListener((ListChangeListener.Change<? extends UsuarioBean> change) -> { });

            // Escuta para as mensagens
            MensagemModel.getLISTA().addListener((ListChangeListener.Change<? extends Node> change) -> {
                while (change.next()) {
                    if (change.wasAdded()) {
                        recebeMensagens(change.getAddedSubList());
                    }
                }
            });
    }

    private void openDialog(UsuarioBean selectedItem) {
        JFXDialogLayout content = new JFXDialogLayout();
        JFXDialog dialog = new JFXDialog(dialogPane, content, JFXDialog.DialogTransition.CENTER);
        Text nome = new Text();
        content.setHeading(new Text(selectedItem.getPessoa().nomeFormatado() + " (" + selectedItem.getLogin() +" )"));
        content.setBody(new Text("Conteúdo do meio"));
        JFXButton botao = new JFXButton("Voltar");
        botao.setOnAction((event) -> {
            dialog.close();
        });
        content.setActions(botao);
        dialogPane.visibleProperty().bindBidirectional(dialog.visibleProperty());
        dialog.show();
        
    }
    
    private void inicializaLista() {
        listaUsuarios.setItems(UsuarioModel.getLista());
        listaUsuarios.setCellFactory(listCellUsuario -> new ListCellUsuario());
    }

}
