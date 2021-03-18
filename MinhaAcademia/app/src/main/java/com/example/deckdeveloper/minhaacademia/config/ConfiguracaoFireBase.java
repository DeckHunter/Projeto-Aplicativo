package com.example.deckdeveloper.minhaacademia.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ConfiguracaoFireBase {
     private static DatabaseReference database;
     private static FirebaseAuth auth;
     private static StorageReference storage;

     public static String getIdUsuario(){
        FirebaseAuth auth = getFireBaseAutenticacao();
        return auth.getCurrentUser().getUid();
     }

     //Retorna a Instacia do FireBaseDataBase
    public static DatabaseReference getFireBaseDataBase(){
        if(database == null){
            database = FirebaseDatabase.getInstance().getReference();
        }
        return database;
    }
    //Recuperar a Instancia do FireAuth
    public static FirebaseAuth getFireBaseAutenticacao(){
        if(auth == null) {
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
    //Recuperar a Instancia do FireBaseStorage
    public static StorageReference getFireBaseStorage(){
        if(storage == null){
            storage = FirebaseStorage.getInstance().getReference();
        }
        return storage;
    }
}
