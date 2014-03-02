package com.piyush.hacknc;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class AlarmSetter {
	
	public static void setNextAlarm(Context context)
	{
		Intent intent = new Intent(context, NotifyPebbleService.class);
		PendingIntent pintent = PendingIntent.getService(context, 0, intent, 0);
		AlarmManager alarm = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		alarm.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+Config.BROADCASTINTERVAL, pintent);
	}

}
