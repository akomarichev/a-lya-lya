package vk.chat;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import org.json.JSONException;

import vk.api.API;
import vk.constants.Constants;
import vk.pref.Pref;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ChangePhotoActivity extends Activity {
	
	private final String TAG = "ChangePhotoActivity";
	
	Button b;
	ImageView iv;
	API api;
	private final static int TAKE_PICTURE = 1;
	private Uri mImageCaptureUri;	

	private static final int PICK_FROM_CAMERA = 1;
	private static final int PICK_FROM_FILE = 2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.change_photo);
	    
	    api = new API(Pref.getAccessTokenHTTPS(ChangePhotoActivity.this));
	    
	    b = (Button) findViewById(R.id.to_change);
	    iv = (ImageView) findViewById(R.id.ava);
	    
        final String [] items			= new String [] {"From Camera", "From SD Card"};				
		ArrayAdapter<String> adapter	= new ArrayAdapter<String> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) {
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					File file		 = new File(Environment.getExternalStorageDirectory(),
							   			"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg");
					mImageCaptureUri = Uri.fromFile(file);

					try {			
						intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
						intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (Exception e) {
						e.printStackTrace();
					}			

					dialog.cancel();
				} else {
					Intent intent = new Intent();

	                intent.setType("image/*");
	                intent.setAction(Intent.ACTION_GET_CONTENT);

	                startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );

		final AlertDialog dialog = builder.create();


		b.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				dialog.show();
			}
		});
    }
    
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode != RESULT_OK) return;

		Bitmap bitmap 	= null;
		String path		= "";

		if (requestCode == PICK_FROM_FILE) {
			mImageCaptureUri = data.getData(); 
			path = getRealPathFromURI(mImageCaptureUri); //from Gallery 

			if (path == null)
				path = mImageCaptureUri.getPath(); //from File Manager

			if (path != null) 
				bitmap 	= BitmapFactory.decodeFile(path);
		} else {
			path	= mImageCaptureUri.getPath();
			bitmap  = BitmapFactory.decodeFile(path);
		}

		iv.setImageBitmap(bitmap);	
		String url = null;
		String res[];
		String res2[];
		try {
			url = api.photosGetProfileUploadServer();
			Log.d("1", mImageCaptureUri.toString());
			Log.d("2", mImageCaptureUri.getPath());
			res = api.uploadPhotoServer(url, getRealPathFromURI(mImageCaptureUri));
			Log.d("ChangePhotoActivity", res[0]);
			Log.d("ChangePhotoActivity", res[1]);
			Log.d("ChangePhotoActivity", res[2]);
			res2 = api.saveProfilePhoto(res[0],res[1],res[2]);
			Log.d("ChangePhotoActivity", res2[0]);
			Log.d("ChangePhotoActivity", res2[1]);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		Toast.makeText(getApplicationContext(),
		"path: " + url, Toast.LENGTH_LONG)
		.show();
	}

	public String getRealPathFromURI(Uri contentUri) {
        String [] proj 		= {MediaStore.Images.Media.DATA};
        Cursor cursor 		= managedQuery( contentUri, proj, null, null,null);
        
        if (cursor == null) return null;
        
        int column_index 	= cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        
        cursor.moveToFirst();

        return cursor.getString(column_index);
	}
}
