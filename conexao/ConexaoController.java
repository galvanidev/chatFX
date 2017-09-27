package conexao;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import usuario.bean.UsuarioBean;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
import org.json.JSONObject;
import util.FrameWork;
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
public class ConexaoController {

    private static Socket servidor = null;
    private static PrintWriter pw;
    private static BufferedReader in;
    private static UsuarioBean usuario;

    private static void fazConexao() throws ConexaoException {
        try {
            // Inicia troca de socket com servidor
            servidor = new Socket(ConfiguracaoServidor.getHost(), ConfiguracaoServidor.getPort());
            DataInputStream is = new DataInputStream(servidor.getInputStream());
            DataOutputStream os = new DataOutputStream(servidor.getOutputStream());
            pw = new PrintWriter(os);
            in = new BufferedReader(new InputStreamReader(is));
        } catch (IOException ex) {
            // Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
            throw new ConexaoException("Verifique sua conexão.");
        }
    }

    public static void fechaConexao(Socket servidor, BufferedReader leitura, PrintWriter escrita) {
        try {
            servidor.close();
            leitura.close();
            escrita.close();
        } catch (IOException ex) {
            // Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cadastra(UsuarioBean usuario, PessoaBean pessoa) {
        try {
            fazConexao();
            MensagemBean mensagem = new MensagemBean(TipoMensagem.CADASTRA, usuario, pessoa);
            usuario.setSenha(FrameWork.criptografar(usuario.getSenha()));
            JSONObject json = mensagem.toJson();
            pw.print(json + "\n");
            pw.flush();

            json = new JSONObject(in.readLine());
            mensagem = MensagemBean.toObject(json);
            if (mensagem.getTipo() == TipoMensagem.SUCESSO) {
                throw new CadastroException(mensagem.getMensagem());
            }
            throw new CadastroException(mensagem.getMensagem());
        } catch (IOException | ConexaoException ex) {
            // Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
            throw new CadastroException("Verifique sua conexão");
        }
    }

    public static void altera(UsuarioBean usuario, String senha) throws CadastroException {
        try {
            Socket s = new Socket(ConfiguracaoServidor.getHost(), ConfiguracaoServidor.getPort());
            DataInputStream in = new DataInputStream(s.getInputStream());
            DataOutputStream o = new DataOutputStream(s.getOutputStream());
            PrintWriter p = new PrintWriter(o);
            BufferedReader i = new BufferedReader(new InputStreamReader(in));
            // Tenta autenticar
            UsuarioBean usuarioLogin = new UsuarioBean(usuario.getLogin(), FrameWork.criptografar(senha));
            MensagemBean mensagem = new MensagemBean(TipoMensagem.LOGIN, usuarioLogin);
            JSONObject json = mensagem.toJson();
            p.print(json + "\n");
            p.flush();
            json = new JSONObject(i.readLine());
            mensagem = MensagemBean.toObject(json);
            fechaConexao(s, i, p);
            if (mensagem.getTipo() == TipoMensagem.ERRO & (!mensagem.getMensagem().contains("logado"))) {
                fechaConexao(s, i, p);
                throw new CadastroException(mensagem.getMensagem());
            }
            usuario.setSenha(FrameWork.criptografar(usuario.getSenha()));
            s = new Socket(ConfiguracaoServidor.getHost(), ConfiguracaoServidor.getPort());
            in = new DataInputStream(s.getInputStream());
            o = new DataOutputStream(s.getOutputStream());
            p = new PrintWriter(o);
            i = new BufferedReader(new InputStreamReader(in));
            // Atualiza cadastro
            mensagem = new MensagemBean(TipoMensagem.ATUALIZA_CADASTRO, usuario);
            json = mensagem.toJson();
            p.print(json + "\n");
            p.flush();
            json = new JSONObject(i.readLine());
            mensagem = MensagemBean.toObject(json);
            ConexaoController.usuario = mensagem.getUsuario();
            throw new CadastroException(mensagem.getMensagem());
        } catch (IOException ex) {
            Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean login(String login, String senha) {
        try {
            fazConexao();
            UsuarioBean u = new UsuarioBean();
            u.setLogin(login);
            u.setSenha(FrameWork.criptografar(senha));
            MensagemBean mensagem = new MensagemBean(TipoMensagem.LOGIN, u);

            JSONObject json = mensagem.toJson();
            pw.print(json + "\n");
            pw.flush();

            json = new JSONObject(in.readLine());
            mensagem = MensagemBean.toObject(json);
            
            if (mensagem.getTipo() == TipoMensagem.SUCESSO) {
                usuario = mensagem.getUsuario();
                System.out.println(mensagem.getUsuario().toJson());
                iniciaThread();
                return true;
            } else {
                fechaConexao(servidor, in, pw);
                throw new LoginException(mensagem.getMensagem());
            }
        } catch (IOException ex) {
            fechaConexao(servidor, in, pw);
            Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void logout() {
        fechaConexao(servidor, in, pw);
    }

    private static void iniciaThread() {
        // Exibe mensagem de sucesso no terminal
        System.out.println("Autenticado no servidor: " + servidor.getInetAddress());
        // Cria uma thread para receber mensagens do servidor
        ThreadConexao thread = new ThreadConexao(servidor, in, pw);
        new Thread(thread).start();
    }
    
    public static void setUsuario(UsuarioBean u) {
            usuario = u;
    }

    public static void enviaMensagem(String texto) {
        MensagemBean mensagem = new MensagemBean(TipoMensagem.MENSAGEM, usuario, texto);
        pw.print(mensagem.toJson() + "\n");
        pw.flush();
    }

    public static UsuarioBean getUsuario() {
        return usuario;
    }

}
