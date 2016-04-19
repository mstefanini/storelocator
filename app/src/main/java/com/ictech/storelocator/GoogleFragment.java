package com.ictech.storelocator;


import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.NetworkOnMainThreadException;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.loopj.android.http.HttpGet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.NameValuePair;
import cz.msebera.android.httpclient.client.ClientProtocolException;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.entity.UrlEncodedFormEntity;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.message.BasicNameValuePair;


/**
 * A simple {@link Fragment} subclass.
 */
public class GoogleFragment extends Fragment{

    private MapView mMapView;
    private GoogleMap google;
    private String sessione;
    private String url = "http://its-bitrace.herokuapp.com/api/v2/stores";
    private String header = "x-bitrace-session";
    private JSONArray jsonArray;


    public GoogleFragment() {
        // Required empty public constructor
    }

    public static GoogleFragment newInstance(String sess){
        GoogleFragment fragment = new GoogleFragment();
        Bundle bundle = new Bundle();
        bundle.putString("session", sess);
        fragment.setArguments(bundle);
        return fragment;
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
        google = mMapView.getMap();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        // CANCELLARE DA QUI A STOP (RIGA 159) E PRENDERE SESSIONE DA MATTEO
        HttpResponse httpResponse;
        HttpClient clientPost = new DefaultHttpClient();

        HttpPost httpPost = new HttpPost("http://its-bitrace.herokuapp.com/api/public/v2/login");
        ArrayList<NameValuePair> myKey = new ArrayList<>();
        myKey.add(new BasicNameValuePair("email", "tsac-2015@tecnicosuperiorekennedy.it"));
        myKey.add(new BasicNameValuePair("password", "AkL6KhBcibHLVGZbs/JyBJqMCGB6nDLK/0ovxGZHojt6EepTxpdfygqKsIWz3Q4FS4wyHY4cIrP1W8nHAd8F4A=="));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(myKey));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //making POST request.
        InputStream postStream = null;
        String resultPost ="";
        try {
            httpResponse = clientPost.execute(httpPost);
            HttpEntity entityPost = httpResponse.getEntity();
            postStream = entityPost.getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(postStream, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            resultPost = sb.toString();
        } catch (NetworkOnMainThreadException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (postStream != null)
                    postStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        JSONObject jsoPost = null;
        try {
            jsoPost = new JSONObject(resultPost);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {

            if(jsoPost.getBoolean("success")){
                JSONObject temp = jsoPost.getJSONObject("data");

                sessione = temp.getString("session");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //SBLOCCARE LA RIGA PER AVERE LA SESSIONE DA MATTEO ED ELIMINA TUTTO QUELLO SOPRA
        //sessione = getArguments().getString("session");
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
                jsonArray = jObject.getJSONArray("data");
                if(jsonArray.length() > 0){
                    for (int i = 0; i < jsonArray.length(); i++) {

                        try {
                            JSONObject oneObject = jsonArray.getJSONObject(i);

                            google.addMarker( new MarkerOptions().position(
                                            new LatLng(oneObject.getDouble("latitude"), oneObject.getDouble("longitude"))).title(oneObject.getString("name")).draggable(true)
                            );
                            Log.d("TAG",oneObject.getDouble("latitude") + " " +  oneObject.getDouble("longitude"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        google.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(Marker marker) {
                Intent intent = new Intent(getActivity(), DetailsActivity.class);
                Bundle bundle = new Bundle();

                bundle.putString("session", sessione);
                try {
                    String id = marker.getId();
                    id = id.substring(1,id.length());
                    bundle.putString("guid", jsonArray.getJSONObject(Integer.parseInt(id)).getString("guid"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
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
