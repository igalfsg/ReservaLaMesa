package com.soneyu.smshub.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.CountCallback;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.soneyu.App;
import com.soneyu.smshub.R;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DayByDayReportFragment extends Fragment implements DatePickerDialog.OnDateSetListener
{
    private int total_customer = 0;
    private int seat = 0;
    private int cancel = 0;
    private TextView total_customer_text_view, cancel_customer_text_view, seat_customer_text_view, average_wait_time_text_view, avg_pt_size_text_view, time_text_view;
    private ListView avg_time_list_view;
    public DayByDayReportFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_by_day_report, container, false);
        total_customer_text_view = (TextView) view.findViewById(R.id.total_customer_text_view);
        seat_customer_text_view = (TextView) view.findViewById(R.id.seat_customer_text_view);
        cancel_customer_text_view = (TextView) view.findViewById(R.id.cancel_customer_text_view);
        average_wait_time_text_view = (TextView) view.findViewById(R.id.average_wait_time_text_view);
        avg_pt_size_text_view = (TextView) view.findViewById(R.id.avg_pt_size_text_view);
        time_text_view = (TextView) view.findViewById(R.id.time_text_view);
        avg_time_list_view = (ListView) view.findViewById(R.id.avg_party_waitime_list_view);
        time_text_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });
        getActivity().setTitle("Reporte Por Dia");
        return view;
    }
    private void runOnUIThreadIfVisible(Runnable run)
    {
        if (getActivity() != null && isAdded())
        {
            getActivity().runOnUiThread(run);
        }
    }
    //private void loadReport(String time)
    private void loadReport(int mes, int dia, int ano)
    {
        /*
        Calendar bottom_lim = Calendar.getInstance() ;
        bottom_lim.set(Calendar.MONTH, mes);
        bottom_lim.set(Calendar.DATE, dia);
        bottom_lim.set(Calendar.YEAR, ano);//puede q tenga q agregar 1900
        bottom_lim.set(Calendar.SECOND, 0);
        bottom_lim.set(Calendar.MINUTE, 0);
        bottom_lim.set(Calendar.HOUR_OF_DAY, 0);
        Calendar top_lim = Calendar.getInstance();
        top_lim.set(Calendar.MONTH, mes);
        top_lim.set(Calendar.DATE, dia);
        top_lim.set(Calendar.YEAR, ano);//puede q tenga q agregar 1900
        top_lim.set(Calendar.SECOND, 59);
        top_lim.set(Calendar.MINUTE, 59);
        top_lim.set(Calendar.HOUR_OF_DAY, 23);*/
        //Toast.makeText(getActivity(), "Error when load report" + mes + ano, Toast.LENGTH_LONG).show();
        //get served clients
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Clientes");
        query.whereEqualTo("restaurant", App.getMacAddress());
        query.whereEqualTo("status", "seat");
        query.whereEqualTo("day", dia);
        query.whereEqualTo("month", mes);
        query.whereEqualTo("year", ano);
        query.countInBackground(new CountCallback() {
            @Override
            public void done(int count, com.parse.ParseException e) {
                if (e == null) {
                    // The count request succeeded. Log the count
                    seat = count;
                    seat_customer_text_view.setText(seat + " Clientes");
                    total_customer = cancel + seat;
                    total_customer_text_view.setText(total_customer + " Clientes");
                } else {
                    // The request failed
                    Toast.makeText(getActivity(), "Error when load report", Toast.LENGTH_LONG).show();
                }
            }
        });


        //get canceled
        ParseQuery<ParseObject> query_canceled = ParseQuery.getQuery("Clientes");
        query_canceled.whereEqualTo("restaurant", App.getMacAddress());
        query_canceled.whereEqualTo("status", "cancel");
        query_canceled.whereEqualTo("day", dia);
        query_canceled.whereEqualTo("month", mes);
        query_canceled.whereEqualTo("year", ano);
        query_canceled.countInBackground(new CountCallback() {
            @Override
            public void done(int count, com.parse.ParseException e) {
                if (e == null) {
                    // The count request succeeded. Log the count
                    cancel = count;
                    cancel_customer_text_view.setText(cancel + " Clientes");
                    total_customer = cancel + seat;
                    total_customer_text_view.setText(total_customer + " Clientes");
                } else {
                    // The request failed
                    Toast.makeText(getActivity(), "Error when load report", Toast.LENGTH_LONG).show();
                }
            }
        });


        /*
        Ion.with(getActivity())
                .load(String.format(Global.CUSTOMER_COUNT_2_URL, App.getMacAddress(), time))
                .setLogging("su", Log.DEBUG)
                .as(GeneralReportResponse.class)
                .setCallback(new FutureCallback<GeneralReportResponse>()
                {
                    @Override
                    public void onCompleted(Exception e, final GeneralReportResponse result)
                    {
                        if (e == null)
                        {
                            if (result.getStatus() == 200)
                            {
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        int total_customer = 0;
                                        int seat = 0;
                                        int cancel = 0;
                                        for (GeneralReportResponse.StatusCount statusCount : result.getData())
                                        {
                                            if(statusCount.getStatus() != null)
                                            {
                                                if (statusCount.getStatus().equalsIgnoreCase("seat"))
                                                {
                                                    seat = statusCount.getTotal();
                                                }
                                                else if(statusCount.getStatus().equalsIgnoreCase("cancel"))
                                                {
                                                    cancel = statusCount.getTotal();
                                                }
                                            }
                                        }
                                        total_customer = seat + cancel;
                                        if(total_customer == 0)
                                        {
                                            total_customer_text_view.setText("0 Clientes");
                                            cancel_customer_text_view.setText("0 Clientes");
                                            seat_customer_text_view.setText("0 Clientes");
                                        }
                                        else
                                        {
                                            total_customer_text_view.setText(total_customer + " Clientes");
                                            cancel_customer_text_view.setText(cancel + " Clientes");
                                            seat_customer_text_view.setText(seat + " Clientes");
                                        }
                                    }
                                });
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Error when load report", Toast.LENGTH_LONG).show();
                                Log.i("mbp", "Error code: " + result.getStatus());
                            }
                        }
                        else
                        {
                            e.printStackTrace();
                        }
                    }
                });
            */


        //get average times for the day
        ParseQuery<ParseObject> query_avg_time = ParseQuery.getQuery("Clientes");
        query_avg_time.setLimit(1000);
        query_avg_time.whereEqualTo("restaurant", App.getMacAddress());
        query_avg_time.whereEqualTo("day", dia);
        query_avg_time.whereEqualTo("month", mes);
        query_avg_time.whereEqualTo("year", ano);
        query_avg_time.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                if (e == null) {
                    // The count request succeeded. Log the count
                    int entrada;
                    int salida;
                    int resultado = 0;
                    int count = 0;
                    for (ParseObject cliente : clientelist){
                        entrada = cliente.getInt("entrada");
                        salida = cliente.getInt("salida");
                        if (entrada > salida){
                            //if they are in a different day
                            salida += (3600 *24);
                        }
                        resultado += salida - entrada;
                        count++;
                    }
                    resultado = (resultado / count);
                    average_wait_time_text_view.setText(App.prettySeconds(resultado));
                } else {
                    // The request failed
                    Toast.makeText(getActivity(), "Error when loading report", Toast.LENGTH_LONG).show();
                }
            }
        });

        /*
        Ion.with(getActivity())
                .load(String.format(Global.AVERAGE_WAIT_TIME_2, App.getMacAddress(), time))
                .setLogging("su", Log.VERBOSE)
                .noCache()
                .as(CountResponse.class)
                .setCallback(new FutureCallback<CountResponse>()
                {
                    @Override
                    public void onCompleted(Exception e, final CountResponse result)
                    {
                        if (e == null)
                        {
                            if (result.getStatus() == 200)
                            {
                                Log.i("su", result.getData() + " seconds");
                                runOnUIThreadIfVisible(new Runnable() {
                                    @Override
                                    public void run() {
                                        average_wait_time_text_view.setText(App.prettySeconds(result.getData()));
                                    }
                                });
                            }
                        }
                        else
                        {
                            Log.i("su", "Error");
                            e.printStackTrace();
                        }
                    }
                });
                */


        //no hice avg wait time per size
        /*
        Ion.with(getActivity())
                .load(String.format(Global.AVG_PARTY_WAITIME_BY_DAY, App.getMacAddress(), time))
                .setLogging("su", Log.VERBOSE)
                .noCache()
                .as(AvgWaitTimeResponse.class)
                .setCallback(new FutureCallback<AvgWaitTimeResponse>() {
                    @Override
                    public void onCompleted(Exception e, final AvgWaitTimeResponse result) {
                        if (e == null) {
                            if (result.getStatus() == 200) {
                                if(result.getData() != null) {
                                    avg_time_list_view.setAdapter(new AvgWaitTimeAdapter(getActivity(), result.getData()));
                                }
                            }
                        } else {
                            Log.i("su", "Error");
                            e.printStackTrace();
                        }
                    }
                });
        */


    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth){
        /*
        Date t = new Date();
        t.setDate(dayOfMonth);
        t.setYear(year - 1900);
        t.setMonth(monthOfYear);*/
        //loadReport("" + t.getTime());
        loadReport(monthOfYear, dayOfMonth, year);

        monthOfYear += 1;
        String date = year + "-" + (monthOfYear < 10 ? "0" + monthOfYear : monthOfYear + "")
                + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : "" + dayOfMonth);
        time_text_view.setText(date);
    }
    public void showDatePicker()
    {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );
        dpd.show(getFragmentManager(), "Datepickerdialog");
    }
}
