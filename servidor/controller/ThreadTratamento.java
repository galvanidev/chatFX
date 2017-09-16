package servidor.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
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
    
    private static Socket socket;
    private static BufferedReader read;
    private static OutputStreamWriter writer;
    private static JSONObject json;
    private static String linha;
    private static UsuarioBean usuario;

    public ThreadTratamento(Socket sock, BufferedReader leitura, OutputStreamWriter escrita, UsuarioBean user) throws IOException {
        read = leitura;
        writer = escrita;
        usuario = user;
        socket = sock;
    }

    @Override
    public void run() {
        try {
            while (read.read() != -1) {
                linha = read.readLine();
                if (linha == null) { continue; };
                json = new JSONObject(linha.split(""));
                ServidorController.enviaMensagem(json);
            }
            System.out.println("caiu");
            ServidorController.removeCliente(usuario, socket, read, writer);
        } catch (IOException ex) {
            ex.getStackTrace();
        }
    }
}
