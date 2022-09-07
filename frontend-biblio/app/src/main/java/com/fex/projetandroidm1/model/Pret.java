package com.fex.projetandroidm1.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Pret {

    private String numlecteur;
    private String numlivre;
    private String datepret;

    public Pret() {
    }

    public Pret(String numlecteur, String numlivre, String datepret) {
        this.numlecteur = numlecteur;
        this.numlivre = numlivre;
        this.datepret = datepret;
    }

    public String getNumlecteur() {
        return numlecteur;
    }

    public void setNumlecteur(String numlecteur) {
        this.numlecteur = numlecteur;
    }

    public String getNumlivre() {
        return numlivre;
    }

    public void setNumlivre(String numlivre) {
        this.numlivre = numlivre;
    }

    public String getDatepret() {
        return datepret;
    }

    public void setDatepret(String datepret) {
        this.datepret = datepret;
    }
}
