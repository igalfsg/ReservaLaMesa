package com.soneyu.smshub.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soneyu.App;
import com.soneyu.data.StatsResponse;
import com.soneyu.smshub.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by songnob on 8/2/2015.
 */
public class WaitTimeAdapter extends BaseAdapter
{
    private StatsResponse.StatsData[] statsDatas;
    private Context mContext;
    private Boolean forDate = false;

    public WaitTimeAdapter(Context context, StatsResponse.StatsData[] stats, Boolean forDate)
    {
        this.statsDatas = stats;
        this.mContext = context;
        this.forDate = forDate;
    }

    @Override
    public int getCount()
    {
        return statsDatas.length;
    }

    @Override
    public StatsResponse.StatsData getItem(int i)
    {
        return statsDatas[i];
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (view == null || view.getTag() == null)
        {
            view = layoutInflater.inflate(R.layout.hour_wait_time_item, null, false);
            viewHolder = new ViewHolder();
            viewHolder.date_time_text_view = (TextView) view.findViewById(R.id.date_time_text_view);
            viewHolder.info_text_view = (TextView) view.findViewById(R.id.info_text_view);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) view.getTag();
        }

        StatsResponse.StatsData data = getItem(i);
        Date time = new Date(data.time);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");

        if (forDate)
        {
            sdf = new SimpleDateFormat("dd-MMM");

        }
        viewHolder.date_time_text_view.setText(sdf.format(time));
        viewHolder.info_text_view.setText(App.prettySeconds((int) data.wait_time));
        return view;
    }

    class ViewHolder
    {
        TextView date_time_text_view;
        TextView info_text_view;
    }
}
