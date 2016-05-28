package com.soneyu.gcmplayground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.soneyu.gcmplayground.constants.RegistrationConstants;
import com.soneyu.gcmplayground.util.Utils;

/**
 * Created by ali  on 12/22/2015.
 */
public class GCMBroadCastReceiver extends BroadcastReceiver {
    private Context mContext;

    @Override
    public void onReceive(Context context, Intent intent) {

        mContext=context;

        if(intent.getAction().equals(RegistrationConstants.NEW_DOWNSTREAM_MESSAGE))
        {
            Log.d(Constants.GCM_DEBUG,"DownStreem message");
            String message=intent.getStringExtra(RegistrationConstants.NOTIFICATION_DATA)+intent.getStringExtra(RegistrationConstants.NOTIFICATION_BODY);

            Toast.makeText(context,message, Toast.LENGTH_LONG).show();

            Utils.createNotification(context,"Push",message,"Alert");

        }

    }

}
