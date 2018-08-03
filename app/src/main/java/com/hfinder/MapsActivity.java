package com.hfinder;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hfinder.entities.Logement;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String ACTION_CHOOSE_POSITION = "com.hfinder.action.choose.postion";
    public static final String LAT = "latitude";
    public static final String LON = "longitude";


    private GoogleMap mMap;
    private LocationManager lm;
    private Intent mIntent;
    private LatLng me;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mIntent = getIntent();
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ActionBar actionBar = this.getSupportActionBar();


        // Set the action bar back button to look like an up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            if(mIntent.getAction() != null){
                if(mIntent.getAction().equals(ACTION_CHOOSE_POSITION)){
                    actionBar.setTitle("Choisissez une position ");
                }
            }

        }

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Location lo = getPosition();
        if(mIntent.getAction()==null){


            if(lo != null){
                me = new LatLng(lo.getLatitude(), lo.getLongitude());
                mMap.addMarker(new MarkerOptions().position(me).title("Votre Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

            }

            Long id = getIntent().getLongExtra(DetailLogement.ID_LOGEMENT, -1);
            if(id == -1){
                if(me != null){
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 14));
                }

            }
            new Task().execute(id);
        }else if(mIntent.getAction().equals(ACTION_CHOOSE_POSITION)){
            if(lo != null){
                me = new LatLng(lo.getLatitude(), lo.getLongitude());
                mMap.addMarker(new MarkerOptions().position(me).title("Votre Position").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(me, 18));
            }
            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    Intent i = new Intent();
                    i.putExtra(LAT, latLng.latitude);
                    i.putExtra(LON, latLng.longitude);
                    setResult(RESULT_OK, i);
                    finish();

                }
            });
        }


        // Add a marker in Sydney and move the camera



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.map_menu, menu);
        return true;
    }


    class Task extends AsyncTask<Long, String, List<Logement>>{
        @Override
        protected List<Logement> doInBackground(Long... longs) {
            List<Logement> ll = null;

            try{
                if(longs[0] == -1){
                    String result = NetWorkUtils.getResponseFromHttpUrl(new URL(NetWorkUtils.BASE_SERVER_URL+"/logement"));
                    ll = JsonUtils.logementListParser(new JSONArray(result));
                }else{
                    String result = NetWorkUtils.getResponseFromHttpUrl(new URL(NetWorkUtils.BASE_SERVER_URL+"/logement/"+longs[0]));
                    ll = new ArrayList<>();
                    ll.add(JsonUtils.logementParser(new JSONObject(result)));
                }
                return ll;
            }catch(Exception e){

            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Logement> logements) {
            super.onPostExecute(logements);
            if(logements == null){
                Toast.makeText(MapsActivity.this, "Erreur lors du chargement des logement...", Toast.LENGTH_LONG).show();

            }else{
                if(logements.size() == 1){
                    LatLng poslo = new LatLng(logements.get(0).getLatitude(), logements.get(0).getLongitude());
                    mMap.addMarker(new MarkerOptions().position(poslo).title(logements.get(0).getLibelle()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(poslo, 14));
                }else{
                    for(Logement lop : logements){
                        LatLng poslo = new LatLng(lop.getLatitude(), lop.getLongitude());

                        mMap.addMarker(new MarkerOptions().position(poslo).title(lop.getLibelle()).zIndex(lop.getId()));
                    }

                    mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(Marker marker) {


                            try{
                                String indf = String.valueOf(marker.getZIndex());
                                int end = indf.indexOf(".");

                                Long idl = Long.parseLong(indf.substring(0,end));
                                Intent i = new Intent(MapsActivity.this, DetailLogement.class);
                                i.putExtra(DetailLogement.ID_LOGEMENT, idl);
                                startActivity(i);
                                return true;
                            }catch (Exception e){
                                Toast.makeText(MapsActivity.this, "id : "+e.getMessage(), Toast.LENGTH_LONG).show();

                            }
                            return false;
                        }
                    });

                }
            }

        }
    }







    //////////////////////////////////////////////////////////


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
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // When the home button is pressed, take the user back to the VisualizerActivity
        if (id == android.R.id.home) {
            // NavUtils.navigateUpFromSameTask(this);
            setResult(RESULT_CANCELED);
            finish();
        }else if (id == R.id.loc){
            Toast.makeText(this,
                    "Votre position..",
                    Toast.LENGTH_SHORT).show();
            if(me != null){
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(me, 14));

            }
        }
        return super.onOptionsItemSelected(item);
    }
}
