package com.cockpunchers.commutator2;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ChangePassword extends Activity {
String password,objectid,email;
Button sendemail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.changepassword);
		email=ParseUser.getCurrentUser().getString("email");
		
		
		
		
		
		Log.d("PASSWORD", email);
		sendemail=(Button)findViewById(R.id.updatepassword);
		sendemail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) 
            {
            	ParseUser.requestPasswordResetInBackground(email,
                        new RequestPasswordResetCallback() {
        public void done(ParseException e) {
        if (e == null) {
        Log.d("PASSWORD", "Email sent");
        Intent in =  new Intent(ChangePassword.this,ProfileSettings.class);
    	startActivity(in);
    	finish();
        Toast.makeText(ChangePassword.this, "Logout,check your emailid "+email+" and reset your password", Toast.LENGTH_LONG).show();
        
        } else {
        Toast.makeText(ChangePassword.this, "Something was not right. Please try again after some time", Toast.LENGTH_LONG).show();
        }
        }
        });

           
            }

        });
			}
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    Intent in =  new Intent(ChangePassword.this,ProfileSettings.class);
    	startActivity(in);
	    finish();

	}

}
