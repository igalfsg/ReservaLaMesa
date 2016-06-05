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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.soneyu.App;
import com.soneyu.core.Global;
import com.soneyu.data.StatsResponse;
import com.soneyu.smshub.R;
import com.soneyu.smshub.data.WaitTimeAdapter;

public class WaitTimeTextReportFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TextView total_served_textview;
    private TextView average_wait_time_textview;
    private TextView total_customer_textview;
    private TextView total_cancel_textview;

    private TextView total_served_day_textview;
    private TextView average_wait_time_day_textview;
    private TextView total_customer_day_textview;
    private TextView total_cancel_day_textview;

    private OnFragmentInteractionListener mListener;
    private ListView hour_wait_time_list_view, days_wait_time_list_view;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaitTimeTextReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaitTimeTextReportFragment newInstance(String param1, String param2)
    {
        WaitTimeTextReportFragment fragment = new WaitTimeTextReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WaitTimeTextReportFragment()
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
        View view = inflater.inflate(R.layout.fragment_report, container, false);

        days_wait_time_list_view = (ListView) view.findViewById(R.id.last_10_days_wait_time_list_view);
        hour_wait_time_list_view = (ListView) view.findViewById(R.id.last_24_hours_wait_time_list_view);

        fetch24HoursReport();
        fetch10DaysReport();

        // find text view

        /*
        total_served_textview = (TextView) view.findViewById(R.id.total_served_textview);
        average_wait_time_textview = (TextView) view.findViewById(R.id.average_waitime_textview);
        total_customer_textview = (TextView) view.findViewById(R.id.total_customer_textview);
        total_cancel_textview = (TextView) view.findViewById(R.id.total_cancel_textview);

        total_served_day_textview = (TextView) view.findViewById(R.id.total_served_day_textview);
        average_wait_time_day_textview = (TextView) view.findViewById(R.id.average_day_textview);
        total_customer_day_textview = (TextView) view.findViewById(R.id.total_customer_day_textview);
        total_cancel_day_textview = (TextView) view.findViewById(R.id.total_cancel_day_textview);
        */
        //loadReport();

        return view;
    }

    private void runOnUIThreadIfVisible(Runnable run)
    {
        if (getActivity() != null && isAdded())
        {
            getActivity().runOnUiThread(run);
        }
    }

    private void fetch24HoursReport()
    {
        Ion.with(getActivity())
                .load(String.format(Global.CUSTOMER_WAIT_TIME_24_URL, App.getMacAddress()))
                .setLogging("su", Log.DEBUG)
                .as(StatsResponse.class)
                .setCallback(new FutureCallback<StatsResponse>()
                {
                    @Override
                    public void onCompleted(Exception e, final StatsResponse result)
                    {
                        if (e != null)
                        {
                            e.printStackTrace();
                            runOnUIThreadIfVisible(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(getActivity(), "Local error when load 24 hours report", Toast.LENGTH_LONG).show();

                                }
                            });

                        }
                        else
                        {
                            if (result.getStatus() == 200)
                            {
                                if (result.getData() != null && result.getData().length > 0)
                                {
                                    runOnUIIfVisible(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            hour_wait_time_list_view.setAdapter(new WaitTimeAdapter(getActivity(), result.getData(), false));
                                        }
                                    });
                                }

                            }
                            else
                            {
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(getActivity(), "Server error when load 24 hours report", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }
                });
    }

    private void fetch10DaysReport()
    {
        Ion.with(getActivity())
                .load(String.format(Global.CUSTOMER_WAIT_TIME_10DAYS_URL, App.getMacAddress()))
                .setLogging("su", Log.DEBUG)
                .as(StatsResponse.class)
                .setCallback(new FutureCallback<StatsResponse>()
                {
                    @Override
                    public void onCompleted(Exception e, final StatsResponse result)
                    {
                        if (e != null)
                        {
                            e.printStackTrace();
                            runOnUIThreadIfVisible(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    Toast.makeText(getActivity(), "Error when load 10 days report, local exception", Toast.LENGTH_SHORT).show();

                                }
                            });

                        }
                        else
                        {
                            if (result.getStatus() == 200)
                            {
                                if (result.getData() != null && result.getData().length > 0)
                                {
                                    runOnUIIfVisible(new Runnable()
                                    {
                                        @Override
                                        public void run()
                                        {
                                            days_wait_time_list_view.setAdapter(new WaitTimeAdapter(getActivity(), result.getData(), true));
                                        }
                                    });
                                }
                            }
                            else
                            {
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        Toast.makeText(getActivity(), "Server error when load 10 days report", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
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

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }
}
