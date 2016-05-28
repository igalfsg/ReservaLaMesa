package com.soneyu.data;

import com.soneyu.smshub.data.Customer;

import java.util.List;

/**
 * Created by songnob on 7/25/2015.
 */
public class CustomerListReponse extends GeneralResponse
{
    public CustomerListReponse (List<Customer> igalin){
        this.data = igalin;
    }
    private List<Customer> data;

    public List<Customer> getData()
    {
        return data;
    }
}
