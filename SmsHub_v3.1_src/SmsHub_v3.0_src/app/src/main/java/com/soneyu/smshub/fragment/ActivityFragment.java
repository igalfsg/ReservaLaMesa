package com.soneyu.smshub.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.soneyu.smshub.R;

public class ActivityFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Switch mSwitch;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ActivityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ActivityFragment newInstance(String param1, String param2)
    {
        ActivityFragment fragment = new ActivityFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ActivityFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private boolean trigger = true;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity, container, false);
        mSwitch = (Switch) view.findViewById(R.id.main_hub_switch);
        final CompoundButton.OnCheckedChangeListener listener = (new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(final CompoundButton compoundButton, final boolean b)
            {
                if(trigger)
                {/*
                    Log.i("su","Trigger is true");
                    final String mode = b ? "_hub_sms_" : "_sub_hub_";
                    Ion.with(getActivity())
                            .load(String.format(Global.HUB_CHANGE_MODE, App.getMacAddress()))
                            .setBodyParameter("mode", mode)
                            .as(GeneralResponse.class)
                            .setCallback(new FutureCallback<GeneralResponse>()
                            {
                                @Override
                                public void onCompleted(Exception e, GeneralResponse result)
                                {
                                    if (e != null)
                                    {
                                        //trigger = false;
                                        compoundButton.setChecked(!b);
                                        Toast.makeText(getActivity(), "Cannot change sms hub type", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                        if (result.getStatus() == 200)
                                        {
                                            Toast.makeText(getActivity(), "Hub type change successful", Toast.LENGTH_LONG).show();
                                        }
                                        else
                                        {
                                            //trigger = false;
                                            compoundButton.setChecked(true);
                                            Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            });*/
                }
                else
                {
                    Log.i("su","Trigger is false");
                    trigger = true;
                    return;
                }

            }
        });
        mSwitch.setOnCheckedChangeListener(listener);
        //trigger = false;
        loadSettings();

        return view;
    }
    private void loadSettings()
    {/*
        Ion.with(getActivity())
                .load(String.format(Global.HUB_INFO_SMS, App.getMacAddress()))
                .setLogging("su", Log.VERBOSE)
                .as(HubResponse.class)
                .setCallback(new FutureCallback<HubResponse>()
                {
                    @Override
                    public void onCompleted(Exception e, HubResponse result)
                    {
                        if (e != null)
                        {
                            mSwitch.setChecked(false);
                            Toast.makeText(getActivity(), "Cannot get hub type", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        else
                        {
                            String str = new Gson().toJson(result);
                            LogZ.i(this, "Result: %s", str);

                            if (result.getStatus() == 200 && result.getData().size() > 0)
                            {
                                if(result.getData().get(0).getType().equalsIgnoreCase("_hub_sms_"))
                                {
                                    mSwitch.setChecked(true);
                                    LogZ.i(this, "I am main hub");
                                }
                                else
                                {
                                    mSwitch.setChecked(false);
                                    LogZ.i(this, "I am sub hub");
                                }
                            }
                            else
                            {
                                mSwitch.setChecked(false);
                                Toast.makeText(getActivity(), result.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });*/
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
