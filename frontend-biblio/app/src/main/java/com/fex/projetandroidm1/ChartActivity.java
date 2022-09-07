package com.fex.projetandroidm1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.fex.projetandroidm1.model.Livre;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private RequestQueue requestQueue;
    private SwipeRefreshLayout refresh;
    private JsonArrayRequest arrayRequest;
    private RecyclerView recyclerView;

    /*PIE CHART PARAMS*/
    private PieChart pieChart;

    /*Lancer dans un émulateur*/
    /*1.Activer WIFI dans l'Emulateur
     *2.http://10.0.2.2:<port> => fait référence à http://localhost:<port> de votre machine*/
    private String url = "http://10.0.2.2:8000/api/livres";
    /*Lancer dans un smartphone Android*/
    /*private String url = "http://192.168.43.206:8000/api/livres; //Adresse IP WIFI*/

    /*Pour régler le problème de TimeoutError*/
    public static int TIMEOUT_MS=15000; //15s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

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
                Log.i("ChartActivity", "onNavigationItemSelected: " + item.getItemId());
                switch (item.getItemId()) {

                    case R.id.menuAccueil:
                        startActivity(new Intent(ChartActivity.this, MainActivity.class));
                        return true;

                    case R.id.menuLecteur:
                        startActivity(new Intent(ChartActivity.this, LecteurActivity.class));
                        return true;

                    case R.id.menuLivre:
                        startActivity(new Intent(ChartActivity.this, LivreActivity.class));
                        return true;

                    case R.id.menuPret:
                        startActivity(new Intent(ChartActivity.this, PretActivity.class));
                        return true;

                    case R.id.menuPretLecteur:
                        startActivity(new Intent(ChartActivity.this, PretLecteurActivity.class));
                        return true;

                    case R.id.menuChart:
                        drawerLayout.close();
                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Erreur de navigation", Toast.LENGTH_LONG).show();
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                Log.i("ChartActivity", "onNavigationItemSelected: nothing clicked");
                return false;
            }
        });

        /*SWIPEDOWN*/
        refresh = (SwipeRefreshLayout) findViewById(R.id.swipedown);
        recyclerView = (RecyclerView) findViewById(R.id.livre);

        refresh.setOnRefreshListener(this);
        refresh.post(new Runnable() {
            @Override
            public void run() {
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
                        int nb_oui=0;
                        int nb_non=0;

                        for(int i=0; i<response.length(); i++){
                            try {
                                jsonObject = response.getJSONObject(i);

                                if(jsonObject.getString("dispolivre").equals("Oui")==true){
                                    nb_oui++;
                                }
                                else if(jsonObject.getString("dispolivre").equals("Non")==true){
                                    nb_non++;
                                }
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        refresh.setRefreshing(false);
                        createPieChart(nb_oui, nb_non);
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

        requestQueue = Volley.newRequestQueue(ChartActivity.this);
        requestQueue.add(arrayRequest);
    }

    /*CONFIG PIE CHART*/
    private void createPieChart(int oui, int non){
        pieChart = findViewById(R.id.pie_chart);

        ArrayList<PieEntry> pieEntries = new ArrayList<>();
        PieEntry pieEntry_Oui = new PieEntry(oui, "Disponible");
        PieEntry pieEntry_Non = new PieEntry(non, "Indisponible");
        pieEntries.add(pieEntry_Oui);
        pieEntries.add(pieEntry_Non);

        PieDataSet pieDataSet = new PieDataSet(pieEntries, ": Livres");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueTextSize(16);

        pieChart.setData(new PieData(pieDataSet));
        pieChart.animateXY(3000, 3000);
        pieChart.invalidate();
        /*pieChart.getDescription().setEnabled(false);*/ /*Hide Description*/
    }

    @Override
    public void onRefresh() {
        getData();
    }
}