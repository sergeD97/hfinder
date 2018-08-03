package com.hfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.preference.PreferenceManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.hfinder.adapter.HomeTabAdapter;
import com.hfinder.adapter.ListLogementAdapter;
import com.hfinder.adapter.MyLogementListAdapter;
import com.hfinder.database.DataBase;
import com.hfinder.database.Info;
import com.hfinder.entities.Logement;
import com.hfinder.entities.LogementTest;
import com.hfinder.fragments.pref.FavorisFragment;
import com.hfinder.fragments.pref.HomeFragment;
import com.hfinder.fragments.pref.MyFragment;
import com.hfinder.fragments.pref.SearchFragment;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener,
        FavorisFragment.OnFragmentInteractionListener, HomeFragment.OnFragmentInteractionListener, MyFragment.OnFragmentInteractionListener, SearchFragment.OnFragmentInteractionListener,
        ListLogementAdapter.LogementListClickListener, MyLogementListAdapter.ItemClickListener, SearchFragment.SearchButtonListener{

    public static final int REQUEST_ADD = 145;
    private DataBase mDb;
    HomeTabAdapter homeTabAdapter;
    ViewPager homeViewPager;
    TabLayout homeTabLayout;
    private TextView email, name;
    private FloatingActionButton fab;
    Long id_u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        id_u = 0L;

        mDb = DataBase.getInstance(getApplicationContext());



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HomeActivity.this, AddLogementActivity1.class);
                startActivityForResult(i, REQUEST_ADD);

            }
        });
        configureTabs();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_email);
        name =  (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_name);
       new HomeTask().execute("load-drawer");

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent set = new Intent(this, HomeSettingActivity.class);
            startActivity(set);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent i = new Intent(this, ProfilActivity.class);
            startActivity(i);
        } else if (id == R.id.nav_map) {
            Intent i = new Intent(this, MapsActivity.class);
            startActivity(i);

        } else if (id == R.id.logout) {
            new HomeTask().execute("logout");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Toast.makeText(HomeActivity.this, "changement de prefernces..", Toast.LENGTH_SHORT).show();

    }

    protected void onDestroy() {

        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
    }


    public void configureTabs(){
        homeTabAdapter = new HomeTabAdapter(getSupportFragmentManager());
        homeViewPager = (ViewPager)findViewById(R.id.home_viewpager);
        homeTabLayout = (TabLayout)findViewById(R.id.home_tab);

        homeViewPager.setAdapter(homeTabAdapter);
        homeTabLayout.setupWithViewPager(homeViewPager);
        homeTabLayout.setTabMode(TabLayout.MODE_FIXED);

        homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position == 2){
                   fab.setVisibility(View.VISIBLE);
                }else {
                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }

    @Override
    public void onLogementItemClick(Logement logement, int viewId) {

       if(viewId == R.id.img_item_home){
           Intent i = new Intent(HomeActivity.this, DetailLogement.class);

           i.putExtra(DetailLogement.ID_LOGEMENT, logement.getId());

           startActivity(i);
       }else if (viewId == R.id.item_home_interest){
           Intent i = new Intent(HomeActivity.this, MapsActivity.class);

           i.putExtra(DetailLogement.ID_LOGEMENT, logement.getId());

           startActivity(i);

       }

    }


    @Override
    public void onSearchClick(String lieu, Long idType, Double prix) {
        Intent i = new Intent(HomeActivity.this, SearchActivity.class);
        i.putExtra(SearchActivity.EX_PRIX_MAX, prix);
        i.putExtra(SearchActivity.EX_TYPE, idType);
        i.putExtra(SearchActivity.EX_LIEU, lieu);

        startActivity(i);
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

    class HomeTask extends AsyncTask<String, String, String[]>{

        @Override
        protected String[] doInBackground(String... strings) {

            if(strings[0].equals("load-drawer")){
                String[] r = new String[3];
                try{
                    r[0] = mDb.infoDAO().find(Info.USER_SURNAME).getValues()+" "+mDb.infoDAO().find(Info.USER_NAME).getValues();
                    r[1] = mDb.infoDAO().find(Info.USER_EMAIL).getValues();
                    r[2] = mDb.infoDAO().find(Info.USER_ID).getValues();
                    return r;
                }catch (Exception e){

                }


            }else{
                if(strings[0].equals("logout")){
                    String[] k = new String[1];
                    try{
                        mDb.infoDAO().deleteAll();
                        k[1] = "ok";
                    }catch (Exception e){
                        k[1] = "error";
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String[] s) {
            if(s != null && s.length==3){
                setUserValue(s[0], s[1]);
                id_u = Long.parseLong(s[2]);
               // Toast.makeText(HomeActivity.this, s[0]+" "+s[1], Toast.LENGTH_SHORT).show();
            }
            if(s != null && s.length==1){
                if(s[1].equals("ok")){
                    finishAndRemoveTask();
                   // NavUtils.navigateUpFromSameTask(HomeActivity.this);
                }else{
                    Toast.makeText(HomeActivity.this, "Oups.. Une erreur est survenue lors de la deconnexion..", Toast.LENGTH_SHORT).show();
                }

            }
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }
    }

    public void setUserValue(String nam, String mail){
        email.setText(mail);
        name.setText(nam);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_ADD){
            if(resultCode == RESULT_OK){
                Toast.makeText(HomeActivity.this, "Ajout reussie", Toast.LENGTH_SHORT).show();
                //reload the fragment;
                if(homeTabAdapter != null){
                    homeTabAdapter.getHomef().reload();
                    homeTabAdapter.getMy().reload();
                }
            }
        }
    }
}
