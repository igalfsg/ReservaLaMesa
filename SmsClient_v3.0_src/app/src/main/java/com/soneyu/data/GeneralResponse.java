package com.soneyu.data;

/**
 * Created by songnob on 7/24/2015.
 */
public class GeneralResponse
{
    private int status;
    private String message;

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}
