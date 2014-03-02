package com.michael.rawr;

import java.util.Date;
import java.util.List;

import com.michael.rawr.BusList.BusStop;

public class BusChooser {
	LatLng origin, dest;
	
	BusChooser(LatLng origin, LatLng dest) {
		this.origin = origin;
		this.dest = dest;
	}
	
	List<BusStop> getRoute() {
		
		BusList list = new BusList(origin, dest, new Date());
		List<BusStop> stops = list.getBusStop();
		return stops;
	}
}
