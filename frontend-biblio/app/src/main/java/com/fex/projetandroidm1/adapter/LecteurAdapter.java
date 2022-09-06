package com.fex.projetandroidm1.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fex.projetandroidm1.R;
import com.fex.projetandroidm1.model.Lecteur;

import java.util.ArrayList;

public class LecteurAdapter extends RecyclerView.Adapter<LecteurAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Lecteur> lecteur;

    public LecteurAdapter(Context context, ArrayList<Lecteur> lecteur) {
        this.context = context;
        this.lecteur = lecteur;
    }

    @NonNull
    @Override
    public LecteurAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.lecteur_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LecteurAdapter.MyViewHolder holder, int position) {
        holder.numlecteur.setText(lecteur.get(position).getNumlecteur());
        holder.nomlecteur.setText(lecteur.get(position).getNomlecteur());
    }

    @Override
    public int getItemCount() {
        return lecteur.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView numlecteur, nomlecteur;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numlecteur = (TextView) itemView.findViewById(R.id.numlecteur);
            nomlecteur = (TextView) itemView.findViewById(R.id.nomlecteur);
        }
    }
}
