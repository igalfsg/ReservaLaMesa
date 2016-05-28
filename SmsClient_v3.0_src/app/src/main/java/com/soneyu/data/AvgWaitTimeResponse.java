package com.soneyu.data;

/**
 * Created by songnob on 8/8/2015.
 */
public class AvgWaitTimeResponse extends GeneralResponse
{
    private AVGData[] data;

    public AVGData[] getData()
    {
        return data;
    }

    public static class AVGData
    {
        private float wait_time;
        private int number_of_people;

        public int getNumber_of_people() {
            return number_of_people;
        }

        public int getWait_time() {
            return (int) wait_time;
        }
    }
}
