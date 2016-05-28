package com.soneyu.smshub.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.soneyu.core.Global;
import com.soneyu.smshub.R;
import com.soneyu.smshub.data.GeneralResponse;
import com.soneyu.smshub.data.SmsClient;

import java.util.List;

/**
 * Created by songnob on 7/24/2015.
 */
public class SmsClientAdapter extends BaseAdapter
{
    private List<SmsClient> clients;
    private Context mContext;

    public SmsClientAdapter(Context context, List<SmsClient> clients)
    {
        this.mContext = context;
        this.clients = clients;
    }

    @Override
    public int getCount()
    {
        return clients.size();
    }

    @Override
    public SmsClient getItem(int i)
    {
        return clients.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        SmsClient client = clients.get(i);

        ViewHolder viewHolder;
        if (view == null || view.getTag() == null)
        {
            view = inflater.inflate(R.layout.sms_client_item, null);
            viewHolder = new ViewHolder();

            viewHolder.avatar_img = (ImageView) view.findViewById(R.id.avatar_img);
            viewHolder.device_name_textview = (TextView) view.findViewById(R.id.device_name_textview);
            viewHolder.device_status_textview = (TextView) view.findViewById(R.id.device_status_textview);
            viewHolder.approval_img = (ImageView) view.findViewById(R.id.approval_image_view);
            viewHolder.approval_img.setTag(client);

            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }



        viewHolder.device_name_textview.setText(client.getName());
        if (client.isActivated())
        {
            viewHolder.device_status_textview.setText("Activated");
            viewHolder.approval_img.setVisibility(View.INVISIBLE);
        }
        else
        {
            viewHolder.device_status_textview.setText("Pending");
            viewHolder.approval_img.setVisibility(View.VISIBLE);
        }
        viewHolder.approval_img.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final  SmsClient client = clients.get(i);
                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setMessage("Approving...");
                dialog.setCancelable(false);
                dialog.show();
                Ion.with(mContext)
                        .load(Global.APPROVE_URL)
                        .setLogging("su", Log.VERBOSE)
                        .setBodyParameter("uuid", client.getUuid())
                        .as(GeneralResponse.class)
                        .setCallback(new FutureCallback<GeneralResponse>()
                        {
                            @Override
                            public void onCompleted(Exception e, GeneralResponse result)
                            {
                                if(e != null)
                                {
                                    Toast.makeText(mContext, "Approve error",Toast.LENGTH_LONG).show();
                                    e.printStackTrace();

                                }else
                                {
                                    if(result.getStatus() == 200)
                                    {
                                        Log.i("su", result.getMessage());
                                        client.setActivated(true);
                                        notifyDataSetChanged();
                                    }
                                    else
                                    {
                                        Log.i("su",result.getStatus() + result.getMessage());
                                        Toast.makeText(mContext, "Approve failed",Toast.LENGTH_LONG).show();
                                    }
                                }
                                dialog.dismiss();
                            }
                        });
            }
        });
        return view;
    }

    static class ViewHolder
    {
        ImageView avatar_img;
        TextView device_name_textview;
        TextView device_status_textview;
        ImageView approval_img;
        TextView note_textview;
    }
}
