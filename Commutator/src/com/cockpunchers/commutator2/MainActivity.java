package com.cockpunchers.commutator2;

import java.util.Calendar;

import com.cockpunchers.commutator2.adapter.TabsPagerAdapter;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;

import com.parse.Parse;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.PushService;

public class MainActivity extends FragmentActivity implements
ActionBar.TabListener {

//For tabs
private ViewPager viewPager;
private TabsPagerAdapter mAdapter;
private ActionBar actionBar;
// Tab titles
private String[] tabs = {"SEARCH", "CREATE", "HISTORY" };
//Tab shit ends

EditText time,time1;
//EditText mdate;

public static final String PREFS_NAME = "MyObj";
public static final String PREFS_NUM = "MyNum";
public static final String PREFS_MSG = "MyMsg";

public static Activity fa;
String number;

@Override
protected void onCreate(Bundle savedInstanceState) {
	this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
	super.onCreate(savedInstanceState);
setContentView(R.layout.activity_main);


Parse.initialize(this, "u3g5xX9gTG1DRbGVa6iMnpxNn2kVL20l2jI1tCar", "Vj5rdncQ6G7IEIgnbDrPUt2ZOtuH6WA1YFucLWmt");

PushService.setDefaultPushCallback(this, MainActivity.class);

fa=this;

// Initialization for tabs
viewPager=(ViewPager)findViewById(R.id.pager);
actionBar = getActionBar();
mAdapter = new TabsPagerAdapter(getSupportFragmentManager());

viewPager.setAdapter(mAdapter);
actionBar.setHomeButtonEnabled(false);
actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);		

// Adding Tabs
for (String tab_name : tabs) {
	actionBar.addTab(actionBar.newTab().setText(tab_name)
			.setTabListener(this));
}

/**
 * on swiping the viewpager make respective tab selected
 * */
viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

	@Override
	public void onPageSelected(int position) {
		// on changing the page
		// make respected tab selected
		actionBar.setSelectedNavigationItem(position);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}
});



}

/*
//date mava

public void selectDate(View view) {
DialogFragment newFragment = new SelectDateFragment();
newFragment.show(getSupportFragmentManager(), "DatePicker");
}

public void populateSetDate(int year, int month, int day) {
mdate = (EditText)findViewById(R.id.date);
mdate.setText(month+"/"+day+"/"+year);
}

public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
final Calendar calendar = Calendar.getInstance();

int yy = calendar.get(Calendar.YEAR);
int mm = calendar.get(Calendar.MONTH);
int dd = calendar.get(Calendar.DAY_OF_MONTH);
return new DatePickerDialog(getActivity(), this, yy, mm, dd);
}

public void onDateSet(DatePicker view, int yy, int mm, int dd) {
populateSetDate(yy, (mm+1), dd);
}
}
*/

//time mava for create
public void selectTime (View view) {
DialogFragment newFragment = new SelectTimeFragment();
newFragment.show(getSupportFragmentManager(), "TimePicker");
}
public void populateSetTime(int hr, int min) {
time = (EditText)findViewById(R.id.time);
time.setText(
        new StringBuilder()
        .append(pad(hr)).append(":")
        .append(pad(min)));
//dispToast();
}

/** Add padding to numbers less than ten */
private static String pad(int c) {
    if (c >= 10)
        return String.valueOf(c);
    else
        return "0" + String.valueOf(c);
}


private void dispToast() {
    Toast.makeText(this, new StringBuilder().append("Time choosen is ").append(time.getText()),   Toast.LENGTH_SHORT).show();
         
}

public class SelectTimeFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
@Override
public Dialog onCreateDialog(Bundle savedInstanceState) {
final Calendar calendar = Calendar.getInstance();

int hr = calendar.get(Calendar.HOUR_OF_DAY);
int min = calendar.get(Calendar.MINUTE);

return new TimePickerDialog(getActivity(), this, hr, min, false);
}
 
public void onTimeSet(TimePicker view, int hr, int min) {
populateSetTime(hr, min);
populateSetTime1(hr, min);
}
}

//time mava for search
public void selectTime1 (View view) {
DialogFragment newFragment = new SelectTimeFragment();
newFragment.show(getSupportFragmentManager(), "TimePicker");
}
public void populateSetTime1(int hr, int min) {
time1 = (EditText)findViewById(R.id.time1);
time1.setText(
      new StringBuilder()
      .append(pad(hr)).append(":")
      .append(pad(min)));

}



@Override
public void onTabReselected(Tab tab, FragmentTransaction ft) {
}

@Override
public void onTabSelected(Tab tab, FragmentTransaction ft) {
// on tab selected
// show respected fragment view
viewPager.setCurrentItem(tab.getPosition());
}

@Override
public void onTabUnselected(Tab tab, FragmentTransaction ft) {
}


@Override
public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu items for use in the action bar
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main, menu);
    return super.onCreateOptionsMenu(menu);
}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
        case R.id.logout:
            logout();
          return true;
        case R.id.sos:
        	sos();
        	return true;
        	
        case R.id.safety:
        	safety();
        	return true;
        
        case R.id.action_settings:
        	settings();
           return true; 
        default:
            return super.onOptionsItemSelected(item);
    }
}

private void logout() {
	// TODO Auto-generated method stub
	ParseUser.logOut();
    Intent in =  new Intent(MainActivity.this,Login.class);
	startActivity(in);
	super.onPause();
	finish();
	
		
}

private void settings() {
Intent in =  new Intent(MainActivity.this,Settings.class);
startActivity(in);
}

private void sos() {
SharedPreferences settings = getSharedPreferences(PREFS_NUM, 0);
number= settings.getString("Num", "No SOS number set");
if(number.equalsIgnoreCase("No SOS number set")||number==null)
	showAlertDialog(this, "No SOS number set",	"Please set an SOS number in settings first", false);
else
{

String messageToSend;
SharedPreferences settings1 = getSharedPreferences(PREFS_MSG, 0);
messageToSend= settings1.getString("Msg", "Help Me! I am in danger, track my phone");
Log.d("SOS",messageToSend);

SmsManager.getDefault().sendTextMessage(number, null, messageToSend, null,null);
Toast.makeText(this, "Message sent." ,   Toast.LENGTH_SHORT).show();

}
}

private void safety()
{
	Intent in =  new Intent(MainActivity.this,Safety.class);
	startActivity(in);
}

@Override
public void onBackPressed() {
//  super.onBackPressed();
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setMessage("Are you sure you want to exit?")
            .setCancelable(false)
            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    MainActivity.this.finish();
                    //dialog.dismiss();

                }
            })
            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.cancel();
                }
            });
    AlertDialog alert = builder.create();
    alert.show();

}

@SuppressWarnings("deprecation")
public void showAlertDialog(Context context, String title, String message,
		Boolean status) {
	AlertDialog alertDialog = new AlertDialog.Builder(context).create();

	// Setting Dialog Title
	alertDialog.setTitle(title);

	// Setting Dialog Message
	alertDialog.setMessage(message);

	// Setting alert dialog icon
	alertDialog.setIcon(R.drawable.fail);

	// Setting OK Button
	alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
		public void onClick(DialogInterface dialog, int which) {
		}
	});

	// Showing Alert Message
	alertDialog.show();
}

}

