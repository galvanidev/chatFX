/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensagem.bean;

import java.io.Serializable;
import java.time.LocalTime;
import org.json.JSONArray;
import org.json.JSONObject;
import pessoa.bean.PessoaBean;
import usuario.bean.UsuarioBean;

/**
 *
 * @author Galvani Júnior
 */
public class MensagemBean implements Serializable {

    private String tipo;
    private String mensagem;
    private UsuarioBean usuario;
    private UsuarioBean[] usuarios;
    private PessoaBean pessoa;
    private LocalTime hora;

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (this.tipo != null)
            json.put("tipo", this.tipo);
        if (this.mensagem != null)
            json.put("mensagem", this.mensagem);
        if (this.usuarios != null)
            json.put("usuarios", this.usuarios);
        if (this.usuario != null)
            json.put("usuario", this.usuario.toJson());
        if (this.pessoa != null)
            json.put("pessoa", this.pessoa.toJson());
        if (this.hora != null)
            json.put("hora", this.hora);
        return json;
    }

    public static MensagemBean toObject(JSONObject json) {
        MensagemBean m = new MensagemBean();
        if (json.has("tipo"))
            m.setTipo(json.getString("tipo"));
        if (json.has("mensagem")) 
            m.setMensagem(json.getString("mensagem"));
        if (json.has("usuarios")) {
            JSONArray us = (JSONArray) json.get("usuarios");
            m.setUsuarios(UsuarioBean.toArrayObject(us));
        }
        if (json.has("usuario")) {
            JSONObject u = (JSONObject) json.get("usuario");
            m.setUsuario(UsuarioBean.toObject(u));
        }
        if (json.has("pessoa")) {
            JSONObject p = (JSONObject) json.get("pessoa");
            m.setPessoa(PessoaBean.toObject(p));
        }
        if (json.has("hora")) {
            m.setHora((LocalTime) json.get("hora"));
        }
        return m;
    }

    public MensagemBean() {
    }

    private MensagemBean(JSONObject json) {
        this.tipo = json.getString("tipo");
        this.mensagem = json.getString("mensagem");
        this.usuario = (UsuarioBean) json.get("usuario");
        this.usuarios = (UsuarioBean[]) json.get("usuarios");
        this.pessoa = (PessoaBean) json.get("pessoa");
        this.hora = (LocalTime) json.get("hora");
    }
    
    public MensagemBean(String tipo) {
        this.tipo = tipo;
    }    
    
    public MensagemBean(String tipo, UsuarioBean usuario) {
        this(tipo);
        this.usuario = usuario;
    }

    public MensagemBean(String tipo, UsuarioBean usuario, String mensagem) {
        this(tipo);
        this.usuario = usuario;
        this.mensagem = mensagem;
    }

    public MensagemBean(String tipo, UsuarioBean usuario, PessoaBean pessoa) {
        this(tipo, usuario);
        this.pessoa = pessoa;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }

    public UsuarioBean getUsuario() {
        return usuario;
    }

    public void setUsuario(UsuarioBean usuario) {
        this.usuario = usuario;
    }

    public UsuarioBean[] getUsuarios() {
        return usuarios;
    }

    public void setUsuarios(UsuarioBean[] usuarios) {
        this.usuarios = usuarios;
    }

    public PessoaBean getPessoa() {
        return pessoa;
    }

    public void setPessoa(PessoaBean pessoa) {
        this.pessoa = pessoa;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }
    
    

}