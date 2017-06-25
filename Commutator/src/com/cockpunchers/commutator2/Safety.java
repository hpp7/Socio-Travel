package com.cockpunchers.commutator2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class Safety extends Activity{
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.safety_tips);
	}
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    Intent in =  new Intent(Safety.this,MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	startActivity(in);
    finish();

	}
	
}
