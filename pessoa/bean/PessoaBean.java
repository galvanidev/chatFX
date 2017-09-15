/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pessoa.bean;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import org.json.JSONObject;

/**
 *
 * @author vlagjuio
 */
public class PessoaBean implements Serializable {
    
    private Integer id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String sexo;
    
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        if (this.id != null)
            json.put("id", this.id);
        if (this.nome != null)
            json.put("nome", this.nome);
        if (this.cpf != null)
            json.put("cpf", this.cpf);
        if (this.dataNascimento != null)
            json.put("data_nascimento", this.dataNascimento);
        if (this.sexo != null)
            json.put("sexo", this.sexo);
        return json;
    }
    
    public static PessoaBean toObject(JSONObject json) {
        PessoaBean p = new PessoaBean();
        if (json.has("id"))
            p.setId(json.getInt("id"));
        if (json.has("nome"))
            p.setNome(json.getString("nome"));
        if (json.has("cpf"))
            p.setCpf(json.getString("cpf"));
        if (json.has("data_nascimento")) {
            LocalDate d = LocalDate.parse((CharSequence) json.get("data_nascimento"));
            p.setDataNascimento(d);
        }
        if (json.has("sexo"))
            p.setSexo(json.getString("sexo"));
        return p;
    }
    
    public PessoaBean() { }

    public PessoaBean(JSONObject json) {
        this.id = json.getInt("id");
        this.nome = json.getString("nome");
        this.cpf = json.getString("cpf");
        this.dataNascimento = (LocalDate) json.get("data_nascimento");
        this.sexo = json.getString("sexo");
    }

    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    
    
}
