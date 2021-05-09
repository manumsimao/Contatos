package com.ifsp.pdm.emanoela.contatos;

import java.io.Serializable;

public class Contato implements Serializable {
    public String nome;
    public String email;
    public String tipoTelefone;
    public String telefone;
    public boolean addCelular;
    public String celular;
    public String site;

    public Contato() {
    }

    public Contato(String nome, String email, String tipoTelefone, String telefone, boolean addCelular, String celular, String site) {
        this.nome = nome;
        this.email = email;
        this.tipoTelefone = tipoTelefone;
        this.telefone = telefone;
        this.addCelular = addCelular;
        this.celular = celular;
        this.site = site;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTipoTelefone() {
        return tipoTelefone;
    }

    public void setTipoTelefone(String tipoTelefone) {
        this.tipoTelefone = tipoTelefone;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean isAddCelular() {
        return addCelular;
    }

    public void setAddCelular(boolean addCelular) {
        this.addCelular = addCelular;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    @Override
    public String toString() {
        return "Contato{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", tipoTelefone='" + tipoTelefone + '\'' +
                ", telefone='" + telefone + '\'' +
                ", addCelular=" + addCelular +
                ", celular='" + celular + '\'' +
                ", site='" + site + '\'' +
                '}';
    }
}
