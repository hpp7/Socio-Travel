package com.cockpunchers.commutator2;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassword extends Activity 
{
	Button sendemail;
	EditText emailid;
	String password,objectid,email;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.resetpassword);
		sendemail=(Button)findViewById(R.id.sendmail);
		emailid=(EditText)findViewById(R.id.editText2);
		sendemail.setOnClickListener(new OnClickListener() 
		{
            @Override
            public void onClick(View v) 
            {
            	
                email=emailid.getText().toString();
                clearErrors();
            	boolean cancel = false;
        		View focusView = null;
        		
        		if (TextUtils.isEmpty(email)) 
        		{
        			emailid.setError(getString(R.string.error_field_required));
        			focusView = emailid;
        			cancel = true;
        		}
        		else
        		{
            	ParseUser.requestPasswordResetInBackground(email,
                        new RequestPasswordResetCallback() {
        public void done(ParseException e) {
        if (e == null) {
        Log.d("PASSWORD", "Email sent");
        Intent in =  new Intent(ForgotPassword.this,Login.class);
    	startActivity(in);
    	finish();
        
        
        } else {
        Toast.makeText(ForgotPassword.this, "Something was not right. Please try again after some time", Toast.LENGTH_LONG).show();
        }
        }
        });
            }
            }

        });
		}
			

		private void clearErrors()
		{
			emailid.setError(null);
			
		}
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    Intent in =  new Intent(ForgotPassword.this,Login.class);
    	startActivity(in);
	    finish();

	}
	/*public void onPause(){
	    super.onBackPressed();
	    Intent in =  new Intent(ForgotPassword.this,ProfileSettings.class);
    	startActivity(in);
	    finish();

	}*/
}
	
		