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
import usuario.bean.UsuarioBean;
import usuario.dao.UsuarioDAO;

/**
 *
 * @author vlagjuio
 */
public class ThreadLogin {
    
    public MensagemBean mensagem;

    public ThreadLogin(UsuarioBean usuario) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<MensagemBean> futureResult = executor.submit(() -> {
                UsuarioDAO udao = new UsuarioDAO();
                UsuarioBean retorno = udao.logar(usuario.getLogin(), usuario.getSenha());
                if (retorno == null) {
                    return mensagem = new MensagemBean(TipoMensagem.ERRO, null, null, "Usuário e/ou senha inválido(s).");
                } else {
                    return mensagem = new MensagemBean(TipoMensagem.SUCESSO, retorno);
                }
            });
            mensagem = futureResult.get();
            executor.shutdown();
        } catch (InterruptedException | ExecutionException ex) {
            mensagem = new MensagemBean(TipoMensagem.ERRO, "Favor tentar novamente mais tarde");
        }
    }
}
