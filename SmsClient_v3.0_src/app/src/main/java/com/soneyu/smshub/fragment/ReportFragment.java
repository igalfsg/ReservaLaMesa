package com.soneyu.smshub.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.soneyu.App;
import com.soneyu.smshub.R;

import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportFragment extends Fragment
{

    private TextView total_customer_text_view, cancel_customer_text_view, seat_customer_text_view, average_wait_time_text_view, avg_pt_size_text_view;
    public ReportFragment()
    {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        getActivity().setTitle("Reporte General");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_real_report, container, false);

        total_customer_text_view = (TextView) view.findViewById(R.id.total_customer_text_view);
        seat_customer_text_view = (TextView) view.findViewById(R.id.seat_customer_text_view);
        cancel_customer_text_view = (TextView) view.findViewById(R.id.cancel_customer_text_view);
        average_wait_time_text_view = (TextView) view.findViewById(R.id.average_wait_time_text_view);
        avg_pt_size_text_view = (TextView) view.findViewById(R.id.avg_pt_size_text_view);

        Spinner spinner = (Spinner) view.findViewById(R.id.select_time_spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                switch (i)
                {
                    case 0:
                        loadReport(60);//last hour
                        break;
                    case 1:
                        loadReport(1);//last day
                        break;
                    case 2:
                        loadReport(3);//last 3 days
                        break;
                    case 3:
                        loadReport(10);//last 10 days
                        break;
                    case 4:
                        loadReport(30);//last 30 days
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        return view;
    }
    private void runOnUIThreadIfVisible(Runnable run)
    {
        if (getActivity() != null && isAdded())
        {
            getActivity().runOnUiThread(run);
        }
    }
    private void loadReport(int minAgo)// 60 is min all the other are days
    {

        if(minAgo == 60){
            Calendar cal = Calendar.getInstance();
            int second = cal.get(Calendar.SECOND);
            cal.add(Calendar.HOUR_OF_DAY, -1);
            second += (cal.get(Calendar.MINUTE) * 60);
            second += (cal.get(Calendar.HOUR_OF_DAY) * 3600);
            int day = (cal.get(Calendar.DATE));
            int month = (cal.get(Calendar.MONTH));
            int year = (cal.get(Calendar.YEAR));
            ParseQuery<ParseObject> query_clients = ParseQuery.getQuery("Clientes");
            query_clients.setLimit(1000);
            query_clients.whereNotEqualTo("salida", 0);
            query_clients.whereEqualTo("restaurant", App.getMacAddress());
            query_clients.whereGreaterThanOrEqualTo("day", day);
            query_clients.whereGreaterThanOrEqualTo("month", month);
            query_clients.whereGreaterThanOrEqualTo("year", year);
            query_clients.whereGreaterThanOrEqualTo("entrada", second);
            query_clients.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                    if (e == null) {
                        int count_cancel = 0;
                        int count_seat = 0;
                        int total_customer;
                        int entrada;
                        int salida;
                        int resultado = 0;
                        int count = 0;
                        int avg_size = 0;
                        for (ParseObject cliente : clientelist) {
                            if (cliente.getString("status").equals("seat") || cliente.getString("status").equals("served")) {
                                count_seat++;
                            } else if (cliente.getString("status").equals("cancel")) {
                                count_cancel++;
                            }
                            entrada = cliente.getInt("entrada");
                            salida = cliente.getInt("salida");
                            if (entrada > salida){
                                //if they are in a different day
                                salida += (3600 *24);
                            }
                            avg_size += cliente.getInt("tamano");
                            resultado += salida - entrada;
                            count++;
                        }//end for
                        //total customers
                        total_customer = count_seat + count_cancel;
                        total_customer_text_view.setText(total_customer + " Clientes");
                        cancel_customer_text_view.setText(count_cancel + " Clientes");
                        seat_customer_text_view.setText(count_seat + " Clientes");
                        //avg time stuff
                        if(count > 0) {
                            resultado = (resultado / count);
                            average_wait_time_text_view.setText(App.prettySeconds(resultado));
                            //avg size
                            avg_size = avg_size / count;
                            avg_pt_size_text_view.setText(Integer.toString(avg_size));
                        }
                        else{
                            avg_size = 0;
                            avg_pt_size_text_view.setText(Integer.toString(avg_size));
                            resultado = 0;
                            average_wait_time_text_view.setText(App.prettySeconds(resultado));
                        }
                    }//end if e == null
                    else {
                        // The request failed
                        Toast.makeText(getActivity(), "Error cargando reporte", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else if (minAgo < 30){
            Calendar cal = Calendar.getInstance();
            minAgo *= -1;
            cal.add(Calendar.DATE, minAgo);
            int day = (cal.get(Calendar.DATE));
            int month = (cal.get(Calendar.MONTH));
            int year = (cal.get(Calendar.YEAR));
            ParseQuery<ParseObject> query_clients = ParseQuery.getQuery("Clientes");
            query_clients.setLimit(1000);
            query_clients.whereNotEqualTo("salida", 0);
            query_clients.whereEqualTo("restaurant", App.getMacAddress());
            query_clients.whereGreaterThanOrEqualTo("day", day);
            query_clients.whereGreaterThanOrEqualTo("month", month);
            query_clients.whereGreaterThanOrEqualTo("year", year);
            query_clients.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                    if (e == null) {
                        int count_cancel = 0;
                        int count_seat = 0;
                        int total_customer;
                        int entrada;
                        int salida;
                        int resultado = 0;
                        int count = 0;
                        int avg_size = 0;
                        for (ParseObject cliente : clientelist) {
                            if (cliente.getString("status").equals("seat") || cliente.getString("status").equals("served")) {
                                count_seat++;
                            } else if (cliente.getString("status").equals("cancel")) {
                                count_cancel++;
                            }
                            entrada = cliente.getInt("entrada");
                            salida = cliente.getInt("salida");
                            if (entrada > salida){
                                //if they are in a different day
                                salida += (3600 *24);
                            }
                            avg_size += cliente.getInt("tamano");
                            resultado += salida - entrada;
                            count++;
                        }//end for
                        //total customers
                        total_customer = count_seat + count_cancel;
                        total_customer_text_view.setText(total_customer + " Clientes");
                        cancel_customer_text_view.setText(count_cancel + " Clientes");
                        seat_customer_text_view.setText(count_seat + " Clientes");
                        //avg time stuff
                        if(count > 0) {
                            resultado = (resultado / count);
                            average_wait_time_text_view.setText(App.prettySeconds(resultado));
                            //avg size
                            avg_size = avg_size / count;
                            avg_pt_size_text_view.setText(Integer.toString(avg_size));
                        }
                        else{
                            avg_size = 0;
                            avg_pt_size_text_view.setText(Integer.toString(avg_size));
                            resultado = 0;
                            average_wait_time_text_view.setText(App.prettySeconds(resultado));
                        }
                    }//end if e == null
                    else {
                        // The request failed
                        Toast.makeText(getActivity(), "Error cargando reporte", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        else {
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -10);
            int day = (cal.get(Calendar.DATE));
            int month = (cal.get(Calendar.MONTH));
            int year = (cal.get(Calendar.YEAR));
            ParseQuery<ParseObject> query_clients = ParseQuery.getQuery("Clientes");
            query_clients.setLimit(1000);
            query_clients.whereNotEqualTo("salida", 0);
            query_clients.whereEqualTo("restaurant", App.getMacAddress());
            query_clients.whereGreaterThanOrEqualTo("day", day);
            query_clients.whereGreaterThanOrEqualTo("month", month);
            query_clients.whereGreaterThanOrEqualTo("year", year);
            query_clients.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                    if (e == null) {
                        int count_cancel = 0;
                        int count_seat = 0;
                        int total_customer;
                        int entrada;
                        int salida;
                        int resultado = 0;
                        int count = 0;
                        int avg_size = 0;
                        for (ParseObject cliente : clientelist) {
                            if (cliente.getString("status").equals("seat") || cliente.getString("status").equals("served")) {
                                count_seat++;
                            } else if (cliente.getString("status").equals("cancel")) {
                                count_cancel++;
                            }
                            entrada = cliente.getInt("entrada");
                            salida = cliente.getInt("salida");
                            if (entrada > salida){
                                //if they are in a different day
                                salida += (3600 *24);
                            }
                            avg_size += cliente.getInt("tamano");
                            resultado += salida - entrada;
                            count++;
                        }//end for
                        final int curr_count_cancel1 = count_cancel;
                        final int curr_count_seat1 = count_seat;
                        final int curr_time1 = resultado;
                        final int count1 = count;
                        final int curr_size1 = avg_size;


                        Calendar cal = Calendar.getInstance();
                        cal.add(Calendar.DATE, -10);
                        int day1 = (cal.get(Calendar.DATE));
                        int month1 = (cal.get(Calendar.MONTH));
                        int year1 = (cal.get(Calendar.YEAR));
                        Calendar cal1 = Calendar.getInstance();
                        cal1.add(Calendar.DATE, -20);
                        int day = (cal1.get(Calendar.DATE));
                        int month = (cal1.get(Calendar.MONTH));
                        int year = (cal1.get(Calendar.YEAR));
                        ParseQuery<ParseObject> query_clients = ParseQuery.getQuery("Clientes");
                        query_clients.setLimit(1000);
                        query_clients.whereNotEqualTo("salida", 0);
                        query_clients.whereEqualTo("restaurant", App.getMacAddress());
                        query_clients.whereGreaterThanOrEqualTo("day", day);
                        query_clients.whereGreaterThanOrEqualTo("month", month);
                        query_clients.whereGreaterThanOrEqualTo("year", year);
                        query_clients.whereLessThan("day", day1);
                        query_clients.whereLessThan("month", month1);
                        query_clients.whereLessThan("year", year1);
                        query_clients.findInBackground(new FindCallback<ParseObject>() {
                            @Override
                            public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                                if (e == null) {
                                    int count_cancel = curr_count_cancel1;
                                    int count_seat = curr_count_seat1;
                                    int entrada;
                                    int salida;
                                    int resultado = curr_time1;
                                    int count = count1;
                                    int avg_size = curr_size1;
                                    for (ParseObject cliente : clientelist) {
                                        if (cliente.getString("status").equals("seat") || cliente.getString("status").equals("served")) {
                                            count_seat++;
                                        } else if (cliente.getString("status").equals("cancel")) {
                                            count_cancel++;
                                        }
                                        entrada = cliente.getInt("entrada");
                                        salida = cliente.getInt("salida");
                                        if (entrada > salida) {
                                            //if they are in a different day
                                            salida += (3600 * 24);
                                        }
                                        avg_size += cliente.getInt("tamano");
                                        resultado += salida - entrada;
                                        count++;
                                    }//end for
                                    final int curr_count_cancel2 = count_cancel;
                                    final int curr_count_seat2 = count_seat;
                                    final int curr_time2 = resultado;
                                    final int count2 = count;
                                    final int curr_size2 = avg_size;
                                    Calendar cal1 = Calendar.getInstance();
                                    cal1.add(Calendar.DATE, -20);
                                    int day1 = (cal1.get(Calendar.DATE));
                                    int month1 = (cal1.get(Calendar.MONTH));
                                    int year1 = (cal1.get(Calendar.YEAR));
                                    Calendar cal = Calendar.getInstance();
                                    cal.add(Calendar.DATE, -30);
                                    int day = (cal.get(Calendar.DATE));
                                    int month = (cal.get(Calendar.MONTH));
                                    int year = (cal.get(Calendar.YEAR));
                                    ParseQuery<ParseObject> query_clients = ParseQuery.getQuery("Clientes");
                                    query_clients.setLimit(1000);
                                    query_clients.whereNotEqualTo("salida", 0);
                                    query_clients.whereEqualTo("restaurant", App.getMacAddress());
                                    query_clients.whereGreaterThanOrEqualTo("day", day);
                                    query_clients.whereGreaterThanOrEqualTo("month", month);
                                    query_clients.whereGreaterThanOrEqualTo("year", year);
                                    query_clients.whereLessThan("day", day1);
                                    query_clients.whereLessThan("month", month1);
                                    query_clients.whereLessThan("year", year1);
                                    query_clients.findInBackground(new FindCallback<ParseObject>() {
                                        @Override
                                        public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                                            if (e == null) {
                                                int count_cancel = curr_count_cancel2;
                                                int count_seat = curr_count_seat2;
                                                int total_customer;
                                                int entrada;
                                                int salida;
                                                int resultado = curr_time2;
                                                int count = count2;
                                                int avg_size = curr_size2;
                                                for (ParseObject cliente : clientelist) {
                                                    if (cliente.getString("status").equals("seat") || cliente.getString("status").equals("served")) {
                                                        count_seat++;
                                                    } else if (cliente.getString("status").equals("cancel")) {
                                                        count_cancel++;
                                                    }
                                                    entrada = cliente.getInt("entrada");
                                                    salida = cliente.getInt("salida");
                                                    if (entrada > salida){
                                                        //if they are in a different day
                                                        salida += (3600 *24);
                                                    }
                                                    avg_size += cliente.getInt("tamano");
                                                    resultado += salida - entrada;
                                                    count++;
                                                }//end for
                                                //total customers
                                                total_customer = count_seat + count_cancel;
                                                total_customer_text_view.setText(total_customer + " Clientes");
                                                cancel_customer_text_view.setText(count_cancel + " Clientes");
                                                seat_customer_text_view.setText(count_seat + " Clientes");
                                                //avg time stuff
                                                if(count > 0) {
                                                    resultado = (resultado / count);
                                                    average_wait_time_text_view.setText(App.prettySeconds(resultado));
                                                    //avg size
                                                    avg_size = avg_size / count;
                                                    avg_pt_size_text_view.setText(Integer.toString(avg_size));
                                                }
                                                else{
                                                    avg_size = 0;
                                                    avg_pt_size_text_view.setText(Integer.toString(avg_size));
                                                    resultado = 0;
                                                    average_wait_time_text_view.setText(App.prettySeconds(resultado));
                                                }
                                            }//end if e == null
                                            else {
                                                // The request failed
                                                Toast.makeText(getActivity(), "Error cargando reporte", Toast.LENGTH_LONG).show();
                                            }
                                        }
                                    });
                                }//end if e == null
                                else {
                                    // The request failed
                                    Toast.makeText(getActivity(), "Error cargando reporte", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }//end if e == null
                    else {
                        // The request failed
                        Toast.makeText(getActivity(), "Error cargando reporte", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        /*
        Ion.with(getActivity())
                .load(String.format(Global.CUSTOMER_COUNT_URL, App.getMacAddress(), minAgo))
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

        Ion.with(getActivity())
                .load(String.format(Global.AVERAGE_WAIT_TIME, App.getMacAddress(), minAgo))
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
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
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

        Ion.with(getActivity())
                .load(String.format(Global.AVERAGE_PARTY_SIZE_URL, App.getMacAddress(), minAgo))
                .setLogging("su", Log.VERBOSE)
                .noCache()
                .as(FloatCountResponse.class)
                .setCallback(new FutureCallback<FloatCountResponse>()
                {
                    @Override
                    public void onCompleted(Exception e, final FloatCountResponse result)
                    {
                        if (e == null)
                        {
                            if (result.getStatus() == 200)
                            {
                                Log.i("su", result.getData() + " seconds");
                                runOnUIThreadIfVisible(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        avg_pt_size_text_view.setText(String.format("%.2f", result.getData()));
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
                });*/
    }
}
