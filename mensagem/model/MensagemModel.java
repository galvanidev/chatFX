/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensagem.model;

import javafx.application.Platform;
import mensagem.controller.MensagemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.Node;
import usuario.bean.UsuarioBean;

/**
 *
 * @author vlagjuio
 */
public class MensagemModel {

    private static final ObservableList<Node> LISTA = FXCollections.observableArrayList();

    public static ObservableList<Node> getLISTA() {
        return LISTA;
    }

    public static void addNode(Node no) {
        Platform.runLater(() -> {
            LISTA.add(no);
        });
    }

    public static void removeNode(Node no) {
        Platform.runLater(() -> {
            LISTA.remove(no);
        });
    }

    public static void addNotificao(UsuarioBean remetente, UsuarioBean destinatario, String horario, int tipo) {
        if (tipo == 2) {
            if (remetente == destinatario) {
                System.out.println("entrada usuario");
//                LISTA.add(MensagemController.getEntradaUsuario(remetente, horario));
            } else {
                System.out.println("entrada cliente");
//                LISTA.add(MensagemController.getEntradaCliente(destinatario, horario));
            }
        } else {
            System.out.println("saida cliente");
//            LISTA.add(MensagemController.getSaidaCliente(remetente, horario));
        }
    }

}
