package com.soneyu.data;

/**
 * Created by songnob on 8/8/2015.
 */
public class GeneralReportResponse extends GeneralResponse
{
    private StatusCount[] data;

    public StatusCount[] getData()
    {
        return data;
    }

    public static class StatusCount
    {
        private String status;
        private int total;

        public String getStatus()
        {
            return status;
        }

        public int getTotal()
        {
            return total;
        }
    }
}
