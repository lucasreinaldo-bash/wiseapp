package de.djuelg.vostore.Firebase;

import com.google.firebase.database.DatabaseReference;

public class Usuario {

    private String nome;
    private String nomeCompleto;
    private String email;
    private String senha;
    private String contrasenha;
    private String id;
    private String fotoPerfilChat;
    private String token;

    public String getFotoPerfilChat() {
        return fotoPerfilChat;
    }

    public Usuario() {

    }



    public Usuario(String nome, String id, String nomeCompleto, String email, String senha, String contrasenha, String token, String fotoPerfilChat) {
        this.nome = nome;
        this.email = email;
        this.id = id;
        this.token = token;
        this.nomeCompleto = nomeCompleto;
        this.senha = senha;
        this.contrasenha = contrasenha;
        this.fotoPerfilChat = fotoPerfilChat;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public void setFotoPerfilChat(String fotoPerfilChat) {
        this.fotoPerfilChat = fotoPerfilChat;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public void saveUser() {
        DatabaseReference databaseReference = ConfiguracaoFirebase.getFirebase();
        databaseReference.child("users").child(getId()).setValue(this);
    }
}