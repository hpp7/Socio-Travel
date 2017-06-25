package com.cockpunchers.commutator2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.cockpunchers.commutator2.R;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SearchRideFragment extends Fragment implements OnItemClickListener {

	String objectId;
	ArrayAdapter<String> adapter;
	ArrayAdapter<String> adapter2;
	AutoCompleteTextView textView;
	AutoCompleteTextView textView2;
	GetPlaces places;
	GetPlaces places2;
	AutoCompleteTextView mEdittext;
	AutoCompleteTextView mEdittext2;
	
	// flag for Internet connection status
	Boolean isInternetPresent = false;
	// Connection detector class
	ConnectionDetector cd;
	int flag,flag2=0 ;

	Button submit;
	AutoCompleteTextView source;
	AutoCompleteTextView destination;
	EditText time;
	String s, d, t , timepref;
	char h1, h2, m1, m2;
	String h,m;
	int hh,mm,size;
	ArrayList<String> objid = new ArrayList<String>();
	String gender;
	ParseGeoPoint geosource,geodestination,geoaverage;
	double lat_source,long_source,lat_destination,long_destination;
	private static final int NOTIFICATION_ID = 2;
	private static final int REQUEST_CODE = 1;

	ProgressDialog pd;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_search_ride,	container, false);

		cd = new ConnectionDetector(this.getActivity().getApplicationContext());

		
		// For setting default time as current time
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String currentTime = sdf.format(new Date());
		EditText edt_time = (EditText) rootView.findViewById(R.id.time1);
		edt_time.setText(currentTime);
		gender= ParseUser.getCurrentUser().getString("Gender");
		submit = (Button) rootView.findViewById(R.id.submitsearch);
		source = (AutoCompleteTextView) rootView
				.findViewById(R.id.searchsource);
		destination = (AutoCompleteTextView) rootView
				.findViewById(R.id.searchdestination);
		time = (EditText) rootView.findViewById(R.id.time1);
		String un = ParseUser.getCurrentUser().getString("Name");
		TextView welcome = (TextView) rootView.findViewById(R.id.welcome);
		welcome.setText("Hello " + un + "!");
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) { // TODO Auto-generated method stub
				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();
				// check for Internet status
				if (isInternetPresent) {

					pd = new ProgressDialog(SearchRideFragment.this.getActivity());
		            pd.setMessage("Searching...");
		            pd.show();
		            pd.setCancelable(false);
				attemptSubmit();
				}
				
				else
				{
					showAlertDialog(SearchRideFragment.this.getActivity(), "No Internet Connection",
							"You don't have internet connection.", false);
				}
			}
		});

		// For auto complete
		mEdittext = (AutoCompleteTextView) rootView
				.findViewById(R.id.searchsource);
		mEdittext.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == false) { // lost focus
					mEdittext.setSelection(0, 0);
				}
			}
		});

		mEdittext2 = (AutoCompleteTextView) rootView
				.findViewById(R.id.searchdestination);
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
				SearchRideFragment.this.getActivity(), R.layout.item_list);
		adapter2 = new ArrayAdapter<String>(
				SearchRideFragment.this.getActivity(), R.layout.item_list);
		textView = (AutoCompleteTextView) rootView.findViewById(R.id.searchsource);
		textView2 = (AutoCompleteTextView) rootView.findViewById(R.id.searchdestination);
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
		

		return rootView;
	}

	// submitcheck
	public void attemptSubmit() {

		clearErrors();

		// Store values at the time of the login attempt.
		s = source.getText().toString();
		d = destination.getText().toString();
		t = time.getText().toString();
		h1 = t.charAt(0);
		h2 = t.charAt(1);
		m1 = t.charAt(3);
		m2 = t.charAt(4);
		h = new StringBuilder().append(h1).append(h2).toString();
		m = new StringBuilder().append(m1).append(m2).toString();
		hh = Integer.parseInt(h);
		mm = Integer.parseInt(m);
		
		timepref=new StringBuilder().append(hh).append(":").append(mm).toString();
		

		final String un = ParseUser.getCurrentUser().getString("username");
		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(s)) {
			pd.dismiss();
			source.setError(getString(R.string.error_field_required));
			focusView = source;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(d)) {
			pd.dismiss();
			destination.setError(getString(R.string.error_field_required));
			focusView = destination;
			cancel = true;
		}

		else {
			// Save new trip data into Parse.com Data Storage
			
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
			                Log.d("geos", " " + geosource);
			                
			              }
			            
			             for (Address a : locations2) {
			                 a.getAddressLine(1);
			                 ad = a;
			           	 lat_destination = a.getLatitude();
			                 long_destination = a.getLongitude();
			                 geodestination= new ParseGeoPoint(lat_destination,long_destination);

				                Log.d("geod", " " + geodestination);
			                
			             }
			          
			         } catch (IOException e) {
			             // TODO Auto-generated catch block
			             e.printStackTrace();
			         }
			
			
			ParseQuery<ParseObject> query = ParseQuery.getQuery("Rides_Created");
			if(gender.equals("Male"))
			{
			//query.whereEqualTo("Destination", d);
			//query.whereEqualTo("Source", s);
			//query.whereWithinGeoBox(key, southwest, northeast)
				query.whereWithinKilometers("GeoSource", geosource, 1.5);
				query.whereWithinKilometers("GeoDestination", geodestination, 1.5);
			query.whereEqualTo("Fem_ride", false);
			query.orderByAscending("S_min");
			}
			else
			{
				query.whereWithinKilometers("GeoSource", geosource, 1.5);
				query.whereWithinKilometers("GeoDestination", geodestination, 1.5);
				//query.whereEqualTo("Destination", d);
				//query.whereEqualTo("Source", s);
				query.orderByAscending("S_min");
			}
			query.findInBackground(new FindCallback<ParseObject>() {
			public void done(List<ParseObject> List1, ParseException e) {
				//Log.d("Search", "Retrieved " + List1.size() + " same destinations and source ");
				if (List1 != null) {
					
					
					objid.clear();
					
					int [] shr = new int[List1.size()];
					int [] ehr = new int[List1.size()];
					int [] sm = new int[List1.size()];
					int [] em = new int[List1.size()];
					flag = List1.size();
					for (int i = 0; i <(List1.size()); i++) 
					{
						
						//adapter3.add(List1.get(i).getObjectId());
						shr[i] = List1.get(i).getInt("S_hr");
						ehr[i] = List1.get(i).getInt("E_hr");
						sm[i] = List1.get(i).getInt("S_min");
						em[i] = List1.get(i).getInt("E_min");
						Log.d("Search", "Retrieved " + shr[i]+" "+sm[i]+ " values");
						Log.d("Search", "Retrieved " + ehr[i]+" "+em[i]+ " values");
						
						if((((shr[i]==22)||(shr[i]==23))&&(ehr[i]==0))&&(hh==22||hh==23||hh==0))
						{
						if((hh==22)&&(shr[i]==22))
						{
						if((mm >= sm[i]))
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						else
						{
						--flag;
						Log.d("Flag cha value", "Flag "+ flag);
						}
						}
						if((hh==23)&&(shr[i]==22))
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						if((hh==0)&&(shr[i]==22))
						{
						if((mm <= em[i]))
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						else
						{
						--flag;
						Log.d("Flag cha value", "Flag "+ flag);
						}
						}
						if((hh==23)&&(shr[i]==23))
						{
						if(hh==shr[i])
						{
						if((mm >= sm[i]))
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						else
						{
						--flag;
						Log.d("Flag cha value", "Flag "+ flag);
						}
						}
						if(hh==ehr[i])
						{
						if(mm <=em[i])
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						}
						else
						{
						--flag;
						Log.d("Flag cha value", "Flag "+ flag);
						}
						}
						}
						
						if(((shr[i]==23)&&(ehr[i]==1))&&(hh==23||hh==0||hh==1))
						{
						if((hh==23))
						{
						if((mm >= sm[i]))
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						else
						{
						--flag;
						Log.d("Flag cha value", "Flag "+ flag);
						}
						}
						if((hh==0))
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						if((hh==1))
						{
						if((mm <= em[i]))
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						else
						{
						--flag;
						Log.d("Flag cha value", "Flag "+ flag);
						}
						}
						}
						
						if((hh >= shr[i]) && (hh <= ehr[i]))
						{
						if(shr[i] == ehr[i])
						{
						if(sm[i] <= em[i])
						{   
						Log.d("Time Search", "starting min less than ending min");
						if( (sm[i] <= mm) && (mm <= em[i]))
						{
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}   
						else
						--flag;
						}
						}
						if(shr[i] < ehr[i])
						{   
						Log.d("Time search", "starting min greater than ending min");
						
						if(hh==shr[i])
						{
						if(((mm >= sm[i]) && (mm <=59)))
						{
							Log.d("Time", "Entered here");
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						
						else
							Log.d("Time", "Didn't get");
						}
						else if(hh==ehr[i])
						{
						if((mm>=0)&&(mm <=em[i]))
						{
							Log.d("Time", "Entered here");
						objid.add(List1.get(i).getObjectId().toString());
						++flag2;
						}
						else
							Log.d("Time", "Didn't get");
						}
						else if((hh>shr[i]) && (hh<ehr[i]))
						{
							objid.add(List1.get(i).getObjectId().toString());
							++flag2;
						}
												
						}
						}//time ends
						else
						{
						--flag;
						Log.d("Flag cha value", "Flag "+ flag);
						}
					}//for ends
						//Toast.makeText(SearchRideFragment.this.getActivity(),flag,Toast.LENGTH_SHORT).show();

						if(((flag-flag2)>=0) && ((flag-flag2)!= flag))
						{
						Log.d("Flag cha value", "Flag "+ flag+" "+flag2);
						offtosearchresults(objid,timepref);
						   }
						else 
						{Log.d("Flag cha value", "Flag "+ flag);
						//ProgressDialog pd = new ProgressDialog(SearchRideFragment.this.getActivity());
						pd.dismiss();
						Toast.makeText(SearchRideFragment.this.getActivity()," No Rides Found ",Toast.LENGTH_SHORT).show();
						}
						/*else
						Log.d("Flag cha value", "Mava happened "+ flag);*/

						}
						else 
						{

							pd.dismiss();
						Log.d("Search", "Error: " + e.getMessage());
						/*for(int i=0 ;i<objid.size() ; i++)
						Log.d("Objectid", objid.get(i).toString());*/
						}
				}
						});
						//Toast.makeText(SearchRideFragment.this.getActivity(),	"Searching for a ride...", Toast.LENGTH_LONG).show();
						//displayNotification("Thank you for submitting.");

						}
						}
	

	

	private void offtosearchresults(ArrayList<String> objid2, String pref) {
	Log.d("Offto", "reached here man" );
	pd.dismiss();
	Intent intent = new Intent(SearchRideFragment.this.getActivity(), SearchResults.class);
	intent.putExtra("ArrayList", objid2);
	intent.putExtra("String", pref);
	startActivity(intent);
		
	}
/*
	@SuppressWarnings("deprecation")
	public void displayNotification(String msg) {
		NotificationManager manager = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification notification = new Notification(R.drawable.ic_launcher,
				msg, System.currentTimeMillis());

		// The PendingIntent will launch activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this
				.getActivity(), REQUEST_CODE, new Intent(this.getActivity(),
				MainActivity.class), 0);

		notification.setLatestEventInfo(this.getActivity(),
				"Your ride has been created",
				"Please wait for others to find your ride :)", contentIntent);

		manager.notify(NOTIFICATION_ID, notification);

	}*/

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
								+ "&types=establishment|geocode&language=in&sensor=false&key=AIzaSyBDDsfSGtGSAiNVVI_6JLo0JFDNssmta90");
				URLConnection tc = googlePlaces.openConnection();
				BufferedReader in = new BufferedReader(new InputStreamReader(tc.getInputStream()));
				
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
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}

}
