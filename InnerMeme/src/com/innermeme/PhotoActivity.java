package com.innermeme;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import com.radiumone.effects_sdk.R1PhotoEffectsSDK;

public class PhotoActivity extends FragmentActivity {
	
	private final String TAG = PhotoActivity.class.getSimpleName();
	
	CameraPreview mPreview;
	Camera mCamera;
	boolean mWaitingForGlobalLayout = true;
	
	private static PhotoActivity instance;
	
	public static Context getContext(){
		return instance;
	}
	
	public static PhotoActivity getInstance(){
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.photo_main);
		
		Screen.setHeight(getResources().getDisplayMetrics().heightPixels);
        Screen.setWidth(getResources().getDisplayMetrics().widthPixels);
		
//        R1PhotoEffectsSDK.getManager().enable(getApplicationContext(), "3d2848e0-77a5-0130-64de-22000ac40812", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkPXxk7BIWriqNE1jRjay4jfioYsVolOJvKoHuyW7svaYCASwCBV4KxQCsvz50UTVLdmddnTlixI5VjXt+3Va6YV2GQf1rT3geuxLen5HpVwtq7bROd9Z9iAvDrqseNWb+iCGnIUrt6k2/9FOESSagTc4/zKcxrprR/zayucJ/iDDPRRrErbZIB8PWi+XT+k04PAiEp0VIr5t37EyTk55mqst+kbxBRLRLMmhWrrVxH+ff7RPFGVaR1z3X5CnTj5mtMb94RS3AzRFAs7gXHqfrzogYrHl8m6sgT+bmSD6V8kYov49cAzRrpE0Enlcr7MGsi7l3NMc4xkjNakEiHsUjwIDAQAB", R1PhotoEffectsSDK.CROP_MODE_ALL);

		// Create an instance of Camera
        mCamera = getCameraInstance();

     // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        
        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(
            new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // get an image from the camera
                    mCamera.takePicture(null, null, mPicture);
                }
            }
        );
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo, menu);
		return true;
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
	
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
	    	
//	    	Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);
//	    	
//	    	Log.d("Bitmap", "Bitmap: "+bitmap);
	    	
//	    	R1PhotoEffectsSDK r1sdk = R1PhotoEffectsSDK.getManager();
//	    	  r1sdk.launchPhotoEffects(PhotoActivity.getContext(), bitmap, true,
//	    	          new R1PhotoEffectsSDK.PhotoEffectsListener() {            
//	    	                  @Override
//	    	                  public void onEffectsComplete(Bitmap output) {
//	    	                          if( null == output ){
//	    	                                  return;
//	    	                          }
//	    	                          // do something with output   
//	    	                  }
//	    	                  
//	    	                  @Override
//	    	                  public void onEffectsCanceled() {
//	    	                          // user canceled                  
//	    	                  }
//	    	          } 
//	    	  );

//	        File pictureFile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
//	        if (pictureFile == null){
//	            Log.d(TAG, "Error creating media file, check storage permissions: " +
//	                e.getMessage());
//	            return;
//	        }
//
//	        try {
//	            FileOutputStream fos = new FileOutputStream(pictureFile);
//	            fos.write(data);
//	            fos.close();
//	        } catch (FileNotFoundException e) {
//	            Log.d(TAG, "File not found: " + e.getMessage());
//	        } catch (IOException e) {
//	            Log.d(TAG, "Error accessing file: " + e.getMessage());
//	        }
	    }
	};

}
