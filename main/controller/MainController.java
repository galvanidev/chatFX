/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.controller;

import cadastro.registro.view.RegistroView;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

/**
 *
 * @author Galvani Júnior
 */
public class MainController {

    @FXML
    private BorderPane principal;
    private static BorderPane conteudo;
    private static Parent login;
    private static Parent registro;
    private static Parent altera;

    public void initialize() throws IOException {
        conteudo = principal;
        login = FXMLLoader.load(getClass().getResource("/login/view/LoginFXML.fxml"));
        registro = FXMLLoader.load(getClass().getResource("/cadastro/registro/view/RegistroFXML.fxml"));
        principal.setCenter(login);
    }

    public static void altera(String tela) {
        switch (tela) {
            case "login":
                conteudo.setCenter(login);
                break;
            case "registro":
                conteudo.setCenter(registro);
                break;
        }
    }

}
