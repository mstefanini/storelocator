package com.ictech.storelocator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.ictech.storelocator.mainfragment.StoreList;


public class MainActivity extends Activity {

    StoreList mList;
    GoogleFragment mMaps;
    private static final String ELENCO = "elenco_fragment";
    private static final String MAPPA = "google_maps_fragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }
}
