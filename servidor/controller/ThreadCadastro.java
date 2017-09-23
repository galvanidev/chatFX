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

/**
 *
 * @author vlagjuio
 */
public class ThreadCadastro {

    public MensagemBean mensagem = new MensagemBean();
    private PessoaBean retornoP = null;
    private UsuarioBean retornoU = null;
    private PessoaDAO pdao = new PessoaDAO();
    private UsuarioDAO udao = new UsuarioDAO();

    public ThreadCadastro(PessoaBean pessoa, UsuarioBean usuario) {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<MensagemBean> futureResult = executor.submit(() -> {

                boolean pessoaExiste = pdao.existe(pessoa);
                boolean usuarioExiste = udao.existe(usuario);

                if (!pessoaExiste && !usuarioExiste) {
                    retornoP = (PessoaBean) pdao.inserir(pessoa);
                    usuario.setPessoa(retornoP);
                    retornoU = (UsuarioBean) udao.inserir(usuario);
                    mensagem.setTipo(TipoMensagem.SUCESSO);
                    mensagem.setMensagem("Usuário cadastrado com sucesso");
                    return mensagem;
                } else {
                    if (pessoaExiste) {
                        mensagem.setTipo(TipoMensagem.ERRO);
                        mensagem.setMensagem("_Esta pessoa já possui cadastro_");
                    }
                    if (usuarioExiste) {
                        mensagem.setTipo(TipoMensagem.ERRO);
                        if (mensagem.getMensagem() == null) {
                            mensagem.setMensagem("_Usuário indisponível_");
                        } else {
                            mensagem.setMensagem(mensagem.getMensagem() + "_Usuário indisponível_");
                        }
                    }
                }
                return mensagem;
            });
            this.mensagem = futureResult.get();
            executor.shutdown();
        } catch (InterruptedException | ExecutionException ex) {
            mensagem = new MensagemBean(TipoMensagem.ERRO, "Favor tentar novamente mais tarde");
            ex.printStackTrace();
        }
    }
}
