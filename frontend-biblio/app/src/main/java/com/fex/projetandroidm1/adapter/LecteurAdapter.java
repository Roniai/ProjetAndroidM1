package com.fex.projetandroidm1.adapter;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fex.projetandroidm1.model.Lecteur;

import java.util.ArrayList;

public class LecteurAdapter extends RecyclerView.Adapter<LecteurAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Lecteur> lecteur;
    private String url="";

    public LecteurAdapter(Context context, ArrayList<Lecteur> lecteur) {
        this.context = context;
        this.lecteur = lecteur;
    }

    @NonNull
    @Override
    public LecteurAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull LecteurAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
