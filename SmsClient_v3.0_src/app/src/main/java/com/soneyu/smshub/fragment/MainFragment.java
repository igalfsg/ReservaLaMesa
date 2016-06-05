package com.soneyu.smshub.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.soneyu.App;
import com.soneyu.core.GcmSender;
import com.soneyu.smshub.R;
import com.soneyu.smshub.data.Customer;
import com.soneyu.smshub.data.CustomerAdapter2;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

public class MainFragment extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public String add_message = "test";
    public String cancel_message;
    public String ready_message;
    public String ping_message;
    private TextView status_textview;
    private Button register_button;
    private ProgressBar loading_prg;
    private FloatingActionButton float_button;
    private boolean device_activated = false;
    private OnFragmentInteractionListener mListener;
    //private ListView customer_listview;
    private RecyclerView customer_recylerview;
    private TextView restaurant_name_text_view, average_wait_time_text_view;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2)
    {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainFragment()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().setTitle("Lista De Espera");
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    public BroadcastReceiver receiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if(intent.getAction().equalsIgnoreCase("update_info"))
            {
                Log.i("su", "RECEIVING Broadcast listener");
                loadLastHourAvgWaitTime();
            }
        }
    };

    private void checkStatus()
    {

        loading_prg.setVisibility(View.VISIBLE);
        register_button.setVisibility(View.INVISIBLE);
        status_textview.setVisibility(View.INVISIBLE);
        float_button.setVisibility(View.INVISIBLE);
        customer_recylerview.setVisibility(View.INVISIBLE);
        restaurant_name_text_view.setVisibility(View.INVISIBLE);
        ParseQuery<ParseObject> activation_query = ParseQuery.getQuery("Usuarios");
        activation_query.whereEqualTo("uuid", App.getMacAddress());
        activation_query.getFirstInBackground(new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject parseObject, ParseException e) {
                if (e == null) {
                    device_activated = true;
                    runOnUIThreadIfVisible(new Runnable() {
                        @Override
                        public void run() {
                            float_button.setVisibility(View.VISIBLE);
                            status_textview.setText("Dispositivo activado");
                            //status_textview.setVisibility(View.VISIBLE);
                            loadCustomer(App.getMacAddress());
                            loadLastHourAvgWaitTime();
                            loading_prg.setVisibility(View.INVISIBLE);

                        }
                    });
                } else {
                    if (e.getCode() == 101) {
                        runOnUIThreadIfVisible(new Runnable() {
                            @Override
                            public void run() {
                                status_textview.setText("Su dispositivo no esta registrado Favor de contactarnos");
                                status_textview.setVisibility(View.VISIBLE);
                                loading_prg.setVisibility(View.INVISIBLE);
                                register_button.setVisibility(View.VISIBLE);
                                restaurant_name_text_view.setVisibility(View.VISIBLE);
                            }
                        });
                    } else {
                        runOnUIThreadIfVisible(new Runnable() {
                            @Override
                            public void run() {
                                status_textview.setText("Error no esperado. Favor de contactarnos");
                                status_textview.setVisibility(View.VISIBLE);
                                loading_prg.setVisibility(View.INVISIBLE);
                                register_button.setVisibility(View.VISIBLE);
                                restaurant_name_text_view.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                }
            }
        });


    }// finish checkstatus

    private void runOnUIThreadIfVisible(Runnable run)
    {
        if (getActivity() != null)
        {
            getActivity().runOnUiThread(run);
        }
        else
        {
            Log.i("su", "UI not show, donot need to update");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the customer_item for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        customer_recylerview = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        customer_recylerview.setLayoutManager(new LinearLayoutManager(container.getContext()));

        register_button = (Button) view.findViewById(R.id.register_button);
        status_textview = (TextView) view.findViewById(R.id.status_textview);
        loading_prg = (ProgressBar) view.findViewById(R.id.loading_prg);
        float_button = (FloatingActionButton) view.findViewById(R.id.float_button);
        //customer_recylerview = (ListView) view.findViewById(R.id.customer_listview);
        restaurant_name_text_view = (TextView) view.findViewById(R.id.restaurant_name_text_view);
        average_wait_time_text_view = (TextView) view.findViewById(R.id.avg_wait_time_text_view);

        float_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNewCustomerDialog();
            }
        });

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String restaurant_name = restaurant_name_text_view.getText().toString().trim();
                if (restaurant_name.length() == 0) {
                    Toast.makeText(getActivity(), "You should enter your restaurant name", Toast.LENGTH_LONG).show();
                } else {
                    // Add custom implementation, as needed.

                    ParseObject new_restaurant = new ParseObject("Usuarios");
                    new_restaurant.put("user", restaurant_name);
                    new_restaurant.put("uuid", App.getMacAddress());
                    new_restaurant.put("add", "Bienvenido %s a " + restaurant_name + " por favor espere en lo que su mesa esta lista.");
                    new_restaurant.put("cancel", "Lo Sentimos %s su mesa ha sido cancelada, visitenos pronto.");
                    new_restaurant.put("ready", "Hola %s, su mesa esta lista. Favor de avisarle a nuestra hostess.");
                    new_restaurant.put("ping", "%s, le recordamos que su mesa esta lista. Favor de avisarle a nuestra hostess.");
                    new_restaurant.pinInBackground();
                    new_restaurant.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                //Toast.makeText(Change_Weight.this, "Changed Weights", Toast.LENGTH_SHORT).show();
                                Log.i("mbp", "registeration");
                                runOnUIThreadIfVisible(new Runnable() {
                                    @Override
                                    public void run() {
                                        checkStatus();
                                    }
                                });
                            } else {
                                Log.i("mbp", "messed up registeration");
                                //Toast.makeText(Change_Weight.this, "Saved on Phone not on network", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });



                }
            }
        });
        checkStatus();
        return view;
    }
    public void loadLastHourAvgWaitTime()
    {
        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);
        cal.add(Calendar.HOUR_OF_DAY, -1);
        second += (cal.get(Calendar.MINUTE) * 60);
        second += (cal.get(Calendar.HOUR_OF_DAY) * 3600);
        int day = (cal.get(Calendar.DATE));
        int month = (cal.get(Calendar.MONTH));
        int year = (cal.get(Calendar.YEAR));
        ParseQuery<ParseObject> query_avg_time = ParseQuery.getQuery("Clientes");
        query_avg_time.setLimit(1000);
        query_avg_time.whereEqualTo("day", day);
        query_avg_time.whereEqualTo("month", month);
        query_avg_time.whereEqualTo("year", year);
        query_avg_time.whereEqualTo("restaurant", App.getMacAddress());
        query_avg_time.whereNotEqualTo("salida", 0);
        query_avg_time.whereGreaterThan("entrada", second);
        query_avg_time.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                if (e == null) {
                    // The count request succeeded. Log the count
                    int entrada;
                    int salida;
                    int resultado = 0;
                    int count = 0;
                    for (ParseObject cliente : clientelist) {
                        entrada = cliente.getInt("entrada");
                        salida = cliente.getInt("salida");
                        if (entrada > salida) {
                            //if they are in a different day
                            salida += (3600 * 24);
                        }
                        resultado += salida - entrada;
                        count++;
                    }
                    int res = 0;
                    if (count > 0) {
                        res = (resultado / count);
                    }
                    final int tot = res;
                    runOnUIThreadIfVisible(new Runnable() {
                        @Override
                        public void run() {
                            average_wait_time_text_view.setText("Tiempo de espera ultima hora: " + App.prettySeconds(tot));
                        }
                    });

                } else {
                    // The request failed
                    Toast.makeText(getActivity(), "Error cargando tiempo de espera" + App.getMacAddress(), Toast.LENGTH_LONG).show();
                }
            }
        });
        //load messages
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Usuarios");
        query.whereEqualTo("uuid", App.getMacAddress());
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (object == null) {
                    Log.d("score", "The getFirst request failed.");
                    add_message = "Bienvenido %s por favor espere en lo que su mesa esta lista.";
                    cancel_message = "Lo Sentimos %s su mesa ha sido cancelada, visitenos pronto.";
                    ready_message = "Hola %s, su mesa esta lista. Favor de avisarle a nuestra hostess.";
                    ping_message = "%s, le recordamos que su mesa esta lista. Favor de avisarle a nuestra hostess.";
                } else {
                    Log.d("score", "Retrieved the object.");
                    add_message = object.getString("add");
                    cancel_message = object.getString("cancel");
                    ready_message = object.getString("ready");
                    ping_message = object.getString("ping");
                    Log.d("score", "got the strings");

                }
            }
        });
    }
    // TODO: Rename method, update argument and hook method into UI event

    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            mListener = (OnFragmentInteractionListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("update_info");

        getActivity().registerReceiver(receiver, filter);
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
        getActivity().unregisterReceiver(receiver);
    }

    private void showNewCustomerDialog()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Agregar Cliente Nuevo");
        alert.setIcon(R.drawable.ic_action_users_add_user_icon);
        LayoutInflater li = getActivity().getLayoutInflater();
        View view = li.inflate(R.layout.activity_new_customer, null);
        final EditText name_edittext = (EditText) view.findViewById(R.id.name_edittext);
        final EditText phone_edittext = (EditText) view.findViewById(R.id.phone_edittext);
        final Spinner people_spinner = (Spinner) view.findViewById(R.id.number_of_people_spinner);
        final EditText note_edittext = (EditText) view.findViewById(R.id.note_edittext);

        alert.setView(view);

        alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = name_edittext.getText().toString().trim();
                String phone = phone_edittext.getText().toString().trim();
                String people = people_spinner.getSelectedItem().toString().trim();
                String note = note_edittext.getText().toString().trim();
                String uuid = App.getMacAddress();
                if (name.length() == 0) {
                    Toast.makeText(getActivity(), "Nombre Invalido", Toast.LENGTH_LONG).show();
                }
                else {
                    addUser(name, phone, people, note, uuid);
                }

                return;
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                return;
            }
        });
        alert.show();
    }


    private void loadCustomer(String uuid)
    {
        customer_recylerview.setVisibility(View.VISIBLE);
        status_textview.setVisibility(View.INVISIBLE);
        //server contact

        ParseQuery<ParseObject> query_clients = ParseQuery.getQuery("Clientes");
        query_clients.setLimit(1000);
        query_clients.whereEqualTo("salida", 0);
        query_clients.whereEqualTo("restaurant", App.getMacAddress());
        query_clients.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> clientelist, com.parse.ParseException e) {
                if (e == null) {
                    // The count request succeeded. Log the count
                    int id;
                    int num;
                    String mesa;
                    String name;
                    String phone;
                    String note;
                    String status;
                    final List<Customer> data = new ArrayList<Customer>();
                    for (ParseObject cliente : clientelist) {
                        id = cliente.getInt("id");
                        name = cliente.getString("nombre");
                        phone = cliente.getString("tel");
                        note = cliente.getString("note");
                        status = cliente.getString("status");
                        num = cliente.getInt("tamano");
                        mesa = cliente.getString("mesa");
                        Customer curr_cus = new Customer(id, name, phone, note, status, num, mesa);
                        data.add(curr_cus);//esto esta medio pitero
                    }

                    Log.i("mbp", "Load data ok");
                    runOnUIThreadIfVisible(new Runnable() {
                        @Override
                        public void run() {
                            customer_recylerview.setVisibility(View.VISIBLE);
                            customer_recylerview.setAdapter(new CustomerAdapter2(getActivity(), MainFragment.this, data));
                        }
                    });
                } else {
                    // The request failed
                    e.printStackTrace();
                    runOnUIThreadIfVisible(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "Error cargando clientes", Toast.LENGTH_LONG).show();
                            customer_recylerview.setVisibility(View.INVISIBLE);

                        }
                    });
                }
            }
        });


    }//finish load customer

    private void addUser(final String name,final String phone, final String people, String note, final String uuid)
    {
        if(!phone.equals("")) {
            ParseQuery<ParseObject> query = ParseQuery.getQuery("Usuarios");
            query.whereEqualTo("uuid", App.getMacAddress());
            query.fromLocalDatastore();
            query.getFirstInBackground(new GetCallback<ParseObject>() {
                public void done(ParseObject object, ParseException e) {
                    if (object == null) {
                        Log.d("score", "The getFirst request failed.");
                        String mensaje = ("Bienvenido %s por favor espere en lo que su mesa esta lista.").replace("%s", name);
                        GcmSender.setTopicMessage(phone, mensaje);
                    } else {
                        Log.d("score", "Retrieved the object.");
                        String mensaje = object.getString("add").replaceAll("%s", name);
                        GcmSender.setTopicMessage(phone, mensaje);

                    }
                }
            });
        }
        //String mensaje = add_message.replaceAll("%s", name);
        Log.d("Messjae", add_message + name);


        Random rand = new Random();
        Calendar cal = Calendar.getInstance();
        int second = cal.get(Calendar.SECOND);
        second += (cal.get(Calendar.MINUTE) * 60);
        second += (cal.get(Calendar.HOUR_OF_DAY) * 3600);
        int day = (cal.get(Calendar.DATE));
        int month = (cal.get(Calendar.MONTH));
        int year = (cal.get(Calendar.YEAR));
        int  id = rand.nextInt(999999) + 1;
        id = (id * second) % 2147000000;
        ParseObject new_client = new ParseObject("Clientes");
        new_client.put("status", "wait");
        new_client.put("restaurant", App.getMacAddress());
        new_client.put("id", id);
        new_client.put("mesa", "0");
        new_client.put("day", day);
        new_client.put("month", month);
        new_client.put("year", year);
        new_client.put("tamano", Integer.parseInt(people));
        new_client.put("note", note);
        new_client.put("entrada", second);
        new_client.put("salida", 0);
        new_client.put("tel", phone);
        new_client.put("nombre", name);
        new_client.pinInBackground();
        new_client.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                     Log.i("mbp", "registeration");
                    //Toast.makeText(getActivity(), "gente" + people, Toast.LENGTH_LONG).show();
                    runOnUIThreadIfVisible(new Runnable() {
                        @Override
                        public void run() {
                            loadCustomer(uuid);
                        }
                    });
                } else {
                    Log.i("mbp", "messed up registeration");
                    Toast.makeText(getActivity(), "No se puede agregar cliente" + e, Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
