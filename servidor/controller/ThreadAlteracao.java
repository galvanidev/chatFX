/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.controller;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import mensagem.bean.MensagemBean;
import mensagem.bean.TipoMensagem;
import pessoa.bean.PessoaBean;
import pessoa.dao.PessoaDAO;
import usuario.bean.UsuarioBean;
import usuario.dao.UsuarioDAO;
import util.DaoException;

/**
 *
 * @author vlagjuio
 */
public class ThreadAlteracao {
    
    public MensagemBean mensagem = new MensagemBean();
    private PessoaDAO pdao = new PessoaDAO();
    private UsuarioDAO udao = new UsuarioDAO();

    public ThreadAlteracao(PessoaBean pessoa, UsuarioBean usuario) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<MensagemBean> futureResult = executor.submit(() -> {
                mensagem.setPessoa((PessoaBean) pdao.alterar(pessoa));
                mensagem.setUsuario((UsuarioBean) udao.alterar(usuario));
                mensagem.setTipo(TipoMensagem.SUCESSO);
                return mensagem;
            });
            this.mensagem = futureResult.get();
            executor.shutdown();
        } catch (InterruptedException | ExecutionException ex) {
            mensagem = new MensagemBean(TipoMensagem.ERRO, "Favor tentar novamente mais tarde.");
        }
    }
    
}
