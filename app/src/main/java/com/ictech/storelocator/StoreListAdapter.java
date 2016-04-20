package com.ictech.storelocator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ictech.storelocator.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Ingrid on 19/04/2016.
 */
public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder>{
    Context mContext;

    public StoreListAdapter(Context context) {
        this.mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout storeHolder;
        public LinearLayout storeNameHolder;
        public TextView storeName;
        public ImageView storeImage;

        public ViewHolder(View itemView) {
            super(itemView);
            storeHolder = (LinearLayout) itemView.findViewById(R.id.mainHolder);
            storeName = (TextView) itemView.findViewById(R.id.storeName);
            storeNameHolder = (LinearLayout) itemView.findViewById(R.id.storeNameHolder);
            storeImage = (ImageView) itemView.findViewById(R.id.storeImage);
        }
    }

    @Override
    public int getItemCount() {
        //TODO: prendere dal Bundle id negozio
        //returns the number of items from your data array.
        //return new PlaceData().placeList().size();
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_places, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        //TODO: collegare Store agli elementi del viewHolder
        //binds the Place object to the UI elements in ViewHolder. You’ll use Picasso to cache the images for the list.
        //final Place store = new PlaceData().placeList().get(position);
        //holder.storeName.setText(store.name);
        //Picasso.with(mContext).load(store.getImageResourceId(mContext)).into(holder.storeImage);
    }
}

