package com.hfinder.fragments.pref;

import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.hfinder.R;
import com.hfinder.adapter.ListLogementAdapter;
import com.hfinder.entities.Logement;
import com.hfinder.entities.LogementTest;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONArray;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private RecyclerView listLogementView;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private View root;
    private Context context;
    private LinearLayoutManager layoutManager;
    List<Logement> listL;
    ListLogementAdapter.LogementListClickListener llisteneser;
    boolean viewCreate;
    private ListLogementAdapter adapter;
    boolean init;
    ProgressBar progress;
    TextView error;
    Button retry;


    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
            root = inflater.inflate(R.layout.fragment_home, container, false);
            listLogementView = (RecyclerView)root.findViewById(R.id.home_list);
            listLogementView.setLayoutManager(layoutManager);
            listLogementView.setHasFixedSize(true);
            listLogementView.setAdapter(adapter);

            retry = (Button)root.findViewById(R.id.retry);
            progress = (ProgressBar) root.findViewById(R.id.progressBar);
            error = (TextView) root.findViewById(R.id.error);
            retry.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // task.setAction("init");
                    new Task(true).execute();
                }
            });


            viewCreate = true;
            new Task(true).execute();
        }else{
            //new Task(false).execute();
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
        if (context instanceof OnFragmentInteractionListener && context instanceof ListLogementAdapter.LogementListClickListener) {
            mListener = (OnFragmentInteractionListener) context;
            layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            this.context = context;
            llisteneser = (ListLogementAdapter.LogementListClickListener)this.context;

            /////test data//////////////////load data here
            listL = new ArrayList<>();
            listL.add(new Logement());
            listL.add(new Logement());

            viewCreate = false;
            adapter= new ListLogementAdapter(listL, llisteneser);
            init = true;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }



    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    /**
     * Task to load data frm the server.
     */
    class Task extends AsyncTask<String, String, List<Logement>> {
        private boolean start;

        public Task(boolean start){
            this.start = start;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(start){
                listLogementView.setVisibility(View.INVISIBLE);
                retry.setVisibility(View.INVISIBLE);
                error.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
            }

        }

        @Override
        protected List<Logement> doInBackground(String... strings) {
            String result = "";

            try{
                result = NetWorkUtils.getResponseFromHttpUrl(new URL(NetWorkUtils.BASE_SERVER_URL+"/logement"));

                return JsonUtils.logementListParser(new JSONArray(result));
            }catch(Exception e){
                List<Logement> li= new ArrayList<>();
                Logement m = new Logement();
                m.setLibelle(e.getMessage());
                li.add(m);
                return  li;

            }

        }

        @Override
        protected void onPostExecute(List<Logement> logements) {
            super.onPostExecute(logements);
            progress.setVisibility(View.INVISIBLE);
            if(logements.size() == 1){
                if(start){
                    error.setText("Impossible d'acceder au serveur");
                    error.setVisibility(View.VISIBLE);
                    retry.setVisibility(View.VISIBLE);
                }else{
                    Snackbar.make(error, logements.get(0).getLibelle(), Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }else{
                listLogementView.setVisibility(View.VISIBLE);
                error.setVisibility(View.INVISIBLE);
                retry.setVisibility(View.INVISIBLE);
                if(true){
                    listL = logements;
                    adapter.setList(listL);
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    public void reload(){
        new Task(true).execute();
    }

    ///task for logement picture loading.

}
