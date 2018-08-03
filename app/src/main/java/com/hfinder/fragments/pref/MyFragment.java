package com.hfinder.fragments.pref;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hfinder.R;
import com.hfinder.adapter.MyLogementListAdapter;
import com.hfinder.database.DataBase;
import com.hfinder.database.Info;
import com.hfinder.entities.Logement;
import com.hfinder.entities.LogementTest;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView listLogementView;
    ProgressBar progress;
    TextView error;
    Button retry;
    private View root;
    private Context context;
    private LinearLayoutManager layoutManager;
    private MyLogementListAdapter.ItemClickListener llistener;
    List<Logement> listL;
    boolean viewCreate;
    DataBase mDb;
    MyLogementListAdapter adapter;
    private int instance=0;

    private OnFragmentInteractionListener mListener;

    public MyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyFragment newInstance(String param1, String param2) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(!viewCreate){
            root =  inflater.inflate(R.layout.fragment_my, container, false);
            listLogementView = (RecyclerView)root.findViewById(R.id.my_list);
            listLogementView.setLayoutManager(layoutManager);
            listLogementView.setHasFixedSize(true);
            listLogementView.setAdapter(adapter);
            listLogementView.setVisibility(View.VISIBLE);
            retry = (Button)root.findViewById(R.id.retry);
            progress = (ProgressBar) root.findViewById(R.id.progressBar);
            error = (TextView) root.findViewById(R.id.error);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  // task.setAction("init");
                    new Task("init").execute();
                }
            });

            new Task("init").execute();

            viewCreate = true;
        }

        return root;
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener && context instanceof MyLogementListAdapter.ItemClickListener) {
            mListener = (OnFragmentInteractionListener) context;
            llistener = (MyLogementListAdapter.ItemClickListener)context;
            this.context = context;
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            listL = new ArrayList<>();
            listL.add(new Logement());listL.add(new Logement());listL.add(new Logement());listL.add(new Logement());listL.add(new Logement());
            adapter = new MyLogementListAdapter(listL, llistener);
            mDb = DataBase.getInstance(context.getApplicationContext());
            instance = 0;
            viewCreate = false;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener and ItemclicKlistener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

   public class Task extends AsyncTask<String, String, List<Logement>>{

        private String action;
        private boolean treat = true;


        public String getAction() {
            return action;
        }

        public void setTreat(boolean treat) {
            this.treat = treat;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public Task(String action) {
            this.action = action;
        }

        @Override
        protected void onPreExecute() {
            if(action.equals("init")){
                progress.setVisibility(View.VISIBLE);
                retry.setVisibility(View.INVISIBLE);
                listLogementView.setVisibility(View.INVISIBLE);
                error.setVisibility(View.INVISIBLE);
            }else if(action.equals("refresh")){

            }
            super.onPreExecute();
        }

        @Override
        protected List<Logement> doInBackground(String... strings) {
            List<Logement> l = null;

            String result = "";
            try{
                String idUser = mDb.infoDAO().find(Info.USER_ID).getValues();
                result = NetWorkUtils.getResponseFromHttpUrl(new URL(NetWorkUtils.BASE_SERVER_URL+"/utilisateur/"+idUser+"/logement_cree"));
                 l = JsonUtils.logementListParser(new JSONArray(result));
                return l;
            }catch(JSONException jse){
                return new ArrayList<>();
            }catch (Exception e){
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Logement> logements) {
            progress.setVisibility(View.INVISIBLE);
            if(logements==null){
                if(instance == 0){
                    error.setText("Impossible de trouver le serveur Hfinder");
                    error.setVisibility(View.VISIBLE);
                    retry.setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(error, "connection perdu avec le serveur", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }


            }else if(logements.size() == 0){
                error.setText("Vous n'avez aucun logement creé");
                error.setVisibility(View.VISIBLE);

            }else{
                //
                if(logements.size() != listL.size()){
                    adapter.setListLogement(logements);
                    adapter.notifyDataSetChanged();
                    listL = new ArrayList<>(logements);
                }
                error.setVisibility(View.INVISIBLE);
                retry.setVisibility(View.INVISIBLE);
                if(listLogementView != null){
                    listLogementView.setVisibility(View.VISIBLE);
                }
                if(instance != 1){
                    instance =1;
                }



            }
            super.onPostExecute(logements);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void reload(){
        new Task("init").execute();
    }
}
