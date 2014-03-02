package com.piyush.hacknc;

import java.io.FileOutputStream;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class RouteFragment extends Fragment {
	
	private static final String SAVEDROUTESFILENAME = "savedRoutes.txt";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

	    View root = inflater.inflate(R.layout.route, container, false);

	    return root;

	}
	
	public void addRouteClickListener(View view)
	{
		FileOutputStream fos = openFileOutput(SAVEDROUTESFILENAME, Context.MODE_PRIVATE);
		fos.write(string.getBytes());
		fos.close();
	}
}
