package com.ictech.storelocator;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Ingrid on 19/04/2016.
 */
public class StoreListAdapter extends RecyclerView.Adapter<StoreListAdapter.ViewHolder>{
    Context mContext;
    ArrayList<Negozio> arrayList;
    String session;

    public StoreListAdapter(Context context, ArrayList<Negozio> arrayList, String session) {
        this.mContext = context;
        this.arrayList = arrayList;
        this.session = session;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView storeName;
        public TextView storeIndex;
        public ImageView storeImage;
        public CardView cardView;
        public ImageView storePhone;

        public ViewHolder(View itemView) {
            super(itemView);
            storeName = (TextView) itemView.findViewById(R.id.storeName);
            storeIndex = (TextView) itemView.findViewById(R.id.storeIndex);
            storeImage = (ImageView) itemView.findViewById(R.id.storeImage);
            cardView = (CardView) itemView.findViewById(R.id.placeCard);
            storePhone = (ImageView) itemView.findViewById(R.id.storePhone);
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
        final Negozio negozio = arrayList.get(position);
        holder.storeName.setText(negozio.nome);
        holder.storeIndex.setText(negozio.indirizzo);
        holder.storeImage.setImageResource(R.drawable.logo_list);
        holder.storePhone.setImageResource(R.drawable.phone);
        holder.storePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ActivityCompat.checkSelfPermission( view.getContext() , android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    //   here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                view.getContext().startActivity(new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + negozio.telefono)));
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), DetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("guid", negozio.guid);
                bundle.putString("session", session);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }
        });
    }
}

