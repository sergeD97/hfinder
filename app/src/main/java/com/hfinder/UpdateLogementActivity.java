package com.hfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hfinder.entities.Logement;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONObject;

import java.net.URL;

public class UpdateLogementActivity extends AppCompatActivity {
    private EditText titl, pri, desc;
    private Button term;
    private String title, des;
    private Double prix;
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        title = i.getStringExtra(DetailLogement.TITLE_LOGEMENT);
        prix = i.getDoubleExtra(DetailLogement.PRIX, 0.0);
        des = i.getStringExtra(DetailLogement.DESC);
        id = i.getLongExtra(DetailLogement.ID_LOGEMENT, 0);

        setContentView(R.layout.activity_update_logement);
        ActionBar actionBar = this.getSupportActionBar();
        titl = (EditText)findViewById(R.id.titl);
        pri = (EditText)findViewById(R.id.pri);
        desc = (EditText)findViewById(R.id.desc);

        titl.setText(title);
        pri.setText(prix+"");
        desc.setText(des);


        term = (Button)findViewById(R.id.term);

        term.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.INVISIBLE);
                new Task().execute(id);
            }
        });

        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            //NavUtils.navigateUpFromSameTask(this);
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class Task extends AsyncTask<Long, String, String>{

        @Override
        protected String doInBackground(Long... longs) {
            try{
                Logement l = JsonUtils.logementParser(new JSONObject(NetWorkUtils.getResponseFromHttpUrl(new URL(NetWorkUtils.BASE_SERVER_URL+"/logement/"+longs[0]))));
                l.setLibelle(titl.getText().toString());
                l.setLoyer(Double.parseDouble(pri.getText().toString()));
                l.setDescription(desc.getText().toString());

                NetWorkUtils.getResponseFromHttpUrl(NetWorkUtils.buildUrlSendLogement(NetWorkUtils.BASE_SERVER_URL+"/logement/insert", l));

                return "ok";
            }catch (Exception e){
                return  e.getMessage();
            }

        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);

            if(s.equals("ok")){
                setResult(RESULT_OK);
                finish();
            }else{
                Toast.makeText(UpdateLogementActivity.this, s, Toast.LENGTH_LONG).show();
            }
        }
    }
}
