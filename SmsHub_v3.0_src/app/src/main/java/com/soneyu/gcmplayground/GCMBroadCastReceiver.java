package com.soneyu.gcmplayground;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
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

        mContext = context;

        if (intent.getAction().equals(RegistrationConstants.NEW_DOWNSTREAM_MESSAGE)) {
            Log.d(Constants.GCM_DEBUG, "DownStreem message");
            String phoneNo = intent.getStringExtra(RegistrationConstants.NOTIFICATION_DATA);
            String body =  intent.getStringExtra(RegistrationConstants.NOTIFICATION_BODY);
            Toast.makeText(context, phoneNo + body, Toast.LENGTH_LONG).show();
            Log.d("tag here", "Message: Igal is hot af " + phoneNo + body);
            Utils.createNotification(context, phoneNo, body, "Alert");
           SmsManager smsManager = SmsManager.getDefault();
            try {
                smsManager.sendTextMessage(phoneNo, null, body, null, null);
            }catch (Exception e){
                Log.d("exception", e.toString());
            }
        }

    }

}
