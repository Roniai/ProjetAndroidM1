package com.fex.projetandroidm1.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Lecteur {

    private String numlecteur;
    private String nomlecteur;

    public Lecteur(JSONObject object) {
        try {
            this.numlecteur = object.getString("numlecteur");
            this.nomlecteur = object.getString("nomlecteur");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Lecteur(String numlecteur, String nomlecteur) {
        this.numlecteur = numlecteur;
        this.nomlecteur = nomlecteur;
    }

    public String getNumlecteur() {
        return this.numlecteur;
    }

    public void setNumlecteur(String numlecteur) {
        this.numlecteur = numlecteur;
    }

    public String getNomlecteur() {
        return this.nomlecteur;
    }

    public void setNomlecteur(String nomlecteur) {
        this.nomlecteur = nomlecteur;
    }
}
