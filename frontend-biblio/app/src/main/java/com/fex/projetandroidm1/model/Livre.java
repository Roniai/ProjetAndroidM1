package com.fex.projetandroidm1.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Livre {

    private String numlivre;
    private String designlivre;
    private String autlivre;
    private String dateeditlivre;
    private String dispolivre;

    public Livre(JSONObject object) {
        try {
            this.numlivre = object.getString("numlivre");
            this.designlivre = object.getString("designlivre");
            this.autlivre = object.getString("autlivre");
            this.dateeditlivre = object.getString("dateeditlivre");
            this.dispolivre = object.getString("dispolivre");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Livre(String numlivre, String designlivre,
                   String autlivre, String dateeditlivre, String dispolivre) {
        this.numlivre = numlivre;
        this.designlivre = designlivre;
        this.autlivre = autlivre;
        this.dateeditlivre = dateeditlivre;
        this.dispolivre = dispolivre;
    }

    public String getNumlivre() {
        return this.numlivre;
    }

    public void setNumlivre(String numlivre) {
        this.numlivre = numlivre;
    }

    public String getDesignlivre() {
        return this.designlivre;
    }

    public void setDesignlivre(String designlivre) {
        this.designlivre = designlivre;
    }

    public String getAutlivre() {
        return this.autlivre;
    }

    public void setAutlivre(String autlivre) {
        this.autlivre = autlivre;
    }

    public String getDateeditlivre() {
        return this.dateeditlivre;
    }

    public void setDateeditlivre(String dateeditlivre) {
        this.dateeditlivre = dateeditlivre;
    }

    public String getDispolivre() {
        return this.dispolivre;
    }

    public void setDispolivre(String dispolivre) {
        this.dispolivre = dispolivre;
    }
}
