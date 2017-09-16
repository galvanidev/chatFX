package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
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
    private static BufferedReader in;
    private static PrintWriter pw;
    private static UsuarioBean destinatario;
    private static String linha;
    private static JSONObject json;

    public ThreadConexao(Socket s, BufferedReader r, PrintWriter p) {
        socket = s;
        in = r;
        pw = p;
    }

    @Override
    public void run() {
        try {
            while (true) {
                pw.print(in.readLine() + "\n");
                pw.flush();
            }
        } catch (IOException ex) {
            // Logger.getLogger(ThreadConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
