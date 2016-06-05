package com.soneyu.smshub.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.soneyu.App;
import com.soneyu.smshub.R;

public class messagesFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Button save_stuff, reset_stuff;
    private OnFragmentInteractionListener mListener;
    private EditText add_client, ready_client, ping_client, cancel_client;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaitTimeTextReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static messagesFragment newInstance(String param1, String param2)
    {
        messagesFragment fragment = new messagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public messagesFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        getActivity().setTitle("Reporte Tiempo De Espera");
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the customer_item for this fragment
        View view = inflater.inflate(R.layout.fragment_messages, container, false);
        add_client = (EditText) view.findViewById(R.id.client_add_txt);
        cancel_client = (EditText) view.findViewById(R.id.client_cancel_txt);
        ping_client = (EditText) view.findViewById(R.id.client_ping_txt);
        ready_client = (EditText) view.findViewById(R.id.client_ready_txt);
        save_stuff = (Button)view.findViewById(R.id.save_messages);
        reset_stuff = (Button)view.findViewById(R.id.reset_messages);
        save_stuff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Usuarios");
                query.whereEqualTo("uuid", App.getMacAddress());
                query.fromLocalDatastore();
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (object == null) {
                            Toast.makeText(getActivity(), "no se encontraron los mensages", Toast.LENGTH_LONG).show();
                            ParseObject new_restaurant = new ParseObject("Usuarios");
                            new_restaurant.put("user", "name");
                            new_restaurant.put("uuid", App.getMacAddress());
                            new_restaurant.put("add", "Bienvenido %s por favor espere en lo que su mesa esta lista.");
                            new_restaurant.put("cancel", "Lo Sentimos %s su mesa ha sido cancelada, visitenos pronto.");
                            new_restaurant.put("ready", "Hola %s, su mesa esta lista. Favor de avisarle a nuestra hostess.");
                            new_restaurant.put("ping", "%s, le recordamos que su mesa esta lista. Favor de avisarle a nuestra hostess.");
                            new_restaurant.pinInBackground();
                        } else {
                            Log.d("score", "Retrieved the object.");
                            object.put("add", add_client.getText().toString());
                            object.put("cancel", cancel_client.getText().toString());
                            object.put("ready", ready_client.getText().toString());
                            object.put("ping", ping_client.getText().toString());
                            Log.d("score", "updated the messages");
                            object.pinInBackground();
                        }
                    }
                });
            }
        });
        reset_stuff.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                ParseQuery<ParseObject> query = ParseQuery.getQuery("Usuarios");
                query.whereEqualTo("uuid", App.getMacAddress());
                query.fromLocalDatastore();
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (object == null) {
                            Toast.makeText(getActivity(), "no se encontraron los mensages", Toast.LENGTH_LONG).show();
                            ParseObject new_restaurant = new ParseObject("Usuarios");
                            new_restaurant.put("user", "name");
                            new_restaurant.put("uuid", App.getMacAddress());
                            new_restaurant.put("add", "Bienvenido %s por favor espere en lo que su mesa esta lista.");
                            new_restaurant.put("cancel", "Lo Sentimos %s su mesa ha sido cancelada, visitenos pronto.");
                            new_restaurant.put("ready", "Hola %s, su mesa esta lista. Favor de avisarle a nuestra hostess.");
                            new_restaurant.put("ping", "%s, le recordamos que su mesa esta lista. Favor de avisarle a nuestra hostess.");
                            pupulateboxes();
                            new_restaurant.pinInBackground();
                        } else {
                            Log.d("score", "Retrieved the object.");
                            object.put("add", "Bienvenido %s por favor espere en lo que su mesa esta lista.");
                            object.put("cancel", "Lo Sentimos %s su mesa ha sido cancelada, visitenos pronto.");
                            object.put("ready", "Hola %s, su mesa esta lista. Favor de avisarle a nuestra hostess.");
                            object.put("ping", "%s, le recordamos que su mesa esta lista. Favor de avisarle a nuestra hostess.");
                            Log.d("score", "updated the messages");
                            pupulateboxes();
                            object.pinInBackground();
                        }
                    }
                });
            }
        });
        pupulateboxes();

        return view;
    }

    private void runOnUIThreadIfVisible(Runnable run)
    {
        if (getActivity() != null && isAdded())
        {
            getActivity().runOnUiThread(run);
        }
    }


    private void pupulateboxes(){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Usuarios");
        query.whereEqualTo("uuid", App.getMacAddress());
        query.fromLocalDatastore();
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                    add_client.setText("Bienvenido %s por favor espere en lo que su mesa esta lista.");
                    cancel_client.setText("Lo Sentimos %s su mesa ha sido cancelada, visitenos pronto.");
                    ready_client.setText("Hola %s, su mesa esta lista. Favor de avisarle a nuestra hostess.");
                    ping_client.setText("%s, le recordamos que su mesa esta lista. Favor de avisarle a nuestra hostess.");
                } else {
                    Log.d("score", "Retrieved the object.");
                    add_client.setText(object.getString("add"), TextView.BufferType.EDITABLE);
                    cancel_client.setText(object.getString("cancel"), TextView.BufferType.EDITABLE);
                    ready_client.setText(object.getString("ready"), TextView.BufferType.EDITABLE);
                    ping_client.setText(object.getString("ping"), TextView.BufferType.EDITABLE);

                    Log.d("score", "got the strings" + object.getString("ping") + object.getString("user"));

                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        //loadReport();
    }


    private void runOnUIIfVisible(Runnable run)
    {
        if (getActivity() != null && isAdded())
        {
            getActivity().runOnUiThread(run);
        }
    }



    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }
}
