/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensagem.controller;

import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import usuario.bean.UsuarioBean;

/**
 *
 * @author vlagjuio
 */
public class MensagemController {

    public static Node getMensagemCliente(String mensagem, String horario, UsuarioBean usuario) {

        VBox box = new VBox();
        Label nome = new Label(usuario.getLogin());
        Label texto = new Label(mensagem);
        Label hora = new Label(horario);

        nome.getStyleClass().add("nomeUsuarioMensagem");

        texto.getStyleClass().add("mensagemCliente");
//        texto.setMinWidth(Double.MAX_VALUE);
        texto.setWrapText(true);
        texto.setTextAlignment(TextAlignment.RIGHT);

        hora.getStyleClass().add("hora");

        box.setNodeOrientation(NodeOrientation.INHERIT);
        box.setAlignment(Pos.TOP_RIGHT);
        box.setMargin(box, new Insets(8, 16, 8, 150));

        box.getChildren().addAll(nome, texto, hora);

        return (VBox) box;
    }

    public static Node getMensagemUsuario(String mensagem, String horario, UsuarioBean usuario) {

        VBox box = new VBox();
        Label nome = new Label(usuario.getLogin());
        Label texto = new Label(mensagem);
        Label hora = new Label(horario);

        nome.getStyleClass().add("nomeUsuarioMensagem");

        texto.getStyleClass().add("mensagemUsuario");
//        texto.setMinWidth(Double.MAX_VALUE);
        texto.setWrapText(true);
        texto.setTextAlignment(TextAlignment.LEFT);

        hora.getStyleClass().add("hora");

        box.setNodeOrientation(NodeOrientation.INHERIT);
        box.setAlignment(Pos.TOP_LEFT);
        box.setMargin(box, new Insets(8, 150, 8, 16));
        box.getChildren().addAll(nome, texto, hora);

        return (VBox) box;
    }
}
