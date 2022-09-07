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
import com.fex.projetandroidm1.model.Livre;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LivreAdapter extends RecyclerView.Adapter<LivreAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Livre> livre;

    private Dialog dialog;
    private String url = "http://10.0.2.2:8000/api/livres";

    public LivreAdapter(Context context, ArrayList<Livre> livre) {
        this.context = context;
        this.livre = livre;
    }

    @NonNull
    @Override
    public LivreAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        view = layoutInflater.inflate(R.layout.livre_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LivreAdapter.MyViewHolder holder, int position) {
        holder.numlivre.setText(livre.get(position).getNumlivre());
        holder.designlivre.setText(livre.get(position).getDesignlivre());
        holder.dispolivre.setText(livre.get(position).getDispolivre());
        holder.editLivre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = livre.get(position).getNumlivre();
                String design = livre.get(position).getDesignlivre();
                String dispo = livre.get(position).getDispolivre();

                /*GET{NUMLIVRE}*/
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url+"/"+num, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {
                                    Livre liv = new Livre();
                                    liv.setNumlivre(response.getString("numlivre"));
                                    liv.setDesignlivre(response.getString("designlivre"));
                                    liv.setAutlivre(response.getString("autlivre"));
                                    liv.setDateeditlivre(response.getString("dateeditlivre"));
                                    liv.setDispolivre(response.getString("dispolivre"));

                                    editLecteur(num, design, liv.getAutlivre(), liv.getDateeditlivre(), dispo);
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                error.printStackTrace();
                            }
                        });

                Volley.newRequestQueue(context).add(jsonObjectRequest);
            }
        });
        holder.deleteLivre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String num = livre.get(position).getNumlivre();
                deleteLecteur(num);
            }
        });
    }

    /*UPDATE - PUT*/
    private void editLecteur(String num, String design, String aut, String dateedit, String dispo){
        TextView close, action;
        EditText numlivre, designlivre, autlivre, dateeditlivre, dispolivre;
        Button submit;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_formslivre);

        action = (TextView) dialog.findViewById(R.id.action);
        action.setText("Modification");

        close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        numlivre = (EditText) dialog.findViewById(R.id.numlivreEdit);
        designlivre = (EditText) dialog.findViewById(R.id.designlivreEdit);
        autlivre = (EditText) dialog.findViewById(R.id.autlivreEdit);
        dateeditlivre = (EditText) dialog.findViewById(R.id.dateeditlivreEdit);
        dispolivre = (EditText) dialog.findViewById(R.id.dispolivreEdit);

        /*Ancienne valeur*/
        numlivre.setText(num);
        designlivre.setText(design);
        autlivre.setText(aut);
        dateeditlivre.setText(dateedit);
        dispolivre.setText(dispo);

        submit = (Button) dialog.findViewById(R.id.submit);
        submit.setText("Modifier");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Nouvelle valeur*/
                String numlivre_val = numlivre.getText().toString();
                String designlivre_val = designlivre.getText().toString();
                String autlivre_val = autlivre.getText().toString();
                String dateeditlivre_val = dateeditlivre.getText().toString();
                String dispolivre_val = dispolivre.getText().toString();
                editSubmit(num, numlivre_val, designlivre_val, autlivre_val, dateeditlivre_val, dispolivre_val);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void editSubmit(String num_old, String num, String design, String aut, String dateedit, String dispo){
        /*GET PARAMS*/
        try{
            JSONObject parameters = new JSONObject();
            parameters.put("numlivre", num);
            parameters.put("designlivre", design);
            parameters.put("autlivre", aut);
            parameters.put("dateeditlivre", dateedit);
            parameters.put("dispolivre", dispo);

            /*UPDATE PARAMS*/
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, url+"/"+num_old, parameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();

                            Toast.makeText(context, "L'ancien livre est mis à jour", Toast.LENGTH_LONG).show();
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
        TextView close, action, msg;
        Button submit, dismiss;

        dialog = new Dialog(context);

        dialog.setContentView(R.layout.dialog_delete);

        /*Message*/
        msg = (TextView) dialog.findViewById(R.id.deleteMsg);
        msg.setText("Voulez vous supprimer ce livre?");

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

                        Toast.makeText(context, "Le livre est supprimé", Toast.LENGTH_LONG).show();
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
        return livre.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        private TextView numlivre, designlivre, dispolivre;
        private ImageView editLivre, deleteLivre;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            numlivre = (TextView) itemView.findViewById(R.id.numlivre);
            designlivre = (TextView) itemView.findViewById(R.id.designlivre);
            dispolivre = (TextView) itemView.findViewById(R.id.dispolivre);
            editLivre = (ImageView) itemView.findViewById(R.id.editLivre);
            deleteLivre = (ImageView) itemView.findViewById(R.id.deleteLivre);
        }
    }
}
