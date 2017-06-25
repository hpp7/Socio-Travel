package com.cockpunchers.commutator2;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ExpandableListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class RidesCreatedFragment extends Fragment {
	
	int i=0,sj=0;
	//String filename = "created_or_not";	
	//String file = "joined_or_not";
	SharedPreferences preferences;
	String objectId,objectId2,objidj; 
	String rc_s,rc_d,rc_type,rc_t,rc_un,un;
	int rc_add,rc_sh,rc_eh,rc_sm,rc_em,rc_ogadd;
	String rc_rid, temp_sh,temp_sm,temp_eh,temp_em;
	String rc_username,gender,age;
	ImageView im4;
	TextView rc_addp;
    public static final String PREFS_NAME = "MyObj";
    public static final String PREF_NAME="MyO";
	
    private AlarmManagerBroadcastReceiver alarm;
    
    
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		//Bundle extras = getArguments();
		//objectId = extras.getString("String");
		//objectId = getActivity().getIntent().getStringExtra("String");
		RideActivity activity = (RideActivity) getActivity();
        objectId= activity.getMyData();
		Log.d("Ridecreated", " "+objectId);
        final View rootView;
		rootView= (View)inflater.inflate(R.layout.ride_created, container, false);
		
		preferences = getActivity().getSharedPreferences("RidePreferences",
				Context.MODE_PRIVATE);

		Button b=(Button) rootView.findViewById(R.id.leave);
		Button button=(Button)rootView.findViewById(R.id.delete);
		
		alarm = new AlarmManagerBroadcastReceiver();
		
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
	
		String un = ParseUser.getCurrentUser().getString("Name");

		//Toast.makeText(this.getActivity().getApplicationContext(), "Current Obj ID: "+ objectId, Toast.LENGTH_SHORT).show();
		
		  if(i==1)
		    {
		    	b.setVisibility(View.INVISIBLE);
		    	button.setVisibility(View.VISIBLE);
		    }
		    else
		    {

		    	b.setVisibility(View.VISIBLE);
		    	button.setVisibility(View.INVISIBLE);
		    	
		    }
		
		  ParseQuery<ParseObject> query = ParseQuery.getQuery("Rides_Created");
		    query.getInBackground(objectId, new GetCallback<ParseObject>() {
		      public void done(ParseObject object, ParseException e) {
		        if (e == null) {

		        	rc_username=object.getString("User_name");
		        	rc_un = object.getString("Host_name");
		        	rc_s= object.getString("Source");
		        	rc_d= object.getString("Destination");
		        	rc_type= object.getString("Vehicle_type");
		        	rc_add= object.getInt("No_of_add_pass");
		        	rc_ogadd=object.getInt("Og_add_pass");
		        	rc_sh= object.getInt("S_hr");
		        	rc_eh=object.getInt("E_hr");
		        	rc_sm=object.getInt("S_min");
		        	rc_em=object.getInt("E_min");
		        	rc_rid=object.getString("R_id");
		        	temp_sh=Integer.toString(rc_sh);
		        	temp_eh=Integer.toString(rc_eh);
		        	temp_sm=Integer.toString(rc_sm);
		        	temp_em=Integer.toString(rc_em);
		        	if(rc_sh<10)
		        		temp_sh="0"+rc_sh;
		        	if(rc_eh<10)
		        		temp_eh="0"+rc_eh;
		        	if(rc_sm<10)
		        		temp_sm="0"+rc_sm;
		        	if(rc_em<10)
		        		temp_em="0"+rc_em;
		        	
		        	
		        	if(rc_type.equalsIgnoreCase("Taxi"))
		        	{
		        		ImageView im2 = (ImageView) rootView.findViewById(R.id.imageView2);
		    	        im2.setVisibility(View.VISIBLE);
		        	}
		        	else if(rc_type.equalsIgnoreCase("Car"))
		        	{
		        		ImageView im3 = (ImageView) rootView.findViewById(R.id.imageView3);
		    	        im3.setVisibility(View.VISIBLE);
		        	}
		        	else if(rc_type.equalsIgnoreCase("AutoRickshaw"))
		        	{
		        		ImageView im1 = (ImageView) rootView.findViewById(R.id.imageView1);
		    	        im1.setVisibility(View.VISIBLE);
		        	}
		        	
		        	TextView rc_name =(TextView)rootView.findViewById(R.id.rc_name);
					rc_name.setText(rc_un);
					TextView rc_source =(TextView)rootView.findViewById(R.id.rc_source);
					rc_source.setText(rc_s);
					TextView rc_dest =(TextView)rootView.findViewById(R.id.rc_dest);
					rc_dest.setText(rc_d);
					TextView rc_typ =(TextView)rootView.findViewById(R.id.rc_type);
					rc_typ.setText(rc_type);
					TextView rc_addp =(TextView)rootView.findViewById(R.id.rc_addp);
					rc_addp.setText(Integer.toString(rc_ogadd-rc_add+1));
					TextView rc_addp2 =(TextView)rootView.findViewById(R.id.rc_addp2);
					rc_addp2.setText(Integer.toString(rc_ogadd+1));
					TextView rc_time =(TextView)rootView.findViewById(R.id.rc_time);
					rc_time.setText(temp_sh + ":" + temp_sm + " - " + temp_eh + ":" + temp_em );
					 //rc_time.setText(rc_tim + " ± " + rc_tt + " mins");
					//Toast.makeText(RideCreated.this, "Ain't no Column name: " +rc_rid, Toast.LENGTH_SHORT).show();
					 
					 ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
         			query.whereEqualTo("Username", rc_username);
         			query.getFirstInBackground(new GetCallback<ParseObject>() {
         				  public void done(ParseObject object, ParseException e) {
         				    if (object != null) 
         				    {
         				      gender=object.getString("Gender");
         				      age=object.getString("Age");
         				      
         				     TextView gen =(TextView)rootView.findViewById(R.id.rc_gender);
         				     TextView ag =(TextView)rootView.findViewById(R.id.rc_age);
         				      gen.setText(gender);
                              ag.setText(age);
                               
                               ParseFile fileObject = (ParseFile)object.get("Profile_Pic");
                               fileObject.getDataInBackground(new GetDataCallback() {
                                 public void done(byte[] data, ParseException e) {
                                   if (e == null) {
                                     Log.d("test", "We've got data in data.");
                                     // use data for something
                                     im4 = (ImageView) rootView.findViewById(R.id.pic);
                                     Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                     im4.setImageBitmap(bmp);
                                     
                                   } else {
                                     Log.d("test", "There was a problem downloading the data.");
                                   }
                                 }
                               });
                               
         				    }
         				    
         				    else 
         				    {
         				      Log.d("profile", "didnt Retrieved the object.");
         				    }
         				  }
         				});
					
					
					
					
					
					
					 // preparing list data
					 
				   //    listAdapter = new ExpandableListAdapter(RideCreated.this, listDataHeader, listDataChild);
				      // listAdapter.setInflater((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE), this);
				        // setting list adapter
				     //   expListView.setAdapter(listAdapter);

		        } 
		        else {
		          // something went wrong
		        	showAlertDialog(RidesCreatedFragment.this.getActivity(), "Ride has been deleted",	"You can now create/search for a new ride! :)", false);
		        	
		        }
		      }
		    });
		    
		    //expandableList.setOnChildClickListener(this);
		 	
		    b.setOnClickListener(new OnClickListener(){
		    	
		    	@Override
		    	public void onClick(View v)
		    	{
		    		sj=0;
		    		 AlertDialog.Builder builder = new AlertDialog.Builder(RidesCreatedFragment.this.getActivity());

					    builder.setMessage("Are you sure you want to leave this ride?")
					            .setCancelable(false)
					            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					                public void onClick(DialogInterface dialog, int id) {
					                	 PushService.unsubscribe(getActivity().getApplicationContext(), rc_rid);
					                	ParseQuery<ParseObject> query = ParseQuery.getQuery("Created_R");
		                                   query.whereEqualTo("R_id", rc_rid);
		                                   query.getFirstInBackground(new GetCallback<ParseObject>() {
		                                         public void done(ParseObject object, ParseException e) {
		                                           if (object != null) 
		                                           {
		                                               objidj=object.getObjectId().toString();                 
		                                             // Toast.makeText(RideCreated.this, "Objid: " +objidj, Toast.LENGTH_SHORT).show();
		                                               	
		                                               ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Created_R");
		                                               query2.getInBackground(objidj, new GetCallback<ParseObject>() {
		                                                 public void done(ParseObject object, ParseException e) {
		                                                  
		                                               	 if (e == null) 
		                                               	 {
		                                                    
		                                                   	
		                                                   	outerloop:
		                                                   	for(int x=0;x<rc_ogadd;x++)
		                                                       {
		                                                           final String colname= "add"+(x+1);
		                                                           final String timepr= "timepref"+(x+1);
		                                                           String col=object.getString(colname);                                       
		                                                           
		                                                           
		                                                           if(col.equals(ParseUser.getCurrentUser().getString("username")))    	
		                                                           {
		                                   	
		                                                           	//Toast.makeText(RidesCreatedFragment.this.getActivity(), "Name: " +col, Toast.LENGTH_SHORT).show();
		                                                           	//Toast.makeText(RidesCreatedFragment.this.getActivity(), "Objid: " +objectId, Toast.LENGTH_SHORT).show();
		                                                           ParseQuery<ParseObject> query = ParseQuery.getQuery("Created_R"); 
		                                                           query.getInBackground(objidj, new GetCallback<ParseObject>() {
		                                                        	   public void done(ParseObject update, ParseException e) {
		                                                        		   if (e == null) 
		                                                        		   {
		                                                        			   update.put(colname,"Available");
		                                                        			   update.put(timepr, "None");
		                                                        			   update.saveInBackground();
		                                                        			   
		                                                        			   ParseQuery<ParseObject> vuery = ParseQuery.getQuery("Rides_Created");
		                                                        			   vuery.getInBackground(objectId, new GetCallback<ParseObject>() {
		                                                        				   public void done(ParseObject update, ParseException e) {
		                                                        					   if (e == null) 
		                                                        					   {
		                                                        						   update.increment("No_of_add_pass",1);
		                                                        						   update.saveInBackground();
		                                                        						
		                                                        					   }
		                                                        				   }
		                                                        			   });	
		                                                        			 }
		                                                        		   }
		                                                        	   });                           	
		                                                           break outerloop;	
		                                                           }
		                                   	
		                                                       }
		                                                   	
		                                                   } 
		                                               	 else {
		                                                     // something went wrong                    
		                                                   	Log.d("Dunno", "Mava"); 

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
		                                  
		                                   ParsePush push = new ParsePush();
		                                   push.setChannel(rc_rid);
		                                   push.setMessage(ParseUser.getCurrentUser().getString("Name") +" left the ride");
		                                   push.sendInBackground();
		                                   //Toast.makeText(RidesCreatedFragment.this.getActivity(), "Ride Left", Toast.LENGTH_SHORT).show();
					    				
		    		/*FileOutputStream fos;
           		try {
           			fos = getActivity().openFileOutput(file, Context.MODE_PRIVATE);
           			fos.write(sj);
           			fos.close();
           		} catch (FileNotFoundException e) {
           			// TODO Auto-generated catch block
           			e.printStackTrace();
           		} catch (IOException e) {
           			// TODO Auto-generated catch block
           			e.printStackTrace();
           		}*/
           		
           		Editor editor = preferences.edit();
				editor.putInt("sjValue", sj);
				editor.commit();
           		
           		Intent in =  new Intent(RidesCreatedFragment.this.getActivity(),MainActivity.class);
					startActivity(in);
					getActivity().finish();
		    	}
					            })
					                .setNegativeButton("No", new DialogInterface.OnClickListener() {
						                public void onClick(DialogInterface dialog, int id) {
						                    dialog.cancel();
						                }
						            });
						    AlertDialog alert = builder.create();
						    alert.show();
						
						
									
					    }
		    });
		    
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 AlertDialog.Builder builder = new AlertDialog.Builder(RidesCreatedFragment.this.getActivity());

				    builder.setMessage("Are you sure you want to delete this ride?")
				            .setCancelable(false)
				            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int id) {
				                	PushService.unsubscribe(getActivity().getApplicationContext(), rc_rid);
				    				
				                	RoomChat.ondelete();
				                	//Toast.makeText(RidesCreatedFragment.this.getActivity(), "Ride Deleted", Toast.LENGTH_SHORT).show();

				    				int i=0;
									final int j=0;
				    				ParseObject.createWithoutData("Rides_Created", objectId).deleteEventually();
				    				
				    				ParseQuery<ParseObject> query = ParseQuery.getQuery("Created_R");
				    				query.whereEqualTo("R_id", rc_rid);
				    				query.getFirstInBackground(new GetCallback<ParseObject>() {
				    					  public void done(ParseObject object, ParseException e) {
				    					    if (object != null) {
						    					objectId2=object.getObjectId().toString();

						    					
				    					      Log.d("Deleting", objectId2 + " deleted..");
							    				ParseObject.createWithoutData("Created_R", objectId2).deleteEventually();

				    					    } else {
				    					      Log.d("score", "Retrieved the object.");
				    					    }
				    					  }
				    					});

				    				Context context = RidesCreatedFragment.this.getActivity().getApplicationContext();
				    				if (alarm != null) {
				    					alarm.CancelAlarm(context);
				    				} else {
				    					Log.d("Alarm", "Value is null");
				    				}
				    				
				    		        /*FileOutputStream fos;
				    				try {
				    					fos = getActivity().openFileOutput(filename, Context.MODE_PRIVATE);
				    					fos.write(i);
				    					fos.close();
				    				} catch (FileNotFoundException e) {
				    					// TODO Auto-generated catch block
				    					e.printStackTrace();
				    				} catch (IOException e) {
				    					// TODO Auto-generated catch block
				    					e.printStackTrace();
				    				}*/
				    				
				    				Editor editor = preferences.edit();
									editor.putInt("iValue", i);
									editor.commit();
				    				ParsePush push = new ParsePush();
	                                   push.setChannel(rc_rid);
	                                   push.setMessage("The Ride you were in was just deleted!!");
	                                   
	                              //     RoomChat.ondelete();
	                                   
	                                   push.sendInBackground();
				    				    Intent in =  new Intent(RidesCreatedFragment.this.getActivity(),MainActivity.class);
				    					startActivity(in);
				    					getActivity().finish();
				    				
				                    //dialog.dismiss();

				                }
				            })
				            .setNegativeButton("No", new DialogInterface.OnClickListener() {
				                public void onClick(DialogInterface dialog, int id) {
				                    dialog.cancel();
				                }
				            });
				    AlertDialog alert = builder.create();
				    alert.show();
				
				
							
			    }
							
			
		});
		  
		
		return rootView;
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
				sj = preferences.getInt("sjValue", 0);
				if(sj==1)
				{
					SharedPreferences pref = getActivity().getSharedPreferences("MyRideID",
							Context.MODE_PRIVATE);
					String rId=pref.getString("RID", null);
    				PushService.unsubscribe(getActivity().getApplicationContext(), rId);
					int sj=0;
					Editor editor2 = preferences.edit();
    				editor2.putInt("sjValue", sj);
    				editor2.commit();
    				
				}
				Intent in =  new Intent(RidesCreatedFragment.this.getActivity(),MainActivity.class);
				in.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				startActivity(in);
				getActivity().finish();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}
}






	
    
   

	
