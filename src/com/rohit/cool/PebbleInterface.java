package com.rohit.cool;

import java.util.ArrayList;
import java.util.UUID;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

import com.piyush.hacknc.NextBusTime;


public class PebbleInterface {
	
	private static final int BUFFER_LENGTH = 32;
	private static final int DATA_KEY = 0;
	private static final UUID APP_UUID = UUID.fromString("68c0e154-bf99-419b-8b6b-8777a8b65991");

	ArrayList<NextBusTime> nextBusTimes;
	
	private static ArrayList<String> BusName;
	private static ArrayList<Integer> BusTime;
	private static ArrayList<String> BusStop;
	
	private static String process(ArrayList<NextBusTime> busData)
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
		return (BusName.get(0)+" " + Integer.toString(BusTime.get(0))+" " +BusStop.get(0));
	}
	
	public static void sendStringToPebble(ArrayList<NextBusTime> busData, Context context) {
					
			//Create a PebbleDictionary
		String message;	
		PebbleDictionary dictionary = new PebbleDictionary();	
			//message = Integer.toString(message.length());
			//message = "hello stringsssss..";
			//message = "\0\0\0\0CPX\0SN";
			//message = "abc";
			message = process(busData);
			if(message.length() < BUFFER_LENGTH) {
			//Store a string in the dictionary using the correct key
			dictionary.addString(DATA_KEY, message);				
			//Send the Dictionary
			PebbleKit.sendDataToPebble(context, APP_UUID, dictionary);	
			
		} else {
			Log.i("sendStringToPebble()", "String too long!");
		}
	}
		
}

