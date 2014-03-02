package com.michael.rawr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONRoute {
	public static class LatLng
	{
		public LatLng(double lat, double lng) {
			latitude = lat;
			longitude = lng;
		}

		public String toString() {
			return Double.toString(latitude) + "," + Double.toString(longitude);
		}
		public double latitude;
		public double longitude;
	};

	private LatLng origin, dest;
	private Date leaveTime;

	public JSONRoute() {
	}
	
	public JSONRoute(LatLng origin, LatLng dest, Date leaveTime) {
		this.origin = origin;
		this.dest = dest;
		this.leaveTime = leaveTime;
	}
	
	public void setOrigin(LatLng origin) {
		this.origin = origin; 
	}

	public void setDestination(LatLng dest) {
		this.dest = dest; 
	}
	
	public void setLeaveTune(Date leaveTime) {
		this.leaveTime = leaveTime;
	}
	
	/* Returns a JSON Object containing the route
	 * Returns null on failure
	 */
	public JSONObject getRoute() {
		String route = getStrRoute();
		if(route == null) {
			return null;
		}
		JSONObject json = null;
		try {
			json = new JSONObject(route);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return json;
	}
	
	/* Retrieves a string from Google's directions API
	 * This will contain Google's approximation of the
	 * route for the user to take
	 * Returns null on failure
	 */
	protected String getStrRoute() {
		/* Attempts to make a connection to Google's API */
		URLConnection conn;
		try {
			final String url_fmt =
					"http://maps.googleapis.com/maps/api/directions/json?" +
							"origin=%s&destination=%s&sensor=false&" +
							"departure_time=%s&mode=transit";
			String dtime = Long.toString(leaveTime.getTime() / 1000);
			String url = String.format(url_fmt, origin.toString(),
					dest.toString(), dtime);
			System.out.println(url);
			conn = new URL(url).openConnection();
			conn.connect();
			System.out.println(conn.getHeaderFields());
		}
		catch(MalformedURLException e) {
			//Stub
			e.printStackTrace();
			return null;
		}
		catch(IOException e) {
			//Stub
			e.printStackTrace();
			return null;
		}
		/* Successful connection! Now to read the datas */
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		}
		catch(IOException e) {
			//Stub
			e.printStackTrace();
			return null;
		}
		String json = "";
		int data;
		try {
			while((data = in.read()) != -1) {
				json += (char)data;
			}
		} catch (IOException e) {
			//Stub
			e.printStackTrace();
			return null;
		}
		return json;
	}
}
