package com.example.deckdeveloper.minhaacademia.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.deckdeveloper.minhaacademia.R;
import com.example.deckdeveloper.minhaacademia.config.ConfiguracaoFireBase;
import com.example.deckdeveloper.minhaacademia.helper.Permissao;
import com.example.deckdeveloper.minhaacademia.helper.UsuarioFireBase;
import com.example.deckdeveloper.minhaacademia.model.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class ConfiguracoesActivity extends AppCompatActivity {

    public String[] permissoesNecessarias = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
    };

    private ImageButton imageButtonCamera,imageButtonGallery;
    private static final int SELECAO_CAMERA = 100;
    private static final int SELECAO_GALLERY = 200;

    private StorageReference storageReference;
    private String idUsuario;

    private CircleImageView circleImageViewPerfil;
    private EditText editPerfilNome;
    private ImageView imageAtualizarNome;

    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes);

        imageButtonCamera = findViewById(R.id.cameraButton);
        imageButtonGallery = findViewById(R.id.galleryButton);
        circleImageViewPerfil = findViewById(R.id.circleImageViewFotoPerfil);
        editPerfilNome = findViewById(R.id.editPerfilNome);
        imageAtualizarNome = findViewById(R.id.imageAtualizarNome);


        storageReference = ConfiguracaoFireBase.getFireBaseStorage();
        idUsuario = UsuarioFireBase.getIdUsuario();
        usuarioLogado = UsuarioFireBase.getDadosUsuarioLogado();

        //Validar Permissoes
        Permissao.ValidarPermicoes(permissoesNecessarias,this,1);

        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Meu Perfil");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Recuperar Dados Do Usuario
        FirebaseUser usuario = UsuarioFireBase.getUsuarioAtual();
        Uri url = usuario.getPhotoUrl();
        if(url != null){
            Glide.with(ConfiguracoesActivity.this)
                    .load(url)
                    .into(circleImageViewPerfil);

        }else{
            circleImageViewPerfil.setImageResource(R.drawable.padrao);
        }

        editPerfilNome.setText(usuario.getDisplayName());

        imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_CAMERA);
                }
            }
        });

        imageButtonGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if(i.resolveActivity(getPackageManager()) != null ){
                    startActivityForResult(i, SELECAO_GALLERY);
                }
            }
        });

        imageAtualizarNome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome = editPerfilNome.getText().toString();
                boolean retorno = UsuarioFireBase.AtualizarNomeUsuario(nome);
                if(retorno){

                    usuarioLogado.setNome(nome);
                    usuarioLogado.atualizar();

                    Toast.makeText(ConfiguracoesActivity.this,
                            "Nome alterado com sucesso",
                            Toast.LENGTH_LONG);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            Bitmap imagem = null;
            try {
                switch(requestCode){
                    case SELECAO_CAMERA:
                        imagem = (Bitmap) data.getExtras().get("data");
                        break;
                    case SELECAO_GALLERY:
                        Uri localDaImagemSelecionada = data.getData();
                        imagem = MediaStore.Images.Media.getBitmap(getContentResolver(),localDaImagemSelecionada);
                        break;
                }

                if(imagem != null){
                    circleImageViewPerfil.setImageBitmap(imagem);

                    //Recuperar Dados Da Imagem
                    ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                    imagem.compress(Bitmap.CompressFormat.JPEG,70,BAOS);
                    byte[] dadosDaImagem = BAOS.toByteArray();

                    //Salvar Imagem No FireBase
                    final StorageReference imagemRef = storageReference
                            .child("imagens")
                            .child("perfil")
                            .child(idUsuario +".jpeg");
                    UploadTask uploadTask = imagemRef.putBytes(dadosDaImagem);
                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ConfiguracoesActivity.this,"Erro ao adicionar imagem",Toast.LENGTH_LONG).show();
                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imagemRef.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    Uri url = task.getResult();
                                    AtualizarFotoUsuario(url);
                                }
                            });
                        }
                    });
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void AtualizarFotoUsuario(Uri url){
        UsuarioFireBase.AtualizarFotoUsuario(url);
        usuarioLogado.setFoto(url.toString());
        usuarioLogado.atualizar();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(int permissoaoResultado : grantResults){
            if(permissoaoResultado == PackageManager.PERMISSION_DENIED){
                AlertaValidacaoPermissao();
            }
        }
    }
    private void AlertaValidacaoPermissao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissões Negadas");
        builder.setMessage("Para utilizar o app é necessário aceitar as permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
    }
}
