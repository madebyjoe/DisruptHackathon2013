package com.radiumone.r1sdkdemo;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// R1 SDK initialization
		r1sdk = R1PhotoEffectsSDK.getManager();
		// call enable method and pass app token, billing license key and cropmode.

		r1sdk.enable(getApplicationContext(), "3d2848e0-77a5-0130-64de-22000ac40812", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkPXxk7BIWriqNE1jRjay4jfioYsVolOJvKoHuyW7svaYCASwCBV4KxQCsvz50UTVLdmddnTlixI5VjXt+3Va6YV2GQf1rT3geuxLen5HpVwtq7bROd9Z9iAvDrqseNWb+iCGnIUrt6k2/9FOESSagTc4/zKcxrprR/zayucJ/iDDPRRrErbZIB8PWi+XT+k04PAiEp0VIr5t37EyTk55mqst+kbxBRLRLMmhWrrVxH+ff7RPFGVaR1z3X5CnTj5mtMb94RS3AzRFAs7gXHqfrzogYrHl8m6sgT+bmSD6V8kYov49cAzRrpE0Enlcr7MGsi7l3NMc4xkjNakEiHsUjwIDAQAB", R1PhotoEffectsSDK.CROP_MODE_ALL);

		// Assign image view
		mFinalImageView = (ImageView) findViewById(R.id.dm_final_image);  

		// Assign button and add an event listener
		mLaunchSDKButton = (Button) findViewById(R.id.dm_launch_sdk_button);
		mLaunchSDKButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Open device gallery
				startSelectPicture();   
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
}
