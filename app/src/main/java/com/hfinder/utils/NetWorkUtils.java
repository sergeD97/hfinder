package com.hfinder.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;


import com.hfinder.entities.Image;
import com.hfinder.entities.Logement;
import com.hfinder.entities.MessageServer;
import com.hfinder.entities.Utilisateur;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Scanner;



/**
 * Created by root on 10/06/18.
 * "http://10.42.0.1:8080";
 */

public class NetWorkUtils {

    public static final  String BASE_SERVER_URL = "http://192.168.8.101:8080";

    public static final  String GEOCODING_SERVER_URL = "https://eu1.locationiq.org/v1/reverse.php";
    public static final  String GEOCODING_KEY = "990283fa371e05";
    public static final  String PARAM_KEY = "key";
    public static final  String PARAM_LON = "lon";
    public static final  String PARAM_LAT = "lat";
    public static final  String PARAM_FORMAT = "format";

    public static final  String PARAM_MAIL = "email";
    public static final  String PARAM_PASSWORD= "password";

    public static final  String PARAM_SEARCH_LIEU= "lieu";
    public static final  String PARAM_SEARCH_PRIX= "prix_max";
    public static final  String PARAM_SEARCH_IDTYPE= "id_type";



    public static String login(String email, String password) throws IOException{
        return getResponseFromHttpUrl(buildUrlLogin(BASE_SERVER_URL+"/utilisateur/login", email, password));
    }

    public static String getPositionInfo(Double lon, Double lat) throws IOException{
        return getResponseFromHttpUrl(buildUrlGeocoding(GEOCODING_SERVER_URL, lon, lat));
    }

    public static URL buildUrlGeocoding(String url1, Double longitude, Double latitude) {
        Uri builtUri = Uri.parse(url1).buildUpon()
                .appendQueryParameter(PARAM_KEY, GEOCODING_KEY)
                .appendQueryParameter(PARAM_LAT, String.valueOf(latitude))
                .appendQueryParameter(PARAM_LON, String.valueOf(longitude))
                .appendQueryParameter(PARAM_FORMAT, "json")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlLogin(String url1, String email, String password) {
        Uri builtUri = Uri.parse(url1).buildUpon()
                .appendQueryParameter(PARAM_MAIL, email)
                .appendQueryParameter(PARAM_PASSWORD, password)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlSearchLogement(String url1, String lieu, Long idType, Double prixMax) {
        Uri builtUri = Uri.parse(url1).buildUpon()
                .appendQueryParameter(PARAM_SEARCH_LIEU, lieu)
                .appendQueryParameter(PARAM_SEARCH_PRIX, String.valueOf(prixMax))
                .appendQueryParameter(PARAM_SEARCH_IDTYPE, String.valueOf(idType))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String searchLogement(String lieu, Long idType, Double prixMax)throws IOException{
        return getResponseFromHttpUrl(buildUrlSearchLogement(BASE_SERVER_URL+"/logement/search",lieu, idType, prixMax));
    }

    public static URL buildUrlSendUser(String url1, Utilisateur u) {
        Uri builtUri = Uri.parse(url1).buildUpon()
                .appendQueryParameter(Utilisateur.PARAM_ID, ""+u.getId())
                .appendQueryParameter(Utilisateur.PARAM_NOM, u.getNom())
                .appendQueryParameter(Utilisateur.PARAM_PRENOM, u.getPrenom())
                .appendQueryParameter(Utilisateur.PARAM_EMAIL, u.getEmail())
                .appendQueryParameter(Utilisateur.PARAM_TELEPHONE, u.getTelephone())
                .appendQueryParameter(Utilisateur.PARAM_password, u.getPassword())
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }



    public static URL buildUrlSendLogement(String url1, Logement u) {
        Uri builtUri = Uri.parse(url1).buildUpon()
                .appendQueryParameter(Logement.PARAM_ID, ""+u.getId())
                .appendQueryParameter(Logement.PARAM_description, u.getDescription())
                .appendQueryParameter(Logement.PARAM_latitude, u.getLatitude()+"")
                .appendQueryParameter(Logement.PARAM_LIBELLE, u.getLibelle())
                .appendQueryParameter(Logement.PARAM_lieu, u.getLieu())
                .appendQueryParameter(Logement.PARAM_longitude, u.getLongitude()+"")
                .appendQueryParameter(Logement.PARAM_loyer, u.getLoyer()+"")
                .appendQueryParameter(Logement.PARAM_proprietaire, u.getProprietaire().getId()+"")
                .appendQueryParameter(Logement.PARAM_type, u.getTypeLogement().getId()+"")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlSendMessage(String url1, MessageServer u) {
        Uri builtUri = Uri.parse(url1).buildUpon()
                .appendQueryParameter(MessageServer.PARAM_contenue, u.getContenue())
                .appendQueryParameter(MessageServer.PARAM_date_envoi, u.getDateEnvoi()+"")
                .appendQueryParameter(MessageServer.PARAM_emet, u.getEmeteur().getId()+"")
                .appendQueryParameter(MessageServer.PARAM_recep, u.getRecepteur().getId()+"")
                .appendQueryParameter(MessageServer.PARAM_vue, u.getVue().toString())
                .appendQueryParameter(MessageServer.PARAM_ID, ""+u.getId())
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildUrlSendImage(String url1, Image u) {
        Uri builtUri = Uri.parse(url1).buildUpon()
                .appendQueryParameter(Image.PARAM_LIBELLE, u.getLibelle())
                .appendQueryParameter(Image.PARAM_LOGE, u.getLogement().getId()+"")
                .appendQueryParameter(Image.PARAM_URL, u.getUrl())
                .appendQueryParameter(Image.PARAM_ID, ""+u.getId())
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        BufferedReader reader = null;


        HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
        try {
            reader = new BufferedReader(new InputStreamReader(connexion.getInputStream()));
            String l, ligne= "";

            while ((l = reader.readLine()) != null) {
                ligne += l;
            }

            return ligne;

        }finally {
            connexion.disconnect();
        }
    }

    public static Bitmap getBitmapFromHttpUrl(String imageUrl) throws Exception {


        URL url = new URL(imageUrl);
        Bitmap bm;

        HttpURLConnection connexion = (HttpURLConnection) url.openConnection();
        try {
            connexion.setDoInput(true);
            //connexion.connect();
            InputStream in = connexion.getInputStream();

            bm = BitmapFactory.decodeStream(in);
            return bm;

        }finally {
            connexion.disconnect();
        }

    }

    public static String uploadFile(String serverUrl, File file , Long idLoge)throws IOException, MalformedURLException {
        URL url = new URL(serverUrl);
        BufferedReader reader = null;
        HttpURLConnection conn;
        String bountry = "XXXXXXXXXXXXXXXXXXXX";
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setRequestMethod("POST");

        conn.setRequestProperty("ENCTYPE", "multipart/form-data");
        conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+bountry);

        DataOutputStream dataOS = new DataOutputStream(conn.getOutputStream());

        String contentDisposition = "Content-Disposition: form-data; name=\"picture\"; filename=\"" + file.getName() + "\"";
        String contentType = "Content-Type: application/octet-stream";
        FileInputStream fis = new FileInputStream(file);

        StringBuffer requestBody = new StringBuffer();
            requestBody.append("--");
            requestBody.append(bountry);
            requestBody.append("\r\n");
            requestBody.append("Content-Disposition: form-data; name=\"id_logement \"");
            requestBody.append("\r\n");
            requestBody.append("\r\n");
            requestBody.append(String.valueOf(idLoge));
            requestBody.append("\r\n");
            requestBody.append("--");
            requestBody.append(bountry+"\r\n");

            requestBody.append(contentDisposition);
            requestBody.append("\r\n");
            requestBody.append(contentType);
            requestBody.append("\r\n");
            requestBody.append("\r\n");
            dataOS.writeBytes(requestBody.toString());
            ///////
        int bytesAvailable = fis.available();

        int maxBufferSize = 8192;
        int bufferSize = Math.min(bytesAvailable, maxBufferSize);
        byte[] buffer = new byte[bufferSize];

        int bytesRead = fis.read(buffer, 0, bufferSize);
        while (bytesRead > 0) {
            dataOS.write(buffer, 0, bufferSize);
            bytesAvailable = fis.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            bytesRead = fis.read(buffer, 0, bufferSize);
        }
            ///////
        StringBuffer requestBody1 = new StringBuffer();

            requestBody1.append("\r\n");
            requestBody1.append("--");
            requestBody1.append(bountry);
            requestBody1.append("--");
            requestBody1.append("\r\n");

            dataOS.writeBytes(requestBody1.toString());

            dataOS.flush();
            dataOS.close();

            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String l, ligne= "";

            while ((l = reader.readLine()) != null) {
                ligne += l;
            }
            conn.disconnect();

            return ligne;



    }

}
