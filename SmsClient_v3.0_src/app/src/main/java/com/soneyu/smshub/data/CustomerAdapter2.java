package com.soneyu.smshub.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.soneyu.App;
import com.soneyu.smshub.R;
import com.soneyu.smshub.fragment.MainFragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by User on 9/1/2015.
 */
public class CustomerAdapter2 extends RecyclerView.Adapter<CustomerAdapter2.ViewHolder>
{
    private List<Customer> customers;
    private Context mContext;
    private boolean nofilter;
    private MainFragment fragment;

    public CustomerAdapter2(Context context, MainFragment frag, List<Customer> customers)
    {
        this.mContext = context;
        this.customers = new ArrayList<>();
        this.fragment = frag;

        for (Customer cs : customers)
        {
            if (cs.getStatus().equalsIgnoreCase("cancel") || cs.getStatus().equalsIgnoreCase("seat"))
            {
            }
            else
            {
                this.customers.add(cs);
            }
        }
        Log.i("mbp", "Number of customer: " + this.customers.size());
    }
    public CustomerAdapter2(Context context, List<Customer> customers, boolean nofilter)
    {
        this.mContext = context;
        this.customers = customers;
        this.nofilter = nofilter;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.customer_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {
        final Customer customer = customers.get(i);

        viewHolder.ready_image_button.setTag(customer);
        viewHolder.cancel_image_button.setTag(customer);
        viewHolder.seat_image_button.setTag(customer);
        viewHolder.ping_image_button.setTag(customer);

        viewHolder.ready_image_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setCancelable(false);
                dialog.setMessage("Notificando Cliente...");
                dialog.show();
                final Customer cus = (Customer) view.getTag();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Clientes");
                query.whereEqualTo("id", cus.getId());
                query.whereEqualTo("restaurant", App.getMacAddress());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject cliente, ParseException e) {
                        if (e == null) {
                            Calendar cal = Calendar.getInstance();
                            int second = cal.get(Calendar.SECOND);
                            second += (cal.get(Calendar.MINUTE) * 60);
                            second += (cal.get(Calendar.HOUR_OF_DAY) * 3600);
                            cliente.put("salida", second);
                            cliente.put("status", "served");
                            cliente.saveInBackground();
                            cus.setStatus("served");
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "No Se Pudo Contactar al Servidor", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

                Intent intent = new Intent();
                intent.setAction("update_info");
                mContext.sendBroadcast(intent);
            }
        });

        viewHolder.ping_image_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setCancelable(false);
                dialog.setMessage("Ping user...");
                dialog.show();
                final Customer cus = (Customer) view.getTag();

                ParseQuery<ParseObject> query = ParseQuery.getQuery("Clientes");
                query.whereEqualTo("id", cus.getId());
                query.whereEqualTo("restaurant", App.getMacAddress());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject cliente, ParseException e) {
                        if (e == null) {
                            cliente.put("status", "ping");
                            cliente.saveInBackground();
                            cus.setStatus("ping");
                            notifyDataSetChanged();
                        } else {
                            Toast.makeText(mContext, "No Se Pudo Contactar al Servidor", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
            }
        });

        viewHolder.seat_image_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                final Customer cus = (Customer) view.getTag();
                showSetTableDialog(cus);
            }
        });


        viewHolder.cancel_image_button.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                final ProgressDialog dialog = new ProgressDialog(mContext);
                dialog.setCancelable(false);
                dialog.setMessage("Notificando Cliente...");
                dialog.show();
                final Customer cus = (Customer) view.getTag();
                ParseQuery pushQuery = ParseInstallation.getQuery();
                pushQuery.whereEqualTo("username", "cell");
                String message = "says Hi!";

                //push stuff
                ParsePush push = new ParsePush();
                push.setQuery(pushQuery); // Set our Installation query
                push.setMessage(message);
                push.sendInBackground();
                Toast.makeText(mContext, "eres un chingon", Toast.LENGTH_LONG).show();


                ParseQuery<ParseObject> query = ParseQuery.getQuery("Clientes");
                query.whereEqualTo("id", cus.getId());
                query.whereEqualTo("restaurant", App.getMacAddress());
                query.getFirstInBackground(new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject cliente, ParseException e) {
                        if (e == null) {
                            Calendar cal = Calendar.getInstance();
                            int second = cal.get(Calendar.SECOND);
                            second += (cal.get(Calendar.MINUTE) * 60);
                            second += (cal.get(Calendar.HOUR_OF_DAY) * 3600);
                            cliente.put("salida", second);
                            cliente.put("status", "cancel");
                            cliente.saveInBackground();
                            cus.setStatus("cancel");
                            customers.remove(cus);
                            notifyDataSetChanged();

                        } else {
                            Toast.makeText(mContext, "No Se Pudo Contactar al Servidor", Toast.LENGTH_LONG).show();
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });

            }
        });

        viewHolder.name_textview.setText(customer.getName());


        if(nofilter)
        {
            viewHolder.phone_textview.setText("Numero De Mesa: " + customer.getTable_number() + ". Numero de Gente: " + customer.getNumber_of_people());
        }
        else
        {
            viewHolder.phone_textview.setText("Numero De Gente: " + customer.getNumber_of_people());
        }

        viewHolder.note_textview.setText(customer.getNote());

        viewHolder.ping_image_button.setVisibility(View.INVISIBLE);
        viewHolder.seat_image_button.setVisibility(View.INVISIBLE);
        viewHolder.ready_image_button.setVisibility(View.INVISIBLE);
        viewHolder.cancel_image_button.setVisibility(View.INVISIBLE);

        if (customer.getStatus().equalsIgnoreCase(Customer.SERVED))
        {
            viewHolder.status_textview.setText("Servido");
            viewHolder.status_textview.setTextColor(Color.GREEN);

            viewHolder.ready_image_button.setVisibility(View.INVISIBLE);
            viewHolder.cancel_image_button.setVisibility(View.INVISIBLE);

            viewHolder.ping_image_button.setVisibility(View.VISIBLE);
            viewHolder.seat_image_button.setVisibility(View.VISIBLE);
        }
        else if (customer.getStatus().equalsIgnoreCase(Customer.WAIT))
        {
            viewHolder.status_textview.setText("Esperando");
            viewHolder.status_textview.setTextColor(Color.RED);

            viewHolder.ready_image_button.setVisibility(View.VISIBLE);
            viewHolder.cancel_image_button.setVisibility(View.VISIBLE);
        }
        else if (customer.getStatus().equalsIgnoreCase(Customer.CANCEL))
        {
            viewHolder.status_textview.setText("Cancelado");
            viewHolder.status_textview.setTextColor(Color.YELLOW);

            viewHolder.ready_image_button.setVisibility(View.INVISIBLE);
            viewHolder.cancel_image_button.setVisibility(View.INVISIBLE);
        }
        else if (customer.getStatus().equalsIgnoreCase("ping"))
        {
            viewHolder.status_textview.setText("Ping");
            viewHolder.status_textview.setTextColor(Color.GREEN);

            viewHolder.seat_image_button.setVisibility(View.VISIBLE);
            viewHolder.cancel_image_button.setVisibility(View.VISIBLE);
        }
        else if (customer.getStatus().equalsIgnoreCase("seat"))
        {
            viewHolder.status_textview.setText("Sentado");
            viewHolder.status_textview.setTextColor(Color.GREEN);
        }

    }
    private void showSetTableDialog(final Customer customer)
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Escoja la Mesa");
        alert.setIcon(R.drawable.ic_action_users_add_user_icon);
        LayoutInflater li = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = li.inflate(R.layout.input_dialog, null);

        final EditText table_edit_text = (EditText) view.findViewById(R.id.table_edit_text);
        alert.setView(view);
        alert.setPositiveButton("OK", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int whichButton)
            {
                final String table = table_edit_text.getText().toString().trim();
                if (table.length() == 0)
                {
                    Toast.makeText(mContext, "numero de mesa no puede estar vacio", Toast.LENGTH_LONG).show();
                }
                else
                {
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Clientes");
                    query.whereEqualTo("id", customer.getId());
                    query.whereEqualTo("restaurant", App.getMacAddress());
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject cliente, ParseException e) {
                            if (e == null) {
                                cliente.put("mesa", table);
                                cliente.put("status", "seat");
                                //Toast.makeText(mContext, "yay"+ cliente.getString("status"), Toast.LENGTH_LONG).show();
                                cliente.saveInBackground();
                                //Toast.makeText(mContext, "yay", Toast.LENGTH_LONG).show();
                                customer.setStatus("ping");
                                customers.remove(customer);
                                notifyDataSetChanged();
                            } else {
                                Toast.makeText(mContext, "No Se Pudo Contactar al Servidor", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    });
                }

                return;
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {

            public void onClick(DialogInterface dialog, int which)
            {
                // TODO Auto-generated method stub
                return;
            }
        });
        alert.show();
    }
    @Override
    public int getItemCount()
    {
        return this.customers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageView avatar_img;
        TextView name_textview;
        TextView status_textview;
        TextView phone_textview;
        ImageButton ready_image_button;
        ImageButton cancel_image_button, ping_image_button, seat_image_button;
        TextView note_textview;

        public ViewHolder(View view)
        {
            super(view);

            avatar_img = (ImageView) view.findViewById(R.id.avatar_image_view);
            name_textview = (TextView) view.findViewById(R.id.name_textview);
            status_textview = (TextView) view.findViewById(R.id.status_textview);
            phone_textview = (TextView) view.findViewById(R.id.phone_textview);
            ready_image_button = (ImageButton) view.findViewById(R.id.ready_image_button);
            cancel_image_button = (ImageButton) view.findViewById(R.id.cancel_image_button);
            note_textview = (TextView) view.findViewById(R.id.note_textview);

            seat_image_button = (ImageButton) view.findViewById(R.id.seat_image_button);
            ping_image_button = (ImageButton) view.findViewById(R.id.ping_image_button);

        }
    }
}
