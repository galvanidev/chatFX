package servidor.controller;

import conexao.ConfiguracaoServidor;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import usuario.bean.UsuarioBean;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
import org.json.JSONObject;
import org.postgresql.util.PSQLException;
import pessoa.bean.PessoaBean;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Galvani Júnior
 */
public final class ServidorController {

    public static Socket cliente;
    private static final int PORT = 8292;
    private static ServerSocket server;
    private static final HashMap<UsuarioBean, BufferedReader> listaUsuarios = new HashMap<>();
    private static OutputStreamWriter escrita;
    private static BufferedReader leitura;

    private static void fechaConexao() {
        try {
            escrita.close();
            leitura.close();
            cliente.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void finalizaServico() {
        if (cliente.isConnected()) {

        }
    }

    public static void enviaMensagem(JSONObject json) {
        System.out.println(json);
        MensagemBean mensagem = MensagemBean.toObject(json);
        System.out.println(mensagem.getMensagem());
        System.out.println(mensagem.getUsuario());
        System.out.println(mensagem.getUsuarios());
        mensagem.setHora(LocalTime.now());
        listaUsuarios.entrySet().forEach((usuario) -> {
            try {
                escrita.write(mensagem.toJson().toString());
            } catch (IOException ex) {
                Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private static void adicionarCliente(UsuarioBean usuario, BufferedReader read) throws IOException {
        listaUsuarios.put(usuario, read);
        UsuarioBean[] usuarios = (UsuarioBean[]) listaUsuarios.keySet().toArray(new UsuarioBean[listaUsuarios.size()]);
        MensagemBean m = new MensagemBean();
        m.setTipo(TipoMensagem.ATUALIZA_LISTA.name());
        m.setUsuarios(usuarios);
        ServidorController.enviaMensagem(m.toJson());
    }

    public static void removeCliente(UsuarioBean usuario) {
        BufferedReader rd = listaUsuarios.get(usuario);
        listaUsuarios.remove(usuario);
        fechaConexao();
    }

    private static UsuarioBean autentica(UsuarioBean u) {
        try {
            ThreadLogin tbd = new ThreadLogin(u);
            u = tbd.usuario;
        } catch (PSQLException ex) {
            Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return u;
    }

    private static MensagemBean cadastraUsuario(PessoaBean pessoa, UsuarioBean usuario) {
        ThreadCadastro tc = new ThreadCadastro(pessoa, usuario);
        return tc.mensagem;
    }

    public static void executa() {

        try {
            server = new ServerSocket(ConfiguracaoServidor.getPort());
            while (true) {
                cliente = server.accept();
                
                // Pega referência das io do socket
                escrita = new OutputStreamWriter(cliente.getOutputStream(), "UTF-8");
                leitura = new BufferedReader(new InputStreamReader(cliente.getInputStream(), "UTF-8"));
                //  Recebe mensagem do cliente
                String linha = leitura.readLine();
                JSONObject json = new JSONObject(linha);
                System.out.println(json.toString());
                MensagemBean m = MensagemBean.toObject(json);
                // Verifica tipo de mensagem, processa e responde
                if (m.getTipo().equals(TipoMensagem.CADASTRAR.name())) {
                    m = cadastraUsuario(m.getPessoa(), m.getUsuario());
                    escrita.write(m.toJson().toString() + "\n");
                    escrita.flush();
                } else if (m.getTipo().equals(TipoMensagem.LOGIN.name())) {
                    m.setUsuario(autentica(m.getUsuario()));
                    if (m.getUsuario() == null) {
                        m.setTipo(TipoMensagem.ERRO.name());
                        m.setMensagem("Usuário e/ou senha inválido(s).");
                        escrita.write(m.toJson().toString() + "\n");
                    } else {
                        if (listaUsuarios.containsKey(m.getUsuario())) {
                            m.setUsuario(null);
                            m.setTipo(TipoMensagem.ERRO.name());
                            m.setMensagem("Usuário já está logado.");
                            escrita.write(m.toJson().toString() + "\n");
                        } else {
                            m.setTipo(TipoMensagem.SUCESSO.name());
                            escrita.write(m.toJson().toString() + "\n");
                            adicionarCliente(m.getUsuario(), leitura);
                            // Cria um Tratador de Clientes em Thread separada
                            ThreadTratamento tc = new ThreadTratamento(null);
                            new Thread(tc).start();
                        }
                    }
                    escrita.close();
                    leitura.close();
                    cliente.close();
                }
            }
            // Segundo try
        } catch (IOException ex) {
            Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
        }
        // Método executa  
        // Método executa  

    }

    public static void main(String[] args) {
        ServidorController.executa();
    }

// Classe
}
