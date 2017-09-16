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
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private PessoaBean retornoP = null;
    private UsuarioBean retornoU = null;
    private PessoaDAO pdao = new PessoaDAO();
    private UsuarioDAO udao = new UsuarioDAO();

    public ThreadAlteracao(PessoaBean pessoa, UsuarioBean usuario) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<MensagemBean> futureResult = executor.submit(() -> {

                retornoP = (PessoaBean) pdao.alterar(pessoa);
                retornoU = (UsuarioBean) udao.alterar(usuario);
                mensagem.setPessoa(retornoP);
                mensagem.setUsuario(retornoU);
                mensagem.setTipo(TipoMensagem.SUCESSO);
                mensagem.setMensagem("Cadastro atualizado");
                return mensagem;
            });
            mensagem = futureResult.get();
            executor.shutdown();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ThreadAlteracao.class.getName()).log(Level.SEVERE, null, ex);
            mensagem = new MensagemBean(TipoMensagem.ERRO, "Favor tenta novamente mais tarde");
        }
    }

}
