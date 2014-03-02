package com.michael.rawr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.michael.rawr.JSONRoute.LatLng;

public class BusList {
	JSONRoute route;
	
	public static class BusStop {
		public String shortname, name;
		public String departStop, arriveStop;
		public Date departETA, arriveETA;
	}
	
	public BusList() {
		route = new JSONRoute();
	}
	
	public BusList(LatLng origin, LatLng dest, Date leaveTime) {
		route = new JSONRoute(origin, dest, leaveTime);
	}

	protected BusStop stepToStops(JSONObject step)
			throws JSONException, ParseException {
		BusStop stop = new BusStop();
		JSONObject details = step.getJSONObject("transit_details");
		stop.departStop = details.getJSONObject("departure_stop").
				getString("name");
		String dtime = details.getJSONObject("departure_time").getString("text");
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm", Locale.US);
		stop.departETA = sdf.parse(dtime);
		stop.arriveStop = details.getJSONObject("arrival_stop").
				getString("name");
		String atime = details.getJSONObject("arrival_time").getString("text");
		stop.arriveETA = sdf.parse(atime);
		stop.name = details.getJSONObject("line").getString("name");
		stop.shortname = details.getJSONObject("line").getString("short_name");
		return stop;
	}
	
	public List<BusStop> getBusStop() {
		JSONObject path = route.getRoute();
		//.routes[0].legs[0].steps[1].travel_mode
		JSONArray steps = null;
		try {
			steps = path.getJSONArray("routes").
					getJSONObject(0).getJSONArray("legs").
					getJSONObject(0).getJSONArray("steps");
		} catch (JSONException e) {
			//Stub
			e.printStackTrace();
			return null;
		}
		List<BusStop> stops = new LinkedList<BusStop>();
		for(int i = 0; i < steps.length(); i++) {
			String tmode = null;
			JSONObject s = null;
			try {
				s = steps.getJSONObject(i);
				tmode = s.getString("travel_mode");
			} catch (JSONException e) {
				//stub
				e.printStackTrace();
				return null;
			}
			if(tmode.compareTo("TRANSIT") == 0) {
				try {
					stops.add(stepToStops(s));
				} catch (JSONException e) {
					//stub
					e.printStackTrace();
					return null;
				} catch (ParseException e) {
					//stub
					e.printStackTrace();
					return null;
				}
			}
		}
		return stops;
	}
}
