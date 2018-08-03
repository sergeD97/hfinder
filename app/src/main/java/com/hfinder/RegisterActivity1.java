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
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.utils.NetWorkUtils;

import java.io.IOException;
import java.net.URL;

public class RegisterActivity1 extends AppCompatActivity implements View.OnClickListener {

    private EditText nom,prenom;
    private Button suivant, retour;
    private TextView error, pbtv;
    private ProgressBar pb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register1);

        nom = (EditText) findViewById(R.id.nom);
        error = (TextView) findViewById(R.id.error);
        prenom = (EditText) findViewById(R.id.prenom);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        pbtv = (TextView) findViewById(R.id.progressBar_tv);
        suivant = (Button) findViewById(R.id.next);
        retour = (Button) findViewById(R.id.back);
        suivant.setOnClickListener(this);
        retour.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == R.id.next){
            String r = check(nom.getText().toString(), prenom.getText().toString());
            if("ok".equals(r)){
                new Task(nom.getText().toString(), prenom.getText().toString()).execute();
            }else{
                Toast.makeText(RegisterActivity1.this, r, Toast.LENGTH_LONG).show();
            }
        }else if(id == R.id.back){
            NavUtils.navigateUpFromSameTask(this);
        }
    }

    public String check(String n, String p){
        if(n.trim().equals("") || p.trim().equals("")){
            return "Veuiller correctement saisir la valeur des champ indiquer";
        }
        return "ok";
    }

    class Task extends AsyncTask<String, String, String>{
        private String n, p;
        public Task(String n, String p){
            this.n = n;
            this.p = p;
        }

        @Override
        protected String doInBackground(String... strings) {

            try{
                //test if server is accessible
                NetWorkUtils.getResponseFromHttpUrl(new URL(NetWorkUtils.BASE_SERVER_URL));
            }catch (IOException e){
                return e.getMessage();
            }
            return "ok";
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pb.setVisibility(View.VISIBLE);
            pbtv.setVisibility(View.VISIBLE);
            error.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pb.setVisibility(View.INVISIBLE);
            pbtv.setVisibility(View.INVISIBLE);

            if (s.equals("error")) {
                error.setVisibility(View.VISIBLE);
            }else{
                error.setVisibility(View.VISIBLE);
                error.setText(s);
                Intent i = new Intent(RegisterActivity1.this, RegisterActivity2.class);
                i.putExtra("nom", n);
                i.putExtra("prenom", p);
                startActivity(i);
                finish();

            }


        }
    }
}
