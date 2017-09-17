/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensagem.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javax.swing.JOptionPane;
import mensagem.bean.MensagemBean;
import mensagem.model.MensagemModel;
import usuario.bean.UsuarioBean;
import usuario.model.UsuarioModel;

/**
 *
 * @author vlagjuio
 */
public class MensagemController {

    public static void setMensagemCliente(MensagemBean mensagem) {

        VBox box = new VBox();
        mensagem.setUsuario(UsuarioModel.get(mensagem.getUsuario().getId()));
        Label nome = new Label(mensagem.getUsuario().getPessoa().getNomeFormatado()
                + " (" + mensagem.getUsuario().getLogin() + ")");
        Label texto = new Label(mensagem.getMensagem());
        Label hora = new Label(mensagem.getHora().toString());

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

        MensagemModel.addNode((VBox) box);
    }

    public static void setMensagemUsuario(MensagemBean mensagem) {
        VBox box = new VBox();
        mensagem.setUsuario(UsuarioModel.get(mensagem.getUsuario().getId()));
        Label nome = new Label(mensagem.getUsuario().getPessoa().getNomeFormatado()
                + " (" + mensagem.getUsuario().getLogin() + ")");
        Label texto = new Label(mensagem.getMensagem());
        Label hora = new Label(mensagem.getHora().toString());

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

        MensagemModel.addNode((VBox) box);
    }

    public static void notificaSaida(UsuarioBean usuario) {
        usuario = UsuarioModel.get(usuario.getId());
        System.out.println(usuario.getPessoa().getNomeFormatado() + " saiu");
    }

    public static void notificaEntrada(UsuarioBean usuario) {
        usuario = UsuarioModel.get(usuario.getId());
        System.out.println(usuario.getPessoa().getNomeFormatado() + " entrou");
    }
}
