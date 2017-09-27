/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro.registro.controller;

import conexao.CadastroException;
import conexao.ConexaoController;
import javafx.util.Duration;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import main.controller.MainController;
import org.controlsfx.control.PopOver;
import pessoa.bean.PessoaBean;
import usuario.bean.UsuarioBean;
import util.TextFieldFormatter;
import util.ValidadorCPF;
import util.ValidadorEmail;

/**
 *
 * @author vlagjuio
 */
public class RegistroController {

    @FXML
    private TextField nome;
    @FXML
    private TextField cpf;
    @FXML
    private DatePicker dataNascimento;
    @FXML
    private ComboBox sexo;
    @FXML
    private TextField login;
    @FXML
    private TextField email;
    @FXML
    private TextField senha;
    @FXML
    private TextField confirmaSenha;
    @FXML
    private Button cadastrar;
    @FXML
    private Label txtErro;
    
    @FXML
    private void initialize() {
        iniciaBinds();
        formataCpf();
        iniciaSexos();
    }

    @FXML
    private void cadastrar() {
        txtErro.setText("");
        if (validaEmail() & validaSenhas() & validaCPF()) {
            PessoaBean pessoa = new PessoaBean();
            pessoa.setNome(nome.getText().toUpperCase());
            pessoa.setCpf(cpf.getText());   
            LocalDate localDate = dataNascimento.getValue();
            pessoa.setDataNascimento(localDate);
            String s = (String) sexo.getSelectionModel().getSelectedItem();
            if (s.equals("Feminino")) 
                pessoa.setSexo("F");
            else 
                pessoa.setSexo("M"); 
            UsuarioBean usuario = new UsuarioBean();
            usuario.setLogin(login.getText());
            usuario.setSenha(senha.getText());
            usuario.setEmail(email.getText());
            Platform.runLater(() -> {
                try {
                    ConexaoController.cadastra(usuario, pessoa);
                } catch (CadastroException ex) {
                    trataMensagens(ex.getMessage());
                }
            });
        }
    }

    @FXML
    private void login() {
        MainController.altera("login");
    }

    private void iniciaBinds() {
        cadastrar.disableProperty().bind(nome.textProperty().isEmpty()
                .or(cpf.textProperty().isEmpty()
                        .or(dataNascimento.editorProperty().isNull())
                        .or(login.textProperty().isEmpty())
                        .or(sexo.getSelectionModel().selectedItemProperty().isNull())
                        .or(login.textProperty().isEmpty()).or(email.textProperty().isEmpty())
                        .or(email.textProperty().isEmpty())
                        .or(senha.textProperty().isEmpty())
                        .or(confirmaSenha.textProperty().isEmpty())
                ));

        nome.textProperty().addListener((ov, oldValue, newValue) -> {
            nome.setText(newValue.toUpperCase());
        });

        email.textProperty().addListener((observable, oldValue, newValue) -> {
            email.setText(newValue.toLowerCase());
        });
    }

    private void iniciaSexos() {
        ArrayList<String> sexos = new ArrayList<>();
        sexos.add("Feminino");
        sexos.add("Masculino");
        sexo.setItems(FXCollections.observableArrayList(sexos));
    }

    @FXML
    private void formataCpf() {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.formatter(cpf, "0123456789]", "###.###.###-##");
    }

    private boolean validaSenhas() {
        if (senha.getText().equals(confirmaSenha.getText())) {
            return true;
        }
        newPop("As senhas não conferem").show(confirmaSenha);
        return false;
    }

    private boolean validaEmail() {
        if (ValidadorEmail.validar(email.getText())) {
            return true;
        }
        newPop("E-mail inválido").show(email);
        return false;
    }

    private boolean validaCPF() {
        if (ValidadorCPF.isValido(cpf.getText()))
            return true;
        newPop("CPF inválido").show(cpf);
        return false;
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

    private void trataMensagens(String mensagem) {
        if (mensagem.contains("_")) {
            String s[] = mensagem.split("_");
            for (String x : s) {
                if (x.contains("pessoa"))
                    newPop(x).show(cpf);
                if (x.contains("Usuário")) 
                    newPop(x).show(login);
            }
        } else {
            if (mensagem.contains("sucesso"))
                txtErro.setText(mensagem);
            else 
                txtErro.setText(mensagem);
        }
    }
}
