/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro.altera.controller;

import com.jfoenix.controls.JFXButton;
import conexao.CadastroException;
import conexao.ConexaoController;
import java.time.LocalDate;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.StringConverter;
import org.controlsfx.control.PopOver;
import pessoa.bean.PessoaBean;
import usuario.bean.UsuarioBean;
import util.ValidadorEmail;

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
    @FXML private Label txtErro;
    
    private UsuarioBean u;
    private String pNome;
    private LocalDate pDataNascimento;
    private String pSexo;
    private String uEmail;
    
    @FXML
    private void initialize() {
        u = ConexaoController.getUsuario();
        setDados();
        iniciaBinds();
    }

    private void iniciaBinds() {
        nome.textProperty().addListener((ov, oldValue, newValue) -> {
            nome.setText(newValue.toUpperCase());
        });
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            email.setText(newValue.toLowerCase());
        });
        alterar.disableProperty().bind(nome.textProperty().isEqualTo(pNome).or(nome.textProperty().isEmpty())
        .and(email.textProperty().isEqualTo(uEmail)).or(email.textProperty().isEmpty())
        .and(novaSenha.textProperty().isEmpty())
        .and(dataNascimento.valueProperty().isEqualTo(pDataNascimento).or(dataNascimento.valueProperty().isNull()))
        .or(senha.textProperty().isEmpty()).or(confirmaSenha.textProperty().isEmpty()));
    }

    private void setDados() {
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
        if (validaEmail() & validaSenhas()) {
            PessoaBean pessoa = u.getPessoa();
            pessoa.setNome(nome.getText().toUpperCase());  
            LocalDate localDate = dataNascimento.getValue();
            pessoa.setDataNascimento(localDate);
            UsuarioBean usuario = u;
            if (!novaSenha.getText().isEmpty()) {
                if (validaNovaSenha()) 
                    usuario.setSenha(novaSenha.getText());
                else
                    return;
            } else {
                usuario.setSenha(senha.getText());
            }
            usuario.setEmail(email.getText());
            Platform.runLater(() -> {
                try {
                    ConexaoController.altera(usuario, senha.getText());
                } catch (CadastroException ex) {
                    System.out.println(ex.getMessage());
                    txtErro.setText(ex.getMessage());
                }
            });
        }
    }
    
    private boolean validaEmail() {
        if (ValidadorEmail.validar(email.getText())) {
            return true;
        }
        newPop("E-mail inválido").show(email);
        return false;
    }
    
    private boolean validaSenhas() {
        if (senha.getText().equals(confirmaSenha.getText())) {
            return true;
        }
        newPop("As senhas não conferem").show(confirmaSenha);
        return false;
    }
    
    private boolean validaNovaSenha() {
        if (novaSenha.getText().equals(senha.getText())) {
            newPop("A nova senha é idêntica a anterior").show(novaSenha);
            return false;
        }
        return true;
    }
    
    private PopOver newPop(String mensagem) {
        Label label = new Label(mensagem);
        label.getStyleClass().add("txtPop");
        PopOver popOver = new PopOver();
        popOver.setContentNode(label);
        popOver.closeButtonEnabledProperty().set(false);
        popOver.arrowLocationProperty().bind(new SimpleObjectProperty<>(PopOver.ArrowLocation.LEFT_CENTER));
        popOver.arrowSizeProperty().set(3);
        popOver.arrowIndentProperty().set(3);
        popOver.hide(Duration.seconds(4));
        popOver.hideOnEscapeProperty();
        popOver.setDetachable(Boolean.FALSE);
        return popOver;
    }
   
}
