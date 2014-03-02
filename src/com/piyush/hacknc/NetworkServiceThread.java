package com.piyush.hacknc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.michael.rawr.AddressToLatLng;
import com.michael.rawr.BusList;
import com.michael.rawr.LatLng;
import com.rohit.cool.PebbleInterface;

public class NetworkServiceThread extends Thread {

		Context context;
		Route earliestRoute;
		
		public NetworkServiceThread(Route route,Context context)
		{
			this.earliestRoute = route;
			this.context = context;
		}
		
	    @Override
	    public void run() 
	    {
			LatLng sourceLatLng = AddressToLatLng.AddressToLatLng(earliestRoute.source);
			LatLng destinationLatLng = AddressToLatLng.AddressToLatLng(earliestRoute.destination);
			
			BusList busList = new BusList(sourceLatLng, destinationLatLng, new Date());   // TODO: Correct the time here
			List<NextBusTime> nextBusTimeList = busList.getBusStop();		
			
			PebbleInterface.sendStringToPebble((ArrayList<NextBusTime>)nextBusTimeList, context);
	    }
}
