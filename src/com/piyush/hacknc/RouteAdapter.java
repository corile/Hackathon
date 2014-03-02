package com.piyush.hacknc;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
 
 
public class RouteAdapter extends ArrayAdapter<Route> {
 
    int resource;
    String response;
    Context context;
    
    //Initialize adapter
    public RouteAdapter(Context context, int resource, List<Route> items) {
        super(context, resource, items);
        this.resource=resource;
    }
     
     
    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LinearLayout alertView;
        //Get the current alert object
        Route route = getItem(position);
         
        //Inflate the view
        if(convertView==null)
        {
            alertView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater)getContext().getSystemService(inflater);
            vi.inflate(resource, alertView, true);
        }
        else
        {
        	System.out.println("Should I be here?");
            alertView = (LinearLayout) convertView;
        }
        //Get the text boxes from the listitem.xml file
        TextView source =(TextView)alertView.findViewById(R.id.source);
        TextView destination =(TextView)alertView.findViewById(R.id.destination);
        TextView time =(TextView)alertView.findViewById(R.id.time);
         
        //Assign the appropriate data from our alert object above
        source.setText(route.source);
        destination.setText(route.destination);
        time.setText(route.time.toString());
         
        return alertView;
    }
 
}