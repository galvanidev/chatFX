/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cadastro.registro.controller;

import conexao.CadastroException;
import conexao.ConexaoController;
import conexao.ConexaoException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Popup;
import javafx.stage.WindowEvent;
import main.controller.MainController;
import pessoa.bean.PessoaBean;
import usuario.bean.UsuarioBean;
import util.TextFieldFormatter;

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
    private Tooltip tooltip;
    @FXML
    private HBox boxEmail;

    @FXML
    private void initialize() {
        iniciaBinds();
        iniciaSexos();
    }

    @FXML
    private void mouse() {
        
    }

    @FXML
    private void cadastrar() {
        if (validaEmail().get() && validaSenhas()) {
            PessoaBean pessoa = new PessoaBean();
            pessoa.setNome(nome.getText());
            pessoa.setCpf(cpf.getText());
            LocalDate localDate = dataNascimento.getValue();
            pessoa.setDataNascimento(localDate);
            String s = (String) sexo.getSelectionModel().getSelectedItem();
            if (s.equals("Masculino")) {
                pessoa.setSexo("M");
            } else {
                pessoa.setSexo("F");
            }
            UsuarioBean usuario = new UsuarioBean();
            usuario.setLogin(login.getText());
            usuario.setSenha(senha.getText());
            usuario.setEmail(email.getText());
            Platform.runLater(() -> {
                try {
                    ConexaoController.cadastra(usuario, pessoa);
                } catch (CadastroException | ConexaoException ex) {
                    System.out.println(ex.getMessage());
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
    private void formataCpf(KeyEvent e) {
        TextFieldFormatter tff = new TextFieldFormatter();
        tff.formatter(cpf, "0123456789]", "###.###.###-##");
    }

    private boolean validaSenhas() {
        if (senha.getText().equals(confirmaSenha.getText())) {
            return true;
        }
        return false;
    }

    private BooleanBinding validaEmail() {
        Pattern regexEmail
                = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
        Matcher matcher = regexEmail.matcher(email.getText());
        BooleanBinding binding = Bindings.createBooleanBinding(()
                -> matcher.find());
        binding.addListener((obs, oldValue, newValue) -> {
            System.out.println(obs + " " + oldValue + " " + newValue);
        });
        // tooltip.show();
        return binding;
    }
}
