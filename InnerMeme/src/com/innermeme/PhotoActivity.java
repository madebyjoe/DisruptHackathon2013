package com.innermeme;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.radiumone.effects_sdk.R1PhotoEffectsSDK;

public class PhotoActivity extends FragmentActivity {

	private final String TAG = PhotoActivity.class.getSimpleName();

	public static final int REQUEST_GALLERY_SELECT = 111;

	private R1PhotoEffectsSDK r1sdk;
	CameraPreview mPreview;
	Camera mCamera;
	boolean mWaitingForGlobalLayout = true;

	private static PhotoActivity instance;

	public static Context getContext() {
		return instance;
	}

	public static PhotoActivity getInstance() {
		return instance;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.photo_main);

		final View rootView = findViewById(R.id.root_view);
		// final ViewTreeObserver vto = rootView.getViewTreeObserver();
		// vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// if (mWaitingForGlobalLayout) {
		// mWaitingForGlobalLayout = false;
		// Screen.setHeight(rootView.getHeight());
		// Screen.setWidth(rootView.getWidth());
		//
		// mPreview = new CameraPreview(getApplicationContext(), mCamera);
		// FrameLayout preview = (FrameLayout)
		// findViewById(R.id.camera_preview);
		// preview.addView(mPreview);
		// }
		// }
		// });

		// Screen.setHeight(rootView.getHeight());
		// Screen.setWidth(rootView.getWidth());

		// Create an instance of Camera
		mCamera = getCameraInstance();

		Screen.setHeight(getResources().getDisplayMetrics().heightPixels);
		Screen.setWidth(getResources().getDisplayMetrics().widthPixels);

		r1sdk = R1PhotoEffectsSDK.getManager();

		r1sdk.enable(
				getApplicationContext(),
				"aee035b0-fa71-0130-5fff-22000afc8c3d",
				"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkCaoRrklKq+Rorv5Z4Rg6DkEU4/R3q6IlrByQyfPJbrt1m3quSVcdqpEI9ftXIadPtmtvUXE9zH7d4JCWTh8TYlOt0uYcSkyM6uH09hLVqU6sQbLs88sVh2zDkSXlwBBLAdBDhmqcdN4K7Tcgru6tHc7LfNBp0nk2CZy8S6+8dyAx/Xgs8umE+U7/hNTRkEwmzO/cmU+EAsxdgSxBqQqpCX7gbgdXY54pkSicmDwEUl+ICOtWiPHXHbhwSDJdMLuoSQfnBUPbNzRvYjmGc5yM9dYlOQwpdH4IL5Oiwp7hD0ImeIRX0n+WnI2CFSONWOZoIMpafEXR4k7IorWcS4d9wIDAQAB",
				R1PhotoEffectsSDK.CROP_MODE_ALL);

		Log.d("TAG", "Hardware good? "
				+ checkCameraHardware(getApplicationContext()));

		// Create our Preview view and set it as the content of our activity.
		mPreview = new CameraPreview(this, mCamera);
		FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
		preview.addView(mPreview);

		Button memeCaptureButton = (Button) findViewById(R.id.button_capture);
		memeCaptureButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// get an image from the camera
				Log.d("TAG", "take picture: " + mCamera);
				if (mCamera == null) {
					mCamera = getCameraInstance();
					Log.d("TAG", "take picture: " + mCamera);
				}
				mCamera.takePicture(null, null, mPicture);
			}
		});

		Button memeGalleryButton = (Button) findViewById(R.id.button_gallery);
		memeGalleryButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startSelectPicture();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.photo, menu);
		return true;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mCamera == null) {
			mCamera = getCameraInstance();
		}
		Log.d(TAG, "camera resume: " + mCamera);
	}

	@Override
	public void onPause() {
		super.onPause();
		releaseCamera();
	}

	protected void startSelectPicture() {

		// Declare and assign an intent for the media gallery
		Intent intent = new Intent(Intent.ACTION_PICK,
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		intent.setType("image/*");

		// Send the user to the device media gallery
		startActivityForResult(intent, REQUEST_GALLERY_SELECT);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		// Handle activities accordingly
		switch (requestCode) {

		// Handle the image returned from the device media gallery
		case REQUEST_GALLERY_SELECT:

			// Check for error
			if (resultCode == RESULT_OK) {

				// Declare and assign URI to selected device media gallery image
				String filePath = getPath(data.getData());
				// Highly recommended to use this method to avoid memory issue
				// while selecting the image.
				Bitmap bitmap = r1sdk.getScaledBitmap(filePath);

				Matrix matrix = new Matrix();
				matrix.postRotate(90);
				Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
						bitmap.getWidth(), bitmap.getHeight(), matrix, true);

				r1sdk.launchPhotoEffects(

				getApplicationContext(), rotatedBitmap, true, // Allow user to
																// crop first?
						new R1PhotoEffectsSDK.PhotoEffectsListener() {

							@Override
							public void onEffectsComplete(Bitmap output) {
								if (null == output) {
									return;
								}
								// mFinalImageView.setImageBitmap(output);
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
	public String getPath(Uri selectedImageUri) {

		String[] filePathColumn = { MediaStore.Images.Media.DATA };

		Cursor cursor = getContentResolver().query(selectedImageUri,
				filePathColumn, null, null, null);
		cursor.moveToFirst();

		int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
		String filePath = cursor.getString(columnIndex);
		cursor.close();
		return filePath;
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
		if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA)) {
			// this device has a camera
			return true;
		} else {
			// no camera on this device
			return false;
		}
	}

	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance() {
		Camera c = null;
		try {
			c = Camera.open(); // attempt to get a Camera instance
		} catch (Exception e) {
			// Camera is not available (in use or does not exist)
			e.printStackTrace();
		}
		Log.d("TAG", "Camera: " + c);

		return c; // returns null if camera is unavailable
	}

	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.setPreviewCallback(null);
			mCamera.release();
			mCamera = null;
		}
	}

	private ShutterCallback mShutter = new ShutterCallback() {

		@Override
		public void onShutter() {
			// TODO Auto-generated method stub

		}

	};

	private PictureCallback mPicture = new PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {

			// mCamera = camera;

			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

			Matrix matrix = new Matrix();
			matrix.postRotate(90);
			Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
					bitmap.getWidth(), bitmap.getHeight(), matrix, true);

			r1sdk.launchPhotoEffects(getApplicationContext(), rotatedBitmap,
					true, new R1PhotoEffectsSDK.PhotoEffectsListener() {
						@Override
						public void onEffectsComplete(Bitmap output) {
							if (null == output) {
								return;
							}
							// do something with output
						}

						@Override
						public void onEffectsCanceled() {
							// user canceled
							// mCamera.startPreview();
						}
					});
		}
	};

}
