package com.soneyu;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseInstallation;
import com.soneyu.smshub.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by songnob on 7/24/2015.
 */
public class App extends Application
{
    public static App app;
    public static String GCMToken;

    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        ParseCrashReporting.enable(this);
        Parse.initialize(this, "V5BiAL6hIhQWEKVgRuLfmgBRUi3aydZVvdQpMIJg", "DV3tHPLPMCdD8IgnoA1ankQSUDDS3zitufGpPit7");
        ParseInstallation.getCurrentInstallation().saveInBackground();

    }

    public static String getMacAddress()
    {
//        WifiManager manager = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
//        WifiInfo info = manager.getConnectionInfo();
//        String address = info.getMacAddress().toLowerCase();//direccion
        //return address;
        return "AA:BB:CC";
    }

    public static String prettySeconds(int seconds)
    {
        if (seconds == 0)
            return "0 segundos.";
        int hour = seconds / 3600;
        int min = (seconds - hour * 3600) / 60;
        int sec = seconds - (hour * 3600 + min * 60);
        String val = "";
        if (hour > 0)
        {
            val += hour + " horas ";
        }
        if (min > 0)
        {
            val += min + " minutos ";
        }
        if (sec > 0)
        {
            val += sec + " segundos";
        }
        return val;
    }

    public String getApplicationName()
    {
        int stringId = getApplicationInfo().labelRes;
        return getString(stringId);
    }

    public void sendAppLog()
    {
        int versionCode = BuildConfig.VERSION_CODE;
        SimpleDateFormat format = new SimpleDateFormat("HH_mm_DD_MM_yyyy");
        String date = format.format(new Date());
        String device = Build.MANUFACTURER + "_" + Build.MODEL + "_" + Build.VERSION.RELEASE;
        String fileName = String.format("%s_%s_%s_app_version_%d.txt", getApplicationName(), device, date, versionCode);
        fileName = fileName.replace(" ","_");
        try
        {
            File outputFile = new File(getExternalCacheDir(), fileName);
            Log.i("su", "Output file: " + outputFile.getAbsolutePath());
            Process process = Runtime.getRuntime().exec("logcat -v time -f " + outputFile.getAbsolutePath());

            emailAppLog(outputFile, "igalfsg@gmail.com");
        }
        catch (IOException ignored)
        {
            Toast.makeText(this, "Unable to create manual log", Toast.LENGTH_LONG).show();
        }
    }

    public void emailAppLog(File logFile, String email)
    {
        Log.i("mbp", "Email App Log...");
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "BUG REPORT: " + getApplicationName());
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Put a description here");
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(logFile));
        sendIntent.setType("message/rfc822");
        sendIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(sendIntent);
    }

}
