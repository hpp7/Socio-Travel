package com.cockpunchers.commutator2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class Loginact extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginact);
		Intent in = new Intent("com.cockpunchers.commutator2.Login");
		in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(in);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}

}
