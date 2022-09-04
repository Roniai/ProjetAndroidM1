package com.fex.projetandroidm1.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Pret {

    private String numlecteur;
    private String numlivre;
    private String datepret;

    public Pret(JSONObject object) {
        try {
            this.numlecteur = object.getString("numlecteur");
            this.numlivre = object.getString("numlivre");
            this.datepret = object.getString("datepret");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Pret(String numlecteur, String numlivre, String datepret) {
        this.numlecteur = numlecteur;
        this.numlivre = numlivre;
        this.datepret = datepret;
    }

    public String getNumlecteur() {
        return this.numlecteur;
    }

    public void setNumlecteur(String numlecteur) {
        this.numlecteur = numlecteur;
    }

    public String getNumlivre() {
        return this.numlivre;
    }

    public void setNumlivre(String numlivre) {
        this.numlivre = numlivre;
    }

    public String getDatepret() {
        return this.datepret;
    }

    public void setDatepret(String datepret) {
        this.datepret = datepret;
    }
}
