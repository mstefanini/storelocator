package com.ictech.storelocator;


import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;


/**
 * A simple {@link Fragment} subclass.
 */
public abstract class GoogleFragment extends Fragment{

    private MapView mMapView;
    private GoogleMap google;
    private String sessione;
    private String url = "http://its-bitrace.herokuapp.com/api/v2/stores";
    private String header = "x-bitrace-session";


    public GoogleFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_google, container, false);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        sessione = getArguments().getString("session");
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet(url);
        request.setHeader(header, sessione);
        HttpResponse response;
        InputStream inputStream = null;
        String result = "";
        try {
            response = client.execute(request);
            HttpEntity HttpEntity = response.getEntity();
            inputStream = HttpEntity.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            result = sb.toString();
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JSONObject jObject = null;
        try {
            jObject = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            if(jObject.getBoolean("success")){
                JSONArray jsonArray = jObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {
                            JSONObject oneObject = jsonArray.getJSONObject(i);
                            google = mMapView.getMap();
                                        // adding marker
                            google.addMarker( new MarkerOptions().position(
                                            new LatLng(oneObject.getDouble("latitude"), oneObject.getDouble("longitude"))).title(oneObject.getString("name"))
                            );
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return view;
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
