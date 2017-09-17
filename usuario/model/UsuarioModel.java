/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuario.model;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import usuario.bean.UsuarioBean;

/**
 *
 * @author vlagjuio
 */
public class UsuarioModel {

    private static final ObservableList<UsuarioBean> LISTA = FXCollections.observableArrayList();

    public static ObservableList<UsuarioBean> getLista() {
        return LISTA;
    }

    public static void add(UsuarioBean u) {
        Platform.runLater(() -> {
            if (LISTA.contains(u)) {
                LISTA.remove(u);
            }
            LISTA.add(u);
        });
    }
    
    public static void atualizaLista(UsuarioBean[] usuarios) {
        LISTA.addAll(usuarios);
    }

    public static void remove(UsuarioBean u) {
        Platform.runLater(() -> {
            LISTA.remove(u);
        });
    }

}
