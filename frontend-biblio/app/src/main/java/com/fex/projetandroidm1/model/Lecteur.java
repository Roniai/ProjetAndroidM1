package com.fex.projetandroidm1.model;

public class Lecteur {
    private String numlecteur;
    private String nomlecteur;

    public Lecteur() {
    }

    public Lecteur(String numlecteur, String nomlecteur) {
        this.numlecteur = numlecteur;
        this.nomlecteur = nomlecteur;
    }

    public String getNumlecteur() {
        return numlecteur;
    }

    public void setNumlecteur(String numlecteur) {
        this.numlecteur = numlecteur;
    }

    public String getNomlecteur() {
        return nomlecteur;
    }

    public void setNomlecteur(String nomlecteur) {
        this.nomlecteur = nomlecteur;
    }
}
