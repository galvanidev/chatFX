
import conexao.CadastroException;
import conexao.ConexaoController;
import conexao.LoginException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
import org.json.JSONArray;
import org.json.JSONObject;
import pessoa.bean.PessoaBean;
import usuario.bean.UsuarioBean;
import util.FrameWork;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author vlagjuio
 */
public class TesteMensagem {

    public static void main(String[] args) {

        UsuarioBean u = new UsuarioBean();
        u.setId(48);
        u.setLogin("juniiorlima");
        u.setEmail("galjx@gmail.com");
        u.setSenha(FrameWork.criptografar("123"));

        PessoaBean p = new PessoaBean();
        p.setId(113);
        p.setNome("gal");
        p.setCpf("000-000-000-00");
        p.setDataNascimento(LocalDate.of(1993, 11, 3));
        p.setSexo("M");

        try {
            ConexaoController.login("juniiorlima", "123");
        } catch (LoginException ex) {
            Logger.getLogger(TesteMensagem.class.getName()).log(Level.SEVERE, null, ex);

        }

        /*
        UsuarioBean[] usuarios;
        usuarios = new UsuarioBean[2];
        usuarios[0] = new UsuarioBean();
        usuarios[0].setLogin("teste 1");
        usuarios[1] = new UsuarioBean();
        usuarios[1].setLogin("teste 2");
        MensagemBean mensagem = new MensagemBean();
        mensagem.setUsuarios(usuarios);
        JSONObject obj = mensagem.toJson();
       
        MensagemBean.toObject(obj);*/
    }
}
