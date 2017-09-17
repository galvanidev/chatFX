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
                if (linha.isEmpty()) { continue; };
                json = new JSONObject(linha);
                MensagemBean mensagem = MensagemBean.toObject(json);
                System.out.println(json);

                switch (mensagem.getTipo()) {
                    case MENSAGEM:
                        // Recebimento das mensagens
                        if (mensagem.getUsuario().equals(ConexaoController.getUsuario())) {
                            MensagemController.setMensagemUsuario(mensagem);
                        } else {
                            MensagemController.setMensagemCliente(mensagem);
                        }
                        break;
                    case ATUALIZA_LISTA:
                        // Quando cliente acaba de entrar
                        ConexaoController.setUsuario(mensagem.getUsuario());
                        UsuarioModel.atualizaLista(mensagem.getUsuarios());
                        break;
                    case LOGIN:
                        // Quando um novo cliente acessa o sistema
                        UsuarioModel.add(mensagem.getUsuario());
                        MensagemController.notificaEntrada(mensagem.getUsuario());
                        break;
                    // Quando um cliente sai do sistema
                    case LOGOUT:
                        MensagemController.notificaSaida(mensagem.getUsuario());
                        UsuarioModel.remove(mensagem.getUsuario());
                        break;
                    case ATUALIZA_CADASTRO:
                        System.out.println("Alguém atualizou seus dados");
                        break;
                }
            }
        } catch (IOException | NullPointerException ex) {
            ConexaoController.fechaConexao(socket, in, pw);
        }
    }

}
