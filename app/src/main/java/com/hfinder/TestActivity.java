package com.hfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.hfinder.database.DataBase;
import com.hfinder.database.Info;

public class TestActivity extends AppCompatActivity {

    private DataBase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);
        mDb = DataBase.getInstance(getApplicationContext());
        new Task().execute();
    }

    class Task extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... strings) {
            String r = null;
            try{
                r = mDb.infoDAO().find(Info.USER_ID).getValues();

            }catch(Exception e){

            }
            return r;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Intent i;
            if(s == null){
                i = new Intent(TestActivity.this, LoginActivity.class);
            }else{
                i = new Intent(TestActivity.this, HomeActivity.class);
            }
            startActivity(i);
            finish();
        }
    }
}
