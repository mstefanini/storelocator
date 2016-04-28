package com.ictech.storelocator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ictech.storelocator.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ingrid on 19/04/2016.
 */
public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder>{
    Context mContext;
    ArrayList<Negozio> arrayList;

    public StoreListAdapter(Context context, ArrayList<Negozio> arrayList) {
        this.mContext = context;
        this.arrayList = arrayList;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView storeName;
        public ImageView storeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            storeName = (TextView) itemView.findViewById(R.id.storeName);
            storeImage = (ImageView) itemView.findViewById(R.id.storeImage);
        }
    }

    @Override
    public int getItemCount() {
        return  arrayList.size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Negozio negozio = arrayList.get(position);
        Log.d("bind", negozio.nome);
        holder.storeName.setText(negozio.nome + "\n" + negozio.indirizzo + "\n" + negozio.telefono);
        holder.storeImage.setImageResource(R.drawable.jessecerchiopremuto);
    }
}

