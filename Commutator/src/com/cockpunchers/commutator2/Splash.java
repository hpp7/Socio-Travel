package com.cockpunchers.commutator2;

import java.util.HashMap;

import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class Splash extends Activity {

	@Override
	protected void onCreate(Bundle YoYoHoney) {
		// TODO Auto-generated method stub
		super.onCreate(YoYoHoney);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.splash);
		Thread timer = new Thread() {
			public void run() {
				try {
					sleep(1500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					Intent openMainActivity = new Intent(
							"com.cockpunchers.commutator2.Loginact");
					startActivity(openMainActivity);

				}
			}

		};
		timer.start();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
}
