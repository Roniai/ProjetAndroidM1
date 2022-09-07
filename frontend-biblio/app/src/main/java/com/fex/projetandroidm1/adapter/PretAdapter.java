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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PretAdapter extends RecyclerView.Adapter<PretAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Pret> pret;

    private Dialog dialog;
    private String url = "http://10.0.2.2:8000/api/prets";

    public PretAdapter(Context context, ArrayList<Pret> pret) {
        this.context = context;
        this.pret = pret;
    }

    @NonNull
    @Override
    public PretAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.pret_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PretAdapter.MyViewHolder holder, int position) {
        holder.numlecteur.setText(pret.get(position).getNumlecteur());
        holder.numlivre.setText(pret.get(position).getNumlivre());
        holder.datepret.setText(pret.get(position).getDatepret());
        holder.editPret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numlecteur = pret.get(position).getNumlecteur();
                String numlivre = pret.get(position).getNumlivre();
                String datepret = pret.get(position).getDatepret();
                editPret(numlecteur, numlivre, datepret);
            }
        });
        holder.deletePret.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numlecteur = pret.get(position).getNumlecteur();
                String numlivre = pret.get(position).getNumlivre();
                deletePret(numlecteur, numlivre);
            }
        });
    }

    /*UPDATE - PUT*/
    private void editPret(String numlec, String numliv, String date){
        TextView close, action;
        EditText numlecteur, numlivre, datepret;
        Button submit;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_formspret);

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
        numlivre = (EditText) dialog.findViewById(R.id.numlivreEdit);
        datepret = (EditText) dialog.findViewById(R.id.datepretEdit);

        /*Ancienne valeur*/
        numlecteur.setText(numlec);
        numlivre.setText(numliv);
        datepret.setText(date);

        submit = (Button) dialog.findViewById(R.id.submit);
        submit.setText("Modifier");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Nouvelle valeur*/
                String numlecteur_val = numlecteur.getText().toString();
                String numlivre_val = numlivre.getText().toString();
                String datepret_val = datepret.getText().toString();
                editSubmit(numlec, numliv, numlecteur_val, numlivre_val, datepret_val);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void editSubmit(String numlecteur_old, String numlivre_old, String numlecteur, String numlivre, String datepret){
        /*GET PARAMS*/
        try{
            JSONObject parameters = new JSONObject();
            parameters.put("numlecteur", numlecteur);
            parameters.put("numlivre", numlivre);
            parameters.put("datepret", datepret);

            /*UPDATE PARAMS*/
            /*url+"/numlecteur="+numlecteur_old+";numlivre="+numlivre_old*/
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                    url+"/numlecteur%3D"+numlecteur_old+"%3Bnumlivre%3D"+numlivre_old,
                    parameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();

                            Toast.makeText(context, "L'ancien prêt est mis à jour", Toast.LENGTH_LONG).show();
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
    private void deletePret(String numlecteur, String numlivre){
        TextView close, action, msg;
        Button submit, dismiss;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_delete);

        /*Message*/
        msg = (TextView) dialog.findViewById(R.id.deleteMsg);
        msg.setText("Voulez vous supprimer ce prêt?");

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
                deleteSubmit(numlecteur, numlivre);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void deleteSubmit(String numlecteur, String numlivre){
        StringRequest request = new StringRequest(Request.Method.DELETE,
                url+"/numlecteur%3D"+numlecteur+"%3Bnumlivre%3D"+numlivre,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        dialog.dismiss();

                        Toast.makeText(context, "Le pret est supprimé", Toast.LENGTH_LONG).show();
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
        return pret.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView numlecteur, numlivre, datepret;
        private ImageView editPret, deletePret;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numlecteur = (TextView) itemView.findViewById(R.id.numlecteur);
            numlivre = (TextView) itemView.findViewById(R.id.numlivre);
            datepret = (TextView) itemView.findViewById(R.id.datepret);
            editPret = (ImageView) itemView.findViewById(R.id.editPret);
            deletePret = (ImageView) itemView.findViewById(R.id.deletePret);
        }
    }
}
