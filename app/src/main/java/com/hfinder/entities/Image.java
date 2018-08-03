
package com.hfinder.entities;

/**
 * @author serge
 */

public class Image{

    public static final String PARAM_ID = "id";
    public static final String PARAM_LIBELLE = "libelle";
    public static final String PARAM_URL = "url";
    public static final String PARAM_LOGE = "logement";

    private Long id=0L;
    private String libelle;
    private String url;
    private Logement logement;

    public Image() {
    }

    public Image(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Logement getLogement() {
        return logement;
    }

    public void setLogement(Logement logement) {
        this.logement = logement;
    }


}
