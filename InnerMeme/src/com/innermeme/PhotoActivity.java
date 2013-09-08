package com.innermeme;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
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
		
        R1PhotoEffectsSDK.getManager().enable(getApplicationContext(), "aee035b0-fa71-0130-5fff-22000afc8c3d",
        		"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkCaoRrklKq+Rorv5Z4Rg6DkEU4/R3q6IlrByQyfPJbrt1m3quSVcdqpEI9ftXIadPtmtvUXE9zH7d4JCWTh8TYlOt0uYcSkyM6uH09hLVqU6sQbLs88sVh2zDkSXlwBBLAdBDhmqcdN4K7Tcgru6tHc7LfNBp0nk2CZy8S6+8dyAx/Xgs8umE+U7/hNTRkEwmzO/cmU+EAsxdgSxBqQqpCX7gbgdXY54pkSicmDwEUl+ICOtWiPHXHbhwSDJdMLuoSQfnBUPbNzRvYjmGc5yM9dYlOQwpdH4IL5Oiwp7hD0ImeIRX0n+WnI2CFSONWOZoIMpafEXR4k7IorWcS4d9wIDAQAB",
        		R1PhotoEffectsSDK.CROP_MODE_ALL);

        Log.d("TAG", "Hardware good? "+checkCameraHardware(getApplicationContext()));
        
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
	    	e.printStackTrace();
	    }
	    Log.d("TAG", "Camera: "+c);
	    
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
	    		    	
//	    	mCamera = camera;
	    	
	    	Bitmap bitmap = BitmapFactory.decodeByteArray(data , 0, data.length);
	    	
	    	Matrix matrix = new Matrix();
	    	matrix.postRotate(90);
	    	Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap , 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

	    		    	
	    	R1PhotoEffectsSDK r1sdk = R1PhotoEffectsSDK.getManager();
	    	  r1sdk.launchPhotoEffects(getApplicationContext(), rotatedBitmap, true,
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
//	    	                	  mCamera.startPreview();
	    	                  }
	    	          } 
	    	  );
	    }
	};

}
