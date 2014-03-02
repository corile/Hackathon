package com.piyush.hacknc;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.content.ContextWrapper;

public class FileUtility {

	public static ArrayList<Route> getRouteList(ContextWrapper activity)
	{
		ArrayList<Route> routeList = new ArrayList<Route>();
		try{
			FileInputStream fis = activity.openFileInput(Constants.SAVEDROUTESFILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			while(fis.available()>0)
			{
				Route route = (Route)ois.readObject();
				routeList.add(route);
				System.out.println(route);
			}
			fis.close();
			System.out.println(routeList);
		}catch(Exception e)
		{
			System.out.println("Exception occured while reading saved route lists");
			e.printStackTrace();
		}
		return routeList;
	}
}
