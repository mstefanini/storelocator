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

public class MainActivity extends Activity implements LocationListener {

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
    private double latitude;
    private double longitude;
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
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //geoLocation();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, this);

        googleFragment = GoogleFragment.newInstance(session, latitude, longitude);
        storeList = StoreList.newInstance(session);

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
        Log.d("TAG", "localizazion start");
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequ7estPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            if (isLocationEnabled()) {

                //criteria = new Criteria();
                // bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();
                /*locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,100,0, this);
                //location = locationManager.getLastKnownLocation(bestProvider);*/
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                Log.d("TAG", "GPS is on");
                Log.d("TAG", "Location = " + location);
                if (location != null) {
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                }
                Toast.makeText(MainActivity.this, "latitude:" + latitude + " longitude:" + longitude, Toast.LENGTH_SHORT).show();

            }
        } else {
            AlertDialog.Builder notifyLocationServices = new AlertDialog.Builder(MainActivity.this);
            notifyLocationServices.setTitle("Switch on Location Services");
            notifyLocationServices.setMessage("Location Services must be turned on to complete this action. Also please take note that if on a very weak network connection,  such as 'E' Mobile Data or 'Very weak Wifi-Connections' it may take even 15 mins to load. If on a very weak network connection as stated above, location returned to application may be null or nothing and cause the application to crash.");
            notifyLocationServices.setPositiveButton("Ok, Open Settings", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent openLocationSettings = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(openLocationSettings);

                }
            });
            notifyLocationServices.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            notifyLocationServices.show();
        }


        Log.d("TAG", "localizazion end");
    }


    public boolean isLocationEnabled(Context context) {
        int locationMode = 0;

        try {
            locationMode = Settings.Secure.getInt(context.getContentResolver(), Settings.Secure.LOCATION_MODE);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return locationMode != Settings.Secure.LOCATION_MODE_OFF;


    }

    public boolean isLocationEnabled() {
        return locationManager
                .isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    @Override
    public void onLocationChanged(Location locationC) {
        location = locationC;
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
        googleFragment = GoogleFragment.newInstance(session, locationC.getLatitude(), locationC.getLongitude());
       // googleFragment.setLocation(locationC);
        locationManager.removeUpdates(this);
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
}
