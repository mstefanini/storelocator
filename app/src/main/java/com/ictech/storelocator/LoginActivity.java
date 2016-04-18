package com.ictech.storelocator;

import android.app.Activity;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;

import com.ictech.storelocator.loginfragment.First_fragment;

public class LoginActivity extends Activity
        implements First_fragment.OnFragmentInteractionListener {

    private static final String FragTag = "firstfragment";
    FragmentManager fragmentManager = getFragmentManager();
    First_fragment first_fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if( (first_fragment = (First_fragment) fragmentManager.findFragmentByTag(FragTag)) == null){
            first_fragment = First_fragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_frame, first_fragment, FragTag)
                    .commit();
        }
    }

    @Override
    public void onFragmentInteraction() {

        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, )
    }
}
