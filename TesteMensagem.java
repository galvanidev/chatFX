import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
import org.json.JSONObject;
import pessoa.bean.PessoaBean;
import usuario.bean.UsuarioBean;
import util.DateUtil;
import util.ValidadorCPF;

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

    public static void main(String[] args) throws ParseException {
        
        MensagemBean mensagem;
        UsuarioBean usuarios[];
        usuarios = new UsuarioBean[1];
        UsuarioBean usuario = new UsuarioBean();
        PessoaBean pessoa = new PessoaBean();
        pessoa.setId(7);
        pessoa.setCpf("712.091.182-52");
        pessoa.setNome("Galvani Júnior");
        pessoa.setDataNascimento(LocalDate.parse("1993-11-03"));
        pessoa.setSexo("M");
        usuario.setId(5);
        usuario.setLogin("gal2");
        usuario.setEmail("juniior.lima@gmail.com");
        usuario.setSenha("123");
        usuario.setPessoa(pessoa);
        //for (int i = 0; i < usuarios.length; i++) {
            usuarios[0] = usuario;
        //}
    }
}
