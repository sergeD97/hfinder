/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hfinder.entities;


import com.hfinder.database.Message;
import com.hfinder.database.converter.DateConverter;

/**
 * @author serge
 */

public class MessageServer {

    public static final String PARAM_ID = "id";
    public static final String PARAM_contenue= "contenue";
    public static final String PARAM_vue = "vue";
    public static final String PARAM_date_envoi = "date_envoie";
    public static final String PARAM_emet = "emeteur";
    public static final String PARAM_recep = "recepteur";

    private Long id=0L;
    private String contenue;
    private Boolean vue;
    private Long dateEnvoi;
    private Utilisateur emeteur;
    private Utilisateur recepteur;

    public MessageServer() {
    }

    public MessageServer(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }

    public Boolean getVue() {
        return vue;
    }

    public void setVue(Boolean vue) {
        this.vue = vue;
    }

    public Long getDateEnvoi() {
        return dateEnvoi;
    }

    public void setDateEnvoi(Long dateEnvoi) {
        this.dateEnvoi = dateEnvoi;
    }

    public Utilisateur getEmeteur() {
        return emeteur;
    }

    public void setEmeteur(Utilisateur emeteur) {
        this.emeteur = emeteur;
    }

    public Utilisateur getRecepteur() {
        return recepteur;
    }

    public void setRecepteur(Utilisateur recepteur) {
        this.recepteur = recepteur;
    }


    public Message toMessage(){
        Message m = new Message();
        m.setId(this.id);
        m.setInterlocuteur(String.valueOf(this.getEmeteur().getId()));
        m.setEmeteur(String.valueOf(this.getEmeteur().getId()));
        m.setStatus(String.valueOf(this.getVue()));
        m.setDate(DateConverter.toDate(this.getDateEnvoi()));

        return m;
    }


}
