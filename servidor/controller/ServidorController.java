package servidor.controller;

import conexao.ConfiguracaoServidor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
    private static final HashMap<UsuarioBean, OutputStreamWriter> listaUsuarios = new HashMap<>();
    private static OutputStreamWriter escrita;
    private static BufferedReader leitura;
    private static Thread thread;

    private static void fechaConexao(Socket socket, BufferedReader leitor, OutputStreamWriter escritor) {
        try {
            leitor.close();
            escritor.close();
            socket.close();
            System.out.println("conexões fechadas");
        } catch (IOException ex) {
            Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void finalizaServico() {
        if (cliente.isConnected()) {

        }
    }

    public static void enviaMensagem(JSONObject json) {
        MensagemBean mensagem = MensagemBean.toObject(json);
        mensagem.setHora(LocalTime.parse(LocalTime.now().toString()));
        listaUsuarios.entrySet().forEach((usuario) -> {
            try {
                usuario.getValue().write(mensagem.toJson().toString() + "\n");
                usuario.getValue().flush();
            } catch (IOException ex) {
                Logger.getLogger(ServidorController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
    }

    private static void adicionarCliente(UsuarioBean usuario, OutputStreamWriter writer) throws IOException {
        listaUsuarios.put(usuario, writer);
        UsuarioBean[] usuarios = (UsuarioBean[]) listaUsuarios.keySet().toArray(new UsuarioBean[listaUsuarios.size()]);
        MensagemBean m = new MensagemBean();
        m.setTipo(TipoMensagem.ATUALIZA_LISTA);
        m.setUsuarios(usuarios);
        enviaMensagem(m.toJson());
    }

    public static void removeCliente(UsuarioBean usuario, Socket socket, BufferedReader leitor, OutputStreamWriter escritor) {
        listaUsuarios.remove(usuario);
        fechaConexao(socket, leitor, escritor);
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
                    escrita = new OutputStreamWriter(client.getOutputStream(), "UTF-8");
                    leitura = new BufferedReader(new InputStreamReader(client.getInputStream(), "UTF-8"));
                    //  Recebe mensagem do cliente
                    String linha = leitura.readLine();
                    JSONObject json = new JSONObject(linha);
                    MensagemBean mensagem = MensagemBean.toObject(json);

                    // Cadastrar usuário
                    if (mensagem.getTipo() == TipoMensagem.CADASTRA) {
                        mensagem = cadastraUsuario(mensagem.getPessoa(), mensagem.getUsuario());
                        escrita.write(mensagem.toJson().toString() + "\n");
                        escrita.flush();

                        // Atualizar um cadastro    
                    } else if (mensagem.getTipo() == TipoMensagem.ATUALIZA_CADASTRO) {
                        mensagem = atualizaCadastro(mensagem.getUsuario());
                        escrita.write(mensagem.toJson().toString() + "\n");
                        escrita.flush();

                    } else if (mensagem.getTipo() == TipoMensagem.LOGIN) {
                        mensagem = autentica(mensagem.getUsuario());
                        if (mensagem.getTipo() == (TipoMensagem.ERRO)) {
                            mensagem = new MensagemBean(mensagem.getTipo(), mensagem.getMensagem());
                            escrita.write(mensagem.toJson().toString() + "\n");
                            escrita.flush();
                        } else {
                            if (listaUsuarios.containsKey(mensagem.getUsuario())) {
                                mensagem = new MensagemBean(TipoMensagem.ERRO, "Usuário já está logado");
                                escrita.write(mensagem.toJson().toString() + "\n");
                                escrita.flush();
                            } else {
                                mensagem.setTipo(TipoMensagem.SUCESSO);
                                escrita.write(mensagem.toJson().toString() + "\n");
                                adicionarCliente(mensagem.getUsuario(), escrita);
                                // Cria um Tratador de Clientes em Thread separada
                                ThreadTratamento tc = new ThreadTratamento(client, leitura, escrita, mensagem.getUsuario());
                                new Thread(tc).start();
                            }
                        }
                    }
                } catch (IOException ex) {
                    try {
                        escrita.close();
                        leitura.close();
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
