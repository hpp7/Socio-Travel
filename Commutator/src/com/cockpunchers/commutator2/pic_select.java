package com.cockpunchers.commutator2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

public class pic_select extends Activity {

	private Uri mImageCaptureUri;
	private ImageView mImageView;
	private AlertDialog dialog;
	static Bitmap bitmap;
	OutputStream output;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_select);
		
        
        Parse.initialize(this, "u3g5xX9gTG1DRbGVa6iMnpxNn2kVL20l2jI1tCar", "Vj5rdncQ6G7IEIgnbDrPUt2ZOtuH6WA1YFucLWmt");
        captureImageInitialization();
        Button set = (Button) findViewById(R.id.set);
		Button button = (Button) findViewById(R.id.SelectImageBtn);
		mImageView = (ImageView) findViewById(R.id.ProfilePicIV);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});

		
		mImageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});

	
	set.setOnClickListener(new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			
			if(bitmap!=null)
			{
            // Convert it to byte
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            // Compress image to lower quality scale 1 - 100
            bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] image = stream.toByteArray();
			}
			
			if(bitmap==null)
			{
				bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.default_medium_profile_pic);
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
		        // Compress image to lower quality scale 1 - 100
		        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
		        byte[] image = stream.toByteArray();
			}
           /* ParseFile file = new ParseFile("profpic.png", image);
            // Upload the image into Parse Cloud
          file.saveInBackground();
          
          ParseUser profile = new ParseUser();
  		profile.put("Profile_Pic", file);
  		profile.saveInBackground(new SaveCallback() {
			  public void done(ParseException e) {
				    if (e == null) {
				      // Success!
				      
				    } else {
				      // Failure!
				    }
				    return;
				  }
			
				});
  		
*/  		
            // Create a New Class called "ImageUpload" in Parse
         //   ParseObject imgupload = new ParseObject("_User");
          //     ParseObject user = new ParseObject("_User");
          //  ParseUser user = new ParseUser();
            //  "ImageName" and set the string
         //   imgupload.put("ImageName", "Profile_pic");

            //  "ImageFile" and insert the image
          //  user.put("Profile_Pic", file);

            // Create the class and the columns
            //user.saveInBackground();

            // Show a simple toast message
            Toast.makeText(pic_select.this, "Image Set",
                    Toast.LENGTH_SHORT).show();
			
			
			
			Intent intent = new Intent(pic_select.this, SignUp.class);
			//intent.putExtra("Image", image);
            startActivity(intent);    
            finish();
		}
	});

}

	private void captureImageInitialization() {
		/**
		 * a selector dialog to display two image source options, from camera
		 * ‘Take from camera’ and from existing files ‘Select from gallery’
		 */
		final String[] items = new String[] { "Take from camera",
				"Select from gallery" };
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.select_dialog_item, items);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) { // pick from
																	// camera
				if (item == 0) {
					/**
					 * To take a photo from camera, pass intent action
					 * ‘MediaStore.ACTION_IMAGE_CAPTURE‘ to open the camera app.
					 */
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					/**
					 * Also specify the Uri to save the image on specified path
					 * and file name. Note that this Uri variable also used by
					 * gallery app to hold the selected image path.
					 */
					mImageCaptureUri = Uri.fromFile(new File(Environment
							.getExternalStorageDirectory(), "tmp_avatar_"
							+ String.valueOf(System.currentTimeMillis())
							+ ".jpg"));

					intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
							mImageCaptureUri);

					try {
						intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else {
					// pick from file
					/**
					 * To select an image from existing files, use
					 * Intent.createChooser to open image chooser. Android will
					 * automatically display a list of supported applications,
					 * such as image gallery or file manager.
					 */
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent,
							"Complete action using"), PICK_FROM_FILE);
				}
			}
		});

		dialog = builder.create();
	}

	public class CropOptionAdapter extends ArrayAdapter<CropOption> {
		private ArrayList<CropOption> mOptions;
		private LayoutInflater mInflater;

		public CropOptionAdapter(Context context, ArrayList<CropOption> options) {
			super(context, R.layout.crop_selector, options);

			mOptions = options;

			mInflater = LayoutInflater.from(context);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup group) {
			if (convertView == null)
				convertView = mInflater.inflate(R.layout.crop_selector, null);

			CropOption item = mOptions.get(position);

			if (item != null) {
				((ImageView) convertView.findViewById(R.id.iv_icon))
						.setImageDrawable(item.icon);
				((TextView) convertView.findViewById(R.id.tv_name))
						.setText(item.title);

				return convertView;
			}

			return null;
		}
	}

	public class CropOption {
		public CharSequence title;
		public Drawable icon;
		public Intent appIntent;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK)
			return;

		switch (requestCode) {
		case PICK_FROM_CAMERA:
			/**
			 * After taking a picture, do the crop
			 */
			doCrop();

			break;

		case PICK_FROM_FILE:
			/**
			 * After selecting image from files, save the selected path
			 */
			mImageCaptureUri = data.getData();

			doCrop();

			break;

		case CROP_FROM_CAMERA:
			Bundle extras = data.getExtras();
			/**
			 * After cropping the image, get the bitmap of the cropped image and
			 * display it on imageview.
			 */
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
               bitmap=extras.getParcelable("data");
				mImageView.setImageBitmap(photo);
			}

			// Find the SD Card path
			File filepath = Environment.getExternalStorageDirectory();

			// Create a new folder in SD Card
			File dir = new File(filepath.getAbsolutePath()
					+ "/yo/");
			dir.mkdirs();

			// Create a name for the saved image
			File file = new File(dir, "myimage.png");

			// Show a toast message on successful save
			Toast.makeText(pic_select.this, "Set As Profile Picture",
					Toast.LENGTH_SHORT).show();
			try {

				output = new FileOutputStream(file);

				// Compress into png format image from 0% - 100%
				bitmap.compress(Bitmap.CompressFormat.PNG, 50, output);
				output.flush();
				output.close();
			}

			catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			File f = new File(mImageCaptureUri.getPath());
			/**
			 * Delete the temporary image
			 */
			if (f.exists())
				f.delete();

			break;

		}
	}

	private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();
		/**
		 * Open image crop app by starting an intent
		 * ‘com.android.camera.action.CROP‘.
		 */
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		/**
		 * Check if there is image cropper app installed.
		 */
		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);

		int size = list.size();

		/**
		 * If there is no image cropper app, display warning message
		 */
		if (size == 0) {

			Toast.makeText(this, "Can not find image crop app",
					Toast.LENGTH_SHORT).show();

			return;
		} else {
			/**
			 * Specify the image path, crop dimension and scale
			 */
			intent.setData(mImageCaptureUri);

			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);
			/**
			 * There is posibility when more than one image cropper app exist,
			 * so we have to check for it first. If there is only one app, open
			 * then app.
			 */

			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));

				startActivityForResult(i, CROP_FROM_CAMERA);
			} else {
				/**
				 * If there are several app exist, create a custom chooser to
				 * let user selects the app.
				 */
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title = getPackageManager().getApplicationLabel(
							res.activityInfo.applicationInfo);
					co.icon = getPackageManager().getApplicationIcon(
							res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);

					co.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(
						getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose Crop App");
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								startActivityForResult(
										cropOptions.get(item).appIntent,
										CROP_FROM_CAMERA);
							}
						});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

						if (mImageCaptureUri != null) {
							getContentResolver().delete(mImageCaptureUri, null,
									null);
							mImageCaptureUri = null;
						}
					}
				});

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}
	
	

}
