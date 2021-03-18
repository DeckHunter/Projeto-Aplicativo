package com.example.deckdeveloper.minhaacademia.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.deckdeveloper.minhaacademia.R;
import com.example.deckdeveloper.minhaacademia.config.ConfiguracaoFireBase;
import com.example.deckdeveloper.minhaacademia.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText campoEmail;
    private TextInputEditText campoSenha;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //getSupportActionBar().hide();

        autenticacao = ConfiguracaoFireBase.getFireBaseAutenticacao();
        campoEmail = findViewById(R.id.editEmail);
        campoSenha = findViewById(R.id.editSenha);

    }
    public void LogarUsuario(Usuario usuario){

        autenticacao.signInWithEmailAndPassword(
                usuario.getEmail(),usuario.getSenha()

        ).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    abrirTelaPrincipal();
                }else{
                    String excecao = "";

                    try {
                        throw task.getException();
                    }catch (FirebaseAuthInvalidUserException e){
                        excecao = "Usuario não está cadastrado";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        excecao = "E-mail ou senha não correspoende a um usuário cadastrado";
                    }catch (Exception e) {
                        excecao = "Erro ao logar usuário: "+e.getMessage();
                        e.printStackTrace();
                    }

                    Toast.makeText(LoginActivity.this,
                            excecao,
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void abrirTelaCadastro(View view){
        Intent intent = new Intent(LoginActivity.this,CadastroActivity.class);
        startActivity(intent);
    }
    public void ValidarCadastroDoUsuario(View view){

        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if(!email.isEmpty()){
            if(!senha.isEmpty()){
                Usuario usuario = new Usuario();
                usuario.setEmail(email);
                usuario.setSenha(senha);

                LogarUsuario(usuario);

            }else{
                Toast.makeText(this,"Preencha Todos Os Campos",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(this,"Preencha Todos Os Campos",Toast.LENGTH_LONG).show();
        }
    }
    public void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser usuarioAtual = autenticacao.getCurrentUser();
        if(usuarioAtual != null){
            abrirTelaPrincipal();
        }
    }
}

