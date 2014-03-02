package com.piyush.hacknc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.michael.rawr.AddressToLatLng;
import com.michael.rawr.BusList;
import com.michael.rawr.LatLng;
import com.rohit.cool.PebbleInterface;

public class NotifyPebbleService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		super.onStartCommand(intent, flags, startId);
		
		Route earliestRoute = getEarliestRoute();
		
		LatLng sourceLatLng = AddressToLatLng.AddressToLatLng(earliestRoute.source);
		LatLng destinationLatLng = AddressToLatLng.AddressToLatLng(earliestRoute.destination);
		
		BusList busList = new BusList(sourceLatLng, destinationLatLng, new Date());   // TODO: Correct the time here
		List<NextBusTime> nextBusTimeList = busList.getBusStop();		
		
		PebbleInterface.sendStringToPebble((ArrayList<NextBusTime>)nextBusTimeList, this);
		
		AlarmSetter.setNextAlarm(this);
		
		System.out.println("That's all folks!");
		
		return START_STICKY;
	}

	protected Route getEarliestRoute()
	{
		ArrayList<Route> routeList = FileUtility.getRouteList(this);
		if(routeList==null||routeList.size()==0)
			return null;
		
		Route earliestRoute = routeList.get(0);
		for (Route route : routeList) {
			 if(isearlier(route.time,earliestRoute.time))
				 earliestRoute = route;
		}
		
		return earliestRoute;
	}
	
	public boolean isearlier(String t1,String t2)
	{
		int hour1 = Integer.parseInt(t1.split(":")[0]);
		int minute1 = Integer.parseInt(t1.split(":")[1]);
		int hour2 = Integer.parseInt(t2.split(":")[0]);
		int minute2 = Integer.parseInt(t2.split(":")[1]);
		
		if(hour1<hour2)
			return true;
		else if(hour1>hour2)
			return false;
		else if(minute1<minute2)
			return true;
		else
			return false;
	}
	
	protected String getTimeString()
	{
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		return Integer.toString(hour)+":"+Integer.toString(minute);
	}
}
