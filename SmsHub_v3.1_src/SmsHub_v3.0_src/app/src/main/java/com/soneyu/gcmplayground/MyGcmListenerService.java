package com.soneyu.gcmplayground; /**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.gcm.GcmListenerService;
import com.soneyu.gcmplayground.constants.RegistrationConstants;


public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = Constants.GCM_DEBUG;

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    @Override
    public void onMessageReceived(String from, Bundle data) {
        super.onMessageReceived(from, data);

        Intent downstreamMessageIntent = new Intent();

        downstreamMessageIntent.setAction(RegistrationConstants.NEW_DOWNSTREAM_MESSAGE);
        downstreamMessageIntent.putExtra(RegistrationConstants.SENDER_ID, from);
        downstreamMessageIntent.putExtra(RegistrationConstants.NOTIFICATION_DATA, data.getString("message"));
        downstreamMessageIntent.putExtra(RegistrationConstants.NOTIFICATION_BODY, data.getString("body","body"));

        getApplicationContext().sendBroadcast(downstreamMessageIntent);
    }


}