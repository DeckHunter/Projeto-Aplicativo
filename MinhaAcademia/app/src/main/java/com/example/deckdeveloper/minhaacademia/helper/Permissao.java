package com.example.deckdeveloper.minhaacademia.helper;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class Permissao {
    public static boolean ValidarPermicoes(String[] permissoes, Activity activity, int requestCode){

        if(Build.VERSION.SDK_INT >= 23){
            List<String> listaDePermissoes = new ArrayList<>();

            for(String permissao : permissoes){
                Boolean temPermissao = ContextCompat.checkSelfPermission(activity,permissao) == PackageManager.PERMISSION_GRANTED;
                if(!temPermissao) listaDePermissoes.add(permissao);
            }

            if(listaDePermissoes.isEmpty()) return true;
            String[] novasPermissoes = new String[listaDePermissoes.size()];
            listaDePermissoes.toArray(novasPermissoes);

            ActivityCompat.requestPermissions(activity,novasPermissoes, requestCode);

        }

        return true;
    }
}
