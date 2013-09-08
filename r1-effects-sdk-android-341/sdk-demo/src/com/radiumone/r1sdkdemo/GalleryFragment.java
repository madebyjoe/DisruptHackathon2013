package com.radiumone.r1sdkdemo;

import java.io.IOException;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.radiumone.effects_sdk.R1PhotoEffectsSDK;

public class GalleryFragment extends Fragment implements OnItemClickListener {
	public static final String EXTRA_BUCKET_NAME = "extra_bucket_name";

	GridView mGridView;
	String mBucketName;
	CursorLoader mCursorLoader;
	Cursor mCursor;

	public GalleryFragment() {
		// Bucket name will be restored through the savedInstanceState

	}

	public String getBucketName() {
		return mBucketName;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putString(EXTRA_BUCKET_NAME, mBucketName);
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		if(null != savedInstanceState){
			mBucketName = savedInstanceState.getString(EXTRA_BUCKET_NAME);
		}
		else {
			mBucketName = getArguments().getString(EXTRA_BUCKET_NAME);
		}
		
		View v =  inflater.inflate(R.layout.gallery_fragment, container, false);

		mGridView = (GridView)v.findViewById(R.id.gridView);
		mGridView.setOnItemClickListener(this);


		mCursorLoader = new CursorLoader(
				getActivity(),
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[] { MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA },
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME + "='" + mBucketName + "'",
				null, null);

		mCursor = mCursorLoader.loadInBackground();


		mGridView.setAdapter( new BaseAdapter() {

			@Override
			public int getCount() {
				return mCursor.getCount();
			}

			@Override
			public Object getItem(int arg0) {
				return null;
			}

			@Override
			public long getItemId(int arg0) {
				return 0;
			}

			@Override
			public View getView(int pos, View convertView, ViewGroup arg2) {

				ImageView imageView;
				if (convertView == null) {  // if it's not recycled, initialize some attributes
					imageView = new ImageView(getActivity());
					imageView.setLayoutParams(new GridView.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT ));					
					imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				} else {
					imageView = (ImageView) convertView;
				}

				mCursor.moveToPosition(pos);

				long id = mCursor.getLong( mCursor.getColumnIndex(MediaStore.Images.Media._ID ));
				String path = mCursor.getString( mCursor.getColumnIndex( MediaStore.Images.Media.DATA) );

				Bitmap bm = MediaStore.Images.Thumbnails.getThumbnail(
						getActivity().getContentResolver(),
						id,
						MediaStore.Images.Thumbnails.MICRO_KIND, null);

				imageView.setTag(path);

				imageView.setImageBitmap(bm);

				return imageView;
			}

		});


		return v;
	}


	@Override
	public void onItemClick(AdapterView<?> arg0, final View v, int arg2, long arg3) {
		// A photo was selected, send it to the effects queue
		String path = (String) v.getTag();

		R1PhotoEffectsSDK r1sdk = R1PhotoEffectsSDK.getManager();		

		Bitmap bm;
		int originalBitmapWidth;
		int originalBitmapHeight;
		int angle = ImageUtils.getOrientation(path);

		BitmapFactory.Options options;
		try {
			options = ImageUtils.readOptions(path);
			originalBitmapWidth = options.outWidth;
			originalBitmapHeight = options.outHeight;
			// swap height/width if rotated
			switch (angle) {
			case 90:
			case 270:
				originalBitmapWidth = options.outHeight;
				originalBitmapHeight = options.outWidth;
				break;
			}
		} catch( IOException Ex ){
			return;
		}


		bm = ImageUtils.loadBitmapImage(path, originalBitmapWidth, originalBitmapHeight);
		if (angle != 0) {
			Matrix mat = new Matrix();
			mat.postRotate(angle);
			// Create rotated (if necessary) bitmap.
			Bitmap correctBmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), mat, true);
			bm.recycle();
			bm = correctBmp;
		}

		r1sdk.launchPhotoEffects(

				getActivity().getApplicationContext(),
				bm,
				true, // Allow user to crop first?
				new R1PhotoEffectsSDK.PhotoEffectsListener() {

					@Override
					public void onEffectsComplete(Bitmap output) {
						if( null == output ){
							return;
						}

						displayDialogImage(output);
					}

					@Override
					public void onEffectsCanceled() {
						// user canceled
						Toast.makeText(getActivity(), "User canceled", Toast.LENGTH_SHORT).show();
					}
				} );
	}




	protected void displayDialogImage(Bitmap bm) {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		ImageView iv = new ImageView( getActivity() );
		iv.setLayoutParams( new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		iv.setImageBitmap(bm);

		builder.setView( iv );

		builder.show();

	}




}
