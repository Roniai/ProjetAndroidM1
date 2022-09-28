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
import com.fex.projetandroidm1.adapter.LecteurAdapter;
import com.fex.projetandroidm1.adapter.PretAdapter;
import com.fex.projetandroidm1.adapter.PretLecteurAdapter;
import com.fex.projetandroidm1.model.Lecteur;
import com.fex.projetandroidm1.model.Livre;
import com.fex.projetandroidm1.model.Pret;
import com.fex.projetandroidm1.model.PretLecteur;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PretLecteurActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private ArrayList<Pret> pret = new ArrayList<>();
    private ArrayList<Livre> livre = new ArrayList<>();
    private ArrayList<PretLecteur> pretLecteur = new ArrayList<>();
    private JsonArrayRequest arrayRequest;
    private RecyclerView recyclerView;
    private PretLecteurAdapter pretLecteurAdapter;
    private String nomlecteur="";
    private int nb_prets = 0;
    private List<String> numlivre = new ArrayList<>();

    Url url_classe = new Url();
    private String urlLecteur = url_classe.getUrl()+"/lecteurs";
    private String urlLivre = url_classe.getUrl()+"/livres";
    private String urlPret = url_classe.getUrl()+"/prets";

    /*Pour régler le problème de TimeoutError*/
    public static int TIMEOUT_MS=20000; //20s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pret_lecteur);

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
                Log.i("PretLecteurActivity", "onNavigationItemSelected: " + item.getItemId());
                switch (item.getItemId()) {

                    case R.id.menuAccueil:
                        startActivity(new Intent(PretLecteurActivity.this, MainActivity.class));
                        return true;

                    case R.id.menuLecteur:
                        startActivity(new Intent(PretLecteurActivity.this, LecteurActivity.class));
                        return true;

                    case R.id.menuLivre:
                        startActivity(new Intent(PretLecteurActivity.this, LivreActivity.class));
                        return true;

                    case R.id.menuPret:
                        startActivity(new Intent(PretLecteurActivity.this, PretActivity.class));
                        return true;

                    case R.id.menuPretLecteur:
                        drawerLayout.close();
                        return true;

                    case R.id.menuChart:
                        startActivity(new Intent(PretLecteurActivity.this, ChartActivity.class));
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Erreur de navigation", Toast.LENGTH_LONG).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.i("PretLecteurActivity", "onNavigationItemSelected: nothing clicked");
                return false;
            }
        });

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
        recyclerView = (RecyclerView) findViewById(R.id.pret);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                refreshData();
            }
        });
    }

    /*SEARCH - GET*/
    public void searchPretLecteur(View v){
        refresh.setRefreshing(true);
        refreshData();

        EditText search;
        search = (EditText) findViewById(R.id.searchEdit);

        /*GET PRETS*/
        arrayRequest = new JsonArrayRequest(urlPret,
                new Response.Listener<JSONArray>(){
                    @Override
                    public void onResponse(JSONArray response) {

                        JSONObject jsonObject = null;
                        for(int i=0; i<response.length(); i++){
                            try {
                                jsonObject = response.getJSONObject(i);

                                if(jsonObject.getString("numlecteur").equals(search.getText().toString())==true){
                                    /*NOMBRE PRETS*/
                                    nb_prets++;

                                    /*LIST PRETS*/
                                    Pret p = new Pret();
                                    p.setNumlecteur(jsonObject.getString("numlecteur"));
                                    p.setNumlivre(jsonObject.getString("numlivre"));
                                    p.setDatepret(jsonObject.getString("datepret"));
                                    pret.add(p);

                                    numlivre.add(jsonObject.getString("numlivre"));
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        if(nb_prets!=0){

                            /*Ordonnancement d'exécution des méthodes*/

                                final Thread taches1 = new Thread(){
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                /*GET NOMLECTEUR*/
                                                getNomLecteur(search.getText().toString());

                                                /*GET LIVRE (DESIGN - AUTEUR)*/
                                                for (int i = 0; i < numlivre.size(); i++) {
                                                    getLivre(numlivre.get(i));
                                                }
                                            }
                                        });
                                    }
                                };
                                taches1.start();
                                new Thread() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    taches1.join();
                                                    /*Toast.makeText(getApplicationContext(), "Ce lecteur n'est jamais prêter un livre", Toast.LENGTH_SHORT).show();
                                                    System.out.println("Mlam tsara");*/
                                                    /*Log.i("PretLecteurActivity", "Ici c'est JOIN");*/
                                                    /*showPretLecteur(search.getText().toString(), nomlecteur, nb_prets);*/

                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                                    }
                                }.start();

                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Ce lecteur n'est jamais prêter un livre", Toast.LENGTH_LONG).show();
                        }

                        Log.i("PretLecteurActivity", "Ici c'est IVELANY");
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



        requestQueue = Volley.newRequestQueue(PretLecteurActivity.this);
        requestQueue.add(arrayRequest);

    }

    /*NOMLECTEUR - GET*/
    public void getNomLecteur(String num){
        /*GET{NUMLECTEUR}*/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlLecteur+"/"+num, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("numlecteur").equals(num)==true){
                                nomlecteur = response.getString("nomlecteur").toString();
                                Log.i("PretLecteurActivity", "nomlecteur:"+nomlecteur);
                            }
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

        Volley.newRequestQueue(PretLecteurActivity.this).add(jsonObjectRequest);
    }

    /*NUMLIVRE - GET*/
    public void getLivre(String num){
        /*GET{NUMLIVRE}*/
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, urlLivre+"/"+num, null,
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
                            livre.add(liv);

                            Log.i("PretLecteurActivity", "LIVRE: Design = "+liv.getDesignlivre());

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

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    /*SHOW RESPONSE*/
    public void showPretLecteur(String num, String nom,int nb){
        /*EN-TETE*/
        TextView numlecteur, nomlecteur, nbpret;

        numlecteur = (TextView) findViewById(R.id.numlecteurLabel);
        nomlecteur = (TextView) findViewById(R.id.nomlecteurLabel);
        nbpret = (TextView) findViewById(R.id.nbpretLabel);

        numlecteur.setText("N°"+num);
        nomlecteur.setText("Nom : "+nom);
        nbpret.setText("Nombre de livres prêtés : "+String.valueOf(nb));

        Log.i("PretLecteurActivity", "numlecteur: " + num + " ,nomlecteur: " + nom+ " ,nbpret: "+ nb);

        Log.i("PretLecteurActivity", "LIVRE: Design SHOW = : " +livre.size());

        /*TABLEAU*/
        ArrayList<PretLecteur> pretLecteur = new ArrayList<>();

        for(int i = 0; i<livre.size(); i++){
            Log.i("PretLecteurActivity", "LIVRE: Design SHOW = "+livre.get(i).getDesignlivre());
        }
        /*for(int i = 0; i<nb_prets; i++){
            PretLecteur pL = new PretLecteur();
            pL.setDesignlivre(livre.get(i).getDesignlivre());
            pL.setAutlivre(livre.get(i).getAutlivre());
            pL.setDatepret(pret.get(i).getDatepret());
            pretLecteur.add(pL);
        }
        adapterPush(pretLecteur);*/
    }

    private void adapterPush(ArrayList<PretLecteur> pretLecteur){
        pretLecteurAdapter = new PretLecteurAdapter(this, pretLecteur);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(pretLecteurAdapter);
    }

    @Override
    public void onRefresh() {
        refreshData();
    }

    public void refreshData(){
        /*Initialiser*/
        nb_prets=0;
        pret.clear();
        livre.clear();
        pretLecteur.clear();
        numlivre.clear();
        TextView numlecteur, nomLecteur, nbpret;

        numlecteur = (TextView) findViewById(R.id.numlecteurLabel);
        nomLecteur = (TextView) findViewById(R.id.nomlecteurLabel);
        nbpret = (TextView) findViewById(R.id.nbpretLabel);

        numlecteur.setText("N°");
        nomLecteur.setText("Nom : ");
        nbpret.setText("Nombre de livres prêtés : ");
    }
}