package com.cockpunchers.commutator2;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;


public class ProfileSettings extends Activity
{
TextView viewProf,cDp,cPassword;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.profile_settings);
		viewProf=(TextView)findViewById(R.id.viewprofile);
		cPassword=(TextView)findViewById(R.id.changepass);
		cDp=(TextView)findViewById(R.id.changedp);
		cPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) 
            {
            	Intent in =  new Intent(ProfileSettings.this,ChangePassword.class);
            	startActivity(in);
            	finish();
            	
            	
            }

			
        });
		cDp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) 
            {
            	Intent in =  new Intent(ProfileSettings.this,Pic_select2.class);
            	startActivity(in);
            	finish();
            	
            }

			
        });
		viewProf.setOnClickListener(new OnClickListener() 
		{
            @Override
            public void onClick(View v) 
            {
            	Intent in =  new Intent(ProfileSettings.this,DP.class);
            	startActivity(in);
            	finish();
            	
            }

			
        });

	}
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
		finish();

	}
}
