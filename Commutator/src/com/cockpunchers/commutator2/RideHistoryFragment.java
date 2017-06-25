package com.cockpunchers.commutator2;

import java.util.ArrayList;

import com.cockpunchers.commutator2.R;
import com.parse.ParseUser;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class RideHistoryFragment extends Fragment {
	

    DataBaseAdapter db; // db mava
    ListView lv;
	Button join;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.fragment_ride_history, container, false);
		

        db = new DataBaseAdapter(this.getActivity());
        

		
		Button button=(Button)rootView.findViewById(R.id.show);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lv = (ListView) getView().findViewById (R.id.pastRides);
				getdata();
				
			    }
				
				
			
		});
		
		Button button1=(Button)rootView.findViewById(R.id.del);
		button1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				lv = (ListView) getView().findViewById (R.id.pastRides);

				deletedata();
				
			    }
				
				
			
		});
		
		return rootView;
	}

	
protected void getdata() {
		
	try
	{
		// The ArrayList that holds the row data
		ArrayList<ArrayList<Object>> row = new ArrayList<ArrayList<Object>>();
		row.clear();
		db=new DataBaseAdapter(getActivity());
		db=db.open();
		// ask the database manager to retrieve the row with the given rowID
		row = db.getAllRowsAsArrays();
		int n = row.size();
		Log.d("Rows", " Number = " +n);

		if(n==0)
		{
			Toast.makeText(RideHistoryFragment.this.getActivity(), "No rides to show, sorry! :(", Toast.LENGTH_SHORT).show();

		}
		// update the form fields to hold the retrieved data
				lv.setAdapter(new CustomAdapter2(this.getActivity(),row));	

		Log.d("SearchResults", "Reached here nigga");
		
	}
	catch (Exception e)
	{
		Log.e("Retrieve Error", e.toString());
		e.printStackTrace();
	}
	
	}

protected void deletedata()
{
	try
	{
		ArrayList<ArrayList<Object>> row = new ArrayList<ArrayList<Object>>();
		row.clear();
		db=new DataBaseAdapter(getActivity());
		db.open();
		
		row = db.getAllRowsAsArrays();
		int n = row.size();

		if(n==0)
		{
			Toast.makeText(RideHistoryFragment.this.getActivity(), "Uh oh, there's nothing to delete", Toast.LENGTH_SHORT).show();

		}
		else
		{
			db=new DataBaseAdapter(getActivity());
			db.open();
			db.delete();
			db.open();
			row = db.getAllRowsAsArrays();
			lv.setAdapter(new CustomAdapter2(this.getActivity(),row));	
	
		}
			
		
		
	}
	catch(Exception e)
	{
		Log.e("Delete Error", e.toString());
		e.printStackTrace();
	}
	
	
}
}
