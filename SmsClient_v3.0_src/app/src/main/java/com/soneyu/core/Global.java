package com.soneyu.core;

/**
 * Created by songnob on 7/24/2015.
 */
public class Global
{
    //public static final String SERVER_URL = "http://soneyu.com:20000";
    public static final String SERVER_URL = "http://216.172.177.16:20000";
    //public static final String SERVER_URL = "http://192.168.1.39:20000";

    public static final String SMS_CLIENT_STATUS = SERVER_URL + "/api/sms_client/status.json?uuid=%s";
    public static final String CUSTOMER_ADD = SERVER_URL + "/api/%s/customer/add_tablita.json";
    public static final String CUSTOMER_LIST = SERVER_URL + "/api/%s/customer/all.json";
    public static final String CUSTOMER_READY = SERVER_URL + "/api/%s/customer/%s/ready.json";

    public static final String CUSTOMER_PING = SERVER_URL + "/api/%s/customer/%s/ping.json";
    public static final String CUSTOMER_SEAT = SERVER_URL + "/api/%s/customer/%s/seat.json?table=%s";

    public static final String CUSTOMER_CANCEL = SERVER_URL + "/api/%s/customer/%s/cancel_tortas.json";
    public static final String REGISTER_URL = SERVER_URL + "/api/sms_client/register.json";
    public static final String CUSTOMER_FILTER_URL = SERVER_URL +"/api/%s/customer/filter_status.json?status=%s&last=%s";
    public static final String CUSTOMER_COUNT_URL = SERVER_URL +"/api/%s/customer/count_status.json?last=%s";
    public static final String AVERAGE_WAIT_TIME = SERVER_URL +"/api/%s/customer/wait_time.json?last=%s";
    public static final String CUSTOMER_STATS_URL = SERVER_URL +"/api/%s/customer/stats.json?last=14400";
    public static final String CUSTOMER_WAIT_TIME_24_URL = SERVER_URL + "/api/sms_client/%s/waitime_24h.json";
    public static final String CUSTOMER_WAIT_TIME_10DAYS_URL = SERVER_URL + "/api/sms_client/%s/waitime_10d.json";
    public static final String AVERAGE_PARTY_SIZE_URL = SERVER_URL + "/api/%s/customer/average_pt_size.json?last=%s";

    public static final String CUSTOMER_COUNT_2_URL = SERVER_URL +"/api/%s/customer/count_status_2.json?time=%s";
    public static final String AVERAGE_WAIT_TIME_2 = SERVER_URL +"/api/%s/customer/wait_time_2.json?time=%s";
    public static final String AVG_PARTY_WAITIME_BY_DAY = SERVER_URL + "/api/sms_client/%s/avg_party_waitime.json?time=%s";

}
