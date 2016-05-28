/**
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

package com.soneyu.core;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;

import com.google.android.gms.gcm.GcmListenerService;
import com.soneyu.smshub.R;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {

        String type_str = data.getString("type");
        if(type_str.equalsIgnoreCase(Global.TYPE_SEND_SMS_WELCOME))
        {
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendWelcomeMessage(phoneno, name);

        }
        else if(type_str.equalsIgnoreCase("5")){
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendWelcomeMessage_todos(phoneno, name, "Loma Linda");
        }
        else if(type_str.equalsIgnoreCase(Global.ADD_FISHERS)){
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendWelcomeMessage_todos(phoneno, name, "Fishers");
        }
        else if(type_str.equalsIgnoreCase(Global.ADD_CANTINA)){
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendWelcomeMessage_todos(phoneno, name, "La No. 20 Cantina");
        }
        else if(type_str.equalsIgnoreCase(Global.ADD_KLEINS)){
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendWelcomeMessage_todos(phoneno, name, "Kleins");
        }
        else if(type_str.equalsIgnoreCase(Global.ADD_HERITAGE)){
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendWelcomeMessage_todos(phoneno, name, "Heritage");
        }
        else if(type_str.equalsIgnoreCase(Global.ADD_TORTAS)){
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendWelcomeMessage1(phoneno, name);
        }
        else if(type_str.equalsIgnoreCase(Global.PING_TORTAS)){
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendPing_tortas(phoneno, name);
        }
        else if(type_str.equalsIgnoreCase(Global.CANCEL_TORTAS)){
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send welcome message to " + name + " phone " + phoneno);
            sendCancelMessage_tortas(phoneno, name);
        }

        else if(type_str.equalsIgnoreCase(Global.TYPE_SEND_SMS_READY))
        {
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send ready message to " + name + " phone " + phoneno);
            sendReadyMessage(phoneno, name);

        }
        else if(type_str.equalsIgnoreCase(Global.TYPE_SEND_SMS_CANCEL))
        {
            String name = data.getString("message");
            String phoneno = data.getString("number");
            Log.d(TAG, "Send cancel message to " + name + " phone " + phoneno);
            sendCancelMessage(phoneno, name);

        }
        else if(type_str.equalsIgnoreCase(Global.TYPE_SMS_CLIENT_REGISTER))
        {
            String name = data.getString("name");
            String mac = data.getString("uuid");
            //sendNotification("Client Register", String.format("Client name %s, id %s want to register", name, mac));
        }

    }
    private void sendWelcomeMessage_todos(String phoneNo, String name, String restaurant)
    {
        String msg = String.format("Bienvenido %s a %s por favor espere en lo que su mesa esta lista", name, restaurant);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);

        sendNotification(String.format("Welcome message delivery to %s (%s)", name, phoneNo));
    }

    private void sendCancelMessage(String phoneno, String name)
    {
        String msg = String.format("Lo Sentimos %s su mesa ha sido cancelada, visitenos pronto.", name);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneno, null, msg, null, null);

        sendNotification(String.format("Cancel message delivery to %s (%s)", name, phoneno));
    }
    private void sendCancelMessage_tortas(String phoneno, String name)
    {
        String msg = String.format("Lo Sentimos %s su orden ha sido cancelada, visitenos pronto.", name);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneno, null, msg, null, null);

        sendNotification(String.format("Cancel message delivery to %s (%s)", name, phoneno));
    }
    private void sendPing_tortas(String phoneno, String name)
    {

        String msg = String.format("Hola %s, su comida esta lista. Favor de avisarle a nustro chef", name);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneno, null, msg, null, null);

        sendNotification(String.format("Ready message delivery to %s (%s)", name, phoneno));
    }
    private void sendReadyMessage(String phoneno, String name)
    {

        String msg = String.format("Hola %s, su mesa esta lista. Favor de avisarle a nuestra hostess.", name);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneno, null, msg, null, null);

        sendNotification(String.format("Ready message delivery to %s (%s)", name, phoneno));
    }

    private void sendWelcomeMessage(String phoneNo, String name)
    {
        String msg = String.format("Bienvenido %s A tiktek por favor espere en lo que su mesa esta lista", name);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);

        sendNotification(String.format("Welcome message delivery to %s (%s)", name, phoneNo));
    }
    private void sendWelcomeMessage1(String phoneNo, String name)
    {
        String msg = String.format("Bienvenido %s a Tortas Juanas por favor espere en lo que su comida esta lista", name);
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);

        sendNotification(String.format("Welcome message delivery to %s (%s)", name, phoneNo));
    }
    private void sendSms(String phoneNo, String msg)
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNo, null, msg, null, null);
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    private void sendNotification(String message) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
