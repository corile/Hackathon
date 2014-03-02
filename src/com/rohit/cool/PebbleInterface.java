package com.rohit.cool;

import java.util.ArrayList;

import com.piyush.hacknc.NextBusTime;


public class PebbleInterface {

	ArrayList<NextBusTime> nextBusTimes;
	
	private static ArrayList<String> BusName;
	private static ArrayList<Integer> BusTime;
	private static ArrayList<String> BusStop;
	
	public static void process(ArrayList<NextBusTime> busData)
	{	
		System.out.println("Hello" + busData.size());
		for(int i=0;i< busData.size();i++)
		{
			BusName.add(busData.get(i).getBusName());
			BusTime.add(busData.get(i).getEta());
			BusStop.add(busData.get(i).getStopName());
			System.out.println(BusName.get(i));
			System.out.println(BusTime.get(i));
			System.out.println(BusStop.get(i));
						
		}
	}
		
}

