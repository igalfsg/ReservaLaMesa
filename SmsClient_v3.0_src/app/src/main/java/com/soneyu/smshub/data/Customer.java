package com.soneyu.smshub.data;

/**
 * Created by songnob on 7/25/2015.
 */
public class Customer
{
    public Customer (int id, String name, String phone, String note, String status, int num, String mesa){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.note = note;
        this.status = status;
        this.number_of_people = num;
        this.table_number = mesa;
    }
    public static final String SERVED = "served";
    public static final String WAIT = "wait";
    public static final String CANCEL = "cancel";
    private int id;
    private String name;
    private int number_of_people;
    private String phone;
    private String note;
    private String status;
    private String table_number;

    public int getId()
    {
        return id;
    }

    public String getName()
    {
        return name;
    }

    public int getNumber_of_people()
    {
        return number_of_people;
    }

    public String getPhone()
    {
        return phone;
    }

    public String getNote()
    {
        return note;
    }

    public String getStatus()
    {
        return status;
    }

    public String getCustomer_belong()
    {
        return customer_belong;
    }

    private String customer_belong;
    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getTable_number()
    {
        return table_number;
    }
}
