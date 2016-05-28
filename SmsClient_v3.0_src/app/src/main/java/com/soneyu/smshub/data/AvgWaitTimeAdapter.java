package com.soneyu.smshub.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.soneyu.App;
import com.soneyu.data.AvgWaitTimeResponse;
import com.soneyu.smshub.R;

/**
 * Created by songnob on 8/2/2015.
 */
public class AvgWaitTimeAdapter extends BaseAdapter {
    private AvgWaitTimeResponse.AVGData[] statsDatas;
    private Context mContext;

    public AvgWaitTimeAdapter(Context context, AvgWaitTimeResponse.AVGData[] stats) {
        this.statsDatas = stats;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return statsDatas.length;
    }

    @Override
    public AvgWaitTimeResponse.AVGData getItem(int i) {
        return statsDatas[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        ViewHolder viewHolder;

        if (view == null || view.getTag() == null) {
            view = layoutInflater.inflate(R.layout.avg_wait_time, null, false);
            viewHolder = new ViewHolder();
            viewHolder.party_size_text_view = (TextView) view.findViewById(R.id.party_size_text_view);
            viewHolder.wait_time_text_view = (TextView) view.findViewById(R.id.wait_time_text_view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        AvgWaitTimeResponse.AVGData data = getItem(i);

        viewHolder.party_size_text_view.setText("Numero de Gente: " + data.getNumber_of_people());
        viewHolder.wait_time_text_view.setText("Tiempo de Espera: " + App.prettySeconds(data.getWait_time()));
        return view;
    }

    class ViewHolder {
        TextView party_size_text_view;
        TextView wait_time_text_view;
    }
}
