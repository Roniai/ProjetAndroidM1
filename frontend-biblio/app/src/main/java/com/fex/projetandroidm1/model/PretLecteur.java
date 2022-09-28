package com.fex.projetandroidm1.model;

public class PretLecteur {
    private String designlivre;
    private String autlivre;
    private String datepret;

    public PretLecteur() {
    }

    public PretLecteur(String designlivre, String autlivre, String datepret) {
        this.designlivre = designlivre;
        this.autlivre = autlivre;
        this.datepret = datepret;
    }

    public String getDesignlivre() {
        return designlivre;
    }

    public void setDesignlivre(String designlivre) {
        this.designlivre = designlivre;
    }

    public String getAutlivre() {
        return autlivre;
    }

    public void setAutlivre(String autlivre) {
        this.autlivre = autlivre;
    }

    public String getDatepret() {
        return datepret;
    }

    public void setDatepret(String datepret) {
        this.datepret = datepret;
    }
}
