package rohit.pebbledatademo;

import java.util.UUID;
import java.util.ArrayList;

import com.piyush.hacknc.NextBusTime;

import android.R;
//import android.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.util.PebbleDictionary;

public class MainActivity extends Activity {
	
	//Keys - identical to watch app keys
	private static final int DATA_KEY = 0;
	private static final int SELECT_BUTTON_KEY = 0;
	private static final int UP_BUTTON_KEY = 1;
	private static final int DOWN_BUTTON_KEY = 2;
	
	private static final int BUFFER_LENGTH = 32;
	private static String str = "Hk!";
	
	//Use the same UUID as on the watch
	private static final UUID APP_UUID = UUID.fromString("68c0e154-bf99-419b-8b6b-8777a8b65991");
	
	private PebbleKit.PebbleDataReceiver dataHandler;
	private TextView statusLabel;

	ArrayList<NextBusTime> nextBusTimes;
	
	private static ArrayList<String> BusName;
	private static ArrayList<Integer> BusTime;
	private static ArrayList<String> BusStop;
	
	public static void process(ArrayList<NextBusTime> busData)
	{	
		System.out.println("Hello" + busData.size());
		for(int i=0;i< busData.size();i++)
		{
			BusName.add(busData.get(i).getBusName());
			BusTime.add(busData.get(i).getEta());
			BusStop.add(busData.get(i).getStopName());
			System.out.println(BusName.get(i));
			System.out.println(BusTime.get(i));
			System.out.println(BusStop.get(i));				
		}
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.activity_list_item); 
	
		//Allocate the TextView used to show which button press was received
		statusLabel = (TextView) findViewById(R.id.button1);
		sendStringToPebble(str) ;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		//Start the companion app on the watch
		PebbleKit.startAppOnPebble(getApplicationContext(), APP_UUID);
		
		//Create a DataReciever
		dataHandler = new PebbleKit.PebbleDataReceiver(APP_UUID) {
			@Override
			public void receiveData(final Context context, final int transactionId, final PebbleDictionary data) {         
				//ACK to prevent timeouts
				PebbleKit.sendAckToPebble(context, transactionId);

				//Get which button was pressed
				int buttonPressed = data.getUnsignedInteger(DATA_KEY).intValue();
				
				//Update UI
				switch(buttonPressed) {
					case SELECT_BUTTON_KEY: {
						statusLabel.setText("Select button pressed");
						sendStringToPebble("Phone says 'Select pressed!'");
						break;
					}
					case UP_BUTTON_KEY: {
						statusLabel.setText("Up button pressed");
						sendStringToPebble("Phone says 'Up pressed!'");
						break;
					}
					case DOWN_BUTTON_KEY: {
						statusLabel.setText("Down button pressed");
						sendStringToPebble("Phone says 'Down pressed!'");
						break;
					}
					default: {
						statusLabel.setText("Unknown button!");
						break;
					}
				}
			}
		};
		
		//Register the DataHandler with Android to receive any messages from the watch
		PebbleKit.registerReceivedDataHandler(getApplicationContext(), dataHandler);
	}

	@Override
	public void onPause() {
		super.onPause();
		
		// Always deregister any Activity-scoped BroadcastReceivers when the Activity is paused
		if (dataHandler != null) {
			unregisterReceiver(dataHandler);
			dataHandler = null;
		}
	}
	
	/**
	 * Send a string to the companion Pebble Watch App
	 * @param message	The string to send
	 */
	private void sendStringToPebble(String message) {
		if(message.length() < BUFFER_LENGTH) {

			
			//Create a PebbleDictionary
			PebbleDictionary dictionary = new PebbleDictionary();	
			message = Integer.toString(message.length());
			message = "hello stringsssss..";
			message = "\0\0\0\0CPX\0SN";	
			//Store a string in the dictionary using the correct key
			dictionary.addString(DATA_KEY, message);	
			
			//Send the Dictionary
			PebbleKit.sendDataToPebble(getApplicationContext(), APP_UUID, dictionary);	
			
		} else {
			Log.i("sendStringToPebble()", "String too long!");
		}
	}

}
