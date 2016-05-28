package com.soneyu;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseUser;

/**
 * Created by songnob on 7/24/2015.
 */
public class App extends Application
{
    private static App app;
    @Override
    public void onCreate()
    {
        super.onCreate();
        app = this;
        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "V5BiAL6hIhQWEKVgRuLfmgBRUi3aydZVvdQpMIJg", "DV3tHPLPMCdD8IgnoA1ankQSUDDS3zitufGpPit7");
        //PushService.setDefaultPushCallback(this, MainActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();
        ParsePush.subscribeInBackground("cell");
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            // do stuff with the user
            ParseUser.logInInBackground("cell", "pass", new LogInCallback() {
                @Override
                public void done(ParseUser parseUser, ParseException e) {
                    if (e == null) {
                        //success user login
                    } else {
                        // he fucked up son
                    }
                }
            });
        }
    }

    public static String getMacAddress()
    {
        WifiManager manager = (WifiManager) app.getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        String address = info.getMacAddress().toLowerCase();
        return address;
    }

}
