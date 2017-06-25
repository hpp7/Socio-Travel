package com.cockpunchers.commutator2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SetSosMessage extends Activity{
	public static final String PREFS_MSG = "MyMsg";
TextView msg;
EditText updatesosmsg;
Button updater;
String message,sosmessage;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setsosmsg);
		updatesosmsg=(EditText)findViewById(R.id.updatedmsg);
		updater=(Button)findViewById(R.id.setsosmessage);
		msg=(TextView)findViewById(R.id.setsostext);
		SharedPreferences settings = getSharedPreferences(PREFS_MSG, 0);
		   message = settings.getString("Msg", "Help Me! I am in danger, track my phone");
		   msg.setText(message);
		   
		updater.setOnClickListener(new OnClickListener() {
		            @Override
		            public void onClick(View v) 
		            {
		            	clearErrors();
		            	boolean cancel = false;
		        		View focusView = null;
		        		sosmessage=updatesosmsg.getText().toString();
		        		if (TextUtils.isEmpty(sosmessage)) {
		        			updatesosmsg.setError(getString(R.string.error_field_required));
		        			focusView = updatesosmsg;
		        			cancel = true;
		        		}
		        		else
		        		{
		        SharedPreferences settings = getSharedPreferences(PREFS_MSG, 0);
		           SharedPreferences.Editor editor = settings.edit();
		           editor.putString("Msg", sosmessage);
		        editor.commit();
		        Intent intent = getIntent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		finish();
		   startActivity(intent);
		        		}
		        		
		            }

		        });
		}
	
	private void clearErrors(){
		updatesosmsg.setError(null);
		
	}
		@Override
		public void onBackPressed() {
		   super.onBackPressed();
		finish();

		}


}
