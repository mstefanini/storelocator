package com.ictech.storelocator.mainfragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ictech.storelocator.R;

/**
 * Created by Ingrid on 18/04/2016.
 */
public class StoreList extends Fragment {

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private ResponseList mResponseList;
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

        return vView;
    }

    public void vadoSuLista(){
        if(mResponseList != null){
            mResponseList.updateList();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Context vHostActivity = getContext();
        if(vHostActivity instanceof ResponseList) {
            mResponseList = (ResponseList) vHostActivity;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //TODO: gestire ciclo vita fragment
    }
}
