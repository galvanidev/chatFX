/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pessoa.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import pessoa.bean.PessoaBean;
import usuario.bean.UsuarioBean;
import util.BaseDAO;
import util.ConexaoPostgreSql;
import util.DaoException;

/**
 *
 * @author Galvani Júnior
 */
public class PessoaDAO implements BaseDAO {

    private Connection con;
    private PreparedStatement pst;
    private ResultSet rs;

    @Override
    public Object inserir(Object obj) throws DaoException {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "INSERT INTO pessoa(nome, cpf, data_nascimento, sexo) VALUES(?, ?, ?, ?) "
                    + "RETURNING id";
            pst = con.prepareStatement(sql);
            PessoaBean pessoa = (PessoaBean) obj;
            pst.setString(1, pessoa.getNome());
            pst.setString(2, pessoa.getCpf());
            pst.setObject(3, pessoa.getDataNascimento());
            pst.setString(4, String.valueOf(pessoa.getSexo()));
            rs = pst.executeQuery();
            while (rs.next()) {
                pessoa.setId(rs.getInt("id"));
            }
            return pessoa;
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
            PessoaBean pessoa = (PessoaBean) obj;
            String sql = "UPDATE pessoa SET nome = ?, cpf = ?, data_nascimento = ?, sexo = ?  WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setString(1, pessoa.getNome());
            pst.setString(2, pessoa.getCpf());
            pst.setObject(3, pessoa.getDataNascimento());
            pst.setString(4, String.valueOf(pessoa.getSexo()));
            pst.setInt(5, pessoa.getId());
            pst.execute();
            return pessoa;
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
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
            String sql = "DELETE FROM pessoa WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, codigo);
            pst.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(con);
        }
        return false;
    }

    @Override
    public Collection listar() throws DaoException {
        String sql = "SELECT * FROM pessoa";
        ArrayList<PessoaBean> pessoas = new ArrayList<>();
        PessoaBean pessoa;
        try {
            con = ConexaoPostgreSql.getConexao();
            pst = con.prepareStatement(sql);
            rs = pst.executeQuery();
            while (rs.next()) {
                pessoa = new PessoaBean();
                pessoa.setId(rs.getInt(1));
                pessoa.setNome(rs.getString("nome"));
                pessoas.add(pessoa);
            }
            return pessoas;
        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public Integer selecionar(Object obj) throws DaoException {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "SELECT * FROM pessoa WHERE cpf = ?";
            pst = con.prepareStatement(sql);
            PessoaBean pessoa = (PessoaBean) obj;
            pst.setString(1, pessoa.getCpf());
            rs = pst.executeQuery();
            if (rs.next()) {
                pessoa.setId(rs.getInt(1));
                return pessoa.getId();
            }

        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(rs);
            ConexaoPostgreSql.fechar(con);
        }
        return null;
    }

    public boolean existe(Object obj) throws DaoException {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "SELECT id FROM pessoa WHERE cpf = ?";
            pst = con.prepareStatement(sql);
            PessoaBean pessoa = (PessoaBean) obj;
            pst.setString(1, pessoa.getCpf());
            rs = pst.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(PessoaBean.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(rs);
            ConexaoPostgreSql.fechar(con);
        }
        return false;
    }

    @Override
    public Object selecionar(Integer codigo) throws DaoException {
        try {
            con = ConexaoPostgreSql.getConexao();
            String sql = "SELECT * FROM pessoa WHERE id = ?";
            pst = con.prepareStatement(sql);
            pst.setInt(1, codigo);
            rs = pst.executeQuery();
            if (rs.next()) {
                PessoaBean pessoa = new PessoaBean();
                pessoa.setId(rs.getInt(1));
                pessoa.setNome(rs.getString("nome"));
                pessoa.setDataNascimento(LocalDate.parse(rs.getString("data_nascimento")));
                pessoa.setSexo(rs.getString("sexo"));
                pessoa.setCpf(rs.getString("cpf"));
                return pessoa;
            }

        } catch (SQLException ex) {
            Logger.getLogger(PessoaDAO.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            ConexaoPostgreSql.fechar(pst);
            ConexaoPostgreSql.fechar(rs);
            ConexaoPostgreSql.fechar(con);
        }
        return null;
    }

}
