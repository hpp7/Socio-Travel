package com.cockpunchers.commutator2;

import java.util.ArrayList;

import com.cockpunchers.commutator2.CustomAdapter.Holder;

import android.content.Context;
import android.database.DataSetObserver;
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

public class CustomAdapter2 extends BaseAdapter {

	ArrayList<ArrayList<Object>> dbrow;
    LayoutInflater inflater=null;
    
    int j;
    String no,source,destination,vehicle,cap,time,timet;
    
	public CustomAdapter2(Context context,
			ArrayList<ArrayList<Object>> row) {
		// TODO Auto-generated constructor stub
		dbrow = row;
        Context mycontext = context;
        
         inflater = ( LayoutInflater )mycontext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return dbrow.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return dbrow.get(position);
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
    TextView n, sou, des, passcap, time;
    ImageView vehcar,vehauto,vehtaxi;
    Button join;
    int position;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		final Holder holder= new Holder();
        RelativeLayout rowView; 
        
        	if (convertView == null) 
        {
        	rowView = (RelativeLayout) inflater.inflate(R.layout.historydisplay, parent, false);       	

        } 
        else {
        	rowView = (RelativeLayout) convertView;
        	}
        
            // rowView = inflater.inflate(R.layout.searchdisplay, null);
     		Log.d("Rows", " Number = " +dbrow.size());

     		j=position;
     		//for(i=position ;i<(dbrow.size()); i++)
            //{
            	 Log.d("Test", dbrow.get(j).toString());
            	 
            		 //Log.d("Inside", "reached here nigga");
            		    holder.join = (Button)rowView.findViewById(R.id.join);
            		    holder.n = (TextView) rowView.findViewById(R.id.resname); 
            		                holder.sou = (TextView) rowView.findViewById(R.id.searessou);
            		                holder.des = (TextView) rowView.findViewById(R.id.searesdes);
            		                holder.passcap = (TextView) rowView.findViewById(R.id.seapass);    
            		                holder.vehcar = (ImageView) rowView.findViewById(R.id.motimg);
            		                holder.vehauto = (ImageView) rowView.findViewById(R.id.motimg1);
            		                holder.vehtaxi = (ImageView) rowView.findViewById(R.id.motimg2);
            		                holder.time = (TextView) rowView.findViewById(R.id.searchrestime);
            		                
            		                no=Integer.toString(j+1);
            		                source=dbrow.get(j).get(1).toString();
            		                destination=dbrow.get(j).get(2).toString();
            		                vehicle=dbrow.get(j).get(5).toString();
            		                cap=dbrow.get(j).get(4).toString();
            		                time=dbrow.get(j).get(3).toString();
            		                timet=dbrow.get(j).get(6).toString();
            		                
            		                holder.n.setText("#"+no);
            		                holder.sou.setText(source);
            		                holder.des.setText(destination);
            		                holder.passcap.setText(cap);
            		                holder.time.setText(time + " ± " + timet + " mins");
            		                if(vehicle.equals("Car"))
            		                holder.vehcar.setVisibility(View.VISIBLE);
            		                if(vehicle.equals("AutoRickshaw"))
            		                holder.vehauto.setVisibility(View.VISIBLE);
            		                if(vehicle.equals("Taxi"))
            		                holder.vehtaxi.setVisibility(View.VISIBLE);
            	//	               break;
            		
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
