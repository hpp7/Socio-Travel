package com.cockpunchers.commutator2;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class RidersFragment extends Fragment {
	//ExpandableListAdapter objExpAdapter=null;
    //ExpandableListView expList;
    ListView rideList;
    //CustomAdapter3 objLisAdapter;
    //private ArrayList<String> parentItems = new ArrayList<String>();
    //private ArrayList<Object> childItems = new ArrayList<Object>();
    Activity activityContext;    
    RidesCreatedFragment update;
    String objectId,rc_rid,objectId2;
    ArrayList<String> unames = new ArrayList<String>();
    ArrayList<String> tprefs = new ArrayList<String>();
    //CustomAdapter3 adapter;
    int i=0,sj=0;
	String filename = "created_or_not";	
	String file = "joined_or_not";
	public static final String PREFS_NAME = "MyObj";
    public static final String PREF_NAME="MyO";

    int rc_add,rc_ogadd;
    //ViewHolder_Child holder_child = null;
    //ViewHolder_Parent holder_parent = null;
    SharedPreferences preferences;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) 
	{
		
		final View rootView;
		rootView= (View)inflater.inflate(R.layout.riders_fragment, container, false);
		rideList = (ListView) rootView.findViewById (R.id.riderslist);
		
		RideActivity activity = (RideActivity) getActivity();
		
		preferences = activity.getSharedPreferences("RidePreferences",
				Context.MODE_PRIVATE);

		//objectId = getActivity().getIntent().getStringExtra("String");
        objectId= activity.getMyData();
        activityContext=getActivity();
        /*FileInputStream fis;
		try {
			fis = this.getActivity().openFileInput(filename);
			i=fis.read();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		
        i = preferences.getInt("iValue", 0);
    	if(i==1)
    	{
		SharedPreferences settings = this.getActivity().getSharedPreferences(PREFS_NAME, 0);
	    objectId = settings.getString("Obj", "aint no");
    	}
    	else{
    		SharedPreferences settings = this.getActivity().getSharedPreferences(PREF_NAME, 0);
    	    objectId = settings.getString("Object", "aint no");
    	}
    	Log.d("FO", "objid"+objectId);
    	//Toast.makeText(this.getActivity().getApplicationContext(), "Current Obj ID: "+ objectId, Toast.LENGTH_SHORT).show();
    	     
    	   		
				getAllIds(rootView);
		
    	
    
    	
    	Button but= (Button)rootView.findViewById(R.id.ridersb);
        but.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
       
        	Toast.makeText(RidersFragment.this.getActivity(), "Refreshing.. Please Wait", Toast.LENGTH_LONG).show();
//        	update=new RidesCreatedFragment();
//        	update.update(objectId);
        	unames.clear();
	   		tprefs.clear();
        	getAllIds(rootView);
        //rideList.deferNotifyDataSetChanged();
        //rideList.setAdapter(new CustomAdapter3(RidersFragment.this.getActivity(),unames));
        }
        });
    	
        return rootView;		
        
        
	}   
   
	
	
	
	
	   private void getAllIds(View view) 
	   {			   
		   ParseQuery<ParseObject> query = ParseQuery.getQuery("Rides_Created");
	  	    query.getInBackground(objectId, new GetCallback<ParseObject>() {
	  		      public void done(ParseObject object, ParseException e) {
	  		        if (object != null) 
	  		        {
	  		        	rc_add= object.getInt("No_of_add_pass");
	  		        	rc_ogadd=object.getInt("Og_add_pass");
	  		        	rc_rid=object.getString("R_id");
	  		        	Log.d("EXP", "ogadd"+rc_rid);
	  		    ParseQuery<ParseObject> query1 = ParseQuery.getQuery("Created_R");
	  			query1.whereEqualTo("R_id", rc_rid);
	  			query1.getFirstInBackground(new GetCallback<ParseObject>() {
	  				  public void done(ParseObject object, ParseException e) {
	  				    if (object != null) 
	  				    {
	  						objectId2=object.getObjectId().toString();			     
	  						
	  						
	  						//Toast.makeText(RideCreated.this, "Objid: " +objectId2, Toast.LENGTH_SHORT).show();

	  						ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Created_R");
	  						query2.getInBackground(objectId2, new GetCallback<ParseObject>() {
	  						  public void done(ParseObject object, ParseException e) {
	  						    if (e == null) 
	  						    {
	  						      // object will be your game score
	  						    	for(int x=0;x<rc_ogadd;x++)
	  								{
	  									String colname= "add"+(x+1);
	  									String colname2= "timepref"+(x+1);
	  									
	  									
	  									Log.d("Current col name ", colname);
	  				    				//Toast.makeText(RideCreated.this, "Column name: " + colname, Toast.LENGTH_SHORT).show();
	  									String col=object.getString(colname);
	  									//Log.d("Name ", col);
	  									if(!col.equals("Available"))
	  									unames.add(col);
	  									
	  									String col2=object.getString(colname2);
	  									//Log.d("Name ", col);
	  									if(!col2.equals("None"))
	  									tprefs.add(col2);
	  									//parentItemsog.add(parentItems.get(x).toString());
	  									//parentItemsog.add("YoYo");
	  									//parentItemsog.add("Suck");
	  									//Log.d("LOG", unames.get(x));
	  				    				//Toast.makeText(RidersFragment.this.getActivity(), "Column name: " + col, Toast.LENGTH_SHORT).show();
	    	
	  								}
	  								rideList.setAdapter(new CustomAdapter3(RidersFragment.this.getActivity(),unames,tprefs));

	  						    } else {
	  						      // something went wrong					
	  						    	//Toast.makeText(RidersFragment.this.getActivity(), "Objid: " +objectId2, Toast.LENGTH_SHORT).show();

	  						    }
	  						  }

	 						
	  						});
	  				    } 
	  				    else 
	  				    {
	  				      Log.d("R_ID", "Didn't retrieve the object.");
	  				    }
	  				  }

	 				
	  				});
	  		        }
	  		        else {
	  			          Log.d("Passegers", "Mava heppneing here");
	  			        }
	  			      }

	 			
	  			    });
  
		   
	    	

		   
	   }
}

	
