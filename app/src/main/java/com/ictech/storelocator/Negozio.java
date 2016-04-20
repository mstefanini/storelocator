package com.ictech.storelocator;

import android.content.Context;

/**
 * Created by Ingrid on 20/04/2016.
 */
public class Negozio {
    protected String nome;
    protected String indirizzo;
    protected String telefono;
    protected String immagine;

    public Negozio(){

    }

    public int getImageResourceId(Context context) {
        return context.getResources().getIdentifier(this.immagine, "drawable", context.getPackageName());
    }

}
