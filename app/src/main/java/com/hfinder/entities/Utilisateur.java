
package com.hfinder.entities;


import java.util.List;

/**
 *
 * @author serge
 */

public class Utilisateur{

    public static final String PARAM_ID = "id";
    public static final String PARAM_NOM = "nom";
    public static final String PARAM_PRENOM = "prenom";
    public static final String PARAM_TELEPHONE = "telephone";
    public static final String PARAM_EMAIL = "email";
    public static final String PARAM_password = "password";

    private Long id=0L;
    private String nom;
    private String prenom;
    private String telephone;
    private String email;
    private String password;
    private List<Logement> logementList;
    private List<Location> locationList;
    private List<MessageServer> messageList;
    private List<MessageServer> messageList1;
    private List<Logement> logementsCree;

    public Utilisateur() {
    }

    public Utilisateur(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public List<Logement> getLogementList() {
        return logementList;
    }

    public void setLogementList(List<Logement> logementList) {
        this.logementList = logementList;
    }

    public List<Logement> getLogementsCree() {
        return logementsCree;
    }

    public void setLogementsCree(List<Logement> logementsCree) {
        this.logementsCree = logementsCree;
    }


    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }


    public List<MessageServer> getMessageList() {
        return messageList;
    }

    public void setMessageList(List<MessageServer> messageList) {
        this.messageList = messageList;
    }


    public List<MessageServer> getMessageList1() {
        return messageList1;
    }

    public void setMessageList1(List<MessageServer> messageList1) {
        this.messageList1 = messageList1;
    }


}
