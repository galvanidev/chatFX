package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.ObjectInputStream;
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

    private static BufferedReader read;
    private static UsuarioBean destinatario;

    public ThreadConexao(BufferedReader br, UsuarioBean usuario) {
        read = br;
        destinatario = usuario;
    }
    
    @Override
    public void run() {
        try {
            while(read.ready()) {
                String linha = read.readLine();
                System.out.println(linha);
            }
        } catch (IOException ex) {
            Logger.getLogger(ThreadConexao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
        
    public void escutaFechamento(EventHandler <WindowEvent> e) {
        System.out.println(e);
    }

}
