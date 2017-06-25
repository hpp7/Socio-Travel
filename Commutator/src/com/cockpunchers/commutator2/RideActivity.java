package com.cockpunchers.commutator2;

import com.cockpunchers.commutator2.adapter.TabsPagerAdapter;
import com.cockpunchers.commutator2.adapter.TabsPagerAdapter2;
import com.parse.ParseUser;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

public class RideActivity extends FragmentActivity implements
ActionBar.TabListener {
	String objectId;
	public static final String PREFS_MSG = "MyMsg";
	public static final String PREFS_NUM = "MyNum";

	
	String number;
	//For tabs
	private ViewPager viewPager;
	private TabsPagerAdapter2 mAdapter;
	private ActionBar actionBar;
	// Tab titles
	private String[] tabs = { "RIDE DETAILS", "RIDERS","RIDE CHAT" };
	//Tab shit ends	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
		super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);
	objectId = getIntent().getStringExtra("String");
	
	// Initialization for tabs
	viewPager=(ViewPager)findViewById(R.id.pager);
	actionBar = getActionBar();
	mAdapter = new TabsPagerAdapter2(getSupportFragmentManager());

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
		public void onPageSelected(int position) 
		{
			// on changing the page
			// make respected tab selected
			actionBar.setSelectedNavigationItem(position);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) 
		{
		}

		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}
	});
	}
	public String getMyData() {
		Log.d("R_F", "here only from taking");
        return objectId;
    }
	
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabSelected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		viewPager.setCurrentItem(arg0.getPosition());
	}

	@Override
	public void onTabUnselected(Tab arg0, FragmentTransaction arg1) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    // Inflate the menu items for use in the action bar
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_ride, menu);
	    return super.onCreateOptionsMenu(menu);
	}
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle presses on the action bar items
    switch (item.getItemId()) {
        
        case R.id.sos:
        sos();
        return true;
//        case R.id.action_settings:
//        settings();
//        return true;
//        case R.id.safety:
//        	safety();
//        	return true;
        default:
            return super.onOptionsItemSelected(item);
    }
}


//
//private void settings() {
//Intent in =  new Intent(RideActivity.this,Settings.class);
//startActivity(in);
//}

private void sos() {
SharedPreferences settings = getSharedPreferences(PREFS_NUM, 0);
number= settings.getString("Num", "No SOS number set");
if(number.equalsIgnoreCase("No SOS number set"))
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

//private void safety()
//{
//	Intent in =  new Intent(RideActivity.this,Safety.class);
//	startActivity(in);
//}

	@Override
	public void onBackPressed() {
	//  super.onBackPressed();
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);

	    builder.setMessage("Are you sure you want to exit?")
	            .setCancelable(false)
	            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id) {
	                    RideActivity.this.finish();
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
