/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente.util;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import usuario.bean.UsuarioBean;

/**
 *
 * @author vlagjuio
 */
public class ItemUsuario {

    public static Node getNode(UsuarioBean u, String horario) {

        Circle circulo = new Circle(30);
        ImageView imagemView = new ImageView();
        Image image = new Image("/cliente/imagens/imagem.jpg");

        Label nome = new Label();
        nome.maxHeight(Double.POSITIVE_INFINITY);
        nome.getStyleClass().add("nomeUsuarioItem");
        nome.setText(u.getLogin());

        Label hora = new Label();
        hora.getStyleClass().add("hora");
        hora.setText(horario);
        
        circulo.setFill(new ImagePattern(image));

        imagemView.setImage(image);
        imagemView.getStyleClass().add("imagemUsuario");
        imagemView.setPickOnBounds(true);
        imagemView.setPreserveRatio(true);

        BorderPane.setAlignment(circulo, Pos.CENTER_LEFT);
        BorderPane.setAlignment(nome, Pos.CENTER_LEFT);
        BorderPane usuario = new BorderPane(nome, null, hora, null, circulo);

        usuario.minHeight(150);
        usuario.minWidth(300);
        usuario.getStyleClass().add("itemListaUsuario");
        usuario.setOnMouseClicked(e -> {
            System.out.println(usuario.getId());
        });
        return (BorderPane) usuario;
    }

}
