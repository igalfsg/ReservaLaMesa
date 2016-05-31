package com.soneyu.smshub.data;

import java.util.List;

/**
 * Created by songnob on 7/24/2015.
 */
public class SmsClientListResponse extends GeneralResponse
{
    private List<SmsClient> data;

    public List<SmsClient> getData()
    {
        return data;
    }

    public void setData(List<SmsClient> data)
    {
        this.data = data;
    }
}
