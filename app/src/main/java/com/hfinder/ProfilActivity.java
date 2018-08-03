package com.hfinder;

import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.database.DataBase;
import com.hfinder.database.Info;
import com.hfinder.entities.Utilisateur;

public class ProfilActivity extends AppCompatActivity {
    private DataBase mDb;
    private Utilisateur user;
    private ActionBar actionBar;
    private TextView nom, phone , mail;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        nom = (TextView)findViewById(R.id.nom);
        phone = (TextView)findViewById(R.id.phone);
        mail = (TextView)findViewById(R.id.mail);
        mDb = DataBase.getInstance(getApplicationContext());
        new Task().execute();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = this.getSupportActionBar();
        if(actionBar != null){
            try{
                actionBar.setDisplayHomeAsUpEnabled(true);
            }catch(Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class Task extends AsyncTask<String, String,String[]>{
        String[] r = new String[3];

        @Override
        protected String[] doInBackground(String... strings) {
            try{
                r[0] = mDb.infoDAO().find(Info.USER_SURNAME).getValues()+" "+mDb.infoDAO().find(Info.USER_NAME).getValues();
                r[1] = mDb.infoDAO().find(Info.USER_EMAIL).getValues();
                r[2] = mDb.infoDAO().find(Info.USER_PHONE).getValues();
                return r;
            }catch (Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s[]) {
            super.onPostExecute(s);

            if(r != null){
                nom.setText(r[0]);
                mail.setText(r[1]);
                phone.setText(r[2]);
            }else{
                nom.setText("Echec du charment");
                mail.setText("Echec du charment");
                phone.setText("Echec du charment");
            }
        }
    }
}
