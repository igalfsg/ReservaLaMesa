package com.soneyu.smshub;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.soneyu.App;
import com.soneyu.core.QuickstartPreferences;
import com.soneyu.smshub.fragment.DayByDayReportFragment;
import com.soneyu.smshub.fragment.MainFragment;
import com.soneyu.smshub.fragment.OnFragmentInteractionListener;
import com.soneyu.smshub.fragment.ReportFragment;
import com.soneyu.smshub.fragment.SeatInfoFragment;
import com.soneyu.smshub.fragment.WaitTimeGraphReportFragment;
import com.soneyu.smshub.fragment.WaitTimeTextReportFragment;
import com.soneyu.smshub.fragment.messagesFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerCallbacks, OnFragmentInteractionListener
{

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Lista De Espera");
        mToolbar = (Toolbar) findViewById(R.id.toolbar_actionbar);
        setSupportActionBar(mToolbar);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.fragment_drawer);

        // Set up the drawer.
        mNavigationDrawerFragment.setup(R.id.fragment_drawer, (DrawerLayout) findViewById(R.id.drawer), mToolbar);

        mRegistrationBroadcastReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                try
                {
                    SharedPreferences sharedPreferences =
                            PreferenceManager.getDefaultSharedPreferences(context);
                    boolean sentToken = sharedPreferences
                            .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                    if (sentToken)
                    {
                        //prgDialog.dismiss();
                        //Toast.makeText(MainActivity.this, "Activate ok", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        //prgDialog.dismiss();
                        Toast.makeText(MainActivity.this, "GCM error", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        };

        if (checkPlayServices())
        {
           // prgDialog.show();
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(MainActivity.this, com.soneyu.core.RegistrationIntentService.class);
            startService(intent);
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause()
    {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position)
    {
        // update the main content by replacing fragments
        if(position == 0)
        {
            MainFragment fragment = MainFragment.newInstance("param1","param2");
            changeFragment(fragment);
        }/*
        else if(position == 2)
        {

            WaitTimeGraphReportFragment waitTimeGraphReportFragment = WaitTimeGraphReportFragment.newInstance("p1", "p2");
            changeFragment(waitTimeGraphReportFragment);
        }*/
        else if(position == 1)
        {
            SeatInfoFragment seatInfoFragment = SeatInfoFragment.newInstance("p1", "p2");
            changeFragment(seatInfoFragment);
        }
        else if(position == 2)
        {
            ReportFragment reportFragment = new ReportFragment();
            changeFragment(reportFragment);
        }
        else if(position == 3)
        {
            DayByDayReportFragment reportFragment = new DayByDayReportFragment();
            reportFragment.setRetainInstance(true);
            changeFragment(reportFragment);
        }
        else if(position == 4)
        {
            messagesFragment frg = new messagesFragment();
            changeFragment(frg);
        }
    }

    private void changeFragment(Fragment newFragment)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.container, newFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    @Override
    public void onBackPressed()
    {
        if (mNavigationDrawerFragment.isDrawerOpen())
            mNavigationDrawerFragment.closeDrawer();
        else
            super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        if (!mNavigationDrawerFragment.isDrawerOpen())
        {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_send_log)
        {
            App.app.sendAppLog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices()
    {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode))
            {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }
            else
            {
                Log.i(TAG, "This device is not supported.");
                Toast.makeText(this, "Your device do not have Google Play Service", Toast.LENGTH_LONG).show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri)
    {

    }
}
