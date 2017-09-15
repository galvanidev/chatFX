/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensagem.model;

import mensagem.controller.MensagemController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import usuario.bean.UsuarioBean;

/**
 *
 * @author vlagjuio
 */
public class MensagemModel {

    private static ObservableList<Node> lista = FXCollections.observableArrayList();

    public static ObservableList<Node> getLista() {
        return lista;
    }

    public static void addMensagem(String mensagem, String horario, UsuarioBean remetente, UsuarioBean destinatario) {
        if (remetente.getLogin().equals(destinatario.getLogin())) {
            lista.add(MensagemController.getMensagemUsuario(mensagem, horario, destinatario));
        } else {
            lista.add(MensagemController.getMensagemCliente(mensagem, horario, remetente));
        }
    }

    public static void addNotificao(UsuarioBean remetente, UsuarioBean destinatario, String horario, int tipo) {
        if (tipo == 2) {
            if (remetente == destinatario) {
                System.out.println("entrada usuario");
//                lista.add(MensagemController.getEntradaUsuario(remetente, horario));
            } else {
                System.out.println("entrada cliente");
//                lista.add(MensagemController.getEntradaCliente(destinatario, horario));
            }
        } else {
            System.out.println("saida cliente");
//            lista.add(MensagemController.getSaidaCliente(remetente, horario));
        }
    }

}
