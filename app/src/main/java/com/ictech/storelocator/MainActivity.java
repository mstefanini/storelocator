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

        // inizializzo fragment di elenco degli store
        FragmentManager fm = getFragmentManager();
        mList = (StoreList) fm.findFragmentByTag(ELENCO);
        if(mList == null) {
            mList = new StoreList();
            FragmentTransaction vTrans = fm.beginTransaction()
                        .add(R.id.container, mList, ELENCO)
                        .commit();
        }

        //inizializzo fragmnet della mappa di google

        FragmentManager fmGoogle = getFragmentManager();
        mMaps = (GoogleFragment) fmGoogle.findFragmentByTag(MAPPA);
        if(mMaps == null){
            mMaps = new GoogleFragment();
            FragmentTransaction vTrans = fmGoogle.beginTransaction()
                        .add(R.id.container, mMaps, MAPPA)
                        .commit();
        }
    }
}
