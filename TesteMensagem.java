
import conexao.CadastroException;
import conexao.ConexaoController;
import conexao.LoginException;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
import java.util.Scanner;
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
        MensagemBean mensagem = new MensagemBean();
        UsuarioBean usuarios[];
        usuarios = new UsuarioBean[5];
        UsuarioBean usuario = new UsuarioBean();
        PessoaBean pessoa = new PessoaBean();
        pessoa.setId(7);
        pessoa.setCpf("712.091.182-52");
        pessoa.setNome("Galvani Júnior");
        pessoa.setDataNascimento(LocalDate.of(1993, 11, 3));
        pessoa.setSexo("M");
        usuario.setId(5);
        usuario.setLogin("gal2");
        usuario.setEmail("juniior.lima@gmail.com");
        usuario.setSenha("123");
        usuario.setPessoa(pessoa);
        for (int i = 0; i < usuarios.length; i++) {
            usuarios[i] = usuario;
        }
        
        try {
            ConexaoController.cadastra(usuario, pessoa);
        } catch(CadastroException ex) {
            System.out.println(ex);
        }
    }
}
