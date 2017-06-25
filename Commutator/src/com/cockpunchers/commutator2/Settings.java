package com.cockpunchers.commutator2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends Activity{
TextView sos,profile;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.action_settings);
		sos=(TextView)findViewById(R.id.sosset);
		profile=(TextView)findViewById(R.id.profile);
		sos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) 
            {
            	Intent in =  new Intent(Settings.this,SosSettings.class);
            	startActivity(in);
            	Log.d("SOS", "SOS works");
            	//Toast.makeText(this, "You pressed SOS mava" , Toast.LENGTH_SHORT).show();
            }

			
        });
		profile.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) 
            {
            	Intent in =  new Intent(Settings.this,ProfileSettings.class);
            	startActivity(in);
            	Log.d("Profile", "Profile works");
            }

			
        });

	}
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    Intent in =  new Intent(Settings.this,MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	startActivity(in);
	    finish();

	}

}
