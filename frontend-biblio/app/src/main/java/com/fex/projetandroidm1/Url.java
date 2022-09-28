package com.fex.projetandroidm1;

public class Url {
    /* ############ Lancer dans un émulateur ############ */
    /*1.Activer WIFI dans l'Emulateur
     *2.http://10.0.2.2:<port> => fait référence à http://localhost:<port> de votre machine*/

    /* ############ Lancer dans un smartphone ############ */
    /*1.Connecter votre ordinateur et votre smartphone sur un même réseau
     *2.Lancer la commande "ipconfig" dans votre cmd pour savoir votre adresse IP (Adresse IPv4)
     *3.http://<adresse_ip>:<port>*/

    private String url;

    public Url() {
        this.url = "http://10.0.2.2:8000/api";
        /*this.url = "http://192.168.43.206:8000/api";*/
    }

    public Url(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
