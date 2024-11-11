package com.servicos.cadastro_servicos.model;



public class AzureUser {
    private String id;
    private String displayName;
    private String mail;

    public AzureUser(String id, String displayName, String mail) {
        this.id = id;
        this.displayName = displayName;
        this.mail = mail;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMail() {
        return mail;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }
}
