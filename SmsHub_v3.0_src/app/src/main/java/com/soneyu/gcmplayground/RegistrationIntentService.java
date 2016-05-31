package com.soneyu.gcmplayground;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.soneyu.gcmplayground.constants.RegistrationConstants;
import com.soneyu.smshub.R;

import java.io.IOException;


public class RegistrationIntentService extends IntentService {

    private static final String TAG = Constants.GCM_DEBUG;

    /**
     * Subscribe 3 topics for user hub:
     *  global: will receive all notification
     *  hub: receive notification from client tu hub
     *  client: receive notification send from server or hub to client
     */
    private static final String[] TOPICS = new String[]{"global", "hub", "client"};
    private Intent regCompleteIntent;

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String token = "";

        regCompleteIntent = new Intent();
        regCompleteIntent.setAction(RegistrationConstants.REGISTRATION_COMPLETE);

        try {
            token = InstanceID.getInstance(this)
                    .getToken(getString(R.string.gcm_semder_id), GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
            Log.d(TAG, "GCM Registration Token: " + token);

            sendRegistrationToServer(token);
        } catch (Exception e) {

            Log.e(TAG, "Failed to complete token refresh", e);
            regCompleteIntent.putExtra(RegistrationConstants.REGISTRATION_COMPLETE, false);
            getApplicationContext().sendBroadcast(regCompleteIntent);
        }


    }

    private void sendRegistrationToServer(String token) throws IOException {
        Log.d(Constants.GCM_DEBUG, "Sending tocken to app sarver  " + token);
        subscribeTopics(token);
    }

    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
