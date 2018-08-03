/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.hfinder.entities;


import java.util.List;

/**
 *
 * @author serge
 */

public class TypeLogement{

    public static final String PARAM_ID = "id";
    public static final String PARAM_libelle = "libelle";

    private Long id=0L;
    private String libelle;
    private List<Logement> logementList;

    public TypeLogement() {
    }

    public TypeLogement(Long id) {
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


    public List<Logement> getLogementList() {
        return logementList;
    }

    public void setLogementList(List<Logement> logementList) {
        this.logementList = logementList;
    }

    @Override
    public String toString() {
        return this.getLibelle();
    }
}
