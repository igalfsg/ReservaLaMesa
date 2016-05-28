package com.soneyu.smshub.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.soneyu.App;
import com.soneyu.core.Global;
import com.soneyu.data.GeneralResponse;
import com.soneyu.smshub.R;

import java.util.Date;
import java.util.List;

/**
 * Created by songnob on 7/25/2015.
 */
public class StatsCustomerAdapter extends BaseAdapter
{
    private List<Customer> customers;
    private Context mContext;

    public StatsCustomerAdapter(Context context, List<Customer> customers)
    {
        this.mContext = context;
        this.customers = customers;
    }

    @Override
    public int getCount()
    {
        return customers.size();
    }

    @Override
    public Customer getItem(int i)
    {
        return customers.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Customer customer = customers.get(i);

        ViewHolder viewHolder;
        if (view == null || view.getTag() == null)
        {
            view = inflater.inflate(R.layout.customer_item_stats, null);
            viewHolder = new ViewHolder();

            viewHolder.avatar_img = (ImageView) view.findViewById(R.id.avatar_image_view);
            viewHolder.name_textview = (TextView) view.findViewById(R.id.name_textview);
            viewHolder.status_textview = (TextView) view.findViewById(R.id.status_textview);
            viewHolder.phone_textview = (TextView) view.findViewById(R.id.phone_textview);
            viewHolder.note_textview = (TextView) view.findViewById(R.id.note_textview);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name_textview.setText(customer.getName());
        viewHolder.phone_textview.setText(customer.getPhone());


        int time = Integer.parseInt(customer.getNote());

        if (customer.getStatus().equalsIgnoreCase(Customer.SERVED))
        {
            viewHolder.status_textview.setText("Served");
            viewHolder.status_textview.setTextColor(Color.GREEN);
            viewHolder.note_textview.setText("Customer wait " + App.prettySeconds(time) + " before served");
        }
        else if (customer.getStatus().equalsIgnoreCase(Customer.WAIT))
        {
            viewHolder.status_textview.setText("Waiting");
            viewHolder.status_textview.setTextColor(Color.RED);
        }
        else
        {
            viewHolder.status_textview.setText("Cancel");
            viewHolder.status_textview.setTextColor(Color.RED);
            viewHolder.note_textview.setText("Customer wait " + App.prettySeconds(time) + " before cancel");
        }

        return view;
    }

    static class ViewHolder
    {
        ImageView avatar_img;
        TextView name_textview;
        TextView status_textview;
        TextView phone_textview;
        TextView note_textview;
    }
}
