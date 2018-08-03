
package com.hfinder.entities;



/**
 * @author serge
 */

public class Location{

    public static final String PARAM_ID = "id";
    public static final String PARAM_DATE_DEB = "date_debut";
    public static final String PARAM_DATE_FIN = "date_fin";
    public static final String PARAM_LOGE = "logement";
    public static final String PARAM_utilisateur = "id";

    private Long id=0L;
    private int dateDebut;
    private int dateFin;
    private Logement logement;
    private Utilisateur utilisateur;

    public Location() {
    }

    public Location(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(int dateDebut) {
        this.dateDebut = dateDebut;
    }

    public int getDateFin() {
        return dateFin;
    }

    public void setDateFin(int dateFin) {
        this.dateFin = dateFin;
    }

    public Logement getLogement() {
        return logement;
    }

    public void setLogement(Logement logement) {
        this.logement = logement;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }


}
