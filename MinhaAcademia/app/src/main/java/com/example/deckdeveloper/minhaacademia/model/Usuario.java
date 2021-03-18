package com.example.deckdeveloper.minhaacademia.model;

import com.example.deckdeveloper.minhaacademia.config.ConfiguracaoFireBase;
import com.example.deckdeveloper.minhaacademia.helper.UsuarioFireBase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class Usuario {
    private String nome;
    private String email;
    private String senha;
    private String idUsuario;
    private String foto;

    public Usuario() {
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public void Salvar(){
        DatabaseReference fireBaseRef = ConfiguracaoFireBase.getFireBaseDataBase();
        DatabaseReference usuarioRef = fireBaseRef.child("usuarios").child(getIdUsuario());
        usuarioRef.setValue(this);
    }

    public void atualizar(){
        String identificadorUsuario = UsuarioFireBase.getIdUsuario();
        DatabaseReference dataBase = ConfiguracaoFireBase.getFireBaseDataBase();

        Map<String,Object> valoresUsuario = converterParaMap();

        DatabaseReference usuarioRef = dataBase
                .child("usuarios")
                .child(identificadorUsuario);
        usuarioRef.updateChildren(valoresUsuario);
    }
    @Exclude
    public Map<String,Object> converterParaMap(){
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email",getEmail());
        usuarioMap.put("nome",getNome());
        usuarioMap.put("foto",getFoto());
        return usuarioMap;
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
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
    @Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }
}
