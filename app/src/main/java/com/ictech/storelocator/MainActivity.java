package com.ictech.storelocator;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class MainActivity extends Activity {

    private static final String SESSTAG = "session";
    private static final String CURRENTITEM = "currentitem";
    private static final String GMAPSFRAG = "googlemapsfragment";
    private static final String ELENCO = "fragment_RecycleView";
    private FragmentManager fragmentManager = getFragmentManager();
    private GoogleFragment googleFragment;
    private StoreList storeList;
    private String session;
    private AHBottomNavigation bottomNavigation;
    private LocationManager locationManager;
    private Criteria criteria;
    private String bestProvider;
    private String latitude;
    private String longitude;
    private Location location;


    private Location lastKnownLocation;

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
        if (intent != null) {
            Bundle bundle = intent.getExtras();
            session = bundle.getString(SESSTAG);
        }

        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem home = new AHBottomNavigationItem("List", R.drawable.list);
        AHBottomNavigationItem map = new AHBottomNavigationItem("Map", R.drawable.compass);
        AHBottomNavigationItem user = new AHBottomNavigationItem("Profile", R.drawable.user);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#FEFEFE"));
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.addItem(home);
        bottomNavigation.addItem(map);
        bottomNavigation.addItem(user);




        geoLocation();
        Log.d("TAG", "localizazion ok");
        googleFragment = GoogleFragment.newInstance(session);
        storeList = StoreList.newInstance(session);
        Bundle bundle = new Bundle();
        bundle.putString(SESSTAG, session);


        if (latitude != null && longitude != null) {
            bundle.putString("latitude", latitude);
            Log.d("TAG", "not null lat");
            bundle.putString("longitude", longitude);
        } else {
            Log.d("TAG", "NULL");
           /* latitude = Double.toString(location.getLatitude());
            longitude = Double.toString(location.getLongitude());
            bundle.putString("latitude", latitude);
            bundle.putString("longitude", longitude);*/
            Log.d("TAG", latitude + longitude + " messi");
        }

        storeList.setArguments(bundle);
        googleFragment.setArguments(bundle);

        if (savedInstanceState != null) {
            session = savedInstanceState.getString(SESSTAG);
            bottomNavigation.setCurrentItem(savedInstanceState.getInt(CURRENTITEM));
        } else {
            fragmentManager.beginTransaction()
                    .add(R.id.frame, storeList, ELENCO)
                    .commit();
            bottomNavigation.setCurrentItem(0);
        }


        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if (position == 0) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, storeList, ELENCO)
                            .commit();
                }
                if (position == 1) {
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame, googleFragment, GMAPSFRAG)
                            .commit();
                }
            }
        });


    }


    public void geoLocation() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
                /*criteria=new Criteria();
                bestProvider=String.valueOf(locationManager.getBestProvider(criteria,true));*/


        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = locationManager.getLastKnownLocation("gps");
                if(location!=null){
                    Log.d("TAG","NON è NULL");
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());

                    Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();

                }else{
                    Log.d("TAG","è NULL");
                    location = locationManager.getLastKnownLocation("gps");
                    latitude = String.valueOf(location.getLatitude());
                    longitude = String.valueOf(location.getLongitude());
                    Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude+"location null", Toast.LENGTH_SHORT).show();
                }


    }




}
