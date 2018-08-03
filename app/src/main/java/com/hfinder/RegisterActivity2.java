package com.hfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.database.DataBase;
import com.hfinder.database.Info;
import com.hfinder.entities.Utilisateur;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class RegisterActivity2 extends AppCompatActivity implements View.OnClickListener {

    EditText phone, mail, pwd, rpwd;
    TextView error;
    Button next, back;
    ProgressBar progressBar;
    String nom, prenom;
    Spinner tel;
    private DataBase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register2);
        mDb = DataBase.getInstance(getApplicationContext());
        nom = getIntent().getStringExtra("nom");
        prenom = getIntent().getStringExtra("prenom");
        phone = (EditText)findViewById(R.id.phone);
        mail = (EditText)findViewById(R.id.mail);
        pwd = (EditText)findViewById(R.id.pwd);
        rpwd = (EditText)findViewById(R.id.rpwd);
        tel  = (Spinner)findViewById(R.id.type);

        error = (TextView) findViewById(R.id.error);
        next = (Button) findViewById(R.id.next);
        back = (Button) findViewById(R.id.back);
        next.setOnClickListener(this);
        back.setOnClickListener(this);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.next){
            String r = check(phone.getText().toString(), mail.getText().toString() , pwd.getText().toString(),rpwd.getText().toString());
            if("ok".equals(r)){
                new Task(String.valueOf(tel.getSelectedItem())+phone.getText().toString(), mail.getText().toString(), pwd.getText().toString(), nom, prenom).execute();
            }else{
                Toast.makeText(RegisterActivity2.this, r, Toast.LENGTH_LONG).show();
            }
        }else if(id == R.id.back){
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    public String check(String n, String p, String s, String m){
        if(n.trim().equals("") || p.trim().equals("") || s.trim().equals("") || m.trim().equals("")){

            return "Veuiller remplir correctement tous les champs SVP..";

        }else if(!s.equals(m)){
            return "le mot de passe ne correspond pas";
        }
        if(!p.contains("@")){
            return "Adresse email non valide.";
        }

        return "ok";
    }

    class Task extends AsyncTask<String, String, String>{
        private Utilisateur user;

        public Task(String phone,String mail,String pwd,String nom ,String prenom){
            user = new Utilisateur();
            user.setEmail(mail);
            user.setTelephone(phone);
            user.setPassword(pwd);
            user.setNom(nom);
            user.setPrenom(prenom);
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url = NetWorkUtils.buildUrlSendUser(NetWorkUtils.BASE_SERVER_URL+"/utilisateur/insert", this.user);
                String result = NetWorkUtils.getResponseFromHttpUrl(url);
                Utilisateur u = JsonUtils.utilisateurParser(new JSONObject(result));

                // persist de data of user into the local db
                mDb.infoDAO().insert(new Info(Info.USER_NAME, u.getNom()));
                mDb.infoDAO().insert(new Info(Info.USER_SURNAME, u.getPrenom()));
                mDb.infoDAO().insert(new Info(Info.USER_EMAIL, u.getEmail()));
                mDb.infoDAO().insert(new Info(Info.USER_PHONE, u.getTelephone()));
                mDb.infoDAO().insert(new Info(Info.USER_ID, String.valueOf(u.getId())));

                return "ok";
            }catch (IOException e){
                return "erreur : "+e.getMessage();
            }catch (JSONException e){
                return "erreur : "+e.getMessage();
            }catch (Exception e){
                return "erreur : "+e.getMessage();
            }

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            error.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(View.INVISIBLE);
            if(s.equals("ok")){
                error.setVisibility(View.INVISIBLE);
                Intent home = new Intent(RegisterActivity2.this,HomeActivity.class);
                startActivityForResult(home, 11);
                //finish();
            }else{

                error.setVisibility(View.VISIBLE);
                error.setText("Impossible d'acceder au serveur..");
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setResult(-1);
        finish();
    }


}
