package com.example.deckdeveloper.minhaacademia.model;

import com.example.deckdeveloper.minhaacademia.config.ConfiguracaoFireBase;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class Academia {

    private String IdAcademia;
    private String instutores;
    private String textNome;
    private String textDias;
    private String textHorario ;
    private String textDescricao;
    private String textLocal;
    private List<String> fotos;

    public Academia() {
        DatabaseReference academiaRef = ConfiguracaoFireBase.getFireBaseDataBase()
                .child("minhas academias");
        setIdAcademia(academiaRef.push().getKey());
    }

    public void salvar(){

        String idUsuario = ConfiguracaoFireBase.getIdUsuario();
        DatabaseReference academiaRef = ConfiguracaoFireBase.getFireBaseDataBase()
                .child("minhas academias");
        academiaRef.child(idUsuario)
                .child(getIdAcademia())
                .setValue(this);
        salvarpublico();
    }
    public void salvarpublico(){

        DatabaseReference academiaRef = ConfiguracaoFireBase.getFireBaseDataBase()
                .child("academias");
        academiaRef.child(getTextNome())
                .child(getIdAcademia())
                .setValue(this);
    }

    public String getIdAcademia() {
        return IdAcademia;
    }

    public void setIdAcademia(String idAcademia) {
        IdAcademia = idAcademia;
    }

    public String getInstutores() {
        return instutores;
    }

    public void setInstutores(String instutores) {
        this.instutores = instutores;
    }
    public String getTextNome() {
        return textNome;
    }

    public void setTextNome(String textNome) {
        this.textNome = textNome;
    }

    public String getTextDias() {
        return textDias;
    }

    public void setTextDias(String textDias) {
        this.textDias = textDias;
    }

    public String getTextHorario() {
        return textHorario;
    }

    public void setTextHorario(String textHorario) {
        this.textHorario = textHorario;
    }

    public String getTextDescricao() {
        return textDescricao;
    }

    public void setTextDescricao(String textDescricao) {
        this.textDescricao = textDescricao;
    }

    public String getTextLocal() {
        return textLocal;
    }

    public void setTextLocal(String textLocal) {
        this.textLocal = textLocal;
    }

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }
}
