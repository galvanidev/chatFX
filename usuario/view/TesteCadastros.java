/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuario.view;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.util.PSQLException;
import pessoa.bean.PessoaBean;
import pessoa.dao.PessoaDAO;
import servidor.controller.ThreadCadastro;
import servidor.controller.ThreadLogin;
import usuario.bean.UsuarioBean;
import usuario.dao.UsuarioDAO;
import util.DaoException;

/**
 *
 * @author vlagjuio
 */
public class TesteCadastros {

    public static void main(String[] args) {
        
        // Erro unique contraints 0 sql state = 23505
        // Erro 29 é sobre not null
        /*
        PessoaBean p = new PessoaBean();
        p.setNome("teste 1");
        p.setCpf("1234");
        p.setDataNascimento(LocalDate.of(1993, 03, 11));
        p.setSexo('M');

        // existir os dois e não funcionar
        // existir pessoa e não funcionar
        // existir o usuário e não funcionar

        PessoaBean u = new PessoaBean();
        u.setUsuario("galvani2");
        u.setEmail("asdasd.da");
        u.setSenha("asdasdas");
        u.setPessoa(p);
        
        /*
        PessoaDAO pdao = new PessoaDAO();
        pdao.inserir(p);
        
        UsuarioDAO udao = new UsuarioDAO();
        udao.inserir(u);
        
        
        ThreadCadastro tc = new ThreadCadastro(p, u);
        Object[] obj = tc.array;
        System.out.println(obj);
        */
    }

}
