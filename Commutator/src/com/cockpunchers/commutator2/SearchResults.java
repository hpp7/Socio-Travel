package com.cockpunchers.commutator2;

import java.util.ArrayList;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class SearchResults extends Activity {
	ArrayAdapter<String> adapterres;
	String objectId, name, source, destination, vehicle;
	int cap;
	int tt;
	int t;
	Button join;
	ListView reslist;
	int i;
	Context context;
	String timepref;
	ArrayList<String> objid = new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchresults);
		objid.clear();
		objid = getIntent().getStringArrayListExtra("ArrayList");
		timepref=getIntent().getStringExtra("String");

		reslist = (ListView) findViewById (R.id.searchlist);
		//reslist.setAdapter(new CustomAdapter(this,objid));	
		Log.d("SearchResults", "Reached here nigga");
		join = (Button) findViewById(R.id.join);
		
		reslist.setAdapter(new CustomAdapter(this,objid,timepref));


	
	}
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();
	}
	
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    Intent in =  new Intent(SearchResults.this,MainActivity.class);
		in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
    	startActivity(in);
	    finish();

	}
	
}

