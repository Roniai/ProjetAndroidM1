package com.fex.projetandroidm1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.fex.projetandroidm1.adapter.PretAdapter;
import com.fex.projetandroidm1.model.Pret;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PretActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private ArrayList<Pret> pret = new ArrayList<>();
    private JsonArrayRequest arrayRequest;
    private JsonObjectRequest jsonObjectRequest;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private PretAdapter pretAdapter;

    Url url_classe = new Url();
    private String url = url_classe.getUrl()+"/prets";

    /*Pour régler le problème de TimeoutError*/
    public static int TIMEOUT_MS=15000; //15s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pret);

        /*MENU NAVIGATION*/
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);

        findViewById(R.id.imageMenu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        /*Navigation*/
        NavigationView navigationView = findViewById(R.id.navigationView);
        navigationView.setItemIconTintList(null);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.i("PretActivity", "onNavigationItemSelected: " + item.getItemId());
                switch (item.getItemId()) {

                    case R.id.menuAccueil:
                        startActivity(new Intent(PretActivity.this, MainActivity.class));
                        return true;

                    case R.id.menuLecteur:
                        startActivity(new Intent(PretActivity.this, LecteurActivity.class));
                        return true;

                    case R.id.menuLivre:
                        startActivity(new Intent(PretActivity.this, LivreActivity.class));
                        return true;

                    case R.id.menuPret:
                        drawerLayout.close();
                        return true;

                    case R.id.menuPretLecteur:
                        startActivity(new Intent(PretActivity.this, PretLecteurActivity.class));
                        return true;

                    case R.id.menuChart:
                        startActivity(new Intent(PretActivity.this, ChartActivity.class));
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Erreur de navigation", Toast.LENGTH_LONG).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.i("PretActivity", "onNavigationItemSelected: nothing clicked");
                return false;
            }
        });

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
        recyclerView = (RecyclerView) findViewById(R.id.pret);

        dialog = new Dialog(this);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                pret.clear();
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

                                Pret p = new Pret();
                                p.setNumlecteur(jsonObject.getString("numlecteur"));
                                p.setNumlivre(jsonObject.getString("numlivre"));
                                p.setDatepret(jsonObject.getString("datepret"));
                                pret.add(p);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        adapterPush(pret);
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

        requestQueue = Volley.newRequestQueue(PretActivity.this);
        requestQueue.add(arrayRequest);
    }

    private void adapterPush(ArrayList<Pret> pret){
        pretAdapter = new PretAdapter(this, pret);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pretAdapter);
    }

    /*ADD - POST*/
    public void addPret(View v){
        TextView close, action;
        EditText numlecteur, numlivre, datepret;
        Button submit;

        dialog.setContentView(R.layout.dialog_formspret);

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
        numlivre = (EditText) dialog.findViewById(R.id.numlivreEdit);
        datepret = (EditText) dialog.findViewById(R.id.datepretEdit);

        submit = (Button) dialog.findViewById(R.id.submit);
        submit.setText("Ajouter");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numlecteur_val = numlecteur.getText().toString();
                String numlivre_val = numlivre.getText().toString();
                String datepret_val = datepret.getText().toString();
                addSubmit(numlecteur_val, numlivre_val, datepret_val);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addSubmit(String numlecteur, String numlivre, String datepret){
        /*GET PARAMS*/
        try{
            JSONObject parameters = new JSONObject();
            parameters.put("numlecteur", numlecteur);
            parameters.put("numlivre", numlivre);
            parameters.put("datepret", datepret);

            /*ADD PARAMS*/
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            refresh.post(new Runnable() {
                                @Override
                                public void run() {
                                    pret.clear();
                                    getData();
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Le nouveau pret est enregistré", Toast.LENGTH_LONG).show();
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
        pret.clear();
        getData();
    }
}