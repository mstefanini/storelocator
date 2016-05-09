package com.ictech.storelocator.loginfragment;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ictech.storelocator.MainActivity;
import com.ictech.storelocator.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link} interface
 * to handle interaction events.
 * Use the {@link Form_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Form_fragment extends Fragment {

    private static final String SESSTAG = "session";
    public static final String DATALOGIN = "data del login";
    private static final String url = "http://its-bitrace.herokuapp.com/api/public/v2/login";

    private OnFromInteraction mListener;

    public Form_fragment() {
        // Required empty public constructor
    }

    public static Form_fragment newInstance() {             
        Form_fragment fragment = new Form_fragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_form_fragment, container, false);    //vengono recuperati gli oggetti dalla view
        final EditText email = (EditText)view.findViewById(R.id.email);
        final EditText pswd = (EditText)view.findViewById(R.id.pswd);

        Button button = (Button)view.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MessageDigest md = null;
                try {
                    md = MessageDigest.getInstance("SHA-512");
                    if (md != null) {
                        md.update(pswd.getText().toString().getBytes());
                        byte byteData[] = md.digest();
                        String base64 = Base64.encodeToString(byteData, Base64.DEFAULT);
                        request(base64.replace("\n", ""), email.getText().toString(), getActivity());  //getContext è API 23  //esegue la richiesta della chiave di sessione dopo aver criptato la pswd(?)
                    } else {
                        requestError();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        });

        return view;
    }

    public void request(String psw, String email, final Context context){
        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.addHeader("Content-Type", "application/x-www-form-urlencoded");
        RequestParams requestParams = new RequestParams();
        requestParams.add("email", email);
        requestParams.add("password", psw);
        asyncHttpClient.post(url, requestParams, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Boolean success;
                try{
                    JSONObject jsonObject = new JSONObject(new String(responseBody));
                    Log.d("Prova", jsonObject.toString());
                    if((success = jsonObject.getBoolean("success"))){
                        Toast.makeText(getActivity(), "Benvenuto in Jesse!", Toast.LENGTH_LONG).show();
                        Bundle bundle = new Bundle();
                        bundle.putString(SESSTAG, jsonObject.getJSONObject("data").getString("session"));
                        bundle.putString(DATALOGIN, jsonObject.getString("data"));
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtras(bundle);

                        startActivity(intent);
                        getActivity().finish();


                    }else{
                        Toast.makeText(getActivity(), "Qualcosa è andato storto, riprova", Toast.LENGTH_LONG).show();
                    }
                }catch (JSONException e){
                    Toast.makeText(getActivity(), "Error on jsonObject", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(context, "Error on connection", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void requestError() {

    }



    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        if (context instanceof OnFromInteraction) {
            mListener = (OnFromInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onPause() {
        super.onPause();


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
    public interface OnFromInteraction {
        void onFromInteraction();
    }
}
