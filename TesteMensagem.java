
import conexao.CadastroException;
import conexao.ConexaoController;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;
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
 * @author vlagjuio
 */
public class TesteMensagem {
    public static void main(String[] args) {
        
        UsuarioBean u = new UsuarioBean();
        u.setLogin("galvani2");
        u.setEmail("juniior.lima@gmail.com");
        u.setSenha("123");
        
        PessoaBean p = new PessoaBean();
        p.setNome("galvani");
        p.setCpf("123");
        p.setDataNascimento(LocalDate.of(1993, 11, 3));
        p.setSexo("M");
        try {
            ConexaoController.cadastra(u, p);
        }catch(CadastroException ex) {
            System.out.println(Arrays.toString(ex.getMessage().split("_")));
        }
    }
}
