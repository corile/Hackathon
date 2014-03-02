package com.piyush.hacknc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Locale;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;
	Button addRouteButton;
	FileOutputStream fos;
	ObjectOutputStream oos;
	
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = null;
			if (position==0)
			{
				fragment = new RouteFragment();
			}
			else if(position==1)
			{
				try{
				fragment = new RouteListFragment();
				}catch(Exception e)
				{
					System.out.println("Error while creating second tab");
					e.printStackTrace();
				}
			}
			else
				fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.title_section1).toUpperCase(l);
			case 1:
				return getString(R.string.title_section2).toUpperCase(l);
			case 2:
				return getString(R.string.title_section3).toUpperCase(l);
			}
			return null;
		}
	}
	
	public void addRouteClickListener(View view) throws FileNotFoundException, IOException
	{
		try{Route route = new Route();
		EditText editText = (EditText)findViewById(R.id.editText1);
		route.source = editText.getText().toString();
		editText = (EditText)findViewById(R.id.editText2);
		route.destination = editText.getText().toString();
		editText = (EditText)findViewById(R.id.editText3);
		route.time = editText.getText().toString();
		
		ArrayList<Route> routeList = FileUtility.getRouteList(this);
		routeList.add(route);
		
		//Need to read and write the whole file again due to some unknow reason
		//http://stackoverflow.com/questions/9542799/problems-with-i-o-with-android-mostly-objectinputstream
		writeRouteArrayToMemory(Constants.SAVEDROUTESFILENAME, routeList);
		System.out.println(route);}catch(Exception e){
			System.out.println("HOly crap!Something is wrong!");
			e.printStackTrace();
		}
		
		finish();
		startActivity(getIntent());
		refreshRouteListFragment();
	}

	private void refreshRouteListFragment()
	{
//		FragmentManager manager = getSupportFragmentManager();
//
//        if (manager != null)
//        {
//            RouteListFragment routeListFrag = (RouteListFragment)manager.findFragmentById(R.id.fragmentItem);
//
//            routeListFrag.onResume();
//        }   
        mSectionsPagerAdapter.getItem(1).onResume();
	}
	
    public void writeRouteArrayToMemory(String filename, ArrayList<Route> routeArray) {
        
        try {
        	
        	fos = openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            for (Route route : routeArray) {
				oos.writeObject(route);
			}
            oos.close();
        } 
        catch (Exception e){
        	System.out.println("Exception while saving route to file."+e);
        }

    }
	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			TextView dummyTextView = (TextView) rootView
					.findViewById(R.id.section_label);
			dummyTextView.setText(Integer.toString(getArguments().getInt(
					ARG_SECTION_NUMBER)));

			return rootView;
		}
	}

}
