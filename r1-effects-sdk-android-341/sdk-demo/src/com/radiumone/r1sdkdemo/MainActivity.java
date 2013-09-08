package com.radiumone.r1sdkdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.radiumone.effects_sdk.R1PhotoEffectsSDK;

public class MainActivity extends Activity {
	// Declare gallery activity result variable
	public static final int REQUEST_GALLERY_SELECT = 111;
	// Declare an image view for the final image
	private ImageView mFinalImageView;

	// Declare a button for launching SDK
	private Button mLaunchSDKButton;
	
	private R1PhotoEffectsSDK r1sdk;

    private final String TAG = MainActivity.class.getSimpleName();
	
	CameraPreview mPreview;
	Camera mCamera;
	boolean mWaitingForGlobalLayout = true;
	
	private static MainActivity instance;
	
	public static Context getContext(){
		return instance;
	}
	
	public static MainActivity getInstance(){
		return instance;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Screen.setHeight(getResources().getDisplayMetrics().heightPixels);
        Screen.setWidth(getResources().getDisplayMetrics().widthPixels);

		// R1 SDK initialization
		r1sdk = R1PhotoEffectsSDK.getManager();
		// call enable method and pass app token, billing license key and cropmode.

		r1sdk.enable(getApplicationContext(), "3d2848e0-77a5-0130-64de-22000ac40812", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkPXxk7BIWriqNE1jRjay4jfioYsVolOJvKoHuyW7svaYCASwCBV4KxQCsvz50UTVLdmddnTlixI5VjXt+3Va6YV2GQf1rT3geuxLen5HpVwtq7bROd9Z9iAvDrqseNWb+iCGnIUrt6k2/9FOESSagTc4/zKcxrprR/zayucJ/iDDPRRrErbZIB8PWi+XT+k04PAiEp0VIr5t37EyTk55mqst+kbxBRLRLMmhWrrVxH+ff7RPFGVaR1z3X5CnTj5mtMb94RS3AzRFAs7gXHqfrzogYrHl8m6sgT+bmSD6V8kYov49cAzRrpE0Enlcr7MGsi7l3NMc4xkjNakEiHsUjwIDAQAB", R1PhotoEffectsSDK.CROP_MODE_ALL);

		// Assign image view
		mFinalImageView = (ImageView) findViewById(R.id.dm_final_image);  

		// Create an instance of Camera
        mCamera = getCameraInstance();

     // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
		
		// Assign button and add an event listener
		mLaunchSDKButton = (Button) findViewById(R.id.dm_launch_sdk_button);
		mLaunchSDKButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Open device gallery
//				startSelectPicture(); 
				mCamera.takePicture(null, null, mPicture);
			}
		});    
	}
	// Create method that sends user to the device media gallery
	// You could also send the user to the camera
	protected void startSelectPicture() {

		// Declare and assign an intent for the media gallery
		Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");

		// Send the user to the device media gallery
		startActivityForResult(intent, REQUEST_GALLERY_SELECT);
	}

	// Create method that handles activity results
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Handle activities accordingly
		switch (requestCode) {

		// Handle the image returned from the device media gallery
		case REQUEST_GALLERY_SELECT:

			// Check for error
			if (resultCode == RESULT_OK) {

				// Declare and assign URI to selected device media gallery image
				String  filePath = getPath(data.getData());
				// Highly recommended to use this method to avoid memory issue while selecting the image.
				Bitmap imageBitmap = r1sdk.getScaledBitmap(filePath);
				
				r1sdk.launchPhotoEffects(

						getApplicationContext(),
						imageBitmap,
						true, // Allow user to crop first?
						new R1PhotoEffectsSDK.PhotoEffectsListener() {

							@Override
							public void onEffectsComplete(Bitmap output) {
								if( null == output ){
									return;
								}
								mFinalImageView.setImageBitmap(output);
							}

							@Override
							public void onEffectsCanceled() {

							}
						}); 
			}
			break;

		}
	} 
	
	
	// Get the Selected image path from the uri
	public String getPath(Uri selectedImageUri){

		String[] filePathColumn = {MediaStore.Images.Media.DATA};

		Cursor cursor = getContentResolver().query(
				selectedImageUri, filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();
		return filePath;
	}
	
	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
	    Camera c = null;
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}
	
	private ShutterCallback mShutter = new ShutterCallback(){

		@Override
		public void onShutter() {
			// TODO Auto-generated method stub
			
		}
		
	};
	
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
	    		    	
	    	camera.startPreview();
	    	
	    	Log.d(TAG, "data: "+data);
	    	
	    	Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);
	    	
	    	Log.d(TAG, "bitmap: "+bitmap);
	    	
	    	Log.d(TAG, "sdk: "+r1sdk);
	    		    	
	    	R1PhotoEffectsSDK r1sdk = R1PhotoEffectsSDK.getManager();
	    	  r1sdk.launchPhotoEffects(getApplicationContext(), bitmap, true,
	    	          new R1PhotoEffectsSDK.PhotoEffectsListener() {            
	    	                  @Override
	    	                  public void onEffectsComplete(Bitmap output) {
	    	                          if( null == output ){
	    	                                  return;
	    	                          }
	    	                          // do something with output   
	    	                  }
	    	                  
	    	                  @Override
	    	                  public void onEffectsCanceled() {
	    	                          // user canceled                  
	    	                  }
	    	          } 
	    	  );
	    }
	};
}
