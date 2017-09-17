/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuario.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import pessoa.bean.PessoaBean;
import pessoa.dao.PessoaDAO;
import usuario.bean.UsuarioBean;
import util.BaseDAO;
import util.ConexaoPostgreSql;
import util.DaoException;

/**
 *
 * @author Galvani Júnior
 */
public class UsuarioDAO implements BaseDAO {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public Object inserir(Object obj) throws DaoException {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "INSERT INTO usuario(login, email, senha, pessoa_id) VALUES(?, ?, ?, ?)";
            pst = con.prepareStatement(sql);
            UsuarioBean usuario = (UsuarioBean) obj;
            pst.setString(1, usuario.getLogin());
            pst.setString(2, usuario.getEmail());
            pst.setString(3, usuario.getSenha());
            pst.setInt(4, usuario.getPessoa().getId());
            pst.execute();
            return usuario;
        } catch (SQLException ex) {
            System.out.println(ex.getErrorCode());
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(con);
        }
        return null;
    }

    @Override
    public Object alterar(Object obj) throws DaoException {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "UPDATE usuario SET email = ?, senha = ? WHERE id = ?";
            pst = con.prepareStatement(sql);
            UsuarioBean usuario = (UsuarioBean) obj;
            pst.setString(1, usuario.getEmail());
            pst.setString(2, usuario.getSenha());
            pst.setInt(3, usuario.getId());
            pst.execute();
            return usuario;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(con);
        }
        return null;
    }

    @Override
    public Boolean excluir(Integer codigo) throws DaoException {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "DELETE FROM usuario WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, codigo);
            pst.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(con);
        }
        return false;
    }

    @Override
    public Collection listar() throws DaoException {
        String sql = "SELECT * FROM usuario";
        ArrayList<UsuarioBean> usuarios = new ArrayList<>();
        UsuarioBean usuario;
        try {
            con = ConexaoPostgreSql.getConexao();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                usuario = new UsuarioBean();
                usuario.setId(rs.getInt(1));
                usuario.setLogin(rs.getString("login"));
                usuario.setEmail(rs.getString("email"));
                usuarios.add(usuario);
            }
            return usuarios;
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public Object selecionar(Integer codigo) throws DaoException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try {
            con = ConexaoPostgreSql.getConexao();
            pst = con.prepareStatement(sql);
            pst.setInt(0, codigo);
            rs = pst.executeQuery();
            if (rs.next()) {
                UsuarioBean usuario = new UsuarioBean();
                usuario.setId(rs.getInt(1));
                usuario.setLogin(rs.getString("login"));
                usuario.setEmail(rs.getString("email"));
                Integer idPessoa = (rs.getInt("pessoa_id"));
                PessoaDAO pdao = new PessoaDAO();
                usuario.setPessoa((PessoaBean) pdao.selecionar(idPessoa));
                return usuario;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(rs);
            ConexaoPostgreSql.fechar(con);
        }
        return null;
    }

    public UsuarioBean logar(String usuario, String senha) throws DaoException {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "SELECT * FROM usuario WHERE login = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, usuario);
            rs = pst.executeQuery();
            if (rs.next()) {
                if (senha.equals(rs.getString(4))) {
                    UsuarioBean u = new UsuarioBean();
                    u.setId(rs.getInt("id"));
                    u.setLogin(rs.getString("login"));
                    u.setEmail(rs.getString("email"));
                    int idPessoa = rs.getInt("pessoa_id");
                    PessoaDAO pdao = new PessoaDAO();
                    u.setPessoa((PessoaBean) pdao.selecionar(idPessoa));
                    return u;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(rs);
            ConexaoPostgreSql.fechar(con);
        }
        return null;
    }

    public boolean existe(Object obj) {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "SELECT id FROM usuario WHERE login like ?";
            pst = con.prepareStatement(sql);
            UsuarioBean usuario = (UsuarioBean) obj;
            pst.setString(1, usuario.getLogin());
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(UsuarioDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(rs);
            ConexaoPostgreSql.fechar(con);
        }
        return false;
    }

}
