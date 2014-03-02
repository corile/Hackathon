package com.piyush.hacknc;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ListView;
import com.piyush.hacknc.Route;

public class RouteListFragment extends ListFragment {
	
	ListView routeListView;
	ArrayList<Route> routeList;
	RouteAdapter routeAdapter;
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    routeList = new ArrayList<Route>();
	    routeList = getRouteList();
	    
	    RouteAdapter adapter = new RouteAdapter(getActivity(),R.layout.routelist, routeList);
	    setListAdapter(adapter);
	  }
	
//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//	    //View root = inflater.inflate(R.layout.route, container, false);
//
//	    //return root;
//		
//		View root = 
//		
//		routeListView = (ListView)findViewById(R.id.listView1);
//		
//		//Initialize our ArrayList
//        routeList = new ArrayList<Route>();
//        //Initialize our array adapter notice how it references the listitems.xml layout
//        routeAdapter = new RouteAdapter(main.this, R.layout.routelist,routeList);
//        
//        routeListView.setAdapter(routeAdapter);
//        
//        routeList = getRouteList();
//        
//        routeAdapter.notifyDataSetChanged();
//	}
	
	public ArrayList<Route> getRouteList()
	{
		ArrayList<Route> routeList = new ArrayList<Route>();
		try{
			FileInputStream fis = getActivity().openFileInput(Constants.SAVEDROUTESFILENAME);
			ObjectInputStream ois = new ObjectInputStream(fis);
			while(fis.available()>0)
			{
				Route route = (Route)ois.readObject();
				routeList.add(route);
			}
			fis.close();
			System.out.println(routeList);
		}catch(Exception e)
		{
			System.out.println("Exception occured while reading saved route lists");
		}
		return routeList;
	}

}
