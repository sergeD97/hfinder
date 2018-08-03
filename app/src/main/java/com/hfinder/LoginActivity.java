package com.hfinder;


import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.database.DataBase;
import com.hfinder.database.Info;
import com.hfinder.database.InfoDAO;
import com.hfinder.database.Message;
import com.hfinder.entities.Utilisateur;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private DataBase mDb;
    private EditText email;
    private EditText password;
    private ProgressBar loading;
    private TextView error;
    private Button connect;
    private TextView inscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDb = DataBase.getInstance(getApplicationContext());

        email = (EditText) findViewById(R.id.et_email_login);
        password = (EditText)findViewById(R.id.et_pwd_login);
        loading = (ProgressBar)findViewById(R.id.pb_login);
        error = (TextView)findViewById(R.id.tv_error_login);
        connect = (Button)findViewById(R.id.bt_login);
        inscription = (TextView)findViewById(R.id.bt_insc);

        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(email.getText().toString().equals("") || password.getText().toString().equals("")){
                    Toast.makeText(LoginActivity.this, "Veuillez specifier correctement vos informations de connection", Toast.LENGTH_LONG).show();
                }else{
                    new Task().execute(email.getText().toString(), password.getText().toString());
                }
            }
        });

        inscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity1.class);
                startActivity(i);
                finish();
            }
        });



    }

    private class Task extends AsyncTask<String, String, String>{


        @Override
        protected String doInBackground(String... strings) {
            String result;
            try{
                result = NetWorkUtils.login(strings[0], strings[1]);

                //
                Utilisateur u = JsonUtils.utilisateurParser(new JSONObject(result));

                // persist de data of user into the local db
                mDb.infoDAO().insert(new Info(Info.USER_NAME, u.getNom()));
                mDb.infoDAO().insert(new Info(Info.USER_SURNAME, u.getPrenom()));
                mDb.infoDAO().insert(new Info(Info.USER_EMAIL, u.getEmail()));
                mDb.infoDAO().insert(new Info(Info.USER_PHONE, u.getTelephone()));
                mDb.infoDAO().insert(new Info(Info.USER_ID, String.valueOf(u.getId())));

            }catch(IOException e){
                return "network";
            }catch (JSONException e){
                return "bad";
            }catch (Exception e){
                return e.getMessage();
            }
            return "ok";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("ok")){
                endloading("ok");
                Intent home = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(home);
                finish();

            }else{
                endloading(s);
            }

        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            startloading();
        }
    }

    public void startloading(){
        connect.setVisibility(View.INVISIBLE);
        error.setVisibility(View.INVISIBLE);
        loading.setVisibility(View.VISIBLE);
    }

    public void endloading(String result){
        if(result.equals("bad")){
            error.setText("Parametre de connexion incorrecte ");
        }else if(result.equals("network")){
            error.setText("Impossible d'acceder au serveur Verifier votre connexion internet");
        }

        connect.setVisibility(View.VISIBLE);
        error.setVisibility(View.VISIBLE);
        loading.setVisibility(View.INVISIBLE);
    }
}
