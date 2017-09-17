package conexao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
import mensagem.controller.MensagemController;
import mensagem.model.MensagemModel;
import org.json.JSONObject;
import usuario.bean.UsuarioBean;
import usuario.model.UsuarioModel;

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

    private Socket socket;
    private BufferedReader in;
    private PrintWriter pw;
    private String linha;
    private JSONObject json;

    public ThreadConexao(Socket s, BufferedReader r, PrintWriter p) {
        socket = s;
        in = r;
        pw = p;
    }

    @Override
    public void run() {
        try {
            while (true) {
                linha = in.readLine();
                if(linha.isEmpty()) { continue; };
                json = new JSONObject(linha);
                MensagemBean mensagem = MensagemBean.toObject(json);
                
                // Recebimento das mensagens
                if (mensagem.getTipo() == TipoMensagem.MENSAGEM) {
                    if (mensagem.getUsuario().equals(ConexaoController.getUsuario())) {
                        MensagemController.setMensagemUsuario(mensagem);
                    } else {
                        MensagemController.setMensagemCliente(mensagem);
                    }
                // Quando cliente acaba de entrar
                } else if (mensagem.getTipo() == TipoMensagem.ATUALIZA_LISTA) {
                    ConexaoController.setUsuario(mensagem.getUsuario());
                    UsuarioModel.atualizaLista(mensagem.getUsuarios());
                // Quando um novo cliente acessa o sistema
                } else if (mensagem.getTipo() == TipoMensagem.LOGIN) {
                    UsuarioModel.add(mensagem.getUsuario());
                // Quando um cliente sai do sistema
                } else if (mensagem.getTipo() == TipoMensagem.LOGOUT) {
                    UsuarioModel.remove(mensagem.getUsuario());
                } else {
                    System.out.println("Alguém atualizou seus dados");
                }                
                
            }
        } catch (IOException | NullPointerException ex) {
            ConexaoController.fechaConexao(socket, in, pw);
        }
    }

}
