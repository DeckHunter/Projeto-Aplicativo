package com.example.deckdeveloper.minhaacademia.helper;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.deckdeveloper.minhaacademia.config.ConfiguracaoFireBase;
import com.example.deckdeveloper.minhaacademia.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UsuarioFireBase {
    public static String getIdUsuario(){
        FirebaseAuth usuario = ConfiguracaoFireBase.getFireBaseAutenticacao();
        String email = usuario.getCurrentUser().getEmail();
        String idUsusario = Base64Custom.CodificarBase64(email);

        return idUsusario;
    }
    public static FirebaseUser getUsuarioAtual(){
        FirebaseAuth usuario = ConfiguracaoFireBase.getFireBaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static boolean AtualizarNomeUsuario(String nome){
        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();
            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil","Erro Ao Atualizar Nome Do Perfil");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public static boolean AtualizarFotoUsuario(Uri url){
        try {
            FirebaseUser user = getUsuarioAtual();
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url).build();

            user.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(!task.isSuccessful()){
                        Log.d("Perfil","Erro Ao Atualizar Foto");
                    }
                }
            });
            return true;
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
    }
    public static Usuario getDadosUsuarioLogado(){

        FirebaseUser fireBaseUsuario = getUsuarioAtual();
        Usuario usuario = new Usuario();
        usuario.setEmail(fireBaseUsuario.getEmail());
        usuario.setNome(fireBaseUsuario.getDisplayName());

        if(fireBaseUsuario.getPhotoUrl() == null){
            usuario.setFoto("");
        }else{
            usuario.setFoto(fireBaseUsuario.getPhotoUrl().toString());
        }
        return usuario;
    }
}
