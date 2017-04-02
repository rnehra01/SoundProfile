package com.example.csn254.soundprofile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Created by rnehra on 2/4/17.
 */

public class custom_slot_adapter extends ArrayAdapter<String> {
    String [] days;
    String [] times;
    Context c;

    public custom_slot_adapter(Context context, String [] days, String [] times) {
        super(context, R.layout.custom_slot_row, days);
        this.c = context;
        this.days = days;
        this.times = times;
    }

    public class ViewHolder{
        TextView day_list;
        TextView time_list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater Inflater=LayoutInflater.from(getContext());
        View cutomView=Inflater.inflate(R.layout.custom_slot_row, parent, false);

        final ViewHolder holder=new ViewHolder();


        holder.day_list=(TextView)cutomView.findViewById(R.id.day);
        holder.time_list=(TextView)cutomView.findViewById(R.id.time_duration);
        holder.day_list.setText(days[position]);
        holder.time_list.setText(times[position]);
        return cutomView;
    }
}
