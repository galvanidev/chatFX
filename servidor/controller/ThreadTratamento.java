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
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
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

    private Socket socket;
    private BufferedReader in;
    private PrintWriter pw;
    private String linha;
    private UsuarioBean usuario;
    private JSONObject json;

    public ThreadTratamento(Socket socket, BufferedReader in, PrintWriter pw, UsuarioBean usuario) throws IOException {
        this.socket = socket;
        this.in = in;
        this.pw = pw;
        this.usuario = usuario;
    }

    @Override
    public void run() {
        try {
            while (true) {
                // Faz a leitura de qualquer mensagem
                linha = in.readLine();
                if (linha.isEmpty()) {continue;};
                MensagemBean mensagem = MensagemBean.toObject(new JSONObject(linha));
                ServidorController.enviaMensagem(mensagem);
            }
        } catch (IOException | NullPointerException ex) {
            ServidorController.removeCliente(usuario, in, pw, socket);
            Logger.getLogger(ThreadTratamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
