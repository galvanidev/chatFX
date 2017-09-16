package conexao;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import usuario.bean.UsuarioBean;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
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
    private static UsuarioBean usuario;
    private static Writer escrita;
    private static BufferedReader leitura;

    private static void fazConexao() {
        try {
            // Inicia troca de socket com servidor
            servidor = new Socket(ConfiguracaoServidor.getHost(), ConfiguracaoServidor.getPort());
            escrita = new BufferedWriter(new OutputStreamWriter(servidor.getOutputStream(), "UTF-8"));
            leitura = new BufferedReader(new InputStreamReader(servidor.getInputStream(), "UTF-8"));
        } catch (IOException ex) {
            throw new ConexaoException("Verifique sua conexão.");
        }
    }

    private static void fechaConexao() {
        try {
            escrita.close();
            leitura.close();
            servidor.close();
        } catch (IOException ex) {
            Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void cadastra(UsuarioBean usuario, PessoaBean pessoa) throws CadastroException {
        try {
            fazConexao();
            MensagemBean mensagem = new MensagemBean(TipoMensagem.CADASTRA, usuario, pessoa);
            usuario.setSenha(FrameWork.criptografar(usuario.getSenha()));
            JSONObject json = mensagem.toJson();
            escrita.write(json.toString() + "\n");
            escrita.flush();
            String linha = leitura.readLine();
            json = new JSONObject(linha);
            mensagem = MensagemBean.toObject(json);
            if (mensagem.getTipo() == TipoMensagem.SUCESSO) {
                throw new CadastroException(mensagem.getMensagem());
            }
            throw new CadastroException(mensagem.getMensagem());
        } catch (IOException ex) {
            Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fechaConexao();
        }
    }

    public static void altera(UsuarioBean usuario, PessoaBean pessoa) throws CadastroException {
        try {
            fazConexao();
            usuario.setPessoa(pessoa);
            MensagemBean mensagem = new MensagemBean(TipoMensagem.ATUALIZA_CADASTRO, usuario);
            JSONObject json = mensagem.toJson();
            escrita.write(json.toString() + "\n");
            escrita.flush();
            String linha = leitura.readLine();
            json = new JSONObject(linha);
            mensagem = MensagemBean.toObject(json);
            throw new CadastroException(mensagem.getMensagem());
        } catch (IOException ex) {
            Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fechaConexao();
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
            escrita.write(json.toString() + "\n");
            escrita.flush();
            
            String linha = leitura.readLine();
            
            json = new JSONObject(linha);
            mensagem = MensagemBean.toObject(json);
            
            if (mensagem.getTipo() == TipoMensagem.SUCESSO) {
                ConexaoController.usuario = mensagem.getUsuario();
                iniciaThread();
                return true;
            } else {
                fechaConexao();
                throw new LoginException(mensagem.getMensagem());
            }
        } catch (IOException ex) {
            fechaConexao();
            Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    public static void logout() {
        try {
            MensagemBean m = new MensagemBean(TipoMensagem.LOGOUT);
            if (servidor != null) {
                escrita.write(m.toJson().toString() + "\n");
                escrita.flush();
                fechaConexao();
            }
        } catch (IOException ex) {
            Logger.getLogger(ConexaoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void iniciaThread() {
        // Exibe mensagem de sucesso no terminal
        System.out.println("Autenticado no servidor: " + servidor.getInetAddress());
        // Cria uma thread para receber mensagens do servidor
        ThreadConexao thread = new ThreadConexao(leitura, usuario);
        new Thread(thread).start();
    }

    public static void enviaMensagem(String texto) throws IOException {
        MensagemBean mensagem = new MensagemBean(TipoMensagem.MENSAGEM, usuario, texto);
        escrita.write(mensagem.toJson().toString() + "\n");
        escrita.flush();
    }

}
