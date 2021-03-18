package com.example.deckdeveloper.minhaacademia.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.deckdeveloper.minhaacademia.R;
import com.example.deckdeveloper.minhaacademia.config.ConfiguracaoFireBase;
import com.example.deckdeveloper.minhaacademia.helper.Permissao;
import com.example.deckdeveloper.minhaacademia.model.Academia;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;

public class CadastrarAcademiaActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText descricao,nome,dias,horario,local;
    private Spinner qtdInstrutores;

    private ImageView imagem1,imagem2,imagem3;

    private String[] permissoes = new String[]{
            Manifest.permission.READ_EXTERNAL_STORAGE
    };
    private ArrayList<String> listaImagens = new ArrayList<>();
    private ArrayList<String> listaUrlFotos = new ArrayList<>();

    private Academia academia;
    private StorageReference storage;

    private android.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_academia);

        //Configuracoes Iniciais
        storage = ConfiguracaoFireBase.getFireBaseStorage();

        //Valiando Permissoes
        Permissao.ValidarPermicoes(permissoes,this,1);
        InicializarComponentes();
        CarregarDadosSpinner();
    }

    private void ExibirMenssagemError(String messagem){
        Toast.makeText(this,messagem,Toast.LENGTH_LONG).show();
    }

    public void SalvarAnuncioAcademia(){

        alertDialog = new SpotsDialog.Builder()
                .setContext(this)
                .setMessage("Salvando Academia")
                .setCancelable(false)
                .build();
        alertDialog.show();
        //Salvar Imagens No Storage
        for(int i = 0; i < listaImagens.size(); i++){
            String urlImagem = listaImagens.get(i);
            int tamanhoDaLista = listaImagens.size();
            SalvarFotosStorage(urlImagem,tamanhoDaLista,i);
        }
    }

    public void SalvarFotosStorage(String urlString, final int totalFotos, int  contador){

        //criando referencia ao storage
        final StorageReference imagemAcademia = storage.child("imagens")
                .child("academias")
                .child(academia.getIdAcademia())
                .child("imagem"+contador);
        //Fazer Upload Da Imagem
        UploadTask uploadTask = imagemAcademia.putFile(Uri.parse(urlString));
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                imagemAcademia.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        Uri url = task.getResult();
                        String urlConvertida = url.toString();
                        listaUrlFotos.add(urlConvertida);
                        if(totalFotos == listaUrlFotos.size()){
                            academia.setFotos(listaUrlFotos);
                            academia.salvar();
                            alertDialog.dismiss();
                            finish();
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                ExibirMenssagemError("Erro ao fazer Upload");
            }
        });
    }

    private Academia ConfigurarAcademia(){

        String instutores = qtdInstrutores.getSelectedItem().toString();
        String textNome = nome.getText().toString();
        String textDias = dias.getText().toString();
        String textHorario = horario.getText().toString();
        String textDescricao = descricao.getText().toString();
        String textLocal = local.getText().toString();

        academia = new Academia();
        academia.setInstutores(instutores);
        academia.setTextDias(textDias);
        academia.setTextDescricao(textDescricao);
        academia.setTextHorario(textHorario);
        academia.setTextNome(textNome);
        academia.setTextLocal(textLocal);

        return academia;
    }

    public void ValidarDadosAcademia(View view){

        ConfigurarAcademia();

            if(listaImagens.size() != 0){
                if(!academia.getInstutores().isEmpty()){
                    if(!academia.getTextNome().isEmpty()){
                        if(!academia.getTextDias().isEmpty()){
                            if(!academia.getTextHorario().isEmpty()){
                                if(!academia.getTextDescricao().isEmpty()){
                                    if(!academia.getTextLocal().isEmpty()){
                                        SalvarAnuncioAcademia();
                                    }else{
                                        ExibirMenssagemError("Adicione uma Localizacao");
                                    }
                                }else{
                                    ExibirMenssagemError("Adicione uma Descrição");
                                }
                            }else{
                                ExibirMenssagemError("Adicione os Horarios");
                            }
                        }else{
                            ExibirMenssagemError("Adicione os Dias");
                        }
                    }else{
                        ExibirMenssagemError("Adicione um Nome");
                    }
                }else{
                    ExibirMenssagemError("Adicione o numero de Instrutores");
                }
            }else{
                ExibirMenssagemError("Adicione uma Foto");
            }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageCadastro1:
                EscolherImagem(1);
                break;
            case R.id.imageCadastro2:
                EscolherImagem(2);
                break;
            case R.id.imageCadastro3:
                EscolherImagem(3);
                break;
        }
    }
    public void EscolherImagem(int requestCode){
        Intent i = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i,requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == Activity.RESULT_OK){
            Uri imagemSelecionada = data.getData();
            String caminhoImagem = imagemSelecionada.toString();

            if(requestCode == 1){
                imagem1.setImageURI(imagemSelecionada);
            }else if(requestCode == 2){
                imagem2.setImageURI(imagemSelecionada);
            }else if(requestCode == 3){
                imagem3.setImageURI(imagemSelecionada);
            }
            listaImagens.add(caminhoImagem);
        }
    }

    private void InicializarComponentes(){

        descricao = findViewById(R.id.editDescricao);
        nome = findViewById(R.id.editNome);
        dias = findViewById(R.id.editDia);
        horario = findViewById(R.id.editHorario);
        qtdInstrutores = findViewById(R.id.spinnerNumInstrutores);
        local = findViewById(R.id.editLocal);

        imagem1 = findViewById(R.id.imageCadastro1);
        imagem2 = findViewById(R.id.imageCadastro2);
        imagem3 = findViewById(R.id.imageCadastro3);

        imagem1.setOnClickListener(this);
        imagem2.setOnClickListener(this);
        imagem3.setOnClickListener(this);
    }
    private void CarregarDadosSpinner(){
        String[] listaInstrutores = new String[]{
            "Nenhum","1","2","3","3...+"
        };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                listaInstrutores
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        qtdInstrutores.setAdapter(adapter);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        for(int permissaoResultado : grantResults){
            if(permissaoResultado == PackageManager.PERMISSION_DENIED){
                AlertaValidacaoPermicao();
            }
        }
    }
    private void AlertaValidacaoPermicao(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissoes Negadas");
        builder.setMessage("Para utilizar o App é necessario aceitar as Permissões");
        builder.setCancelable(false);
        builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
