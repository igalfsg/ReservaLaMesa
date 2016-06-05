package com.soneyu.core;

/**
 * Created by songnob on 7/24/2015.
 */
public class Global {
    //public static final String SERVER_URL = "http://soneyu.com:20000";
    public static final String SERVER_URL = "http://216.172.177.16:20000";

    public static final String CUSTOMER_WAIT_TIME_24_URL = SERVER_URL + "/api/sms_client/%s/waitime_24h.json";
    public static final String CUSTOMER_WAIT_TIME_10DAYS_URL = SERVER_URL + "/api/sms_client/%s/waitime_10d.json";
}