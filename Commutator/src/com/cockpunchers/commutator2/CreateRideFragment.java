package com.cockpunchers.commutator2;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cockpunchers.commutator2.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;
import com.parse.SaveCallback;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class CreateRideFragment extends Fragment {

	ArrayAdapter<String> adapter;
	ArrayAdapter<String> adapter2;
	AutoCompleteTextView textView;
	AutoCompleteTextView textView2;
	GetPlaces places;
	GetPlaces places2;
	AutoCompleteTextView mEdittext;
	AutoCompleteTextView mEdittext2;

	SharedPreferences settings;

	SimpleDateFormat sdf;

	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;

	ProgressDialog pd;

	long a;

	Button submit, del;
	AutoCompleteTextView source;
	AutoCompleteTextView destination;
	CheckBox checkBox;
	EditText no, time;
	EditText timet;
	String s, d, veh, tim;
	Spinner spinner;
	String sp, n, t;
	String filename = "created_or_not";
	String file = "joined_or_not";
	String r_id;

	int i = 0, sj = 0;
	String fem_check;
	Boolean femride = false;
	char h1, h2, m1, m2;
	String hh, mm;
	int ogh, ogm, sh, sm, eh, em, ott, noadd, noadd2;
	int temp;
	
	ParseGeoPoint geosource,geodestination,geoaverage;
	double lat_source,long_source,lat_destination,long_destination;

	String start_time, end_time;
	long timedif;

	String objectId, objectId2;
	String rc_s, rc_d, rc_add, rc_type, rc_tim, rc_tt;

	public static final String PREFS_NAME = "MyObj";

	DataBaseAdapter dataBaseAdapter; // db mava

	private AlarmManagerBroadcastReceiver alarm;
	RelativeLayout rl1, rl2;

	private static final int NOTIFICATION_ID = 2;
	private static final int REQUEST_CODE = 1;

	SharedPreferences preferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		final View rootView;
		preferences = getActivity().getSharedPreferences("RidePreferences",
				Context.MODE_PRIVATE);

		cd = new ConnectionDetector(this.getActivity().getApplicationContext());

		
		i = preferences.getInt("iValue", 0);
		sj = preferences.getInt("sjValue", 0);

		rootView = (View) inflater.inflate(R.layout.fragment_create_ride,
				container, false);

		alarm = new AlarmManagerBroadcastReceiver();
		if (i == 1 || sj == 1) {

			Intent in = new Intent(CreateRideFragment.this.getActivity(),
					RideActivity.class);
			in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
			startActivity(in);
			getActivity().finish();
		}

		String un = ParseUser.getCurrentUser().getString("Name");
		TextView welcome = (TextView) rootView.findViewById(R.id.welcome);
		welcome.setText("Hello " + un + "!");

		fem_check = ParseUser.getCurrentUser().getString("Gender");

		if (fem_check.equals("Female")) {

			RelativeLayout rl1 = (RelativeLayout) rootView
					.findViewById(R.id.rl1);
			rl1.setVisibility(View.VISIBLE);
		}

		checkBox = (CheckBox) rootView.findViewById(R.id.checkBox);
		checkBox.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (checkBox.isChecked()) {
					femride = true;
					// Toast.makeText(CreateRideFragment.this.getActivity(), ""+
					// femride, Toast.LENGTH_SHORT).show();

				}

			}
		});

		sdf = new SimpleDateFormat("HH:mm");
		String currentTime = sdf.format(new Date());
		EditText edt_time = (EditText) rootView.findViewById(R.id.time);
		edt_time.setText(currentTime);
		start_time = currentTime;
		end_time = currentTime;

		submit = (Button) rootView.findViewById(R.id.rideBut);
		no = (EditText) rootView.findViewById(R.id.pass);
		timet = (EditText) rootView.findViewById(R.id.editText1);
		source = (AutoCompleteTextView) rootView.findViewById(R.id.source);
		destination = (AutoCompleteTextView) rootView
				.findViewById(R.id.destination);
		spinner = (Spinner) rootView.findViewById(R.id.spinner1);
		time = (EditText) rootView.findViewById(R.id.time);

		// For auto complete
		mEdittext = (AutoCompleteTextView) rootView.findViewById(R.id.source);
		mEdittext.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false) { // lost focus
					mEdittext.setSelection(0, 0);
				}
			}
		});

		mEdittext2 = (AutoCompleteTextView) rootView
				.findViewById(R.id.destination);
		mEdittext2.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false) { // lost focus
					mEdittext2.setSelection(0, 0);
				}
			}
		});

		// AutoComplete
		adapter = new ArrayAdapter<String>(
				CreateRideFragment.this.getActivity(), R.layout.item_list);
		adapter2 = new ArrayAdapter<String>(
				CreateRideFragment.this.getActivity(), R.layout.item_list);
		textView = (AutoCompleteTextView) rootView.findViewById(R.id.source);
		textView2 = (AutoCompleteTextView) rootView
				.findViewById(R.id.destination);
		adapter.setNotifyOnChange(true);
		adapter2.setNotifyOnChange(true);
		textView.setAdapter(adapter);
		textView2.setAdapter(adapter2);
		textView.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Log.e("Text Changed", "s : " + s + " start: " + start
				// + " before : " + before + " count : " + count);

				if (count % 3 == 1) {
					// Log.e("TextChanged", "div by 3 it is");
					adapter.clear();
					places = new GetPlaces();
					places.execute(textView.getText().toString());

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		textView2.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// Log.e("Text Changed", "s : " + s + " start: " + start
				// + " before : " + before + " count : " + count);

				if (count % 3 == 1) {
					// Log.e("TextChanged", "div by 3 it is");

					adapter2.clear();
					places2 = new GetPlaces();
					places2.execute(textView2.getText().toString());

				}

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}

		});

		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { // TODO Auto-generated method stub

				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {
					// AsyncTaskRunner runner = new AsyncTaskRunner();
					// runner.execute();

					attemptSubmit();
				} else {
					showAlertDialog(CreateRideFragment.this.getActivity(),
							"No Internet Connection",
							"You don't have internet connection.", false);
				}

			}
		});

		return rootView;
	}

	protected void insertdata() {

		dataBaseAdapter = new DataBaseAdapter(getActivity());
		dataBaseAdapter = dataBaseAdapter.open();
		dataBaseAdapter.insertEntry(s, d, tim, n, veh, t);

	}

	// submitcheck
	public void attemptSubmit() {

		clearErrors();

		// Store values at the time of the login attempt.
		s = source.getText().toString();
		d = destination.getText().toString();
		tim = timet.getText().toString();
		n = no.getText().toString();
		veh = spinner.getSelectedItem().toString();
		t = time.getText().toString();

		end_time=t;
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String currentTime = sdf.format(new Date());
		start_time = currentTime;
		
		try {
			timedif = sdf.parse(end_time).getTime() - sdf.parse(start_time).getTime();
			Log.d("diff","= " +timedif);
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
				
		final String un = ParseUser.getCurrentUser().getString("username");
		String host = ParseUser.getCurrentUser().getString("Name");
		boolean cancel = false;
		View focusView = null;
		String valid="true";

		if(timedif<=0)
		{
			showAlertDialog(CreateRideFragment.this.getActivity(), "Invalid time",	"Please enter a time that is greater than the current time", false);
			valid="false";
		}
		
		if (TextUtils.isEmpty(s)) {
			source.setError(getString(R.string.error_field_required));
			focusView = source;
			cancel = true;
			valid="false";

		}

		if (TextUtils.isEmpty(d)) {
			destination.setError(getString(R.string.error_field_required));
			focusView = destination;
			cancel = true;
			valid="false";

		}

		if (d.equals(s)) {

			destination
					.setError(getString(R.string.Same_Source_And_Destination));

			focusView = destination;
			cancel = true;
			valid="false";

		}

		if (TextUtils.isEmpty(n)) {
			no.setError(getString(R.string.error_field_required));
			focusView = no;
			cancel = true;
			valid="false";

		}

		if (TextUtils.isEmpty(tim)) {
			timet.setError(getString(R.string.error_field_required));
			focusView = timet;
			cancel = true;
			valid="false";

		} 
		
		
		else if (Integer.valueOf(tim) > 60) {
			timet.setError(getString(R.string.error_invalid_tt));
			focusView = timet;
			cancel = true;
			valid="false";

		}
		
		

		if(valid.equals("true")) {
			// Save new trip data into Parse.com Data Storage

			pd = new ProgressDialog(getActivity());
			pd.setMessage("Creating your ride");
			pd.show();
			pd.setCancelable(false);

			
			
			insertdata();

			// Toast.makeText(CreateRideFragment.this.getActivity(),
			// "Your ride is set!", Toast.LENGTH_SHORT).show();

			noadd = Integer.parseInt(n);
			temp = noadd;
			noadd2 = noadd;

			ott = Integer.parseInt(tim);
			h1 = t.charAt(0);
			h2 = t.charAt(1);
			m1 = t.charAt(3);
			m2 = t.charAt(4);
			String hh = new StringBuilder().append(h1).append(h2).toString();
			String mm = new StringBuilder().append(m1).append(m2).toString();
			ogh = Integer.parseInt(hh);
			ogm = Integer.parseInt(mm);
			sm = ogm - ott;
			em = ogm + ott;
			sh = ogh;
			eh = ogh;
			r_id = new StringBuilder().append(un).append(1).toString();

			SharedPreferences preferences = getActivity().getSharedPreferences(
					"MyRideID", Context.MODE_PRIVATE);
			Editor editor2 = preferences.edit();
			editor2.putString("RID", r_id);
			editor2.commit();
			
			Geocoder geoCoder = new Geocoder(getActivity().getApplicationContext(),Locale.getDefault());
			try {
			             List<Address> locations = geoCoder.getFromLocationName(s, 1);
			             List<Address> locations2 = geoCoder.getFromLocationName(d, 1);
			             
			             Address ad = null;
			             for (Address a : locations) {
			                 a.getAddressLine(1);
			                 ad = a;
			           	 lat_source = a.getLatitude();
			                 long_source = a.getLongitude();
			                 
			                geosource= new ParseGeoPoint(lat_source,long_source); 
			                
			              }
			            
			             for (Address a : locations2) {
			                 a.getAddressLine(1);
			                 ad = a;
			           	 lat_destination = a.getLatitude();
			                 long_destination = a.getLongitude();
			                 geodestination= new ParseGeoPoint(lat_destination,long_destination);
			                 
			                
			             }
			          
			         } catch (IOException e) {
			             // TODO Auto-generated catch block
			             e.printStackTrace();
			         }
			
			if (sm < 0) {
				sm = 60 + sm;
				--sh;
				if (sh < 0)
					sh = 23;
			}

			if (em >= 60) {
				++eh;
				em = em - 60;
				if (eh == 24)
					eh = 00;
			}

			final ParseObject Rides_Create = new ParseObject("Rides_Created");
			Rides_Create.put("Host_name", host);
			Rides_Create.put("User_name", un);
			Rides_Create.put("Source", s);
			Rides_Create.put("Destination", d);
			Rides_Create.put("GeoSource", geosource);
			Rides_Create.put("GeoDestination", geodestination);			
			Rides_Create.put("S_hr", sh);
			Rides_Create.put("E_hr", eh);
			Rides_Create.put("S_min", sm);
			Rides_Create.put("E_min", em);
			Rides_Create.put("No_of_add_pass", noadd);
			Rides_Create.put("Vehicle_type", veh);
			Rides_Create.put("Fem_ride", femride);
			Rides_Create.put("R_id", r_id);
			Rides_Create.put("Og_add_pass", noadd2);

			final ParseObject Created_ride = new ParseObject("Created_R");
			Created_ride.put("R_id", r_id);
			Created_ride.put("Host", un);
			for (; temp > 0; temp--) {
				String addpas = new StringBuilder().append("add").append(temp)
						.toString();
				Created_ride.put(addpas, "Available");
				String timepref = new StringBuilder().append("timepref")
						.append(temp).toString();
				Created_ride.put(timepref, "None");
			}

			Created_ride.saveInBackground(new SaveCallback() {
				public void done(ParseException e) {
					if (e == null) {
						// Success!

					} else {
						// Failure!
					}
					return;
				}

			});

			Rides_Create.saveInBackground(new SaveCallback() {
				public void done(ParseException e) {
					if (e == null) {
						// Success!
						objectId = Rides_Create.getObjectId();
						// Toast.makeText(CreateRideFragment.this.getActivity(),
						// objectId, Toast.LENGTH_SHORT).show();
						sendObj();
					} else {
						// Failure!
					}
					return;
				}

			});

		}
	}

	private void sendObj() {
		// TODO Auto-generated method stub
		
		end_time = t;
		Date elapsed;
		try {
			elapsed = new Date(sdf.parse(end_time).getTime()
					- sdf.parse(start_time).getTime());
			Log.d("Time Difference", " = " + end_time + " - " + start_time + "=" + sdf.format(elapsed));
			a = sdf.parse(end_time).getTime()
					- sdf.parse(start_time).getTime()
					+ Integer.parseInt(tim) * 1000 * 60;
			Log.d("a cha value", " = " + a);
			Context context = this.getActivity().getApplicationContext();
			if (alarm != null) {
				alarm.setOnetimeTimer(context, a , r_id);
			} else {
				Log.d("Alarm", "Value is null");
			}
		} catch (java.text.ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}


		
		displayNotification("Thank you for submitting.");

		int i = 1;

		SharedPreferences settings = getActivity().getSharedPreferences(
				PREFS_NAME, 0);
		SharedPreferences.Editor editor = settings.edit();
		editor.putString("Obj", objectId);
		editor.commit();

		/*
		 * settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
		 * SharedPreferences.Editor editor = settings.edit();
		 * editor.putString("Obj", objectId); editor.commit();
		 * 
		 * FileOutputStream fos; try { fos =
		 * getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
		 * fos.write(i); fos.close(); } catch (FileNotFoundException e) { //
		 * TODO Auto-generated catch block e.printStackTrace(); } catch
		 * (IOException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); }
		 */

		Editor editor2 = preferences.edit();
		editor2.putInt("iValue", i);
		editor2.commit();

		ParseInstallation.getCurrentInstallation().saveInBackground();

		PushService.subscribe(getActivity().getApplicationContext(), r_id,
				MainActivity.class);

		pd.dismiss();

		Intent in = new Intent(CreateRideFragment.this.getActivity(),
				RideActivity.class);
		in.putExtra("String", objectId);
		in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
		startActivity(in);
		getActivity().finish();

	}

	

	public void displayNotification(String msg) {

		NotificationManager manager = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				msg, System.currentTimeMillis());

		// The PendingIntent will launch activity if the user selects this
		// notification
		Context context=getActivity().getApplicationContext();
		Intent notificationIntent= new Intent(context ,
				App.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | 
			    Intent.FLAG_ACTIVITY_SINGLE_TOP | 
			    Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent contentIntent = PendingIntent.getActivity(this
				.getActivity(), REQUEST_CODE,notificationIntent , 0);

		notification.setLatestEventInfo(this.getActivity(),
				"Your ride has been created",
				"Please wait for others to find your ride :)", contentIntent);

	    notification.flags |= Notification.FLAG_AUTO_CANCEL;
		manager.notify(NOTIFICATION_ID, notification);

	}

	private void clearErrors() {
		source.setError(null);
		destination.setError(null);
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

	class GetPlaces extends AsyncTask<String, Void, ArrayList<String>> {

		String s;

		@Override
		// three dots is java for an array of strings
		protected ArrayList<String> doInBackground(String... args) {

			s = args[0];
			// Log.e("gottaGo", "doInBackground");

			ArrayList<String> predictionsArr = new ArrayList<String>();

			try {

				URL googlePlaces = new URL(
				// URLEncoder.encode(url,"UTF-8");
						"https://maps.googleapis.com/maps/api/place/autocomplete/json?input="
								+ URLEncoder.encode(s.toString(), "UTF-8")
								+ "&types=geocode|establishment&language=in&sensor=false&key=AIzaSyBDDsfSGtGSAiNVVI_6JLo0JFDNssmta90");
				URLConnection tc = googlePlaces.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(
						tc.getInputStream()));

				String line;
				StringBuffer sb = new StringBuffer();
				// take Google's legible JSON and turn it into one big
				// string.
				while ((line = in.readLine()) != null) {
					sb.append(line);
				}
				// turn that string into a JSON object
				JSONObject predictions = new JSONObject(sb.toString());
				// now get the JSON array that's inside that object
				JSONArray ja = new JSONArray(
						predictions.getString("predictions"));
				JSONObject jo;

				for (int i = 0; i < ja.length(); i++) {
					jo = (JSONObject) ja.get(i);
					// add each entry to our array
					predictionsArr.add(jo.getString("description"));
				}
			} catch (IOException e) {

				Log.e("YourApp", "GetPlaces : doInBackground", e);

			} catch (JSONException e) {

				Log.e("YourApp", "GetPlaces : doInBackground", e);

			}

			return predictionsArr;

		}

		// then our post

		@Override
		protected void onPostExecute(ArrayList<String> result) {

			// Log.e("YourApp", "onPostExecute : " + result.size());
			// update the adapter
			adapter = new ArrayAdapter<String>(getActivity().getBaseContext(),
					R.layout.item_list);
			adapter.setNotifyOnChange(true);
			// attach the adapter to textview
			textView.setAdapter(adapter);

			for (String string : result) {

				// Log.e("YourApp", "onPostExecute : result = " + string);
				adapter.add(string);
				adapter.notifyDataSetChanged();
			}

			adapter2 = new ArrayAdapter<String>(getActivity().getBaseContext(),
					R.layout.item_list);
			adapter2.setNotifyOnChange(true);
			// attach the adapter to textview
			textView2.setAdapter(adapter2);

			for (String string2 : result) {

				// Log.e("YourApp", "onPostExecute : result = " + string);
				adapter2.add(string2);
				adapter2.notifyDataSetChanged();
			}

			// Log.e("YourApp",
			// "onPostExecute : autoCompleteAdapter" + adapter.getCount());

		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		/*
		 * SharedPreferences settings =
		 * getActivity().getSharedPreferences(PREFS_NAME, 0);
		 * SharedPreferences.Editor editor = settings.edit();
		 * editor.putString("Obj", objectId); editor.commit();
		 */
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		/*
		 * SharedPreferences settings =
		 * getActivity().getSharedPreferences(PREFS_NAME, 0);
		 * SharedPreferences.Editor editor = settings.edit();
		 * editor.putString("Obj", objectId); editor.commit();
		 */
	}

	/*
	 * public void onBackPressed() { // TODO Auto-generated method stub
	 * super.getActivity().onBackPressed(); new
	 * AlertDialog.Builder(this.getActivity())
	 * .setIcon(android.R.drawable.ic_dialog_alert)
	 * .setTitle("Closing Activity")
	 * .setMessage("Are you sure you want to close this activity?")
	 * .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	 * 
	 * @Override public void onClick(DialogInterface dialog, int which) {
	 * getActivity().finish(); }
	 * 
	 * }) .setNegativeButton("No", null) .show();
	 * 
	 * }
	 */
}
