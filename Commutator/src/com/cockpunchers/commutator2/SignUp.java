package com.cockpunchers.commutator2;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;
import com.quickblox.core.QBCallback;
import com.quickblox.core.result.Result;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.listeners.SessionListener;
import com.quickblox.module.chat.smack.SmackAndroid;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

public class SignUp extends Activity implements OnClickListener, QBCallback {
	private EditText mUserNameEditText;
	private EditText mNameEditText;
	private EditText mEmailEditText;
	private EditText mPasswordEditText;
	private EditText mAgeEditText;
	private EditText mConfirmPasswordEditText;
	private Spinner mGenderSpinner;
	private Button mCreateAccountButton;
	private ImageView mImageView;
	private String mEmail;
	private String mname;
	private String mUsername;
	private String mPassword;
	private String mGender;
	private String mAge;
	private String mConfirmPassword;
	static Bitmap bitmap1;
	OutputStream output;
	private QBUser user;
	private static final String TAG = Login.class.getSimpleName();
	private SmackAndroid smackAndroid;
	private ProgressDialog progressDialog;    
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	private byte[] image;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);

		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());
		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Loading");
		mNameEditText = (EditText) findViewById(R.id.etname);
		mUserNameEditText = (EditText) findViewById(R.id.etUsername);
		mEmailEditText = (EditText) findViewById(R.id.etEmail);
		mPasswordEditText = (EditText) findViewById(R.id.etPassword);
		mConfirmPasswordEditText = (EditText) findViewById(R.id.etPasswordConfirm);
		mGenderSpinner = (Spinner) findViewById(R.id.male_fem);
		mAgeEditText = (EditText) findViewById(R.id.etage);
		mCreateAccountButton = (Button) findViewById(R.id.btnCreateAccount);
		mImageView = (ImageView) findViewById(R.id.ProfilePicIV);
		smackAndroid = SmackAndroid.init(this);

        bitmap1=pic_select.bitmap;
		if(bitmap1!=null)
			{
			mImageView.setImageBitmap(pic_select.bitmap);
			}

        
		

		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent in = new Intent(SignUp.this, pic_select.class);
				startActivity(in);

			}
		});
		
		mCreateAccountButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					// make HTTP requests
					createAccount();


				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					showAlertDialog(SignUp.this, "No Internet Connection",
							"You don't have internet connection.", false);
				}

			}
		});
	}

//	@Override
//	public void onClick(View v) {
//		// TODO Auto-generated method stub
//		switch (v.getId()) {
//		case R.id.btnCreateAccount:
//			
//			
//			
//			
//			
//			// get Internet status
//			isInternetPresent = cd.isConnectingToInternet();
//			// check for Internet status
//			if (isInternetPresent) {
//				// Internet Connection is Present
//				// make HTTP requests
//				createAccount();
//			} else {
//				// Internet connection is not present
//				// Ask user to connect to Internet
//				showAlertDialog(SignUp.this,"No Internet Connection","You don't have internet connection.", false);
//			}
//
//			break;
//
//		default:
//			break;
//		}
//	}

	private void createAccount() {
		clearErrors();

		boolean cancel = false;
		View focusView = null;

		// Store values at the time of the login attempt.
		mEmail = mEmailEditText.getText().toString();
		mname = mNameEditText.getText().toString();
		mUsername = mUserNameEditText.getText().toString();
		mPassword = mPasswordEditText.getText().toString();
		mConfirmPassword = mConfirmPasswordEditText.getText().toString();
		mGender = mGenderSpinner.getSelectedItem().toString();
		mAge=mAgeEditText.getText().toString();

		
        
		if(bitmap1!=null)
		{// Convert it to byte
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        // Compress image to lower quality scale 1 - 100
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
        image = stream1.toByteArray();
		}
		if(bitmap1==null)
		{
			bitmap1=BitmapFactory.decodeResource(getResources(), R.drawable.default_medium_profile_pic);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
	        // Compress image to lower quality scale 1 - 100
	        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream);
	        image = stream.toByteArray();
		}
        
		// Check for a valid confirm password.
		if (TextUtils.isEmpty(mConfirmPassword)) {
			mConfirmPasswordEditText
					.setError(getString(R.string.error_field_required));
			focusView = mConfirmPasswordEditText;
			cancel = true;
		} else if (mPassword != null && !mConfirmPassword.equals(mPassword)) {
			mPasswordEditText
					.setError(getString(R.string.error_invalid_confirm_password));
			focusView = mPasswordEditText;
			cancel = true;
		}
		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordEditText
					.setError(getString(R.string.error_field_required));
			focusView = mPasswordEditText;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordEditText
					.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordEditText;
			cancel = true;
		}

		// Check for a valid username and name
		if (TextUtils.isEmpty(mname)) {
			mNameEditText.setError(getString(R.string.error_field_required));
			focusView = mNameEditText;
			cancel = true;
		}

		if (TextUtils.isEmpty(mUsername)) {
			mUserNameEditText
					.setError(getString(R.string.error_field_required));
			focusView = mUserNameEditText;
			cancel = true;
		}
		
		if (TextUtils.isEmpty(mAge)) {
			mAgeEditText
					.setError(getString(R.string.error_field_required));
			focusView = mAgeEditText;
			cancel = true;
		}
		else if(Integer.valueOf(mAge)<13) {
			mAgeEditText.setError(getString(R.string.error_invalid_age));
			focusView =mAgeEditText;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailEditText.setError(getString(R.string.error_field_required));
			focusView = mEmailEditText;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailEditText.setError(getString(R.string.error_invalid_email));
			focusView = mEmailEditText;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			Toast.makeText(getApplicationContext(), "Signing Up..",
					Toast.LENGTH_SHORT).show();
			signUp(mname, mUsername.toLowerCase(Locale.getDefault()), mEmail,
					mPassword, mGender, mAge, image);
			reg();

		}

	}

	private void reg() {
		// TODO Auto-generated method stub
		

		user = new QBUser(mUsername, "qwerty1234567890");

		progressDialog.show();
		QBUsers.signUpSignInTask(user, this);
	}

	private void signUp(String mname, final String mUsername, String mEmail,
			String mPassword, String mGender, String mAge, byte[] image) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), mname + " - " + mEmail,
				Toast.LENGTH_SHORT).show();
		// Create the ParseFile
        //ParseFile file = new ParseFile("profile.png", image);
          // Upload the image into Parse Cloud
       // file.saveInBackground();

		//image=getIntent().getByteArrayExtra("Image");
		
		ParseFile file = new ParseFile("profile.png", image);
		file.saveInBackground();
		ParseObject profile = new ParseObject("Profile");

		profile.put("Name", mname);
		profile.put("Username", mUsername);
		profile.put("Email", mEmail);
		profile.put("Gender", mGender);
		profile.put("Age", mAge);
		profile.put("Profile_Pic", file);
		profile.saveInBackground();
		
		
		ParseUser user = new ParseUser();
		user.put("Name", mname);
		user.setUsername(mUsername);
		user.setPassword(mPassword);
		user.setEmail(mEmail);
		user.put("Gender", mGender);
		user.put("Age", mAge);
		user.signUpInBackground(new SignUpCallback() {
			public void done(ParseException e) {
				if (e == null) {
					signUpMsg("Account Created Successfully");
					Intent in = new Intent(getApplicationContext(),
							EmailVer.class);
					startActivity(in);
				} else {
					// Sign up didn't succeed. Look at the ParseException
					// to figure out what went wrong
					signUpMsg("Account already taken.");
				}
			}
		});
		
		/*
	*/}

	protected void signUpMsg(String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
	}

	private void clearErrors() {
		mNameEditText.setError(null);
		mEmailEditText.setError(null);
		mUserNameEditText.setError(null);
		mPasswordEditText.setError(null);
		mConfirmPasswordEditText.setError(null);
		mAgeEditText.setError(null);
	}

	@SuppressWarnings("deprecation")
	public void showAlertDialog(Context context, String title, String message,
			Boolean status) {
		AlertDialog alertDialog = new AlertDialog.Builder(context).create();

		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting alert dialog icon
		alertDialog.setIcon(R.drawable.fail);

		// Setting OK Button
		alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	@Override
	protected void onDestroy() {
	//	
		super.onDestroy();
		//smackAndroid.onDestroy();
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	    Intent in =  new Intent(SignUp.this,Login.class);
		in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(in);
	}

	@Override
	public void onComplete(Result result) {
		if (result.isSuccess()) {
			((App) getApplication()).setQbUser(user);
			QBChatService.getInstance().loginWithUser(user,
					new SessionListener() {
						@Override
						public void onLoginSuccess() {
							Log.i(TAG, "success when login");
							//Intent i = new Intent();
							//setResult(RESULT_OK, i);
							// Intent intent = new Intent(getApplicationContext(),RideChatFragment.class);
					         //   startActivity(intent);
					            
							
							
							finish();
						}

						@Override
						public void onLoginError() {
							Log.i(TAG, "error when login");
						}

						@Override
						public void onDisconnect() {
							Log.i(TAG, "disconnect when login");

						}

						@Override
						public void onDisconnectOnError(Exception exc) {
							Log.i(TAG, "disconnect error when login");
						}
					});
		} else {
			AlertDialog.Builder dialog = new AlertDialog.Builder(this);
			dialog.setMessage(
					"Error(s) occurred. Look into DDMS log for details, "
							+ "please. Errors: " + result.getErrors()).create()
					.show();
		}
	}



	@Override
	public void onComplete(Result arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}
}
