/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.controller;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.postgresql.util.PSQLException;
import usuario.bean.UsuarioBean;
import usuario.dao.UsuarioDAO;

/**
 *
 * @author vlagjuio
 */
public class ThreadLogin {

    public UsuarioBean usuario;

    public ThreadLogin(UsuarioBean usuario) throws PSQLException {
        try {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Future<UsuarioBean> futureResult = executor.submit(() -> {
                UsuarioDAO udao = new UsuarioDAO();
                UsuarioBean retorno = udao.logar(usuario.getLogin(), usuario.getSenha());
                return retorno;
            });
            this.usuario = futureResult.get();
            executor.shutdown();
        } catch (InterruptedException | ExecutionException ex) {
            Logger.getLogger(ThreadLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
