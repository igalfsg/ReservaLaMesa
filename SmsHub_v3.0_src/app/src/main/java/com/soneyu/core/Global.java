package com.soneyu.core;

/**
 * Created by songnob on 7/24/2015.
 */
public class Global
{
    //public static final String SERVER_URL = "http://soneyu.com:20000";
    //public static final String SERVER_URL = "http://192.168.1.39:20000";
    public static final String SERVER_URL = "http://216.172.177.16:20000";
    public static final String TYPE_SEND_SMS_WELCOME = "1";
    public static final String TYPE_SMS_CLIENT_REGISTER = "2";// no se va a usar

    public static final String ADD_FISHERS = "6";
    public static final String ADD_CANTINA = "7";
    public static final String ADD_HERITAGE = "8";
    public static final String ADD_TORTAS = "9";
    public static final String PING_TORTAS = "10";
    public static final String CANCEL_TORTAS = "11";
    public static final String ADD_KLEINS = "12";

    public static final String SMS_CLIENT_LIST_URL = SERVER_URL + "/api/sms_client/all.json";
    public static final String APPROVE_URL = SERVER_URL + "/api/sms_hub/approve.json";
    public static final String HUB_INFO_SMS = SERVER_URL + "/api/sms_hub/%s/info.json";
    public static final String HUB_CHANGE_MODE = SERVER_URL + "/api/sms_hub/%s/change_mode.json";

    public static final String TYPE_SEND_SMS_READY = "3";
    public static final String TYPE_SEND_SMS_CANCEL = "4";
}
