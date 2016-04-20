package com.ictech.storelocator;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ictech.storelocator.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Ingrid on 18/04/2016.
 */
public class StoreList extends Fragment {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private ResponseList iResponseList;
    private Negozio mNegozio;
    private StoreListAdapter mAdapter;
    private ArrayList<Negozio> listShop = new ArrayList<>();
    private static final String LISTA = "elenco_store";

    //TODO
    //inizializzo la variabile come Object generico intanto
    Object mList;

    public StoreList() {

    }

    public static StoreList newInstance(String string){
        StoreList fragment = new StoreList();
        Bundle bundle = new Bundle();
        bundle.putString(LISTA, string);
        fragment.setArguments(bundle);
        return fragment;
    }


    public interface ResponseList {
        //TODO: implementa interfaccia al fragment elenco store
        void updateList();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vView = inflater.inflate(R.layout.content_list_frame,container,false);

        if(savedInstanceState != null){
            mList = savedInstanceState.get(LISTA);
        }

        mRecyclerView = (RecyclerView) (vView.findViewById(R.id.list));
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        richiesta();

        mAdapter = new StoreListAdapter(getActivity(), listShop);
        mRecyclerView.setAdapter(mAdapter);

        return vView;
    }

    public void richiesta(){
        RequestParams params = new RequestParams();
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("x-bitrace-session", "12dcac5c-6bde-407a-813d-41da7a36999a");

        asyncHttpClient.get("http://its-bitrace.herokuapp.com/api/v2/stores", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                boolean success;

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));

                    success = jsonObject.getBoolean("success");
                    Log.d("Vidu", "" + jsonObject.getBoolean("success"));

                    Negozio element = new Negozio();
                    for(int i = 0; i<jsonObject.getJSONArray("data").length(); i++){

                        element.nome = jsonObject.getJSONArray("data").getJSONObject(i).getString("name");
                        element.indirizzo = jsonObject.getJSONArray("data").getJSONObject(i).getString("address");
                        element.telefono = jsonObject.getJSONArray("data").getJSONObject(i).getString("phone");
                        listShop.add(element);
                        mAdapter.notifyItemChanged(i);
                    }
                    //jsonObject.getJSONArray("data").getJSONObject(0).getString("phone");
                    Log.d("Vidu", "jhkjhkjh" + jsonObject.getJSONArray("data").getJSONObject(0).getString("phone"));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();

            }
        });
    }

    public void vadoSuLista(){
        if(iResponseList != null){
            iResponseList.updateList();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Context vHostActivity = getActivity();
        if(vHostActivity instanceof ResponseList) {
            iResponseList = (ResponseList) vHostActivity;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iResponseList = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: gestire ciclo vita fragment
    }
}
