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
import com.fex.projetandroidm1.adapter.LivreAdapter;
import com.fex.projetandroidm1.model.Livre;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class LivreActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private ArrayList<Livre> livre = new ArrayList<>();
    private JsonArrayRequest arrayRequest;
    private JsonObjectRequest jsonObjectRequest;
    private RecyclerView recyclerView;
    private Dialog dialog;
    private LivreAdapter livreAdapter;

    Url url_classe = new Url();
    private String url = url_classe.getUrl()+"/livres";

    /*Pour régler le problème de TimeoutError*/
    public static int TIMEOUT_MS=15000; //15s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livre);

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
                Log.i("LivreActivity", "onNavigationItemSelected: " + item.getItemId());
                switch (item.getItemId()) {

                    case R.id.menuAccueil:
                        startActivity(new Intent(LivreActivity.this, MainActivity.class));
                        return true;

                    case R.id.menuLecteur:
                        startActivity(new Intent(LivreActivity.this, LecteurActivity.class));
                        return true;

                    case R.id.menuLivre:
                        drawerLayout.close();
                        return true;

                    case R.id.menuPret:
                        startActivity(new Intent(LivreActivity.this, PretActivity.class));
                        return true;

                    case R.id.menuPretLecteur:
                        startActivity(new Intent(LivreActivity.this, PretLecteurActivity.class));
                        return true;

                    case R.id.menuChart:
                        startActivity(new Intent(LivreActivity.this, ChartActivity.class));
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Erreur de navigation", Toast.LENGTH_LONG).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.i("LivreActivity", "onNavigationItemSelected: nothing clicked");
                return false;
            }
        });

        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
        recyclerView = (RecyclerView) findViewById(R.id.livre);

        dialog = new Dialog(this);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
                livre.clear();
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

                                Livre liv = new Livre();
                                liv.setNumlivre(jsonObject.getString("numlivre"));
                                liv.setDesignlivre(jsonObject.getString("designlivre"));
                                liv.setAutlivre(jsonObject.getString("autlivre"));
                                liv.setDateeditlivre(jsonObject.getString("dateeditlivre"));
                                liv.setDispolivre(jsonObject.getString("dispolivre"));
                                livre.add(liv);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        adapterPush(livre);
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

        requestQueue = Volley.newRequestQueue(LivreActivity.this);
        requestQueue.add(arrayRequest);
    }

    private void adapterPush(ArrayList<Livre> livre){
        livreAdapter = new LivreAdapter(this, livre);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(livreAdapter);
    }

    /*ADD - POST*/
    public void addLivre(View v){
        TextView close, action;
        EditText numlivre, designlivre, autlivre, dateeditlivre, dispolivre;
        Button submit;

        dialog.setContentView(R.layout.dialog_formslivre);

        action = (TextView) dialog.findViewById(R.id.action);
        action.setText("Ajout");

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

        submit = (Button) dialog.findViewById(R.id.submit);
        submit.setText("Ajouter");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String numlivre_val = numlivre.getText().toString();
                String designlivre_val = designlivre.getText().toString();
                String autlivre_val = autlivre.getText().toString();
                String dateeditlivre_val = dateeditlivre.getText().toString();
                String dispolivre_val = dispolivre.getText().toString();
                addSubmit(numlivre_val, designlivre_val, autlivre_val, dateeditlivre_val, dispolivre_val);
            }
        });

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    private void addSubmit(String num, String design, String aut, String dateedit, String dispo){
        /*GET PARAMS*/
        try{
            JSONObject parameters = new JSONObject();
            parameters.put("numlivre", num);
            parameters.put("designlivre", design);
            parameters.put("autlivre", aut);
            parameters.put("dateeditlivre", dateedit);
            parameters.put("dispolivre", dispo);

            /*ADD PARAMS*/
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, parameters,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            dialog.dismiss();
                            refresh.post(new Runnable() {
                                @Override
                                public void run() {
                                    livre.clear();
                                    getData();
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Le nouveau livre est enregistré", Toast.LENGTH_LONG).show();
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
        livre.clear();
        getData();
    }
}