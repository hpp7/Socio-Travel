package com.cockpunchers.commutator2;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

public class DP extends Activity {
	private ImageView mImageView;
	static Bitmap bitmap1;
	private byte[] image;
	String uname,gender,age,fullname,uemail;
	TextView name,gen,ag,email,username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_profile);
		mImageView = (ImageView) findViewById(R.id.vppic);
		name=(TextView)findViewById(R.id.vpname);
		gen=(TextView)findViewById(R.id.vpgender);
		ag=(TextView)findViewById(R.id.vpage);
		email=(TextView)findViewById(R.id.vpemail);
		username=(TextView)findViewById(R.id.vpuname);
		uname= ParseUser.getCurrentUser().getString("username");
		
	   
	    ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
		query.whereEqualTo("Username", uname);                                    
		query.getFirstInBackground(new GetCallback<ParseObject>() {
			  public void done(ParseObject object, ParseException e) {
			    if (object != null) 
			    {
			    	 gender=object.getString("Gender");
				     age=object.getString("Age");
				     fullname=object.getString("Name");
				     uemail= object.getString("Email");
				     
				      
				      name.setText(fullname);
                     gen.setText(gender);
                     ag.setText(age);
                     username.setText(uname);
                     email.setText(uemail);
                     ParseFile fileObject = (ParseFile)object.get("Profile_Pic");
                     fileObject.getDataInBackground(new GetDataCallback() {
                       public void done(byte[] data, ParseException e) {
                         if (e == null) {
                           Log.d("Profile", "DP received");
                           // use data for something
                           Bitmap bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                           //mImageView.setImageBitmap(bmp);
                           //Bitmap bm = BitmapFactory.decodeResource(getResources(),
                   	         //   R.drawable.default_medium_profile_pic); 
                           Bitmap resized = Bitmap.createScaledBitmap(bmp, 200, 200, true);
	    Bitmap conv_bm = getRoundedRectBitmap(resized, 100);
	    mImageView.setImageBitmap(conv_bm);

                         } else {
                           Log.d("Profile", "There was a problem downloading the data.");
                         }
                       }
                     });
                     
				    }
				    
				    else 
				    {
				      Log.d("Profile", "didnt Retrieved the object.");
				    }
				  }
				});
			    
        
		
	}
	public static Bitmap getRoundedRectBitmap(Bitmap bitmap, int pixels) {
	    Bitmap result = null;
	    try {
	        result = Bitmap.createBitmap(200, 200, Bitmap.Config.ARGB_8888);
	        Canvas canvas = new Canvas(result);

	        int color = 0xff424242;
	        Paint paint = new Paint();
	        Rect rect = new Rect(0, 0, 200, 200);

	        paint.setAntiAlias(true);
	        canvas.drawARGB(0, 0, 0, 0);
	        paint.setColor(color);
	        canvas.drawCircle(100, 100, 90, paint);
	        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	        canvas.drawBitmap(bitmap, rect, rect, paint);

	    } catch (NullPointerException e) {
	    } catch (OutOfMemoryError o) {
	    }
	    return result;
	}
	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    Intent in =  new Intent(DP.this,ProfileSettings.class);
    	startActivity(in);
	    finish();

	}

}
