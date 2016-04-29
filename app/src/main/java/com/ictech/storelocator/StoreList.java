package com.ictech.storelocator;

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

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
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
    private static final String SESSION = "session";

    public StoreList() {

    }

    public static StoreList newInstance(String string){
        StoreList fragment = new StoreList();
        Bundle bundle = new Bundle();
        bundle.putString(SESSION, string);
        fragment.setArguments(bundle);
        return fragment;
    }


    public interface ResponseList {
        void updateList();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vView = inflater.inflate(R.layout.content_list_frame, container, false);
        String stringa = null;
        Bundle bundle;
        if((bundle = getArguments()) != null)
            stringa = bundle.getString(SESSION);

        if(savedInstanceState != null){
            stringa = savedInstanceState.getString(SESSION);
        }

        mRecyclerView = (RecyclerView)vView.findViewById(R.id.list);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);

        mAdapter = new StoreListAdapter(getActivity(), listShop, stringa);
        mRecyclerView.setAdapter(mAdapter);
        richiesta(stringa);

        return vView;
    }

    public void richiesta(String string){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("x-bitrace-session", string);

        asyncHttpClient.get("http://its-bitrace.herokuapp.com/api/v2/stores", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try{
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    boolean success;
                    if((success = jsonObject.getBoolean("success"))){
                        Log.d("prova", "siamo riusciti ad entrare! ");
                        Log.d("prova", ""+jsonObject.getJSONArray("data").length());
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        int length = jsonArray.length();
                        for (int i = 0; i < length; ++i) {
                            Negozio element = new Negozio();
                            element.nome = jsonObject.getJSONArray("data").getJSONObject(i).getString("name");
                            element.indirizzo = jsonObject.getJSONArray("data").getJSONObject(i).getString("address");
                            element.telefono = jsonObject.getJSONArray("data").getJSONObject(i).getString("phone");
                            element.guid = jsonObject.getJSONArray("data").getJSONObject(i).getString("guid");
                            listShop.add(element);
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        /*asyncHttpClient.get("", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {

                boolean success;

                try {
                    JSONObject jsonObject = new JSONObject(new String(responseBody));

                    success = jsonObject.getBoolean("success");
                    Log.d("Vidu", "" + jsonObject.getBoolean("success"));

                    Negozio element = new Negozio();
                    if (success) {
                        for (int i = 0; i < jsonObject.getJSONArray("data").length(); i++) {

                            element.nome = jsonObject.getJSONArray("data").getJSONObject(i).getString("name");
                            element.indirizzo = jsonObject.getJSONArray("data").getJSONObject(i).getString("address");
                            element.telefono = jsonObject.getJSONArray("data").getJSONObject(i).getString("phone");
                            listShop.add(element);
                            //mAdapter.notifyItemChanged(i);
                            mAdapter.notifyAll();
                        }
                        //jsonObject.getJSONArray("data").getJSONObject(0).getString("phone");
                        Log.d("Vidu", "jhkjhkjh" + jsonObject.getJSONArray("data").getJSONObject(0).getString("phone"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();

            }
        });*/
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
