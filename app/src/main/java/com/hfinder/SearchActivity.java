package com.hfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.adapter.MyLogementListAdapter;
import com.hfinder.entities.Logement;
import com.hfinder.entities.TypeLogement;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity implements MyLogementListAdapter.ItemClickListener {
    public static final String EX_LIEU = "lieu";
    public static final String EX_TYPE = "id_type";
    public static final String EX_PRIX_MAX = "primax";

    private RecyclerView resultList;
    private ProgressBar progress;
    private TextView error;

    private String lieu;
    private Long idType;
    private Double prixMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent i = getIntent();
        lieu = i.getStringExtra(EX_LIEU);
        idType = i.getLongExtra(EX_TYPE, 0);
        prixMax = i.getDoubleExtra(EX_PRIX_MAX, 0);

        resultList = (RecyclerView)findViewById(R.id.result_list);
        progress = (ProgressBar)findViewById(R.id.progress);
        error = (TextView)findViewById(R.id.error);
        ActionBar actionBar = this.getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }



        new Task().execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            //NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClicked(Long id, int viewId) {
        if(viewId == R.id.item_view){
            Intent i = new Intent(this, DetailLogement.class);
            i.putExtra(DetailLogement.ID_LOGEMENT, id);
            startActivity(i);
        }else if(viewId == R.id.map){
            Intent i = new Intent(this, MapsActivity.class);
            i.putExtra(DetailLogement.ID_LOGEMENT, id);
            startActivity(i);
        }

    }

    class Task extends AsyncTask<String, String, List<Logement>>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            resultList.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
            error.setVisibility(View.INVISIBLE);
        }

        @Override
        protected List<Logement> doInBackground(String... strings) {
            try{
                String r = NetWorkUtils.searchLogement(lieu,idType,prixMax);
                return JsonUtils.logementListParser(new JSONArray(r));
            }catch (JSONException e){
                return new ArrayList<>();
            }catch(Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Logement> s) {
            super.onPostExecute(s);
            progress.setVisibility(View.INVISIBLE);
            if(s!=null){
                if(s.size() == 0){
                    error.setText("Désolé.. Aucun Logement trouvé..");
                    error.setVisibility(View.VISIBLE);
                }else{
                    MyLogementListAdapter ad = new MyLogementListAdapter(s,SearchActivity.this);
                    LinearLayoutManager lm = new LinearLayoutManager(SearchActivity.this);
                    resultList.setAdapter(ad);
                    resultList.setLayoutManager(lm);
                    resultList.setVisibility(View.VISIBLE);
                }
            }else{
                error.setText("Probleme de connexion...");
                error.setVisibility(View.VISIBLE);
            }
        }
    }
}
