package com.example.deckdeveloper.minhaacademia.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.deckdeveloper.minhaacademia.R;
import com.example.deckdeveloper.minhaacademia.model.Academia;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AdapterAcademias extends RecyclerView.Adapter<AdapterAcademias.MyViewHolder> {

    private Context context;
    private List<Academia> academias;

    public AdapterAcademias(Context context, List<Academia> academias) {
        this.context = context;
        this.academias = academias;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_anuncio_academias,parent,false);
        return new MyViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Academia academia = academias.get(position);
        holder.titulo.setText(academia.getTextNome());
        holder.valor.setText("R$ 50.00");

        //Pegar primeira imagem da lista
        List<String> urlFotos = academia.getFotos();
        String urlCapa = urlFotos.get(0);

        Picasso.get().load(urlCapa).into(holder.foto);
    }

    @Override
    public int getItemCount() {
        return academias.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView titulo;
        TextView valor;
        ImageView foto;

        public MyViewHolder(View itemView){
            super(itemView);

            titulo = itemView.findViewById(R.id.textNome);
            valor = itemView.findViewById(R.id.textPreço);
            foto = itemView.findViewById(R.id.imageLogo);
        }
    }
}
