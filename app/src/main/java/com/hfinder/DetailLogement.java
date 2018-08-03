package com.hfinder;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.database.DataBase;
import com.hfinder.database.Info;
import com.hfinder.entities.Image;
import com.hfinder.entities.Logement;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DetailLogement extends AppCompatActivity {
    public static final String TITLE_LOGEMENT = "titre_logement";
    public static final String ID_LOGEMENT = "id_logement";
    public static final String LIEU = "lieu";
    public static final String ID_PRIO = "id_prio";
    public static final String DESC = "desc";
    public static final String PRO = "prio";
    public static final String ID_U = "id_user";
    public static final String PRIX = "prix";

    public static final int R_CODE = 100;

   // Double prix;
   // private String logement, pro, lie, desc;
    private long id, id_u;
    Task mTask;
    private DataBase mDb;

    //Bitmap[] bits;

    TextView proprietaire, title, lieu, description, error;
    ImageView msg, imgp;
    ImageView[] img;
    ProgressBar progress;
    FloatingActionButton fab;
    ConstraintLayout allView;
    ActionBar actionBar;
    Logement loge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_logement);
        Intent intent = getIntent();

        id = intent.getLongExtra(ID_LOGEMENT, -1);
        mDb = DataBase.getInstance(getApplicationContext());

       /* bits = new Bitmap[4];
        for(int i = 0; i<4; i++){
            bits[i] = null;
        }*/

       img= new ImageView[4];
       proprietaire= (TextView)findViewById(R.id.proprietaire);
       title =(TextView)findViewById(R.id.title);
       lieu  =(TextView)findViewById(R.id.lieu);
       error  =(TextView)findViewById(R.id.error);
       description = (TextView)findViewById(R.id.description);
       img[0] = (ImageView) findViewById(R.id.img1);
       fab = (FloatingActionButton) findViewById(R.id.fab);
       img[1] = (ImageView) findViewById(R.id.img2);
       img[2] = (ImageView) findViewById(R.id.img3);
       img[3] = (ImageView) findViewById(R.id.img4);
       imgp = (ImageView)findViewById(R.id.imgp);

       msg = (ImageView) findViewById(R.id.msg);
       allView = (ConstraintLayout)findViewById(R.id.allview);
       progress = (ProgressBar) findViewById(R.id.progressbar);




       msg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent callIntent = new Intent(Intent.ACTION_CALL);
               if(loge != null){
               callIntent.setData(Uri.parse("tel:"+loge.getProprietaire().getTelephone()));
               try{
                   startActivity(callIntent);
               }catch (SecurityException e){
                   Toast.makeText(DetailLogement.this, "Impossiblr de lancer l'appel : Permission refusée", Toast.LENGTH_LONG).show();
               }}else{
                   Toast.makeText(DetailLogement.this, "Impossiblr de lancer l'appel : Aucun numero", Toast.LENGTH_LONG).show();

               }


           }
       });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = this.getSupportActionBar();
        if(actionBar != null){
            try{

                actionBar.setDisplayHomeAsUpEnabled(true);
            }catch(Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            //info.setText(logement +" -- "+ String.valueOf(id));
        }

       try{

            new Task().execute(id);
        }catch(Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == R_CODE){
                new Task().execute(id);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
           // NavUtils.navigateUpFromSameTask(this);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    class Task extends AsyncTask<Long, String, Logement>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            allView.setVisibility(View.INVISIBLE);
            error.setVisibility(View.INVISIBLE);
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected Logement doInBackground(Long... lo) {
            String r;
            try{
                id_u = Long.parseLong(mDb.infoDAO().find(Info.USER_ID).getValues());
                r = NetWorkUtils.getResponseFromHttpUrl(new URL(NetWorkUtils.BASE_SERVER_URL+"/logement/"+lo[0]));

                return JsonUtils.logementParser(new JSONObject(r));

            }catch (Exception e){
                return null;
            }



        }

        @Override
        protected void onPostExecute(final Logement lo) {
            super.onPostExecute(lo);
            progress.setVisibility(View.INVISIBLE);
            try{
                if(lo != null){
                    loge = lo;
                    allView.setVisibility(View.VISIBLE);
                    title.setText(lo.getLibelle());
                    lieu.setText(lo.getLieu());
                    description.setText("Prix / Loyer(mensuel) : "+lo.getLoyer()+" FCFA\n\n"+lo.getDescription());
                    proprietaire.setText(lo.getProprietaire().getPrenom()+" "+lo.getProprietaire().getNom());
                    if(actionBar!=null){
                        actionBar.setTitle(lo.getLibelle());
                    }
                    List<Image> iml = lo.getImageList();
                    for(int i = 0; i<iml.size() && i<img.length ; i++){
                        if(i==0){
                            Picasso.with(DetailLogement.this).load(iml.get(i).getUrl()).fit().centerCrop().into(imgp);
                        }
                        Picasso.with(DetailLogement.this).load(iml.get(i).getUrl()).fit().centerInside().placeholder(R.drawable.loader).error(R.drawable.hfinderx).into(img[i], new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError() {

                            }


                        });
                        img[i].setVisibility(View.VISIBLE);
                    }

                    if(id_u == lo.getProprietaire().getId()){
                        fab.setVisibility(View.VISIBLE);
                    }

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent i = new Intent(DetailLogement.this, UpdateLogementActivity.class);
                            i.putExtra(TITLE_LOGEMENT, lo.getLibelle());
                            i.putExtra(DESC, lo.getDescription());
                            i.putExtra(ID_LOGEMENT, lo.getId());
                            i.putExtra(PRIX, lo.getLoyer());
                            startActivityForResult(i, R_CODE);
                        }
                    });
                }else{
                    error.setText("Impossible charger les Informations sur le logement.. Verifier votre connexion internet");
                    error.setVisibility(View.VISIBLE);
                }


            }catch (Exception e){
                Toast.makeText(DetailLogement.this, e.getMessage(), Toast.LENGTH_LONG).show();

            }



        }
    }
}
