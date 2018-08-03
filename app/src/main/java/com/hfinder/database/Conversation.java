package com.hfinder.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

/**
 * Created by root on 30/05/18.
 */

@Entity(tableName = "conversation")
public class Conversation {
    @NonNull
    @PrimaryKey
    private String interlocuteur;
    private String nom;

    public Conversation(@NonNull String interlocuteur, String nom) {
        this.interlocuteur = interlocuteur;
        this.nom = nom;
    }

    @Ignore
    public Conversation() {
    }

    @NonNull
    public String getInterlocuteur() {
        return interlocuteur;
    }

    public void setInterlocuteur(@NonNull String interlocuteur) {
        this.interlocuteur = interlocuteur;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
