package com.ictech.storelocator;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class MainActivity extends Activity {

    private static final String SESSTAG = "session";
    private static final String CURRENTITEM = "currentitem";
    private static final String GMAPSFRAG = "googlemapsfragment";
    private static final String HOMEFRAG = "homefragment";
    private FragmentManager fragmentManager = getFragmentManager();
    private GoogleFragment googleFragment;
    private String session;
    private AHBottomNavigation bottomNavigation;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SESSTAG, session);
        outState.putInt(CURRENTITEM, bottomNavigation.getCurrentItem());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if(intent != null){
            Bundle bundle = intent.getExtras();
            session = bundle.getString(SESSTAG);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem home = new AHBottomNavigationItem("Home", R.mipmap.ic_launcher);
        AHBottomNavigationItem map = new AHBottomNavigationItem("Map", R.mipmap.ic_launcher);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.addItem(home);
        bottomNavigation.addItem(map);

        if(savedInstanceState != null) {
            session = savedInstanceState.getString(SESSTAG);
            bottomNavigation.setCurrentItem(savedInstanceState.getInt(CURRENTITEM));
        }else{
            bottomNavigation.setCurrentItem(0);
        }

        googleFragment = GoogleFragment.newInstance(session);
        // TODO: aggiungere fragment lista!


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    // TODO
                }
                if (position == 1) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, googleFragment, GMAPSFRAG)
                            .commit();
                }
            }
        });




    }
}
