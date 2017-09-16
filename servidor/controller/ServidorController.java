package servidor.controller;

import conexao.ConfiguracaoServidor;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
import org.json.JSONObject;
import pessoa.bean.PessoaBean;
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
public final class ServidorController {

    public static Socket cliente;
    private static ServerSocket server;
    public static final HashMap<UsuarioBean, PrintWriter> listaUsuarios = new HashMap<>();
    private static Thread thread;
    private static DataInputStream is;
    private static DataOutputStream os;

    private static void fechaConexao(Socket socket, BufferedReader in, PrintWriter pw) {
        try {
            socket.close();
            in.close();
            pw.close();
            System.out.println("fechou as conexões");
        } catch (IOException ex) {
            Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void enviaMensagem(JSONObject json) {
        MensagemBean mensagem = MensagemBean.toObject(json);
        mensagem.setHora(LocalTime.parse(LocalTime.now().toString()));
        listaUsuarios.entrySet().forEach((usuario) -> {
            usuario.getValue().print(mensagem.toJson() + "\n");
            usuario.getValue().flush();
        });
    }

    private static void adicionarCliente(UsuarioBean usuario, PrintWriter pw, BufferedReader in) throws IOException {
        listaUsuarios.put(usuario, pw);
        /*UsuarioBean[] usuarios = (UsuarioBean[]) listaUsuarios.keySet().toArray(new UsuarioBean[listaUsuarios.size()]);
        MensagemBean m = new MensagemBean();
        m.setTipo(TipoMensagem.LOGIN);
        m.setUsuarios(usuarios);
        enviaMensagem(m.toJson());*/
    }

    public static void removeCliente(UsuarioBean usuario, Socket socket, BufferedReader in, PrintWriter pw) {
        listaUsuarios.remove(usuario);
        fechaConexao(socket, in, pw);
    }

    private static MensagemBean autentica(UsuarioBean u) {
        ThreadLogin tbd = new ThreadLogin(u);
        return tbd.mensagem;
    }

    private static MensagemBean cadastraUsuario(PessoaBean pessoa, UsuarioBean usuario) {
        ThreadCadastro tc = new ThreadCadastro(pessoa, usuario);
        return tc.mensagem;
    }

    private static MensagemBean atualizaCadastro(UsuarioBean usuario) {
        ThreadAlteracao tdb = new ThreadAlteracao(usuario.getPessoa(), usuario);
        return tdb.mensagem;
    }

    public static void executa() {
        try {
            server = new ServerSocket(ConfiguracaoServidor.getPort());
            while (true) {
                cliente = server.accept();
                trataCliente();
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void trataCliente() {
        Socket client = cliente;
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    // Pega referência das io do socket

                    is = new DataInputStream(client.getInputStream());
                    os = new DataOutputStream(client.getOutputStream());

                    PrintWriter pw = new PrintWriter(os);
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));

                    //  Recebe mensagem do cliente
                    JSONObject json = new JSONObject(in.readLine());
                    MensagemBean mensagem = MensagemBean.toObject(json);

                    // Cadastrar usuário
                    if (mensagem.getTipo() == TipoMensagem.CADASTRA) {
                        mensagem = cadastraUsuario(mensagem.getPessoa(), mensagem.getUsuario());
                        pw.println(mensagem.toJson() + "\n");
                        pw.flush();

                        // Atualizar um cadastro    
                    } else if (mensagem.getTipo() == TipoMensagem.ATUALIZA_CADASTRO) {
                        mensagem = atualizaCadastro(mensagem.getUsuario());
                        pw.println(mensagem.toJson() + "\n");
                        pw.flush();

                    } else if (mensagem.getTipo() == TipoMensagem.LOGIN) {
                        mensagem = autentica(mensagem.getUsuario());
                        if (mensagem.getTipo() == (TipoMensagem.ERRO)) {
                            mensagem = new MensagemBean(mensagem.getTipo(), mensagem.getMensagem());
                            pw.println(mensagem.toJson() + "\n");
                            pw.flush();
                        } else {
                            if (listaUsuarios.containsKey(mensagem.getUsuario())) {
                                mensagem = new MensagemBean(TipoMensagem.ERRO, "Usuário já está logado");
                                pw.println(mensagem.toJson() + "\n");
                                pw.flush();
                            } else {
                                adicionarCliente(mensagem.getUsuario(), pw, in);
                                
                                // Cria um Tratador de Clientes em Thread separada
                                ThreadTratamento tc = new ThreadTratamento(client, in, pw, mensagem.getUsuario());
                                new Thread(tc).start();
                                mensagem.setTipo(TipoMensagem.SUCESSO);
                                pw.println(mensagem.toJson() + "\n");
                                pw.flush();
                            }
                        }
                    }
                } catch (IOException ex) {
                    try {
                        cliente.close();
                        Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex1) {
                        Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex1);
                    }
                }
            }
        };
        thread.start();
    }

    public static void main(String[] args) {
        ServidorController.executa();
    }

// Classe
}
