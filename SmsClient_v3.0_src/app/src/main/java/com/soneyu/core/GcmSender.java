package com.soneyu.core;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Silico Studio on 5/27/2016.
 */
public class GcmSender {

    public static final String API_KEY = "AIzaSyDNXYulwWKygiqiI73K6KMX_3d_nqER5_Q";
 //   public static final String API_KEY = "AIzaSyD1ImZrrJ7w-8LgUR7Hydgy3Etm9vHDFtA";



    public static void setTopicMessage(final String message,final String body )
    {
        AsyncTask asyncTask=new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                sendNotification(message,body);
                return null;
            }
        };

        asyncTask.execute();
    }
    private static void sendNotification(String message,String body) {

        try {
            // Prepare JSON containing the GCM message content. What to send and where to send.
            JSONObject jGcmData = new JSONObject();
            JSONObject jData = new JSONObject();
            jData.put("message", message);
            jData.put("body",body);
            jGcmData.put("to", "/topics/global");

            jGcmData.put("data", jData);

            URL url = new URL("https://android.googleapis.com/gcm/send");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "key=" + API_KEY);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Send GCM message content.
            OutputStream outputStream = conn.getOutputStream();
            outputStream.write(jGcmData.toString().getBytes());

            // Read GCM response.
            InputStream inputStream = conn.getInputStream();
            String resp = "Response";
                    //IOUtils.toString(inputStream);
            System.out.println(resp);
            System.out.println("Check your device/emulator for notification or logcat for " +
                    "confirmation of the receipt of the GCM message.");
        } catch (IOException e) {
            System.out.println("Unable to send GCM message.");
            System.out.println("Please ensure that API_KEY has been replaced by the server " +
                    "API key, and that the device's registration token is correct (if specified).");
            e.printStackTrace();
        } catch (JSONException e) {


        }
    }

}