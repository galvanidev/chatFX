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
        } catch (IOException ex) {
            Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void enviaMensagem(MensagemBean mensagem) {
        UsuarioBean u;
        PessoaBean p;
        MensagemBean m;
        mensagem.setHora(LocalTime.parse(LocalTime.now().toString()));
        switch (mensagem.getTipo()) {
            case LOGIN:
                // Se for login, envia pra todo mundo menos para o que está entrando
                listaUsuarios.entrySet().forEach((usuario) -> {
                    if (!mensagem.getUsuario().equals(usuario.getKey())) {
                        usuario.getValue().println(mensagem.toJson() + "\n");
                        usuario.getValue().flush(); 
                    }
                });
                break;

            case LOGOUT:
                // Se for logout, envia para todos menos para o que está saindo
                listaUsuarios.entrySet().forEach((usuario) -> {
                    usuario.getValue().println(mensagem.toJson() + "\n");
                    usuario.getValue().flush();
                });
                break;

            case MENSAGEM:
                // Se for tipo mensagem, envia pra todo mundo  
                u = new UsuarioBean(mensagem.getUsuario().getId(),
                        mensagem.getUsuario().getLogin());
                m = new MensagemBean(mensagem.getTipo(), u,
                        mensagem.getMensagem(), mensagem.getHora());
                listaUsuarios.entrySet().forEach((usuario) -> { 
                    usuario.getValue().println(m.toJson() + "\n");
                    usuario.getValue().flush();
                });
                break;
            case ATUALIZA_CADASTRO:
                // Se for tipo atualização, envia pra todo mundo 
                u = mensagem.getUsuario();
                p = new PessoaBean(u.getPessoa().getId(), 
                                u.getPessoa().getNome());
                u.setPessoa(p);
                m = mensagem;
                m.setUsuario(u);
                listaUsuarios.entrySet().forEach((usuario) -> {
                    if (usuario != m.getUsuario()) {
                        usuario.getValue().println(m.toJson() + "\n");
                        usuario.getValue().flush();
                    } else {
                        usuario.getValue().println(mensagem.toJson() + "\n");
                        usuario.getValue().flush();
                    }
                });
                break;
        }
    }

    private static void adicionarCliente(MensagemBean mensagem, PrintWriter pw, BufferedReader in) throws IOException {
        listaUsuarios.put(mensagem.getUsuario(), pw);
        UsuarioBean[] usuarios = (UsuarioBean[]) listaUsuarios.keySet().toArray(new UsuarioBean[listaUsuarios.size()]);
        MensagemBean mensagemUsuario = new MensagemBean(TipoMensagem.ATUALIZA_LISTA, mensagem.getUsuario(), usuarios);
        mensagemUsuario.setHora(LocalTime.parse(LocalTime.now().toString()));
        pw.println(mensagemUsuario.toJson() + "\n");
        pw.flush();
        MensagemBean mensagemTodos = new MensagemBean(TipoMensagem.LOGIN, mensagem.getUsuario());
        enviaMensagem(mensagemTodos);
    }

    public static void removeCliente(UsuarioBean u, BufferedReader in, PrintWriter pw, Socket socket) {
        // Retorna um array das keys e busca pelo dono do printwriter para poder fechar corretamente
        listaUsuarios.remove(u);
        fechaConexao(socket, in, pw);
        enviaMensagem(new MensagemBean(TipoMensagem.LOGOUT, new UsuarioBean(u.getId(), u.getLogin())));

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
        thread = new Thread() {
            @Override
            public void run() {
                try {
                    Socket client = cliente;
                    // Pega referência das io do socket
                    is = new DataInputStream(client.getInputStream());
                    os = new DataOutputStream(client.getOutputStream());

                    PrintWriter pw = new PrintWriter(os);
                    BufferedReader in = new BufferedReader(new InputStreamReader(is));

                    //  Recebe requisição do cliente
                    JSONObject json = new JSONObject(in.readLine());
                    MensagemBean mensagem = MensagemBean.toObject(json);
                    MensagemBean resposta;

                    switch (mensagem.getTipo()) {
                        // Cadastra novo usuário
                        case CADASTRA:
                            resposta = cadastraUsuario(mensagem.getPessoa(), mensagem.getUsuario());
                            pw.println(resposta.toJson() + "\n");
                            pw.flush();
                            break;
                        // Atualiza seu cadastro
                        case ATUALIZA_CADASTRO:
                            resposta = atualizaCadastro(mensagem.getUsuario());
                            pw.println(resposta.toJson() + "\n");
                            pw.flush();
                            break;
                        case LOGIN:
                            // Tenta autenticar
                            resposta = autentica(mensagem.getUsuario());
                            if (resposta.getTipo() == (TipoMensagem.ERRO)) {
                                resposta = new MensagemBean(resposta.getTipo(), resposta.getMensagem());
                                pw.println(resposta.toJson() + "\n");
                                pw.flush();
                            } else {
                                // Verifica se já está logado
                                if (listaUsuarios.containsKey(resposta.getUsuario())) {
                                    pw.println(new MensagemBean(TipoMensagem.ERRO, "Usuário já está logado").toJson() + "\n");
                                    pw.println(resposta.toJson() + "\n");
                                    pw.flush();
                                } else {
                                    // Cliente pode acessar, cria uma nova thread para tratar as mensagens
                                    ThreadTratamento tc = new ThreadTratamento(client, in, pw, resposta.getUsuario());
                                    new Thread(tc).start();
                                    pw.println(new MensagemBean(TipoMensagem.SUCESSO, resposta.getUsuario()).toJson() + "\n");
                                    pw.flush();
                                    resposta = new MensagemBean(TipoMensagem.LOGIN, resposta.getUsuario());
                                    adicionarCliente(resposta, pw, in);
                                }
                            }
                            break;
                    }
                } catch (IOException ex) {
                    System.out.println(ex.getStackTrace());
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
