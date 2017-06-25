package com.cockpunchers.commutator2;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
	
	final public static String ONE_TIME = "onetime";
	String rId;

	SharedPreferences preferences;
	private CreateRideFragment alarm;
	String objectId2,objectId;
	int i=0,sj=0;
	static AlertDialog alertDialog;
	@Override
	public void onReceive(final Context context, Intent intent) {
		 PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
         PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "YOUR TAG");
         //Acquire the lock
         wl.acquire();

         Parse.initialize(context, "u3g5xX9gTG1DRbGVa6iMnpxNn2kVL20l2jI1tCar", "Vj5rdncQ6G7IEIgnbDrPUt2ZOtuH6WA1YFucLWmt");


         //You can do the processing here update the widget/remote views.
         Bundle extras = intent.getExtras();
         rId= extras.getString("RideID");
         StringBuilder msgStr = new StringBuilder();
         
         	 msgStr.append("Ride just expired");
         
                  Toast.makeText(context, msgStr, Toast.LENGTH_SHORT).show();
                  Log.d("Ride", "Ended");
                  ParsePush push = new ParsePush();
                  push.setChannel(rId);
                  push.setMessage("Your ride has ended and shall be deleted soon. ");
                  push.sendInBackground();
         
                  
                  ParseQuery<ParseObject> query = ParseQuery.getQuery("Rides_Created");
    				query.whereEqualTo("R_id", rId);
    				query.getFirstInBackground(new GetCallback<ParseObject>() {
    					  public void done(ParseObject object, ParseException e) {
    					    if (object != null) {
  		    					objectId=object.getObjectId().toString();

    					      Log.d("Deleting", objectId + " The getFirst request failed.");
    					      ParseObject.createWithoutData("Rides_Created", objectId).deleteEventually();
    					      RoomChat.ondelete();
    					    } else {
    					      Log.d("score", "Retrieved the object.");
    					    }
    					  }
    					});
                 
  				
  				ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Created_R");
  				query1.whereEqualTo("R_id", rId);
  				query1.getFirstInBackground(new GetCallback<ParseObject>() {
  					  public void done(ParseObject object, ParseException e) {
  					    if (object != null) {
		    					objectId2=object.getObjectId().toString();

  					      Log.d("Deleting", objectId2 + " The getFirst request failed.");
			    				ParseObject.createWithoutData("Created_R", objectId2).deleteEventually();
			    				PushService.unsubscribe(context, rId);

  					    } else {
  					      Log.d("score", "Retrieved the object.");
  					    }
  					  }
  					});
                  
  				preferences = context.getSharedPreferences("RidePreferences", Context.MODE_PRIVATE);
  		         Editor editor2 = preferences.edit();
  		 		editor2.putInt("iValue", i);
  		 		editor2.putInt("sjValue", i);
  		 		editor2.commit();
  				
  				//showAlertDialog(context, "Ride has expired",	"Please create navin", false);
  				
  			
         //Release the lock
         wl.release();
         
	}
	
		
	public void SetAlarm(Context context, Long a)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        //After after 30 seconds
        am.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), a , pi); 
    }

    public void CancelAlarm(Context context)
    {
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
    	Log.d("Alarm", "= canceled");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(sender);
    }
    
    public void setOnetimeTimer(Context context, Long a , String r_id)
    {
    	rId=r_id;
    	Log.d("Ride id", "="+rId);
    	AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.TRUE);
        intent.putExtra("RideID", rId);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + a+ 60000, pi);
    }
}
