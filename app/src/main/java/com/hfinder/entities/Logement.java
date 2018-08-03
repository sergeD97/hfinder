
package com.hfinder.entities;


import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.hfinder.utils.NetWorkUtils;

import java.util.List;


/**
 * @author serge
 */

public class Logement{

    public static final String PARAM_ID = "id";
    public static final String PARAM_LIBELLE = "libelle";
    public static final String PARAM_lieu = "lieu";
    public static final String PARAM_loyer = "loyer";
    public static final String PARAM_longitude = "longitude";
    public static final String PARAM_latitude= "latitude";
    public static final String PARAM_description = "description";
    public static final String PARAM_proprietaire = "proprietaire";
    public static final String PARAM_type = "type";

    private PictureLoadListener pictureLoadListener;



    private Long id=0L;
    private String libelle;
    private String lieu;
    private Double loyer;
    private Double longitude;
    private Double latitude;
    private String description;
    private List<Utilisateur> utilisateurList;
    private List<Image> imageList;
    private TypeLogement typeLogement;
    private List<Location> locationList;
    private Utilisateur proprietaire;

    //only for android
    private Bitmap picture;

    public Logement() {
        this.picture=null;
    }

    public Logement(Long id) {
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

    public String getLieu() {
        return lieu;
    }

    public void setLieu(String lieu) {
        this.lieu = lieu;
    }

    public Double getLoyer() {
        return loyer;
    }

    public void setLoyer(Double loyer) {
        this.loyer = loyer;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Utilisateur getProprietaire() {
        return proprietaire;
    }

    public void setProprietaire(Utilisateur proprietaire) {
        this.proprietaire = proprietaire;
    }
    
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Bitmap getPicture() {
        return picture;
    }



    public void setPicture(Bitmap picture) {
        this.picture = picture;
    }

    public List<Utilisateur> getUtilisateurList() {
        return utilisateurList;
    }

    public void setUtilisateurList(List<Utilisateur> utilisateurList) {
        this.utilisateurList = utilisateurList;
    }


    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public TypeLogement getTypeLogement() {
        return typeLogement;
    }

    public void setTypeLogement(TypeLogement typeLogement) {
        this.typeLogement = typeLogement;
    }


    public List<Location> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<Location> locationList) {
        this.locationList = locationList;
    }


    public PictureLoadListener getPictureLoadListener() {
        return pictureLoadListener;
    }

    public void setPictureLoadListener(PictureLoadListener pictureLoadListener) {
        this.pictureLoadListener = pictureLoadListener;
    }

    public void loadPicture(){
        new Task().execute();
    }

    public interface PictureLoadListener{
        public void onLoadCompleted();
    }

    class Task extends AsyncTask<String, String, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... strings) {

            try{
                if(imageList != null){
                    if(imageList.size() != 0){
                        return NetWorkUtils.getBitmapFromHttpUrl(imageList.get(0).getUrl());
                    }
                }

            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);
            picture = s;
            if(pictureLoadListener != null && s != null){
                pictureLoadListener.onLoadCompleted();
            }
        }
    }


}
