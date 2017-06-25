package com.cockpunchers.commutator2;

import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.quickblox.core.QBCallback;
import com.quickblox.core.QBSettings;
import com.quickblox.core.result.Result;
import com.quickblox.module.auth.QBAuth;
import com.quickblox.module.chat.QBChatService;
import com.quickblox.module.chat.listeners.SessionListener;
import com.quickblox.module.chat.smack.SmackAndroid;
import com.quickblox.module.users.QBUsers;
import com.quickblox.module.users.model.QBUser;

public class Login extends Activity implements QBCallback {
	Button btn_LoginIn = null;
	Button btn_SignUp = null;
	TextView btn_ForgetPass = null;

	private EditText mUserNameEditText;
	private EditText mPasswordEditText;
	private static final String APP_ID = "8372";
	private static final String AUTH_KEY = "KpwEEwu75SytF98";
	private static final String AUTH_SECRET = "Qj2TujvnDf7Czps";
	private String mUsername;
	private static final String TAG = Login.class.getSimpleName();
	private int loginCount = 1;

	private int mActionBarTitleColor;
	private int mActionBarHeight;
	private int mHeaderHeight;
	private int mMinHeaderTranslation;
	private ListView mListView;
	private KenBurnsView mHeaderPicture;
	private ImageView mHeaderLogo;
	private View mHeader;
	private View mPlaceHolderView;
	private AccelerateDecelerateInterpolator mSmoothInterpolator;
	private EditText messageEditText;

	private ListView messagesContainer;
	private RectF mRect1 = new RectF();
	private RectF mRect2 = new RectF();

	private AlphaForegroundColorSpan mAlphaForegroundColorSpan;
	private SpannableString mSpannableString;

	private TypedValue mTypedValue = new TypedValue();
	private TextView meLabel;
	private TextView companionLabel;
	private ScrollView container;
	private Button sendButton;

	
	private QBUser user;
	private SmackAndroid smackAndroid;
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	ProgressDialog pd;

	boolean isParseIn, isBloxin;

	public void bloxAfterParseLogin(final String uName) {
		user = new QBUser(uName, "qwerty1234567890");
		QBUsers.signIn(user, new QBCallback() {

			@Override
			public void onComplete(Result arg0, Object arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onComplete(Result arg0) {
				// TODO Auto-generated method stub
				Log.e("Login", "Apna login");
				if (arg0.isSuccess()) {
					((App) getApplication()).setQbUser(user);
					QBChatService.getInstance().loginWithUser(user,
							new SessionListener() {

								@Override
								public void onLoginSuccess() {
									// TODO Auto-generated method stub
									Log.e("Login.java",
											"Login successful bunty!");
									isBloxin = true;
									if (isBloxin && isParseIn) {
										pd.dismiss();
										Intent in = new Intent("com.cockpunchers.commutator2.MainActivity");
										in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
										startActivity(in);
										
									}
								}

								@Override
								public void onLoginError() {
									// TODO Auto-generated method stub

								}

								@Override
								public void onDisconnectOnError(Exception arg0) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onDisconnect() {
									// TODO Auto-generated method stub

								}
							});
				} else {
					if (loginCount >= 4) {
						pd.dismiss();
						Toast.makeText(getBaseContext(),
								"Incorrect credentials. Please try again.",
								Toast.LENGTH_LONG).show();
						return;
					} else
						loginCount++;
					new Handler().postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							bloxAfterParseLogin(uName);
						}
					}, 2000);
				}
			}
		});

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		smackAndroid = SmackAndroid.init(this);
		// Initializing Parse SDK
		onCreateParse();
		onCreateQuick();
		// Calling ParseAnalytics to see Analytics of our app
		ParseAnalytics.trackAppOpened(getIntent());

		// creating connection detector class instance
		cd = new ConnectionDetector(getApplicationContext());
		mSmoothInterpolator = new AccelerateDecelerateInterpolator();
		mHeaderHeight = getResources().getDimensionPixelSize(
				R.dimen.header_height);
		mMinHeaderTranslation = -mHeaderHeight + getActionBarHeight();
		mHeader = findViewById(R.id.header);
		mHeaderPicture = (KenBurnsView) findViewById(R.id.header_picture);
		mHeaderPicture.setResourceIds(R.drawable.mpic3, R.drawable.mpic4,
				R.drawable.mpic5, R.drawable.mpic6);
		mHeaderLogo = (ImageView) findViewById(R.id.header_logo);

		// messageEditText = (EditText) findViewById(R.id.messageEdit);

		mActionBarTitleColor = getResources().getColor(
				R.color.actionbar_title_color);

		// mSpannableString = new SpannableString(
		// getString(R.string.));
		mAlphaForegroundColorSpan = new AlphaForegroundColorSpan(
				mActionBarTitleColor);

		setupActionBar();

		btn_LoginIn = (Button) findViewById(R.id.btn_login);
		btn_SignUp = (Button) findViewById(R.id.btn_signup);

		mUserNameEditText = (EditText) findViewById(R.id.username);
		mPasswordEditText = (EditText) findViewById(R.id.password);
		btn_ForgetPass = (TextView) findViewById(R.id.btn_forgtpassword);

		// login("asdf", "1234");

		btn_LoginIn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					// Internet Connection is Present
					// make HTTP requests
					pd = new ProgressDialog(Login.this);
					pd.setMessage("Logging in.. This may take a while, so please be patient :)");
					pd.setCancelable(false);
					pd.show();
					attemptLogin();

				} else {
					// Internet connection is not present
					// Ask user to connect to Internet
					showAlertDialog(Login.this, "No Internet Connection",
							"You don't have internet connection.", false);
				}

			}
		});

		btn_SignUp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(Login.this, SignUp.class);
				startActivity(in);
			}
		});

		btn_ForgetPass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent in = new Intent(Login.this, ForgotPassword.class);
				startActivity(in);
			}
		});

		ParseUser curuser = ParseUser.getCurrentUser();
		if (curuser != null) {
			pd = new ProgressDialog(Login.this);
			pd.setMessage("Logging in.. This may take a while, so please be patient :)");
			pd.setCancelable(false);
			pd.show();
			// Toast.makeText(getApplicationContext(),
			// "Logging in..",Toast.LENGTH_SHORT).show();
			loginSuccessful();
			bloxAfterParseLogin(curuser.getString("username"));

		}

	}

	private void onCreateQuick() {
		QBSettings.getInstance().fastConfigInit(APP_ID, AUTH_KEY, AUTH_SECRET);
		QBAuth.createSession(this);
		// quickblox_login(mUsername);

	}

	public void onCreateParse() {
		Parse.initialize(this, "u3g5xX9gTG1DRbGVa6iMnpxNn2kVL20l2jI1tCar",
				"Vj5rdncQ6G7IEIgnbDrPUt2ZOtuH6WA1YFucLWmt");

	}

	public void attemptLogin() {

		clearErrors();

		// Store values at the time of the login attempt.
		String username = mUserNameEditText.getText().toString();
		mUsername = mUserNameEditText.getText().toString();
		String password = mPasswordEditText.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(password)) {
			pd.dismiss();

			mPasswordEditText
					.setError(getString(R.string.error_field_required));
			focusView = mPasswordEditText;
			cancel = true;
		} else if (password.length() < 4) {
			pd.dismiss();

			mPasswordEditText
					.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordEditText;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(username)) {
			pd.dismiss();

			mUserNameEditText
					.setError(getString(R.string.error_field_required));
			focusView = mUserNameEditText;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// perform the user login attempt.
			login(username.toLowerCase(Locale.getDefault()), password);
			bloxAfterParseLogin(username);
			// quickblox_login(mUsername);
		}
	}

	private void quickblox_login(String username) {

		user = new QBUser("asdf", "qwerty1234567890");

		// progressDialog.show();
		QBUsers.signIn(user, this);

	}

	private void login(String lowerCase, String password) {
		// TODO Auto-generated method stub
		ParseUser.logInInBackground(lowerCase, password, new LogInCallback() {
			@Override
			public void done(ParseUser user, ParseException e) {
				// TODO Auto-generated method stub
				if (e == null)
					loginSuccessful();
				else
					loginUnSuccessful();
			}
		});

	}

	protected void loginSuccessful() {
		// TODO Auto-generated method stub
		Boolean ev = ParseUser.getCurrentUser().getBoolean("emailVerified");
		if (!ev) {
			pd.dismiss();
			//Toast.makeText(getApplicationContext(), "Logging In...",
					//Toast.LENGTH_SHORT).show();
			Intent in = new Intent(Login.this, EmailVer.class);
			startActivity(in);

		} else {

			// QBChatService.getInstance().logout();
			// quickblox_login(mUsername);
			isParseIn = true;
			Log.e("Login", "Parse Login ho gaya!");
			if (isBloxin && isParseIn) {
				pd.dismiss();
				Intent in = new Intent("com.cockpunchers.commutator2.MainActivity");
				in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(in);
			}
		}
	}

	protected void loginUnSuccessful() {
		// TODO Auto-generated method stub
		pd.dismiss();
		Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
		showAlertDialog(Login.this, "Login",
				"Username or Password is invalid.", false);
	}

	private void clearErrors() {
		mUserNameEditText.setError(null);
		mPasswordEditText.setError(null);
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
		// finish();
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent();
		setResult(RESULT_CANCELED, intent);
		// finish();
	}

	@Override
	public void onComplete(Result result) {
		if (result.isSuccess()) {
			((App) getApplication()).setQbUser(user);
			QBChatService.getInstance().loginWithUser(user,
					new SessionListener() {
						@Override
						public void onLoginSuccess() {
							// if (progressDialog != null) {
							// progressDialog.dismiss();
							// }
							Log.i(TAG, "success when login");

							// Intent intent = new Intent();
							// setResult(RESULT_OK, intent);
							// finish();
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
			Toast.makeText(getApplicationContext(), "Please check your internet connection!", Toast.LENGTH_SHORT).show();

		}
	}

	@Override
	public void onComplete(Result arg0, Object arg1) {
		// TODO Auto-generated method stub

	}
	
	private void setTitleAlpha(float alpha) {
		mAlphaForegroundColorSpan.setAlpha(alpha);
		mSpannableString.setSpan(mAlphaForegroundColorSpan, 0,
				mSpannableString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		getActionBar().setTitle(mSpannableString);
	}

	public static float clamp(float value, float max, float min) {
		return Math.max(Math.min(value, min), max);
	}

	private void interpolate(View view1, View view2, float interpolation) {
		getOnScreenRect(mRect1, view1);
		getOnScreenRect(mRect2, view2);

		float scaleX = 1.0F + interpolation
				* (mRect2.width() / mRect1.width() - 1.0F);
		float scaleY = 1.0F + interpolation
				* (mRect2.height() / mRect1.height() - 1.0F);
		float translationX = 0.5F * (interpolation * (mRect2.left
				+ mRect2.right - mRect1.left - mRect1.right));
		float translationY = 0.5F * (interpolation * (mRect2.top
				+ mRect2.bottom - mRect1.top - mRect1.bottom));

		view1.setTranslationX(translationX);
		view1.setTranslationY(translationY - mHeader.getTranslationY());
		view1.setScaleX(scaleX);
		view1.setScaleY(scaleY);
	}

	

	private void scrollDown() {
		messagesContainer.setSelection(messagesContainer.getCount() - 1);
	}

	private RectF getOnScreenRect(RectF rect, View view) {
		rect.set(view.getLeft(), view.getTop(), view.getRight(),
				view.getBottom());
		return rect;
	}

	public int getScrollY() {
		View c = mListView.getChildAt(0);
		if (c == null) {
			return 0;
		}

		int firstVisiblePosition = mListView.getFirstVisiblePosition();
		int top = c.getTop();

		int headerHeight = 0;
		if (firstVisiblePosition >= 1) {
			headerHeight = mPlaceHolderView.getHeight();
		}

		return -top + firstVisiblePosition * c.getHeight() + headerHeight;
	}

	private void setupActionBar() {
		ActionBar actionBar = getActionBar();
		actionBar.setIcon(R.drawable.ic_transparent);

		// getActionBarTitleView().setAlpha(0f);
	}

	private ImageView getActionBarIconView() {
		return (ImageView) findViewById(android.R.id.home);
	}

	/*
	 * private TextView getActionBarTitleView() { int id =
	 * Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
	 * return (TextView) findViewById(id); }
	 */

	public int getActionBarHeight() {
		if (mActionBarHeight != 0) {
			return mActionBarHeight;
		}
		getTheme().resolveAttribute(android.R.attr.actionBarSize, mTypedValue,
				true);
		mActionBarHeight = TypedValue.complexToDimensionPixelSize(
				mTypedValue.data, getResources().getDisplayMetrics());
		return mActionBarHeight;
	}

}
