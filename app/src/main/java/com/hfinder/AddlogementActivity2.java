package com.hfinder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.database.DataBase;
import com.hfinder.database.Info;
import com.hfinder.database.InfoDAO;
import com.hfinder.entities.Logement;
import com.hfinder.entities.TypeLogement;
import com.hfinder.entities.Utilisateur;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddlogementActivity2 extends AppCompatActivity implements View.OnLongClickListener {
    public static final int MAX_PICTURE_NUMBER = 4;
    private ImageView img1, img2, img3, img4;
    Button ter;
    TextView error;
    Uri filUri;
    String[] paths = new String[MAX_PICTURE_NUMBER];
    private DataBase mDb;
    Bitmap bm;
    File file, f1, f2, f3, f4;
    Logement loge;
    ProgressBar p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addlogement2);
        img1 = (ImageView)findViewById(R.id.img1);
        img2 = (ImageView)findViewById(R.id.img2);
        img3 = (ImageView)findViewById(R.id.img3);
        img4 = (ImageView)findViewById(R.id.img4);
        p = (ProgressBar) findViewById(R.id.progressBar);
        mDb = DataBase.getInstance(getApplicationContext());
        loge = new Logement();
        initLogement();

        img1.setOnLongClickListener(this);
        img2.setOnLongClickListener(this);
        img3.setOnLongClickListener(this);
        img4.setOnLongClickListener(this);
        paths[0] = null;paths[1] = null;paths[2] = null;paths[3] = null;



        error = (TextView) findViewById(R.id.error);
        ter = (Button) findViewById(R.id.ter);
        file = new File(Environment.getExternalStorageDirectory(),
                "Download/tmp.jpg");
        f1 = new File(Environment.getExternalStorageDirectory(),
                "Download/tmp1.jpg");
        f2 = new File(Environment.getExternalStorageDirectory(),
                "Download/tmp2.jpg");
        f3 = new File(Environment.getExternalStorageDirectory(),
                "Download/tmp3.jpg");
        f4 = new File(Environment.getExternalStorageDirectory(),
                "Download/tmp4.jpg");
        filUri = Uri.fromFile(file);
        ter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(paths[0]==null && paths[1]==null && paths[2]==null && paths[3]==null ){
                    Toast.makeText(getApplication(), "Vous devez selectionner au moins une image qui decrit votre logement", Toast.LENGTH_LONG).show();

                }else{
                    new Task().execute(paths[0], paths[1], paths[2], paths[3]);
                }

            }
        });

        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle("Photos");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if(paths[0]!=null && paths[1]!=null && paths[2]!=null && paths[3]!=null ){
            Toast.makeText(getApplication(), "Nombre max d'image.. Appuyer longtemp sur une photo pour supprimer", Toast.LENGTH_LONG).show();

            return super.onOptionsItemSelected(item);
        }

        if (id == android.R.id.home) {
            //setResult(RESULT_CANCELED);
            //finish();
            NavUtils.navigateUpFromSameTask(this);
        }else if(id == R.id.galery){
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, 101);

        }else if(id == R.id.camera){
            if (getApplicationContext().getPackageManager().hasSystemFeature(
                    PackageManager.FEATURE_CAMERA)) {
                Uri uri = Uri.fromFile(file);
                int c = 0;

                if(paths[0]==null){
                    paths[0] = f1.getPath();
                    uri = Uri.fromFile(f1);
                    c=1;
                }else if(paths[1]==null){
                    paths[1] = f2.getPath();
                    uri = Uri.fromFile(f2);
                    c=2;
                }else if(paths[2]==null){
                    paths[2] = f3.getPath();
                    uri = Uri.fromFile(f3);
                    c=3;
                }else if(paths[3]==null){
                    paths[3] = f4.getPath();
                    uri = Uri.fromFile(f4);
                    c=4;
                }

                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    // start the image capture Intent
                    startActivityForResult(intent, c);
                }

            } else {
                Toast.makeText(getApplication(), "Impossible d'acceder a la camera", Toast.LENGTH_LONG).show();
            }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_photo, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != 101 && resultCode == RESULT_OK) {
// Si l'image est une miniature
            if (data != null) {
                if (data.hasExtra("data")){
                    Bitmap thumbnail = data.getParcelableExtra("data");
                }

            } else {
                try{


                    if(requestCode == 1){
                        bm = BitmapFactory.decodeFile(f1.getPath());
                        img1.setImageBitmap(bm);
                        bm=null;
                        img1.setVisibility(View.VISIBLE);
                    }else if(requestCode == 2){
                        bm = BitmapFactory.decodeFile(f2.getPath());
                        img2.setImageBitmap(bm);
                        img2.setVisibility(View.VISIBLE);
                    }else if(requestCode == 3){
                        bm = BitmapFactory.decodeFile(f3.getPath());
                        img3.setImageBitmap(bm);
                        img3.setVisibility(View.VISIBLE);
                    }else if(requestCode == 4){
                        bm = BitmapFactory.decodeFile(f4.getPath());
                        img4.setImageBitmap(bm);
                        img4.setVisibility(View.VISIBLE);
                    }

                }catch (Exception e){
                }
            }
        }else if(requestCode == 101 && resultCode == RESULT_OK){
            try{
                Bitmap b = BitmapFactory.decodeFile(getPathFromUri(data.getData()));
                if(paths[0]==null){
                    paths[0] = getPathFromUri(data.getData());
                    img1.setImageBitmap(b);
                    img1.setVisibility(View.VISIBLE);
                }else if(paths[1]==null){
                    paths[1] = getPathFromUri(data.getData());
                    img2.setImageBitmap(b);
                    img2.setVisibility(View.VISIBLE);
                }else if(paths[2]==null){
                    paths[2] = getPathFromUri(data.getData());
                    img3.setImageBitmap(b);
                    img3.setVisibility(View.VISIBLE);
                }else if(paths[3]==null){
                    paths[3] = getPathFromUri(data.getData());
                    img4.setImageBitmap(b);
                    img4.setVisibility(View.VISIBLE);
                }

            }catch (Exception e){
                Toast.makeText(getApplication(), "Format de fichier non supporté", Toast.LENGTH_LONG).show();

            }


        }else if(requestCode == 1 && resultCode != RESULT_OK){
            paths[0]=null;
        }else if(requestCode == 2 && resultCode != RESULT_OK){
            paths[1]=null;
        }else if(requestCode == 3 && resultCode != RESULT_OK){
            paths[2]=null;
        }else if(requestCode == 4 && resultCode != RESULT_OK){
            paths[3]=null;
        }
    }

    public String getPathFromUri(Uri uri) {
        String[] projection = {MediaStore.MediaColumns.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndex(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();

        return cursor.getString(column_index);
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        if(id == R.id.img1){
            img1.setVisibility(View.GONE);
            paths[0] = null;

            return true;

        }else if(id == R.id.img2){
            img2.setVisibility(View.GONE);
            paths[1] = null;
            return true;

        }else if(id == R.id.img3){
            img3.setVisibility(View.GONE);
            paths[2] = null;
            return true;

        }else if(id == R.id.img4){
            img4.setVisibility(View.GONE);
            paths[3] = null;
            return true;

        }


        return false;
    }

    class Task extends AsyncTask <String, String, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try{
                ter.setVisibility(View.INVISIBLE);
                p.setVisibility(View.VISIBLE);
            }catch (Exception e){
                Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_LONG).show();

            }

        }

        @Override
        protected void onPostExecute(String s) {
            p.setVisibility(View.INVISIBLE);
            ter.setVisibility(View.VISIBLE);
            if(!s.equals("ok")){
                Snackbar.make(error, s, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }else{

                // All thigs were good........ back to hmoe page
                setResult(RESULT_OK);
                finish();
            }

            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(String... strings) {
            String r = "";
            boolean status = false;


            try{
                ///get name of position of that logement using openstreetmap API

                ///upload logement here
                loge.setProprietaire(new Utilisateur(Long.parseLong(mDb.infoDAO().find(Info.USER_ID).getValues())));


                r = NetWorkUtils.getResponseFromHttpUrl(NetWorkUtils.buildUrlSendLogement(NetWorkUtils.BASE_SERVER_URL+"/logement/insert", loge));
                Logement lo = JsonUtils.logementParser(new JSONObject(r));

                for(int i = 0; i!=strings.length ;i++){
                    if(strings[i]!=null){
                        r = NetWorkUtils.uploadFile(NetWorkUtils.BASE_SERVER_URL+"/image/insert",new File(strings[i]),lo.getId());
                        status = true;
                    }
                }
                return "ok";
            }catch (Exception e){
                if(status){
                    return "ok";
                }

                return e.getMessage();
            }


        }

    }

    public void initLogement(){
        Intent i = getIntent();

            loge.setLibelle(i.getStringExtra(Logement.PARAM_LIBELLE));
            loge.setLoyer(i.getDoubleExtra(Logement.PARAM_loyer, 50000.0));
            loge.setLongitude(i.getDoubleExtra(Logement.PARAM_longitude, 4.00));
            loge.setLatitude(i.getDoubleExtra(Logement.PARAM_latitude, 4.00));
            loge.setDescription(i.getStringExtra(Logement.PARAM_description));
            loge.setTypeLogement(new TypeLogement(i.getLongExtra("type_id", 1L)));
            loge.setLieu(i.getStringExtra(Logement.PARAM_lieu));



    }
}
