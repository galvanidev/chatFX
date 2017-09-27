/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package login.controller;

import cliente.view.ClienteView;
import conexao.ConexaoController;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import main.controller.MainController;
import main.view.MainView;

/**
 *
 * @author vlagjuio
 */
public class LoginController {

    @FXML
    BorderPane principal;
    @FXML
    Button btnEntrar;
    @FXML
    TextField tfUsuario;
    @FXML
    TextField tfSenha;
    @FXML
    Label txtErro;
    @FXML
    HBox boxMensagem;
    private boolean task;

    @FXML
    private void initialize() {
        iniciaBinds();
    }

    @FXML
    private void tfUsuarioKeyPressed(KeyEvent e) {        
        if (e.getCode().equals(KeyCode.ENTER)) {
            entrar();
        }
    }

    @FXML
    private void tfSenhaKeyPressed(KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            entrar();
        }
    }

    @FXML
    private void entrar() {
        if (!task) {
            task = true;
            Task<Boolean> login = new Task<Boolean>() {
                @Override
                protected Boolean call() {
                    Platform.runLater(() -> {
                        ProgressBar pg = new ProgressBar(ProgressBar.INDETERMINATE_PROGRESS);
                        boxMensagem.getChildren().clear();
                        boxMensagem.getChildren().add(pg);
                    });
                    return ConexaoController.login(getUsuario(), getSenha());
                }

                @Override
                protected void failed() {
                    super.failed();
                    task = false;
                    updateMessage(getException().getMessage());
                    txtErro.setText(getMessage());
                    boxMensagem.getChildren().clear();
                    boxMensagem.getChildren().add(txtErro);
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, getException().getStackTrace());
                }

                @Override
                protected void succeeded() {
                    super.succeeded();
                    new ClienteView().start(MainView.primaryStage);
                    MainView.primaryStage.setMaximized(true);
                }

            };
            Thread t = new Thread(login);
            t.start();
        }
    }

    @FXML
    private void cadastrar() {
        MainController.altera("registro");        
    }

    private String getUsuario() {
        return tfUsuario.getText();
    }

    private String getSenha() {
        return tfSenha.getText();
    }

    private void iniciaBinds() {
        
        tfUsuario.textProperty().addListener((ov, oldValue, newValue) -> {
            tfUsuario.setText(newValue.toLowerCase());
        });
        
        btnEntrar.disableProperty().bind(tfUsuario.textProperty().isEmpty()
                .or(tfSenha.textProperty().isEmpty()));
    }
    
}
