package com.cockpunchers.commutator2;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.PushService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
public class CustomAdapter extends BaseAdapter{ 
    SearchResults yoyo;
ArrayList<String> objid,riders;
    
ProgressDialog pd ;
    
Context context;
    int i,cap,sh,eh,sm,em,sj=0,flag=0,cap2;
    String name,source,destination,vehicle,st,et,rid,id,objjid,capa,timepref;
    private static LayoutInflater inflater=null;
    int join = 1;
    public static final String PREF_NAME="MyO";

String file = "joined_or_not";
SharedPreferences preferences;
    public CustomAdapter(SearchResults searchResults, ArrayList<String> objidis, String time) 
    {
   timepref=time;
    objid=objidis;
        context=searchResults;
        
         inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return objid.size();
    }
 
    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
 
    class Holder
    {
    TextView n, sou, des, passcap, time;
    ImageView vehcar,vehauto,vehtaxi;
    Button join;
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
       final Holder holder= new Holder();
        final View rowView; 
        
         
             rowView = inflater.inflate(R.layout.searchdisplay, null);
             
             for(i=position ;i<(objid.size()); i++)
    {	
            Log.d("Test", objid.get(i).toString());
   
    ParseQuery<ParseObject> query = ParseQuery.getQuery("Rides_Created");
    query.getInBackground(objid.get(position).toString(), new GetCallback<ParseObject>() {
    @Override
    public void done(ParseObject object, ParseException e) {
    if (e == null) {
    Log.d("Inside", "reached here nigga");
    holder.join = (Button)rowView.findViewById(R.id.join);
    holder.n = (TextView) rowView.findViewById(R.id.resname); 
                holder.sou = (TextView) rowView.findViewById(R.id.searessou);
                holder.des = (TextView) rowView.findViewById(R.id.searesdes);
                holder.passcap = (TextView) rowView.findViewById(R.id.seapass);    
                holder.vehcar = (ImageView) rowView.findViewById(R.id.motimg);
                holder.vehauto = (ImageView) rowView.findViewById(R.id.motimg1);
                holder.vehtaxi = (ImageView) rowView.findViewById(R.id.motimg2);
                holder.time = (TextView) rowView.findViewById(R.id.searchrestime);
       name = object.getString("Host_name");
    source = object.getString("Source");
    destination = object.getString("Destination");
    vehicle = object.getString("Vehicle_type");
    rid = object.getString("R_id");
    cap = object.getInt("Og_add_pass");
    cap2 = object.getInt("No_of_add_pass");
    sh = object.getInt("S_hr");
    eh = object.getInt("E_hr");
    sm = object.getInt("S_min");
    em = object.getInt("E_min");
    st= new StringBuilder().append(sh).append(":").append(sm).toString();
    et= new StringBuilder().append(eh).append(":").append(em).toString();
    holder.n.setText(name);
    holder.sou.setText(source);
    holder.des.setText(destination);
    holder.passcap.setText(Integer.toString(cap2));
    holder.time.setText(st+" to "+et);
    if(vehicle.equals("Car"))
    holder.vehcar.setVisibility(View.VISIBLE);
    if(vehicle.equals("AutoRickshaw"))
    holder.vehauto.setVisibility(View.VISIBLE);
    if(vehicle.equals("Taxi"))
    holder.vehtaxi.setVisibility(View.VISIBLE);
   
   
   
    } else {
    // something went wrong
    }
    }

    });
   
    }
         
             ((Button)rowView.findViewById(R.id.join)).setOnClickListener(new OnClickListener() {

                 @Override
                 public void onClick(View v) {
                 
                sj=1;
                join = 2;
                //AsyncTaskRunner runner = new AsyncTaskRunner();
    		    //runner.execute();

                pd= new ProgressDialog(context);
                pd.setMessage("Joining the ride");
                pd.show();
                pd.setCancelable(false);
                final String bhej = objid.get(position).toString();

                SharedPreferences settings = parent.getContext()
						.getSharedPreferences(PREF_NAME, 0);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("Object", bhej);
				editor.putInt("join", 2);
				editor.commit();

				preferences = parent.getContext().getSharedPreferences(
						"RidePreferences", Context.MODE_PRIVATE);

             	/*SharedPreferences settings = parent.getContext().getSharedPreferences(PREF_NAME, 0);
             	   SharedPreferences.Editor editor = settings.edit();
             	    editor.putString("Object", bhej);
             	editor.commit();*/
                 
				
             	
             	ParseQuery<ParseObject> query = ParseQuery.getQuery("Created_R");
                    query.whereEqualTo("R_id", rid);
                    query.getFirstInBackground(new GetCallback<ParseObject>() {
                          public void done(ParseObject object, ParseException e) {
                            if (object != null) 
                            {
                                objjid=object.getObjectId().toString();                 
                                //Toast.makeText(RideCreated.this, "Objid: " +objectId2, Toast.LENGTH_SHORT).show();

                                ParseQuery<ParseObject> query2 = ParseQuery.getQuery("Created_R");
                                query2.getInBackground(objjid, new GetCallback<ParseObject>() {
                                  public void done(ParseObject object, ParseException e) {
                                   
                                 if (e == null) {
                                      // object will be your game score
                                    capa=Integer.toString(cap);
                                    Log.d("Capacity",capa);

outerloop:
                                    for(int x=0;x<cap;x++)
                                        {
                                            final String colname= "add"+(x+1);
                                            final String timepre= "timepref"+(x+1);
                                            String col=object.getString(colname);                                       
                                            
                                            
                    if(col.equals("Available"))
                    {
                   
                    flag=1;
                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Created_R"); 
                    query.getInBackground(objjid, new GetCallback<ParseObject>() {
                    public void done(ParseObject update, ParseException e) {
                       if (e == null) 
                       {
                         update.put(colname,ParseUser.getCurrentUser().getString("username"));
                         update.put(timepre,timepref);
                         update.saveInBackground();
                         ParseQuery<ParseObject> vuery = ParseQuery.getQuery("Rides_Created"); 
                    vuery.getInBackground(bhej, new GetCallback<ParseObject>() {
                     public void done(ParseObject update, ParseException e) {
                       if (e == null) 
                       {
                       	update.increment("No_of_add_pass",-1);  
                       	update.saveInBackground();
                       }
                     }
                    });
                    
                       
                    
                   /* FileOutputStream fos;
                 	try {
                 	fos = parent.getContext().openFileOutput(file, Context.MODE_PRIVATE);
                 	fos.write(sj);
                 	fos.close();
                 	} catch (FileNotFoundException e1) {
                 	// TODO Auto-generated catch block
                 	e1.printStackTrace();
                 	} catch (IOException e1) {
                 	// TODO Auto-generated catch block
                 	e.printStackTrace();
                 	}*/
                    Editor editor2 = preferences.edit();
    				editor2.putInt("sjValue", sj);
    				editor2.commit();

    				preferences = parent.getContext().getSharedPreferences(
    						"MyRideID", Context.MODE_PRIVATE);
    				editor2 = preferences.edit();
    				editor2.putString("RID", rid);
    				editor2.commit();
                    
                    Intent intent = new Intent(parent.getContext(), RideActivity.class );
                              intent.putExtra("String",bhej);
                             
                              parent.getContext().startActivity(intent);
                              MainActivity.fa.finish();
                              //((Activity)context).finish();                         
                       }
                     }
                    });
                   
                    break outerloop;	
                    }
                   
                    }
                                    if(flag==0)
                                    {
                                    	pd.dismiss();
                                    Toast.makeText(parent.getContext(), "Sorry all seats were taken!! :(", Toast.LENGTH_LONG).show();
                                    }
                                    else
                                    {
                                		ParseInstallation.getCurrentInstallation().saveInBackground();

                                    	ParsePush push = new ParsePush();
                                        push.setChannel(rid);
                                        push.setMessage(ParseUser.getCurrentUser().getString("Name") +" joined the ride");
                                        push.sendInBackground();
                                        PushService.subscribe(context, rid, SearchResults.class); 
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
             	
       
       
                 }
             });	
       
       
          	
                 
            
            
         rowView.setOnClickListener(new OnClickListener() {         
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+objid.get(position), Toast.LENGTH_LONG).show();
            }
        });  
        return rowView;
    }


 
}