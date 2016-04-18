package com.ictech.storelocator;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;

import com.ictech.storelocator.loginfragment.First_fragment;
import com.ictech.storelocator.loginfragment.Form_fragment;

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
        form_fragment = Form_fragment.newInstance();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_frame, form_fragment, FragTag2)
                .commit();
    }

    @Override
    public void onFromInteraction() {
    }
}
