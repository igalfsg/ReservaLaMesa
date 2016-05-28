package com.soneyu.data;

/**
 * Created by songnob on 7/30/2015.
 */
public class StatsResponse extends GeneralResponse
{
    public StatsData[] getData()
    {
        return data;
    }

    private StatsData[] data;
    public static class StatsData
    {
        public float wait_time;
        public long time;
        public String hour;
    }
}
