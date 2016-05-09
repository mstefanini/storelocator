package com.ictech.storelocator;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

/**
 * Created by clemente on 27/04/16.
 */
public class DetailsActivity extends Activity {
    private String sessione;
    private String guid;
    private static final String URL = "http://its-bitrace.herokuapp.com/api/v2/stores/";
    private static final String HEADER = "x-bitrace-session";
    private JSONObject jsonObject;
    private String response;
    private double latitude;
    private double longitude;
    private TextView phone;
    private TextView address;
    private double zoom = 0.3;
    private TextView title;
    private TextView persona;
    private TextView description;
    private TextView email;
    private ImageButton phoneImage;
    private ImageButton addressImage;
    private ImageButton emailImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (savedInstanceState != null) {
            sessione = savedInstanceState.getString("sessione");
            guid = savedInstanceState.getString("guid");
        } else {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                guid = bundle.getString("guid");
                sessione = bundle.getString("session");
            }
        }

        phone = (TextView) findViewById(R.id.textPhone);
        address = (TextView) findViewById(R.id.textAddress);
        title = (TextView) findViewById(R.id.textTitle);
        description = (TextView) findViewById(R.id.textDescription);
        persona = (TextView) findViewById(R.id.person);
        email = (TextView) findViewById(R.id.textEmail);
        phoneImage = (ImageButton) findViewById(R.id.phoneImage);
        addressImage = (ImageButton) findViewById(R.id.addressImage);
        emailImage = (ImageButton) findViewById(R.id.emailImage);
        AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader(HEADER, sessione);
        client.get(URL + guid, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Toast.makeText(getApplicationContext(), "connessione ok", Toast.LENGTH_SHORT);
                try {
                    jsonObject = new JSONObject(new String(responseBody));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                try {
                    if (jsonObject.getBoolean("success")) {
                        response = jsonObject.getJSONObject("data").toString();
                        jsonObject = new JSONObject(response);
                        try {
                            title.setText(jsonObject.getString("name"));
                            phone.setText(jsonObject.getString("phone"));
                            address.setText(jsonObject.getString("address"));
                            latitude = jsonObject.getDouble("latitude");
                            longitude = jsonObject.getDouble("longitude");
                            description.setText(jsonObject.getString("description"));
                            persona.setText(jsonObject.getJSONObject("sales_person").getString("last") + " " + jsonObject.getJSONObject("sales_person").getString("first"));
                            email.setText(jsonObject.getJSONObject("sales_person").getString("email"));
                            ImageView imageView = (ImageView) findViewById(R.id.imageViewDetail);
                            // link img placeholder  Picasso.with(this).load(jsonObject.getString("featured_image")).into(imageView);
                            Picasso.with(getApplicationContext())
                                    .load("https://placeholdit.imgix.net/~text?txtsize=75&txt=800%C3%97800&w=800&h=800")
                                    .placeholder(R.drawable.jessecerchiopremuto)
                                    .into(imageView);
                            JSONArray t = jsonObject.getJSONArray("products");
                            LinearLayout layout = (LinearLayout) findViewById(R.id.products);
                            for (int i = 0; i < t.length(); i++) {
                                View child = getLayoutInflater().inflate(R.layout.product_view, null);
                                TextView pName = (TextView) child.findViewById(R.id.productName);
                                pName.setText(t.getJSONObject(i).getString("name") + " : ");
                                TextView pPrice = (TextView) child.findViewById(R.id.productPrice);
                                pPrice.setText(t.getJSONObject(i).getString("price") + " €");
                                String Available = "Available";
                                TextView vAvaible = (TextView) child.findViewById(R.id.available);
                                if (!t.getJSONObject(i).getBoolean("isAvailable"))
                                    Available = "Not Available";
                                vAvaible.setText(" " + Available);

                                if(Available.equals("Not Available")){
                                    vAvaible.setTextColor(Color.parseColor("#8c1d32"));
                                }else{
                                    vAvaible.setTextColor(Color.parseColor("#1a6631"));
                                }

                                layout.addView(child);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Connessione fallita", Toast.LENGTH_SHORT).show();
            }
        });

        phoneImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone.getText().toString())));
            }
        });


        addressImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri gmmIntentUri = Uri.parse("geo:" + latitude + "," + longitude + "?q=" + latitude + "," + longitude + "&z=" + zoom);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "Necessiti di Google Maps", Toast.LENGTH_SHORT).show();
                }
            }
        });


        emailImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:" + email.getText());
                intent.setData(data);
                startActivity(intent);
            }
        });

    }



    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("sessione", sessione);
        outState.putString("guid", guid);
        outState.putString("response", response);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
