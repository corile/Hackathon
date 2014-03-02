package com.michael.rawr;

public class LatLng {

	public LatLng(double lat, double lng) {
		latitude = lat;
		longitude = lng;
	}

	public LatLng() {
	}

	public String toString() {
		return Double.toString(latitude) + "," + Double.toString(longitude);
	}
	
	public double latitude;
	public double longitude;
}
