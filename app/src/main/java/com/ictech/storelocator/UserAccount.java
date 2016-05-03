package com.ictech.storelocator;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserAccount extends android.app.Fragment {

    private static final String SESSION = "session";
    private static final String DATALOGIN = "data";
    protected TextView nome;
    protected TextView cognome;
    protected TextView email;
    private String stringa;
    private String data;


    public static UserAccount newInstance(String aSession, String aData){
        UserAccount fragment = new UserAccount();
        Bundle bundle = new Bundle();
        bundle.putString(SESSION, aSession);
        bundle.putString(DATALOGIN, aData);
        fragment.setArguments(bundle);
        return fragment;
    }


    public UserAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vView = inflater.inflate(R.layout.fragment_user_account, container, false);

        Bundle bundle;
        if((bundle = getArguments()) != null)
            stringa = bundle.getString(SESSION);
            data = bundle.getString(DATALOGIN);

        if(savedInstanceState != null){
            stringa = savedInstanceState.getString(SESSION);
            data = savedInstanceState.getString(DATALOGIN);
        }

        nome = (TextView) vView.findViewById(R.id.nome);
        cognome = (TextView) vView.findViewById(R.id.cognome);
        email = (TextView) vView.findViewById(R.id.email);

        try {
            JSONObject jsonData = new JSONObject(data);

            nome.setText("" + jsonData.getString("name"));
            cognome.setText("" + jsonData.getString("surname"));
            email.setText("" + jsonData.getString("email"));

        } catch(Exception e) {
            e.printStackTrace();
        }

        return vView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SESSION, stringa);
        outState.putString(DATALOGIN, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }
}
