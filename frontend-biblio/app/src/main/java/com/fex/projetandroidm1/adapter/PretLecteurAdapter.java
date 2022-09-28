package com.fex.projetandroidm1.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.fex.projetandroidm1.R;
import com.fex.projetandroidm1.model.Pret;
import com.fex.projetandroidm1.model.PretLecteur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PretLecteurAdapter extends RecyclerView.Adapter<PretLecteurAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<PretLecteur> pretLecteur;

    public PretLecteurAdapter(Context context, ArrayList<PretLecteur> pretLecteur) {
        this.context = context;
        this.pretLecteur = pretLecteur;
    }

    @NonNull
    @Override
    public PretLecteurAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.list_pret_lecteur, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PretLecteurAdapter.MyViewHolder holder, int position) {
        holder.designlivre.setText(pretLecteur.get(position).getDesignlivre());
        holder.autlivre.setText(pretLecteur.get(position).getAutlivre());
        holder.datepret.setText(pretLecteur.get(position).getDatepret());
    }

    @Override
    public int getItemCount() {
        return pretLecteur.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView designlivre, autlivre, datepret;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            designlivre = (TextView) itemView.findViewById(R.id.designlivre);
            autlivre = (TextView) itemView.findViewById(R.id.autlivre);
            datepret = (TextView) itemView.findViewById(R.id.datepret);
        }
    }
}

