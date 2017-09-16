/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package usuario.bean;

import java.io.Serializable;
import java.util.Objects;
import org.json.JSONArray;
import org.json.JSONObject;
import pessoa.bean.PessoaBean;

/**
 *
 * @author Galvani Júnior
 */
public class UsuarioBean implements Serializable {

    private Integer id;
    private String login;
    private String email;
    private String senha;
    private PessoaBean pessoa;

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (this.id != null) {
            json.put("id", this.id);
        }
        if (this.login != null) {
            json.put("login", this.login);
        }
        if (this.senha != null) {
            json.put("senha", this.senha);
        }
        if (this.email != null) {
            json.put("email", this.email);
        }
        if (this.pessoa != null) {
            json.put("pessoa", this.pessoa.toJson());
        }
        return json;
    }

    public static UsuarioBean toObject(JSONObject json) {
        UsuarioBean u = new UsuarioBean();
        if (json.has("id")) {
            u.setId(json.getInt("id"));
        }
        if (json.has("login")) {
            u.setLogin(json.getString("login"));
        }
        if (json.has("senha")) {
            u.setSenha(json.getString("senha"));
        }
        if (json.has("email")) {
            u.setEmail(json.getString("email"));
        }
        if (json.has("pessoa")) {
            JSONObject p = (JSONObject) json.get("pessoa");
            u.setPessoa(PessoaBean.toObject(p));
        }
        return u;
    }

    public static UsuarioBean[] toArrayObject(JSONArray json) {
        UsuarioBean[] us = null;
        for (int i = 0; i < json.length(); i++) {
            JSONObject j = json.getJSONObject(i);
            us[i] = (UsuarioBean) j.get("usuario");
        }
        return us;
    }

    public UsuarioBean() {
    }
    
    public UsuarioBean(UsuarioBean trataUsuario) {
        this.id = trataUsuario.getId();
        this.login = trataUsuario.getLogin();
    }

    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the senha
     */
    public String getSenha() {
        return senha;
    }

    /**
     * @param senha the senha to set
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.login);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UsuarioBean other = (UsuarioBean) obj;
        return Objects.equals(this.login, other.login);
    }

    @Override
    public String toString() {
        return this.login;
    }

    /**
     * @return the pessoa
     */
    public PessoaBean getPessoa() {
        return pessoa;
    }

    /**
     * @param pessoa the pessoa to set
     */
    public void setPessoa(PessoaBean pessoa) {
        this.pessoa = pessoa;
    }

}
