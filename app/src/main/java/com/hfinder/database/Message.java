package com.hfinder.database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


/**
 * Created by root on 28/05/18.
 */

@Entity(tableName = "message")
public class Message {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String interlocuteur;
    private String status;
    private String contenue;
    private String emeteur;
    private Date date;




    public Message(long id, String interlocuteur, String status, String contenue, Date date, String emeteur) {
        this.id = id;
        this.interlocuteur = interlocuteur;
        this.status = status;
        this.contenue = contenue;
        this.date = date;
        this.emeteur = emeteur;
    }

    @Ignore
    public Message(String interlocuteur, String contenue, String emeteur) {

        this.interlocuteur = interlocuteur;
        if(emeteur.equals(DataBase.MESSAGE_ME)){
            this.status = DataBase.MESSAGE_STATUS_VUE;
        }else{
            this.status = DataBase.MESSAGE_STATUS_NON_VUE;
        }
        this.contenue = contenue;
        this.date = new Date();
        this.emeteur = emeteur;
    }

    @Ignore
    public Message(){
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInterlocuteur() {
        return interlocuteur;
    }

    public void setInterlocuteur(String interlocuteur) {
        this.interlocuteur = interlocuteur;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getContenue() {
        return contenue;
    }

    public void setContenue(String contenue) {
        this.contenue = contenue;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getEmeteur() {
        return emeteur;
    }

    public void setEmeteur(String emeteur) {
        this.emeteur = emeteur;
    }
}
