package com.hfinder.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hfinder.fragments.pref.FavorisFragment;
import com.hfinder.fragments.pref.HomeFragment;
import com.hfinder.fragments.pref.MyFragment;
import com.hfinder.fragments.pref.SearchFragment;

/**
 * Created by root on 04/06/18.
 */




public class HomeTabAdapter extends FragmentPagerAdapter{

    HomeFragment homef;
    MyFragment my;
    SearchFragment search;

    public HomeFragment getHomef() {
        return homef;
    }

    public MyFragment getMy() {
        return my;
    }

    public SearchFragment getSearch() {
        return search;
    }

    public HomeTabAdapter(FragmentManager fm){
        super(fm);
        homef = HomeFragment.newInstance("home","fragment");
        my = MyFragment.newInstance("Mes etablissements", "fragment");
        search = SearchFragment.newInstance("rechercher", "fragment");
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return homef;
        }else if(position == 1){
            return search;
        }else if(position == 2){
            return my;
        }

        return HomeFragment.newInstance("home","fragment");

    }

    @Override
    public int getCount() {
        return 3;
    }




    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0){
            return "Accueil";
        }else if(position == 1){
            return "Recherche";
        }else if(position == 2){
            return "Mes Logements";
        }
        return "";
    }
}
