package com.michael.rawr;

import java.util.Date;
import java.util.List;

import com.piyush.hacknc.NextBusTime;

public class BusChooser {
	LatLng origin, dest;
	
	BusChooser(LatLng origin, LatLng dest) {
		this.origin = origin;
		this.dest = dest;
	}
	
	List<NextBusTime> getRoute() {
		
		BusList list = new BusList(origin, dest, new Date());
		List<NextBusTime> stops = list.getBusStop();
		return stops;
	}
}
