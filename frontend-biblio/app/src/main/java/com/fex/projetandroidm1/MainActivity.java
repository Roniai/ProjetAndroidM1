package com.fex.projetandroidm1;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fex.projetandroidm1.adapter.LecteurAdapter;
import com.fex.projetandroidm1.model.Lecteur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnRefreshListener {

    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private ArrayList<Lecteur> lecteur = new ArrayList<>();
    private JsonArrayRequest arrayRequest;
    private JsonObjectRequest jsonObjectRequest;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private LecteurAdapter lecteurAdapter;

    /*1.Activer WIFI dans l'Emulateur
    * 2.http://10.0.2.2:<port> => fait référence à http://localhost:<port> de votre machine*/
    private String url = "http://10.0.2.2:8000/api/lecteurs";

    /*Lancer dans un smartphone Android => Adresse IP WIFI : http://192.168.43.206/*/

    /*Pour régler le problème de TimeoutError*/
    public static int TIMEOUT_MS=15000; //15s

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
        recyclerView = (RecyclerView) findViewById(R.id.lecteur);

        dialog = new Dialog(this);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                lecteur.clear();
                getData();
            }
        });
    }

    /*READ - GET*/
    private void getData(){
        refresh.setRefreshing(true);

        arrayRequest = new JsonArrayRequest(url,
            new Response.Listener<JSONArray>(){
                @Override
                public void onResponse(JSONArray response) {

                    JSONObject jsonObject = null;
                    for(int i=0; i<response.length(); i++){
                        try {
                            jsonObject = response.getJSONObject(i);

                            Lecteur lec = new Lecteur();
                            lec.setNumlecteur(jsonObject.getString("numlecteur"));
                            lec.setNomlecteur(jsonObject.getString("nomlecteur"));
                            lecteur.add(lec);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                    adapterPush(lecteur);
                    refresh.setRefreshing(false);
                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

        //Spécifier la durée d'attente de réponse du serveur
        arrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        requestQueue = Volley.newRequestQueue(MainActivity.this);
        requestQueue.add(arrayRequest);
    }

    private void adapterPush(ArrayList<Lecteur> lecteur){
        lecteurAdapter = new LecteurAdapter(this, lecteur);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(lecteurAdapter);
    }

    /*ADD - POST*/
    public void addLecteur(View v){
        TextView close, action;
        EditText numlecteur, nomlecteur;
        Button submit;

        dialog.setContentView(R.layout.dialog_formslec);

        action = (TextView) dialog.findViewById(R.id.action);
        action.setText("Ajout");

        close = (TextView) dialog.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        numlecteur = (EditText) dialog.findViewById(R.id.numlecteurEdit);
        nomlecteur = (EditText) dialog.findViewById(R.id.nomlecteurEdit);

        submit = (Button) dialog.findViewById(R.id.submit);
        submit.setText("Ajouter");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numlecteur_val = numlecteur.getText().toString();
                String nomlecteur_val = nomlecteur.getText().toString();
                addSubmit(numlecteur_val, nomlecteur_val);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addSubmit(String num, String nom){
        /*GET PARAMS*/
        try{
            JSONObject parameters = new JSONObject();
            parameters.put("numlecteur", num);
            parameters.put("nomlecteur", nom);

            /*ADD PARAMS*/
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        dialog.dismiss();
                        refresh.post(new Runnable() {
                            @Override
                            public void run() {
                                lecteur.clear();
                                getData();
                            }
                        });

                        Toast.makeText(getApplicationContext(), "Le nouveau lecteur est enregistré", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Une erreur s'est produite", Toast.LENGTH_LONG).show();
                        error.printStackTrace();
                    }
                });

            Volley.newRequestQueue(this).add(jsonObjectRequest);

        }catch (JSONException e){
            e.printStackTrace();
        }
    }


    @Override
    public void onRefresh() {
        lecteur.clear();
        getData();
    }
}