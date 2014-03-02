package com.piyush.hacknc;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ListView;

public class RouteListFragment extends ListFragment {
	
	ListView routeListView;
	ArrayList<Route> routeList;
	RouteAdapter routeAdapter;
	
	@Override
	  public void onActivityCreated(Bundle savedInstanceState) {
	    super.onActivityCreated(savedInstanceState);
	    
	    routeList = new ArrayList<Route>();
	    routeList = FileUtility.getRouteList(getActivity());
	    
	    routeAdapter = new RouteAdapter(getActivity(),R.layout.routelist, routeList);
	    setListAdapter(routeAdapter);
	  }
	
	@Override
	public void onResume()
	{
		super.onResume();
		if(routeList!=null)
		{
			routeList.clear();
			routeList.addAll(FileUtility.getRouteList(getActivity()));
			for(Route item : routeList ){
				System.out.println(item);
			}
			routeAdapter.notifyDataSetChanged();
		}
		
		System.out.println("Another Muhahaha");
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


}
