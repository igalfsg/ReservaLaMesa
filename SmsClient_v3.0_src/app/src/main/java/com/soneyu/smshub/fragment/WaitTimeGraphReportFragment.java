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

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.soneyu.App;
import com.soneyu.core.Global;
import com.soneyu.data.StatsResponse;
import com.soneyu.smshub.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WaitTimeGraphReportFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;
    private ListView customer_listview;
    private GraphView graphHour, graphDay;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WaitTimeGraphReportFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WaitTimeGraphReportFragment newInstance(String param1, String param2)
    {
        WaitTimeGraphReportFragment fragment = new WaitTimeGraphReportFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public WaitTimeGraphReportFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        getActivity().setTitle("Reporte Grafico");
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void runOnUIThreadIfVisible(Runnable run)
    {
        if (getActivity() != null && isAdded())
        {
            getActivity().runOnUiThread(run);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the customer_item for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        graphDay = (GraphView) view.findViewById(R.id.graphDay);
        graphHour = (GraphView) view.findViewById(R.id.graphHour);
        displayGraphView();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        loadStats2();
        loadStats3();
    }

    private DataPoint[] toDataPoint(StatsResponse.StatsData[] data)
    {
        DataPoint[] result = new DataPoint[data.length];
        for (int i = 0; i < data.length; i++)
        {
            Date t = new Date(data[i].time);
            t.setMinutes(0);
            t.setSeconds(0);
            Log.i("mbp", "Time: " + t.toLocaleString());
            result[i] = new DataPoint(t, data[i].wait_time / 60.0f);
        }
        return result;
    }

    private void displayGraphView()
    {
        DataPoint[] result = new DataPoint[24];
        for (int i = 0; i < 24; i++)
        {
            result[i] = new DataPoint(i, 0);
        }

        DataPoint[] result2 = new DataPoint[10];
        for (int i = 0; i < 10; i++)
        {
            result2[i] = new DataPoint(i, 0);
        }
        graphHour.addSeries(new LineGraphSeries<DataPoint>(result));
        graphDay.addSeries(new LineGraphSeries<DataPoint>(result2));

    }

    public void hourChartFormat()
    {
        graphHour.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            @Override
            public String formatLabel(double value, boolean isValueX)
            {
                if (isValueX)
                {
                    // show normal x values
                    return super.formatLabel(value, isValueX);
                }
                else
                {
                    // show currency for y values
                    return super.formatLabel(value, isValueX) + " mins";
                }
            }
        });
    }

    private void loadStats2()
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
                                    Toast.makeText(getActivity(), "Error when load customer list", Toast.LENGTH_LONG).show();

                                }
                            });

                        }
                        else
                        {
                            if (result.getStatus() == 200)
                            {
                                if (result.getData() != null && result.getData().length > 0)
                                {
                                    DataPoint[] dps = toDataPoint(result.getData());
                                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dps);

                                    series.setDataPointsRadius(4);
                                    series.setThickness(4);
                                    series.setDrawDataPoints(true);

                                    //series.setSize(5);
                                    graphHour.addSeries(series);
                                    series.setTitle("Promedio De Espera en las Ultimas 24 horas (minutos)");
                                    SimpleDateFormat date = new SimpleDateFormat("HH");
                                    graphHour.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), date));
                                    graphHour.getGridLabelRenderer().setNumHorizontalLabels(24); // only 4 because of the space

                                    Date d = new Date(System.currentTimeMillis());
                                    d.setMinutes(0);
                                    d.setSeconds(0);
                                    Log.i("mbp", "Date: " + d.toGMTString());

                                    graphHour.getViewport().setMinX(d.getTime() - (24 * 60 * 60 * 1000));
                                    graphHour.getViewport().setMaxX(d.getTime());
                                    graphHour.getViewport().setXAxisBoundsManual(true);
                                    //series.setShape(PointsGraphSeries.Shape.POINT);
                                }

                            }
                            else
                            {
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                    }
                                });

                            }
                        }
                    }
                });
    }

    private DataPoint[] toDataPointDay(StatsResponse.StatsData[] data)
    {
        DataPoint[] result = new DataPoint[data.length];
        for (int i = 0; i < data.length; i++)
        {
            Date t = new Date(data[i].time);
            t.setMinutes(0);
            t.setSeconds(0);
            t.setHours(0);

            Log.i("mbp", "Date: " + t.toGMTString());
            result[i] = new DataPoint(t, data[i].wait_time / 60.0f);
        }
        return result;
    }

    private void loadStats3()
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
                                    Toast.makeText(getActivity(), "Error when load customer list", Toast.LENGTH_LONG).show();

                                }
                            });

                        }
                        else
                        {
                            if (result.getStatus() == 200)
                            {
                                if (result.getData() != null && result.getData().length > 0)
                                {
                                    DataPoint[] dps = toDataPointDay(result.getData());
                                    LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(dps);

                                    series.setDataPointsRadius(4);
                                    series.setThickness(4);
                                    series.setDrawDataPoints(true);

                                    graphDay.addSeries(series);
                                    series.setTitle("Average wait time (last 10 days)");
                                    SimpleDateFormat date = new SimpleDateFormat("dd/MMM");
                                    graphDay.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), date));
                                    graphDay.getGridLabelRenderer().setNumHorizontalLabels(10); // only 4 because of the space

                                    // set manual x bounds to have nice steps
                                    Date d = new Date(System.currentTimeMillis());
                                    d.setMinutes(0);
                                    d.setSeconds(0);
                                    d.setHours(0);
                                    Log.i("mbp", "Date: " + d.toString());

                                    graphDay.getViewport().setMinX(d.getTime() - (10 * 24 * 60 * 60 * 1000));
                                    graphDay.getViewport().setMaxX(d.getTime());
                                    graphDay.getViewport().setXAxisBoundsManual(true);
                                    //graphDay.getViewport(
                                }
                            }
                            else
                            {
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {

                                    }
                                });

                            }
                        }
                    }
                });
    }
    /*
    private void loadStats()
    {
        Ion.with(getActivity())
                .load(String.format(Global.CUSTOMER_STATS_URL, App.getMacAddress()))
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
                                    customer_listview.setVisibility(View.INVISIBLE);

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
                                        customer_listview.setVisibility(View.VISIBLE);
                                        customer_listview.setAdapter(new StatsCustomerAdapter(getActivity(), result.getData()));
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
                                        customer_listview.setVisibility(View.INVISIBLE);
                                    }
                                });

                            }
                        }
                    }
                });

    }*/

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
