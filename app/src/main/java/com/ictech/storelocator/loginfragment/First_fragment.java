package com.ictech.storelocator.loginfragment;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ictech.storelocator.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link First_fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link First_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class First_fragment extends Fragment {

   private OnFragmentInteractionListener mListener;

    public First_fragment() {
        // Required empty public constructor
    }

    public static First_fragment newInstance() {
        return new First_fragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_first_fragment, container, false ); //recupera gli elementi della view
        final ImageButton imgBtn = (ImageButton)view.findViewById(R.id.imageButton);  //recupero imgbtn
        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Drawable backgrounds[] = new Drawable[2];
        Resources res = getResources();
        backgrounds[0] = res.getDrawable(android.R.drawable.btn_star_big_on);
        backgrounds[1] = res.getDrawable(android.R.drawable.btn_star_big_off);

        TransitionDrawable crossfader = new TransitionDrawable(backgrounds);

        ImageView image = (ImageView)findViewById(R.id.image);
        image.setImageDrawable(crossfader);

        crossfader.startTransition(3000);

*/
                imgBtn.setBackgroundResource(R.drawable.jessecerchiopremuto);
                mListener.onFragmentInteraction();
            }
        }); //associo al bottone il listner
        return view;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;   //dissocia il listner liberando memoria
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction();
    }
}
