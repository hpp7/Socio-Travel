package com.cockpunchers.commutator2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class SosSettings extends Activity {
	TextView setno,setmsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sossettings);
		setno=(TextView)findViewById(R.id.setsosno);
		setmsg=(TextView)findViewById(R.id.sossetmsg);
		setno.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) 
            {
            	Intent in =  new Intent(SosSettings.this,SOS.class);
            	startActivity(in);
            	Log.d("SOS", "SOS works");
            	//Toast.makeText(this, "You pressed SOS mava" , Toast.LENGTH_SHORT).show();
            }

			
        });
		setmsg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) 
            {
            	Intent in =  new Intent(SosSettings.this,SetSosMessage.class);
            	startActivity(in);
            	Log.d("Profile", "Profile works");
            }

			
        });

	}
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
		finish();

	}

}
