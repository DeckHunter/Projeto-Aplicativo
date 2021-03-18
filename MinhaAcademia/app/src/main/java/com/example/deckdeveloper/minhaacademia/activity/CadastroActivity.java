package com.example.deckdeveloper.minhaacademia.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.deckdeveloper.minhaacademia.R;
import com.example.deckdeveloper.minhaacademia.config.ConfiguracaoFireBase;
import com.example.deckdeveloper.minhaacademia.helper.Base64Custom;
import com.example.deckdeveloper.minhaacademia.helper.UsuarioFireBase;
import com.example.deckdeveloper.minhaacademia.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.ExecutionException;

public class CadastroActivity extends AppCompatActivity {

    private TextInputEditText campoNome;
    private TextInputEditText campoEmail;
    private TextInputEditText campoSenha;

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        //getSupportActionBar().hide();

        campoNome = findViewById(R.id.textNome);
        campoEmail = findViewById(R.id.textEmail);
        campoSenha = findViewById(R.id.textSenha);
    }
    public void SalvarUsuarioFireBase(final Usuario usuario){
        autenticacao = ConfiguracaoFireBase.getFireBaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(

            usuario.getEmail(),usuario.getSenha()

        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Toast.makeText(CadastroActivity.this,"Sucesso ao cadastrar usuário",Toast.LENGTH_LONG).show();
                    UsuarioFireBase.AtualizarNomeUsuario(usuario.getNome());
                    abrirTelaPrincipal();

                    try {
                        String identificadorUsuario = Base64Custom.CodificarBase64(usuario.getEmail());
                        usuario.setIdUsuario(identificadorUsuario);
                        usuario.Salvar();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    String execao = "";
                    try {
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        execao = "digite um senha mais forte";
                    }catch (FirebaseAuthInvalidCredentialsException e){
                        execao = "por favor, digite um email valido";
                    }catch (FirebaseAuthUserCollisionException e){
                        execao = "Essa conta já foi regsitrada";
                    }catch (Exception e){
                        execao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this,execao,Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void ValidarCadastroDoUsuario(View view){

        String nome = campoNome.getText().toString();
        String email = campoEmail.getText().toString();
        String senha = campoSenha.getText().toString();

        if(!nome.isEmpty()){
            if(!email.isEmpty()){
                if(!senha.isEmpty()){

                    Usuario usuario = new Usuario();
                    usuario.setEmail(email);
                    usuario.setNome(nome);
                    usuario.setSenha(senha);

                    SalvarUsuarioFireBase(usuario);
                }else{
                    Toast.makeText(CadastroActivity.this,"Preencha Todos Os Campos",Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(CadastroActivity.this,"Preencha Todos Os Campos",Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(CadastroActivity.this,"Preencha Todos Os Campos",Toast.LENGTH_LONG).show();
        }
    }
    public void abrirTelaPrincipal(){
        Intent intent = new Intent(CadastroActivity.this,MainActivity.class);
        startActivity(intent);
    }
}
