package servidor.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONObject;
import usuario.bean.UsuarioBean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Galvani Júnior
 */
public class ThreadTratamento implements Runnable {

    private static BufferedReader read;
    private static JSONObject json;
    private static String linha;
    
    public ThreadTratamento(BufferedReader cliente) {
        read = cliente;
    }

    @Override
    public void run() {
        try {
            while (true) {
                linha = read.readLine();
                if (linha == null) { continue; };
                json = new JSONObject(linha);
                ServidorController.enviaMensagem(json);
            }
        } catch (IOException ex) {
            System.out.println("caiu cliente");
        }
    }
}
