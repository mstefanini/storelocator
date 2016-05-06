package com.ictech.storelocator;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleFragment extends Fragment{
    private static final String TAGSESSIONE = "lastSessio";
    private static final String RESPONSE = "response";
    private MapView mMapView;
    private GoogleMap google;
    private String sessione;
    private String url = "http://its-bitrace.herokuapp.com/api/v2/stores";
    private static final String HEADER = "x-bitrace-session";
    private JSONArray jsonArray;
    private JSONObject jObject;
    private String response;
    private double latitude;
    private double longitude;

    public interface IOsetMappe{
        double getMyLatitude();
        double getMyLongitude();
    }

    private IOsetMappe mListener = new IOsetMappe() {

        @Override
        public double getMyLatitude() {
            return 0;
        }

        @Override
        public double getMyLongitude() {
            return 0;
        }
    };
    public GoogleFragment() {
        // Required empty public constructor
    }

    public static GoogleFragment newInstance(String sess, double aLatitude, double aLongitude){
        GoogleFragment fragment = new GoogleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("session", sess);
        bundle.putDouble("latitude", aLatitude);
        bundle.putDouble("longitude", aLongitude);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(getActivity() instanceof IOsetMappe){
            mListener = (IOsetMappe)getActivity();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_google, container, false);
        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        google = mMapView.getMap();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        sessione = getArguments().getString("session");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


       if(savedInstanceState != null){
            if(savedInstanceState.getString(TAGSESSIONE).equals(sessione) && savedInstanceState.getString(RESPONSE) != null) {
                add2map(savedInstanceState.getString(RESPONSE));
                latitude = savedInstanceState.getDouble("latitude");
                longitude = savedInstanceState.getDouble("longitude");
            } else{
                Connection(url, sessione);
                latitude = mListener.getMyLatitude();
                longitude = mListener.getMyLongitude();
            }
        } else {
           Connection(url, sessione);
           latitude = mListener.getMyLatitude();
           longitude = mListener.getMyLongitude();
        }


        //setLocation();

        google.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude))
                .title("My Position")
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        google.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {

                String id = marker.getId();
                id = id.substring(1, id.length());
                if (Integer.parseInt(id) != 0) {
                    Intent intent = new Intent(getActivity(), DetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("session", sessione);
                    try {
                        bundle.putString("guid", jsonArray.getJSONObject(Integer.parseInt(id)).getString("guid"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }

        });



        return view;
    }


    public void add2map(String respons){
        response = respons;
        try {
            jObject = new JSONObject(respons);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            if(jObject.getBoolean("success")){
                jsonArray = jObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for (int i = 0; i < jsonArray.length(); i++) {
                        try {
                            JSONObject oneObject = jsonArray.getJSONObject(i);
                            google.addMarker(new MarkerOptions().position(
                                            new LatLng(oneObject.getDouble("latitude"), oneObject.getDouble("longitude"))).title(oneObject.getString("name")).snippet(
                                            "Phone: " + oneObject.getString("phone")).draggable(true)
                            );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Toast toast = Toast.makeText(getActivity(), jObject.getString("error"), Toast.LENGTH_SHORT);
                toast.show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void Connection(String URL, String session){

        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader(HEADER, session);
        client.get(URL, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        response = new String(responseBody);
                        add2map(response);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                         Toast.makeText(getActivity(), "Problemi di connessione", Toast.LENGTH_SHORT).show();
                        int seconds = 3;
                        long startTime = System.currentTimeMillis();
                        long endTime = System.currentTimeMillis() + (seconds * 1000);
                        long temp = startTime + 1000 ;
                        while (startTime < endTime) {
                            if (startTime == temp) {
                                seconds--;
                                temp += 1000;
                            }
                            startTime = System.currentTimeMillis();
                        }
                        Connection(url, sessione);

                    }
                }
        );
    }


    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(TAGSESSIONE, sessione);
        outState.putString(RESPONSE, response);
        outState.putDouble("latitude", latitude);
        outState.putDouble("longitude", longitude);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
