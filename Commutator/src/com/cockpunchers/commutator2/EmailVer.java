package com.cockpunchers.commutator2;

import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class EmailVer extends Activity {
   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.emailver);
		
		Button button=(Button)findViewById(R.id.em_ver);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				    ParseUser.logOut();
					finish();
				    Intent in =  new Intent(EmailVer.this,Login.class);
					in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
					startActivity(in);
					
				
				
			    }
				
				
			
		});
	}

	
	

}
