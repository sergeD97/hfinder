package com.hfinder.fragments.pref;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.hfinder.AddLogementActivity1;
import com.hfinder.R;
import com.hfinder.adapter.TypeSpinnerAdapter;
import com.hfinder.entities.TypeLogement;
import com.hfinder.utils.JsonUtils;
import com.hfinder.utils.NetWorkUtils;

import org.json.JSONArray;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SearchFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private boolean create;
    private EditText lieu, prix;
    private Spinner type;
    private Button search;
    private Context context;
    private List<TypeLogement> ltl;

    private OnFragmentInteractionListener mListener;
    private SearchButtonListener slistener;
    TypeSpinnerAdapter tsa;

    public SearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        if(create){
            lieu = (EditText)root.findViewById(R.id.lieu);
            prix = (EditText)root.findViewById(R.id.prix);
            type = (Spinner)root.findViewById(R.id.type);
            search = (Button)root.findViewById(R.id.search);
            ltl = new ArrayList<>();
            ltl.add(new TypeLogement(0L));



            search.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String l = lieu.getText().toString().trim();
                    String p = prix.getText().toString().trim();
                    Long idType;
                    try{
                        idType = ((TypeLogement)type.getSelectedItem()).getId();
                    }catch (Exception e){
                        idType = 0L;
                    }


                    if(l.isEmpty() && p.isEmpty() && idType==0){
                        Toast.makeText(context,"Veuiller specifier au moins un critere de recherche...", Toast.LENGTH_LONG).show();
                         }else{
                        try{
                            if(p.trim().equals("")){
                                p = "0.0";
                            }
                            Double pp = Double.parseDouble(p);
                            slistener.onSearchClick(l,idType,pp);
                        }catch (Exception e){
                            Toast.makeText(context,"Erreur...", Toast.LENGTH_LONG).show();


                        }

                    }
                }
            });
            new Task().execute("load_type");

            create = false;
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
        if (context instanceof OnFragmentInteractionListener && context instanceof SearchButtonListener) {
            mListener = (OnFragmentInteractionListener) context;
            slistener = (SearchButtonListener)context;
            this.context = context;
            //
            create = true;

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

    public interface SearchButtonListener{
        public void onSearchClick(String lieu, Long idType, Double prix);
    }


    class Task extends AsyncTask<String, String, List<TypeLogement>> {
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


            }else{
                //all things is okay
                s.add(0, new TypeLogement(0L));
                    ltl = s;
                    tsa = new TypeSpinnerAdapter(context,android.R.layout.simple_spinner_item,ltl);
                    tsa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    type.setAdapter(tsa);


            }
            super.onPostExecute(s);
        }


    }
}