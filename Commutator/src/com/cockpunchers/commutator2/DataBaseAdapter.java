package com.cockpunchers.commutator2;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

public class DataBaseAdapter 
{
		static final String DATABASE_NAME = "RidesCreated.db";
		static final int DATABASE_VERSION = 1;
		public static final int NAME_COLUMN = 1;
		// TODO: Create public field for each column in your table.
		// SQL Statement to create a new database.
		static final String DATABASE_CREATE = "create table "+"RIDES"+
		                             "( " +"ID"+" integer primary key autoincrement not null,"+ "Source text,Destination text,Time text,No_of_add_pass text,Vehicle_type text,Time_tolerance text); ";
		// Variable to hold the database instance
		public  SQLiteDatabase db;
		// Context of the application using the database.
		private final Context context;
		// Database open/upgrade helper
		private DataBaseHelper dbHelper;
		public  DataBaseAdapter(Context _context) 
		{
			context = _context;
			dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		public  DataBaseAdapter open() throws SQLException 
		{
			db = dbHelper.getWritableDatabase();
			return this;
		}
		public void close() 
		{
			db.close();
		}

		public  SQLiteDatabase getDatabaseInstance()
		{
			return db;
		}

		public  void insertEntry(String s,String d,String tim,String n,String veh,String t)
		{
	       ContentValues newValues = new ContentValues();
			// Assign values for each row.
	        newValues.put("Source", s); 
			newValues.put("Destination", d); 
			newValues.put("Time_tolerance", tim); 
			newValues.put("No_of_add_pass", n); 
			newValues.put("Vehicle_type", veh); 
			newValues.put("Time", t); 
			// Insert the row into your table
			db.insert("RIDES", null, newValues);
			//Toast.makeText(context, "Reminder Is Successfully Saved", Toast.LENGTH_LONG).show();
			close();
		}
		
		public void  updateEntry(String s,String d,String tim,String n,String veh,String t)
		{
			// Define the updated row content.
			ContentValues updatedValues = new ContentValues();
			// Assign values for each row.
			updatedValues.put("Source", s); 
			updatedValues.put("Destination", d); 
			updatedValues.put("Time_tolerance", tim); 
			updatedValues.put("No_of_add_pass", n); 
			updatedValues.put("Vehicle_type", veh); 
			updatedValues.put("Time", t); 
			close();
	        			   
		}		
		
		
		public ArrayList<ArrayList<Object>> getAllRowsAsArrays()
		{
			// create an ArrayList that will hold all of the data collected from
			// the database.
			ArrayList<ArrayList<Object>> dataArrays =
				new ArrayList<ArrayList<Object>>();
		 
			// this is a database call that creates a "cursor" object.
			// the cursor object store the information collected from the
			// database and is used to iterate through the data.
			Cursor cursor;
		 
			try
			{
				// ask the database object to create the cursor.
				/*cursor = db.query(
						"RIDES",
						new String[]{"ID", "Source", "Destination","Time","No_of_add_pass","Vehicle_type","Time_tolerance"},
						null, null, null, null, null
				);
				
*/
			    cursor = db.rawQuery("SELECT * FROM RIDES",new String [] {});
		 
				// move the cursor's pointer to position zero.
				cursor.moveToFirst();

				Log.e("Ojb retreived", "no: " + cursor.getCount());
		 
				// if there is data after the current cursor position, add it
				// to the ArrayList.
				if (!cursor.isAfterLast())
				{
					do
					{
						ArrayList<Object> dataList = new ArrayList<Object>();
		 
						dataList.add(cursor.getLong(0));
						dataList.add(cursor.getString(1));
						dataList.add(cursor.getString(2));
						dataList.add(cursor.getString(3));
						dataList.add(cursor.getString(4));
						dataList.add(cursor.getString(5));
						dataList.add(cursor.getString(6));
		 
						dataArrays.add(dataList);
					}
					// move the cursor's pointer up one position.
					while (cursor.moveToNext());
				}
				Log.e("Ojb retreived", "no: " + cursor.getCount());
				db.close();
			}
			catch (SQLException e)
			{
				Log.e("DB Error", e.toString());
				e.printStackTrace();
			}
		 
			// return the ArrayList that holds the data collected from
			// the database.
			return dataArrays;
		}
		 
		public void delete()
		{
			db.execSQL("delete from RIDES");
			Toast.makeText(context, "Done deleting!", Toast.LENGTH_LONG).show();
			close();
		}


}



