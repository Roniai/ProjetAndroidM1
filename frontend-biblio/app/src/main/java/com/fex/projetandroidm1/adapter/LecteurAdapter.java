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
import com.fex.projetandroidm1.model.Lecteur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LecteurAdapter extends RecyclerView.Adapter<LecteurAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Lecteur> lecteur;

    private Dialog dialog;
    private String url = "http://10.0.2.2:8000/api/lecteurs";

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
        holder.editLecteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = lecteur.get(position).getNumlecteur();
                String nom = lecteur.get(position).getNomlecteur();
                editLecteur(num, nom);
            }
        });
        holder.deleteLecteur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = lecteur.get(position).getNumlecteur();
                deleteLecteur(num);
            }
        });
    }

    /*UPDATE - PUT*/
    private void editLecteur(String num, String nom){
        TextView close, action;
        EditText numlecteur, nomlecteur;
        Button submit;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_formslec);

        action = (TextView) dialog.findViewById(R.id.action);
        action.setText("Modification");

        close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        numlecteur = (EditText) dialog.findViewById(R.id.numlecteurEdit);
        nomlecteur = (EditText) dialog.findViewById(R.id.nomlecteurEdit);

        /*Ancienne valeur*/
        numlecteur.setText(num);
        nomlecteur.setText(nom);

        submit = (Button) dialog.findViewById(R.id.submit);
        submit.setText("Modifier");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Nouvelle valeur*/
                String numlecteur_val = numlecteur.getText().toString();
                String nomlecteur_val = nomlecteur.getText().toString();
                editSubmit(num, numlecteur_val, nomlecteur_val);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void editSubmit(String num_old, String num, String nom){
        /*GET PARAMS*/
        try{
            JSONObject parameters = new JSONObject();
            parameters.put("numlecteur", num);
            parameters.put("nomlecteur", nom);

            /*UPDATE PARAMS*/
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url+"/"+num_old, parameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();

                            Toast.makeText(context, "L'ancien lecteur est mis à jour", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Une erreur s'est produite", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    });

            Volley.newRequestQueue(context).add(jsonObjectRequest);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    /*DELETE - DELETE*/
    private void deleteLecteur(String num){
        TextView close, action;
        Button submit, dismiss;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_deletelec);

        close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        /*Boutton Annuler*/
        dismiss = (Button) dialog.findViewById(R.id.dismiss);
        dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        /*Boutton Supprimer*/
        submit = (Button) dialog.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteSubmit(num);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void deleteSubmit(String num){
            StringRequest request = new StringRequest(Request.Method.DELETE, url+"/"+num,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            dialog.dismiss();

                            Toast.makeText(context, "Le lecteur est supprimé", Toast.LENGTH_LONG).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(context, "Une erreur s'est produite", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    });

            Volley.newRequestQueue(context).add(request);
    }


    @Override
    public int getItemCount() {
        return lecteur.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView numlecteur, nomlecteur;
        private ImageView editLecteur, deleteLecteur;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numlecteur = (TextView) itemView.findViewById(R.id.numlecteur);
            nomlecteur = (TextView) itemView.findViewById(R.id.nomlecteur);
            editLecteur = (ImageView) itemView.findViewById(R.id.editLecteur);
            deleteLecteur = (ImageView) itemView.findViewById(R.id.deleteLecteur);
        }
    }
}
