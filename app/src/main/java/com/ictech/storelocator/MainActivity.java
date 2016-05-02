package com.ictech.storelocator;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

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
    Location lastKnownLocation;

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


        googleFragment = GoogleFragment.newInstance(session);
        storeList = StoreList.newInstance(session);
        Bundle bundle = new Bundle();
        bundle.putString(SESSTAG, session);

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {

            @Override
            public void onLocationChanged(Location location) {
                lastKnownLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

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

        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        String locationProvider = LocationManager.GPS_PROVIDER;
        locationManager.requestLocationUpdates(locationProvider, 0, 0, locationListener);
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if( location != null) {
            bundle.putString("latitude", String.valueOf(location.getLatitude()));
            bundle.putString("longitude", String.valueOf(location.getLongitude()));

        }

        storeList.setArguments(bundle);
        googleFragment.setArguments(bundle);

        if(savedInstanceState != null) {
            session = savedInstanceState.getString(SESSTAG);
            bottomNavigation.setCurrentItem(savedInstanceState.getInt(CURRENTITEM));
        }else{
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
}
