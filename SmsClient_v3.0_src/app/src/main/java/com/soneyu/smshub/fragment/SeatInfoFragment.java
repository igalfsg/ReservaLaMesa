package com.soneyu.smshub.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.soneyu.App;
import com.soneyu.smshub.R;
import com.soneyu.smshub.data.Customer;
import com.soneyu.smshub.data.CustomerAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class SeatInfoFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SeatInfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    private ListView customer_list_view;
    public static SeatInfoFragment newInstance(String param1, String param2)
    {
        SeatInfoFragment fragment = new SeatInfoFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public SeatInfoFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        getActivity().setTitle("Clientes Sentados");
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
        View view = inflater.inflate(R.layout.fragment_util, container, false);
        customer_list_view = (ListView) view.findViewById(R.id.seat_customer_listview);
        loadCustomer();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
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
    private void runOnUIThreadIfVisible(Runnable run)
    {
        if (getActivity() != null && isAdded())
        {
            getActivity().runOnUiThread(run);
        }
    }
    private void loadCustomer()
    {
        customer_list_view.setVisibility(View.VISIBLE);


        ParseQuery<ParseObject> query_clients = ParseQuery.getQuery("Clientes");
        query_clients.setLimit(1000);
        query_clients.whereEqualTo("status", "seat");
        query_clients.whereEqualTo("restaurant", App.getMacAddress());
        query_clients.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                if (e == null) {
                    // The count request succeeded. Log the count
                    int id;
                    int num;
                    String name;
                    String phone;
                    String note;
                    String status;
                    String mesa;
                    //List<Customer> templist= new ArrayList<Customer>();
                    final List<Customer> data = new ArrayList<Customer>();
                    for (ParseObject cliente : clientelist) {
                        id = cliente.getInt("id");
                        name = cliente.getString("nombre");
                        phone = cliente.getString("tel");
                        note = cliente.getString("note");
                        status = cliente.getString("status");
                        num = cliente.getInt("tamano");
                        mesa = cliente.getString("mesa");
                        Customer curr_cus = new Customer(id, name, phone, note, status, num, mesa);
                        data.add(curr_cus);//esto esta medio pitero
                    }
                    Collections.reverse(data);
                    Log.i("mbp", "Load data ok");
                    runOnUIThreadIfVisible(new Runnable() {
                        @Override
                        public void run() {
                            customer_list_view.setVisibility(View.VISIBLE);
                            customer_list_view.setAdapter(new CustomerAdapter(getActivity(), data, true));
                        }
                    });
                } else {
                    // The request failed
                    e.printStackTrace();
                    runOnUIThreadIfVisible(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Error cargando clientes", Toast.LENGTH_LONG).show();
                            customer_list_view.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });



        /*

        Ion.with(getActivity())
                .load(String.format(Global.CUSTOMER_FILTER_URL, App.getMacAddress(), "seat", "14400"))
                .setLogging("su", Log.DEBUG)
                .as(CustomerListReponse.class)
                .setCallback(new FutureCallback<CustomerListReponse>()
                {
                    @Override
                    public void onCompleted(Exception e, final CustomerListReponse result)
                    {
                        if (e != null)
                        {
                            e.printStackTrace();
                            runOnUIThreadIfVisible(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(getActivity(), "Error when load customer list", Toast.LENGTH_LONG).show();
                                    customer_list_view.setVisibility(View.INVISIBLE);

                                }
                            });

                        }
                        else
                        {
                            if (result.getStatus() == 200)
                            {
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        customer_list_view.setVisibility(View.VISIBLE);
                                        customer_list_view.setAdapter(new CustomerAdapter(getActivity(), result.getData(), true));
                                    }
                                });
                            }
                            else
                            {
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        customer_list_view.setVisibility(View.INVISIBLE);
                                    }
                                });

                            }
                        }
                    }
                });*/
    }
    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }
}
