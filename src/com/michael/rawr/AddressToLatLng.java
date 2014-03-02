package com.michael.rawr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONException;
import org.json.JSONObject;

public class AddressToLatLng {
	
	protected static final String url_fmt = "http://maps.googleapis.com/maps/api/geocode/json?sensor=false&address=";
	
	public static LatLng AddressToLatLng(String address) {
		String fmtaddr = address.replaceAll(", ", ",").replaceAll(" ", "+");
		String jlatlng = getJSONLatLng(url_fmt + fmtaddr);

		JSONObject location = null;
		try {
			location = new JSONObject(jlatlng).getJSONArray("results").
					getJSONObject(0).getJSONObject("geometry").
					getJSONObject("location");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		LatLng pos = new LatLng();
		try {
			pos.latitude = location.getDouble("lat");
			pos.longitude = location.getDouble("lng");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return pos;
	}
	
	protected static String getJSONLatLng(String url) {

		/* Attempts to make a connection to Google's API */
		URLConnection conn;
		try {
			conn = new URL(url).openConnection();
			conn.connect();
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
