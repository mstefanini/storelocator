package com.ictech.storelocator.mainfragment;

import android.app.Activity;
import android.app.Fragment;
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
    public final String LISTA = "elenco_store";
    //TODO
    //inizializzo la variabile come Object generico intanto
    Object mList;

    public interface ResponseList {
        //TODO
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
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Activity vHostActivity = getActivity();
        if(vHostActivity instanceof ResponseList) {
            mResponseList = (ResponseList) vHostActivity;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putAll();
    }
}
