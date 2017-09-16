package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Writer;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import mensagem.bean.MensagemBean;
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
public class ThreadConexao implements Runnable {

    private static Socket socket;
    private static BufferedReader read;
    private static Writer writer;
    private static UsuarioBean destinatario;
    private static String linha;
    private static JSONObject json;
    public ThreadConexao(Socket s, BufferedReader r, Writer w) {
        socket = s;
        read = r;
        writer = w;
    }
    
    @Override
    public void run() {
        try {
            while((linha = read.readLine()) != null) {
                linha = read.readLine();
                if (linha == null) { continue; }
                json = new JSONObject(linha);
                System.out.println(json.toString());
            }
        } catch (IOException ex) {
            // Logger.getLogger(ThreadConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void verifica() {
        
    }
    
}
