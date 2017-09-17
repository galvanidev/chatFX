/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mensagem.bean;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Arrays;
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
        if (this.tipo != null) {
            json.put("tipo", this.tipo);
        }
        if (this.mensagem != null) {
            json.put("mensagem", this.mensagem);
        }
        if (this.usuarios != null) {
            JSONArray array = new JSONArray(this.usuarios);
            json.put("usuarios", array);
        }
        if (this.usuario != null) {
            json.put("usuario", this.usuario.toJson());
        }
        if (this.pessoa != null) {
            json.put("pessoa", this.pessoa.toJson());
        }
        if (this.hora != null) {
            json.put("hora", this.hora.toString());
        }
        return json;
    }

    public static MensagemBean toObject(JSONObject json) {
        MensagemBean m = new MensagemBean();
        if (json.has("tipo")) {
            String tipo = json.getString("tipo");
            m.setTipo(TipoMensagem.valueOf(tipo));
        }
        if (json.has("mensagem")) {
            m.setMensagem(json.getString("mensagem"));
        }
        if (json.has("usuarios")) {
            JSONArray us = (JSONArray) json.get("usuarios");
            UsuarioBean[] usuarios = new UsuarioBean[us.length()];
            for (int i = 0; i < us.length(); i++) {
               usuarios[i] = UsuarioBean.toObject(us.getJSONObject(i));
            }
            m.setUsuarios(usuarios);
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
            m.setHora(LocalTime.parse((CharSequence) json.get("hora")));
        }
        return m;
    }

    public MensagemBean() {
    }

    public MensagemBean(TipoMensagem tipo) {
        this.tipo = tipo.name();
    }
    
    public MensagemBean(TipoMensagem tipo, UsuarioBean[] usuarios) {
        this(tipo);
        this.usuarios = usuarios;
    }

    public MensagemBean(TipoMensagem tipo, String mensagem) {
        this(tipo);
        this.mensagem = mensagem;
    }

    public MensagemBean(TipoMensagem tipo, UsuarioBean usuario) {
        this(tipo);
        this.usuario = usuario;
    }

    public MensagemBean(TipoMensagem tipo, UsuarioBean usuario, PessoaBean pessoa) {
        this(tipo, usuario);
        this.pessoa = pessoa;
    }

    public MensagemBean(TipoMensagem tipo, UsuarioBean usuario, String mensagem) {
        this(tipo, mensagem);
        this.usuario = usuario;
    }

    public MensagemBean(TipoMensagem tipo, UsuarioBean usuario, PessoaBean pessoa, String mensagem) {
        this(tipo, usuario, mensagem);
        this.pessoa = pessoa;
    }

    public TipoMensagem getTipo() {
        return TipoMensagem.valueOf(tipo);
    }

    public void setTipo(TipoMensagem tipo) {
        this.tipo = tipo.name();
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
