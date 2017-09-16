package servidor.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
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
    private static BufferedReader in;
    private static PrintWriter pw;
    private static String linha;
    private static UsuarioBean usuario;

    public ThreadTratamento(Socket sock, BufferedReader leitura, PrintWriter escrita, UsuarioBean user) throws IOException {
        in = leitura;
        pw = escrita;
        usuario = user;
        socket = sock;
    }

    @Override
    public void run() {
        try {
            while (true) {
                linha = in.readLine();
                if (linha.isEmpty()) { continue; };
                JSONObject json = new JSONObject(linha);
                System.out.println(linha);                
                ServidorController.enviaMensagem(json);
            }
        } catch (IOException | NullPointerException ex) {
            ServidorController.removeCliente(usuario, socket, in, pw);
            // System.out.println(Arrays.toString(ex.getStackTrace()));
        }
    }
}
