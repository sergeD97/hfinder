package com.hfinder.utils;


import com.hfinder.entities.Image;
import com.hfinder.entities.Location;
import com.hfinder.entities.Logement;
import com.hfinder.entities.MessageServer;
import com.hfinder.entities.TypeLogement;
import com.hfinder.entities.Utilisateur;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 10/06/18.
 */

public class JsonUtils {

    public static Utilisateur utilisateurParser(JSONObject json) throws JSONException {

        Long id = json.getLong("id");
        String nom = json.getString("nom");
        String prenom = json.getString("prenom");
        String telephone = json.getString("telephone");
        String email = json.getString("email");

        Utilisateur u = new Utilisateur();
        u.setId(id);
        u.setNom(nom);
        u.setPrenom(prenom);
        u.setEmail(email);
        u.setTelephone(telephone);
        return u;
    }

    public static String getLieuFromPosition(JSONObject json) throws JSONException{
        return json.getString("display_name");
    }

    public static List<Utilisateur> utilisateurListParser(JSONArray jsarray) throws JSONException {
        int lenght = jsarray.length();
        List<Utilisateur> list = new ArrayList<>();
        for(int i=0; i<lenght; i++){
            list.add(utilisateurParser(jsarray.getJSONObject(i)));
        }

        return  list;
    }

    public static Logement logementParser(JSONObject json) throws JSONException {
        Long id = json.getLong("id");
        String libelle = json.getString("libelle");
        String lieu = json.getString("lieu");
        Double loyer = json.getDouble("loyer");
        Double longitude = json.getDouble("longitude");
        Double latitude = json.getDouble("latitude");
        String description = json.getString("description");

        Logement lo = new Logement();
        lo.setId(id);
        lo.setLibelle(libelle);
        lo.setLieu(lieu);
        lo.setLoyer(loyer);
        lo.setLongitude(longitude);
        lo.setLatitude(latitude);
        lo.setDescription(description);
        try{
            lo.setUtilisateurList(utilisateurListParser(json.getJSONArray("utilisateurList")));
        }catch(JSONException e){
            lo.setUtilisateurList(new ArrayList<Utilisateur>());
        }
        try{
            lo.setImageList(imageListParser(json.getJSONArray("imageList")));
        }catch(JSONException e){
            lo.setImageList(new ArrayList<Image>());
        }

        lo.setTypeLogement(typeLogementParser(json.getJSONObject("typeLogement")));
        lo.setProprietaire(utilisateurParser(json.getJSONObject("proprietaire")));

        return lo;
    }

    public static TypeLogement typeLogementParser(JSONObject json) throws JSONException {

        Long id = json.getLong("id");
        String libelle = json.getString("libelle");

        TypeLogement t = new TypeLogement();
        t.setId(id);
        t.setLibelle(libelle);
        return t;
    }

    public static List<TypeLogement> typeLogementListParser(JSONArray jsarray) throws JSONException {
        int lenght = jsarray.length();
        List<TypeLogement> list = new ArrayList<>();
        for(int i=0; i<lenght; i++){
            list.add(typeLogementParser(jsarray.getJSONObject(i)));
        }

        return  list;
    }

    public static Image imageParser(JSONObject json) throws JSONException {
        Long id = json.getLong("id");
        String libelle = json.getString("libelle");
        String url = json.getString("url");

        Image i = new Image();
        i.setId(id);
        i.setLibelle(libelle);
        i.setUrl(url);

        return i;
    }

    public static List<Image> imageListParser(JSONArray jsarray) throws JSONException {
        int lenght = jsarray.length();
        List<Image> list = new ArrayList<>();
        for(int i=0; i<lenght; i++){
            list.add(imageParser(jsarray.getJSONObject(i)));
        }

        return  list;
    }

    public static MessageServer messageServerParser(JSONObject json) throws JSONException {
        Long id = json.getLong("id");
        String contenue = json.getString("contenue");
        boolean vue = json.getBoolean("vue");
        Long date_envoie = json.getLong("dateEnvoi");
        Utilisateur em = utilisateurParser(json.getJSONObject("emeteur"));
        Utilisateur rec = utilisateurParser(json.getJSONObject("recepteur"));

        MessageServer m = new MessageServer();
        m.setId(id);
        m.setContenue(contenue);
        m.setVue(vue);
        m.setDateEnvoi(date_envoie);
        m.setEmeteur(em);
        m.setRecepteur(rec);

        return m;
    }

    public static List<MessageServer> messageServerListParser(JSONArray jsarray) throws JSONException {
        int lenght = jsarray.length();
        List<MessageServer> list = new ArrayList<>();
        for(int i=0; i<lenght; i++){
            list.add(messageServerParser(jsarray.getJSONObject(i)));
        }

        return  list;
    }

    public static List<Logement> logementListParser(JSONArray jsarray) throws JSONException {
        int lenght = jsarray.length();
        List<Logement> list = new ArrayList<>();
        for(int i=0; i<lenght; i++){
            list.add(logementParser(jsarray.getJSONObject(i)));
        }

        return  list;
    }

    public static Location locationParser(JSONObject json) throws JSONException {
        Long id = json.getLong("id");
        int date_debut = json.getInt("dateDebut");
        int date_fin = json.getInt("dateFin");

        Logement loge = logementParser(json.getJSONObject("logement"));
        Utilisateur u = utilisateurParser(json.getJSONObject("utilisateur"));

        Location l = new Location();
        l.setId(id);
        l.setDateDebut(date_debut);
        l.setDateFin(date_fin);
        l.setLogement(loge);
        l.setUtilisateur(u);

        return l;
    }

    public static List<Location> locationListParser(JSONArray jsarray) throws JSONException {
        int lenght = jsarray.length();
        List<Location> list = new ArrayList<>();
        for(int i=0; i<lenght; i++){
            list.add(locationParser(jsarray.getJSONObject(i)));
        }

        return  list;
    }



}
