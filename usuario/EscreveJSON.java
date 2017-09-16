/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuario;

import java.time.LocalDate;
import java.time.Month;
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
import org.json.JSONObject;
import pessoa.bean.PessoaBean;
import usuario.bean.UsuarioBean;

/**
 *
 * @author vlagjuio
 */
public class EscreveJSON {
    
    public static void main(String[] args) {
        
        UsuarioBean u = new UsuarioBean();
        u.setLogin("juniior.lima");
        
        PessoaBean p = new PessoaBean();
        p.setNome("Galvani");
        MensagemBean m = new MensagemBean(TipoMensagem.CADASTRA, u, p);
        
        System.out.println(m.toJson());
        
    }
    
}
