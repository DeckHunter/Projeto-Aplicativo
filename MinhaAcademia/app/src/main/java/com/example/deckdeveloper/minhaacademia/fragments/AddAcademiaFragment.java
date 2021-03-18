package com.example.deckdeveloper.minhaacademia.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.deckdeveloper.minhaacademia.R;
import com.example.deckdeveloper.minhaacademia.activity.CadastrarAcademiaActivity;
import com.example.deckdeveloper.minhaacademia.adapter.AdapterAcademias;
import com.example.deckdeveloper.minhaacademia.model.Academia;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class AddAcademiaFragment extends Fragment implements List<Academia> {

    private FloatingActionButton buttonAdd;
    private RecyclerView recyclerAcademias;
    private List<Academia> academias = new ArrayList<>();
    private AdapterAcademias adapterAcademias;

    public AddAcademiaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_add_academia, container, false);
        buttonAdd = view.findViewById(R.id.floatingActionButton);

        recyclerAcademias = view.findViewById(R.id.recyclerAcademias);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext().getApplicationContext(), CadastrarAcademiaActivity.class));
            }
        });

        //Configurar Recycler View
        recyclerAcademias.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerAcademias.setHasFixedSize(true);

        adapterAcademias = new AdapterAcademias(this,academias );
        recyclerAcademias.setAdapter(adapterAcademias);

        return view;
    }

}
