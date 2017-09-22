/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro.altera.controller;

import com.jfoenix.controls.JFXButton;
import conexao.ConexaoController;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import usuario.bean.UsuarioBean;

/**
 *
 * @author Galvani Júnior
 */
public class AlteraController {
    
    @FXML private JFXButton alterar;
    @FXML private JFXButton cancelar;
    @FXML private TextField nome;
    @FXML private TextField email;
    @FXML private PasswordField novaSenha;
    @FXML private PasswordField senha;
    @FXML private PasswordField confirmaSenha;
    @FXML private DatePicker dataNascimento;
    
    private String pNome;
    private LocalDate pDataNascimento;
    private String pSexo;
    private String uEmail;
    
    @FXML
    private void initialize() {
        setDados();
        iniciaBinds();
    }

    private void iniciaBinds() {
        StringProperty nomeProperty = nome.textProperty();
        StringProperty emailProperty = email.textProperty();
        alterar.disableProperty().bind(nomeProperty.isEqualTo(pNome).or(nomeProperty.isEmpty())
        .and(emailProperty.isEqualTo(uEmail)).or(emailProperty.isEmpty())
        .and(dataNascimento.valueProperty().isEqualTo(pDataNascimento))
        .or(senha.textProperty().isEmpty()).or(confirmaSenha.textProperty().isEmpty()));
    }

    private void setDados() {
        UsuarioBean u = ConexaoController.getUsuario();
        pNome = u.getPessoa().getNome().toUpperCase();
        pDataNascimento = u.getPessoa().getDataNascimento();
        uEmail = u.getEmail();
        nome.setText(pNome);
        email.setText(uEmail);
        dataNascimento.setValue(pDataNascimento);
    }

    @FXML
    private void cancelar() {
        Stage stage = (Stage) cancelar.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    private void alterar() {
    }
    
}
