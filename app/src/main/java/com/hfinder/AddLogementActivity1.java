package com.hfinder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.adapter.TypeSpinnerAdapter;
import com.hfinder.entities.Logement;
import com.hfinder.entities.TypeLogement;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class AddLogementActivity1 extends AppCompatActivity {
    public static int REQUEST_PHOTOS = 100;
    public static int REQUEST_POSITION = 156;

    EditText titre, loyer, lon, lat, description;
    TextView error;
    Spinner type;
    CheckBox gps;
    Button next;
    Button pickPosition;
    Task task;

    ConstraintLayout group;
    ProgressBar p;
    private LocationManager lm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_logement1);
        task = new Task();
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        titre = (EditText) findViewById(R.id.title);
        loyer = (EditText) findViewById(R.id.loyer);
        lon = (EditText) findViewById(R.id.lon);
        lat = (EditText) findViewById(R.id.lat);
        type = (Spinner) findViewById(R.id.type);
        gps = (CheckBox) findViewById(R.id.gps);
        error = (TextView) findViewById(R.id.error);
        description = (EditText) findViewById(R.id.description);
        group = (ConstraintLayout) findViewById(R.id.group);
        p = (ProgressBar) findViewById(R.id.progressBar);

        task.execute("load_type");

        next = (Button) findViewById(R.id.next);
        pickPosition = (Button) findViewById(R.id.pick);
        pickPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AddLogementActivity1.this, MapsActivity.class);
                i.setAction(MapsActivity.ACTION_CHOOSE_POSITION);
                startActivityForResult(i, REQUEST_POSITION);
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double longit = 4.0351;
                double latit = -9.023;

                if(check()){
                    if(!gps.isChecked()){
                    if(checkLatLng()){
                        longit = Double.parseDouble(lon.getText().toString().trim());
                        latit = Double.parseDouble(lat.getText().toString().trim());
                        new Task1(latit, longit).execute();

                    }else{
                        Toast.makeText(AddLogementActivity1.this,"Valeur de latitude ou longitude incorrect",Toast.LENGTH_LONG).show();
                    }}else{
                        Location l = getPosition();

                        if(l != null){
                            latit = l.getLatitude();
                            longit = l.getLongitude();
                            new Task1(latit, longit).execute();
                        }

                    }
                }else{
                    Toast.makeText(AddLogementActivity1.this,"Veuiller remplir correctement les champ SVP...",Toast.LENGTH_LONG).show();
                }

            }
        });

        ActionBar actionBar = this.getSupportActionBar();

        if(actionBar != null){
            actionBar.setTitle("Nouveau Logement");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
           NavUtils.navigateUpFromSameTask(this);
            setResult(RESULT_CANCELED);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean check(){
        if(titre.getText().toString().trim().equals("") ||
           loyer.getText().toString().trim().equals("") ){
            return false;
        }
        return true;
    }

    public boolean checkLatLng(){

            try{
                Double lati = Double.parseDouble(lat.getText().toString().trim());
                Double ln = Double.parseDouble(lon.getText().toString().trim());
                return true;
            }catch (NumberFormatException e){
                return false;
            }

    }

    class Task extends AsyncTask<String, String, List<TypeLogement>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected List<TypeLogement> doInBackground(String... strings) {
            String result;
            List<TypeLogement> listType;
            if(strings[0].equals("load_type")){
                try {
                    result = NetWorkUtils.getResponseFromHttpUrl(new URL(NetWorkUtils.BASE_SERVER_URL+"/type_logement"));
                    listType = JsonUtils.typeLogementListParser(new JSONArray(result));
                    return listType;
                } catch (IOException e) {
                    return null;
                }catch (Exception e){
                    return null;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<TypeLogement> s) {
            if(s == null){
                //somethings wrong
                error.setText("Impossible d'acceder au serveur Hfinder...");
                error.setVisibility(View.VISIBLE);
                p.setVisibility(View.GONE);

            }else{
                //all things is okay
                TypeSpinnerAdapter tsa = new TypeSpinnerAdapter(AddLogementActivity1.this,android.R.layout.simple_spinner_item,s);
                tsa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                type.setAdapter(tsa);
                group.setVisibility(View.VISIBLE);
                p.setVisibility(View.GONE);

            }
            super.onPostExecute(s);
        }


    }

    public Location getPosition(){

        Location loc = null;
        try{
            lm.requestLocationUpdates("gps", 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {

                }
            });

            loc = lm.getLastKnownLocation("gps");
            if (loc == null) {
                loc = lm.getLastKnownLocation("network");
            }
            if(loc == null){
                Toast.makeText(this,
                        "Impossible de recupere la postion.",
                        Toast.LENGTH_SHORT).show();

            }
            return loc;

        }catch(SecurityException e){
            Toast.makeText(this,
                    "Impossible de recupere la postion.. Permission refusée",
                    Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PHOTOS){
            if(resultCode == RESULT_OK){
                setResult(RESULT_OK);
                finish();
            }
        }

        if(requestCode == REQUEST_POSITION){
            if(resultCode == RESULT_OK){
                if(data != null){
                    lon.setText(data.getDoubleExtra(MapsActivity.LON, 0)+"");
                    lat.setText(data.getDoubleExtra(MapsActivity.LAT, 0)+"");
                }

            }
        }
    }

    public class Task1 extends AsyncTask<String, String, String>{
        double lon, lat;

        public Task1(double lat, double lon) {
            this.lon = lon;
            this.lat = lat;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            group.setVisibility(View.INVISIBLE);
            p.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try{
                String result = NetWorkUtils.getPositionInfo(lon, lat);
                return JsonUtils.getLieuFromPosition(new JSONObject(result));

            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            group.setVisibility(View.VISIBLE);
            p.setVisibility(View.INVISIBLE);
            String posi;
            if(s!=null){
                posi = s;
            }else{
                posi = "Lieu inconnue";
                Toast.makeText(AddLogementActivity1.this,
                        "Désolé, Nous n'avons pas pus determiner le nom du lieu.. Serveur OpenStreetmap inaccecible",
                        Toast.LENGTH_SHORT).show();

            }
            Toast.makeText(AddLogementActivity1.this,
                    "Localisation : "+lat+" "+lon+", " +posi,
                    Toast.LENGTH_SHORT).show();
                Intent i = new Intent(AddLogementActivity1.this, AddlogementActivity2.class);
                i.putExtra(Logement.PARAM_LIBELLE, titre.getText().toString().trim());
                i.putExtra(Logement.PARAM_description, description.getText().toString().trim());
                i.putExtra(Logement.PARAM_latitude, lat);
                i.putExtra(Logement.PARAM_longitude, lon);
                i.putExtra(Logement.PARAM_lieu,posi);
                i.putExtra(Logement.PARAM_loyer, Double.parseDouble(loyer.getText().toString().trim()));
                i.putExtra("type_id", ((TypeLogement)type.getSelectedItem()).getId());

                startActivityForResult(i, REQUEST_PHOTOS);


        }
    }
}
