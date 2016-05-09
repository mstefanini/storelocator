package com.ictech.storelocator;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;

import com.ictech.storelocator.loginfragment.First_fragment;
import com.ictech.storelocator.loginfragment.Form_fragment;

import java.text.Normalizer;

public class LoginActivity extends Activity
        implements First_fragment.OnFragmentInteractionListener,
                    Form_fragment.OnFromInteraction {

    private static final String FragTag = "firstfragment";
    private static final String FragTag2 = "formfragment";
    FragmentManager fragmentManager = getFragmentManager();
    Form_fragment form_fragment;
    First_fragment first_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);   //carica il layout associato alla activity

        if( (form_fragment = (Form_fragment) fragmentManager.findFragmentByTag(FragTag2)) == null){
            form_fragment = Form_fragment.newInstance();      //se non esiste ancora il fragment di firstfragment lo inizializza
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_frame, form_fragment, FragTag2)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);

        //ft.setCustomAnimations(R.anim.slide_in, R.anim.hyperspace_out, R.anim.hyperspace_in, R.anim.slide_out );


        form_fragment = Form_fragment.newInstance();

        ft.replace(R.id.fragment_frame, form_fragment, FragTag2);
        ft.addToBackStack(null);
        // Start the animated transition.
        ft.commit();
        /*form_fragment = Form_fragment.newInstance();    //crea l'istanza del form fragment
        fragmentManager.beginTransaction()              //switch da first_fragment a form_fragment
                .replace(R.id.fragment_frame, form_fragment, FragTag2)
                .commit(); */
    }

   /*
    FragmentTransaction ft = getFragmentManager().beginTransaction();
ft.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right);

DetailsFragment newFragment = DetailsFragment.newInstance();

ft.replace(R.id.details_fragment_container, newFragment, "detailFragment");

// Start the animated transition.
ft.commit();
 */

    @Override
    public void onFromInteraction() {
    }
}
