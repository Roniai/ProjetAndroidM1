package com.fex.projetandroidm1;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader

public class MainActivity extends AppCompatActivity {

    private ListView lecteurList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getLecteurs();
    }

    private void getLecteurs() {
        List<Header> headers = new ArrayList<Header>();
        headers.add(new BasicHeader("Accept", "application/json"));

        LecteurRestClient.get(MainActivity.this, "api/lecteurs", headers.toArray(new Header[headers.size()]),
                null, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        ArrayList<Lecteur> lecteurArray = new ArrayList<Lecteur>();
                        LecteurAdapter lecteurAdapter = new LecteurAdapter(MainActivity.this, lecteurArray);

                        for (int i = 0; i < response.length(); i++) {
                            try {
                                lecteurAdapter.add(new Lecteur(response.getJSONObject(i)));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        lecteurList = (ListView) findViewById(R.id.list_lecteurs);
                        lecteurList.setAdapter(lecteurAdapter);
                    }
                });
    }
}