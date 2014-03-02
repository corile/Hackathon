package com.piyush.hacknc;

import java.io.Serializable;

import com.piyush.hacknc.Constants.Day;

import android.text.format.Time;

public class Route implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2L;
	
	public String source;
	public String destination;
	public String time;	
	//public Day[] days;

	public String toString()
	{
		return source+";"+destination+";"+time;
	}
}
