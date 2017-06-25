package com.cockpunchers.commutator2;

import java.util.ArrayList;
import java.util.List;

import com.cockpunchers.commutator2.CustomAdapter.Holder;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CustomAdapter3 extends BaseAdapter {

    ArrayList<ArrayList<Object>> dbrow;
    LayoutInflater inflater=null;
    String objectId,objectId2,rc_rid;
    int rc_add,rc_ogadd;
    ArrayList<String> unames = new ArrayList<String>();
    ArrayList<String> tprefs = new ArrayList<String>();
    
    int j;
    String uname,age,fullname,gender,tpref;
    int no;
    
   
   
	public CustomAdapter3(FragmentActivity activity, ArrayList<String> unames2, ArrayList<String> tprefs2) 
	{
		Context mycontext = activity;
		unames=unames2;
		tprefs=tprefs2;
		Log.d("MOFO", "Getting closer");
		inflater = ( LayoutInflater )mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}


	@Override
    public int getCount() {
        // TODO Auto-generated method stub
        return unames.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return unames.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    /*@Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
*/
    class Holder
    {
    TextView n, name, gen, ag , tp;
    ImageView prof;
    int position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) 
    {
        // TODO Auto-generated method stub
    	final Holder holder= new Holder();
        final View rowView; 
        
         
             rowView = inflater.inflate(R.layout.commuter_details, null);
        
            // rowView = inflater.inflate(R.layout.searchdisplay, null);
             //Log.d("Rows", " Number = " +dbrow.size());

             //j=position;
             //for(i=position ;i<(dbrow.size()); i++)
            //{
                 //Log.d("Test", dbrow.get(j).toString());
                 
                     
                        //holder.join = (Button)rowView.findViewById(R.id.join);
                        //holder.n = (TextView) rowView.findViewById(R.id.Riderno); 
                        holder.name= (TextView) rowView.findViewById(R.id.Ridername);
                        holder.ag= (TextView) rowView.findViewById(R.id.Riderage);
                        holder.gen= (TextView) rowView.findViewById(R.id.Ridergender);
                        holder.tp= (TextView) rowView.findViewById(R.id.Ridertp);
                        holder.prof= (ImageView) rowView.findViewById(R.id.pic);
                        
                                    //holder.sou = (TextView) rowView.findViewById(R.id.searessou);
                                    //holder.des = (TextView) rowView.findViewById(R.id.searesdes);
                                   // holder.passcap = (TextView) rowView.findViewById(R.id.seapass);    
                                   // holder.vehcar = (ImageView) rowView.findViewById(R.id.motimg);
                                   // holder.vehauto = (ImageView) rowView.findViewById(R.id.motimg1);
                                   // holder.vehtaxi = (ImageView) rowView.findViewById(R.id.motimg2);
                                   // holder.time = (TextView) rowView.findViewById(R.id.searchrestime);
                                    Log.d("HERE", "Neva give up -alfred");
                                   // no=position+1;
                                    uname=unames.get(position).toString();
                                    tpref=tprefs.get(position).toString();
                                    

                                    holder.tp.setText("Time Prefernce: " + tpref);
                                    
                                    ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
                        			query.whereEqualTo("Username", uname);                                    
                        			query.getFirstInBackground(new GetCallback<ParseObject>() {
                        				  public void done(ParseObject object, ParseException e) {
                        				    if (object != null) 
                        				    {
                        				      gender=object.getString("Gender");
                        				      age=object.getString("Age");
                        				      fullname=object.getString("Name");
                        				      
                        				      
                        				      holder.name.setText(fullname);
                                              holder.gen.setText(gender);
                                              holder.ag.setText(age);
                                              
                                              ParseFile fileObject = (ParseFile)object.get("Profile_Pic");
                                              fileObject.getDataInBackground(new GetDataCallback() {
                                                public void done(byte[] data, ParseException e) {
                                                  if (e == null) {
                                                    Log.d("test", "We've got data in data.");
                                                    // use data for something
                                                    Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                                                    holder.prof.setImageBitmap(bmp);

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
                        				
                                    //source=dbrow.get(j).get(1).toString();
                                    //destination=dbrow.get(j).get(2).toString();
                                    //vehicle=dbrow.get(j).get(5).toString();
                                    //cap=dbrow.get(j).get(4).toString();
                                    //time=dbrow.get(j).get(3).toString();
                                    //timet=dbrow.get(j).get(6).toString();
                                    
                                    //holder.n.setText(position+1);
                                   
                                    
                                    
                                    //holder.sou.setText(source);
                                    //holder.des.setText(destination);
                                    //holder.passcap.setText(cap);
                                    //holder.time.setText(time + " ± " + timet + " mins");
                                    //if(vehicle.equals("Car"))
                                    //holder.vehcar.setVisibility(View.VISIBLE);
                                    //if(vehicle.equals("AutoRickshaw"))
                                    //holder.vehauto.setVisibility(View.VISIBLE);
                                    //if(vehicle.equals("Taxi"))
                                    //holder.vehtaxi.setVisibility(View.VISIBLE);
                //                   break;
                    
           //  }
            return rowView;
    }
    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }

    /*@Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return dbrow.size();
    }

    

    @Override
    public boolean isEmpty() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean areAllItemsEnabled() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        // TODO Auto-generated method stub
        return false;
    }*/

}