package com.radiumone.r1sdkdemo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;

public class ImageUtils {

	/**
	 * Get the orientation of a photo
	 * @param path
	 * @return 0, 90, 180, or 270
	 */
	public static int getOrientation(String path) {
		ExifInterface exif;
		int angle = 0;

		try {
			exif = new ExifInterface(path);

			int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

			//            Logger.debug(this, "Orientation in crop image: " + orientation);
			switch (orientation) {
			case ExifInterface.ORIENTATION_ROTATE_90:
				angle = 90;
				break;
			case ExifInterface.ORIENTATION_ROTATE_180:
				angle = 180;
				break;
			case ExifInterface.ORIENTATION_ROTATE_270:
				angle = 270;
				break;
			case ExifInterface.ORIENTATION_NORMAL:
				break;
			default:
				angle = 0;
				break;
			}
		} catch (IOException e1) {
			Log.e("R1SDKdemo", "Problems loading " + path, e1);
		}
		return angle;
	}

	/**
	 * Read in the options for a bitmap
	 * @param path
	 * @return BitmapFactory.Options
	 * @throws IOException
	 */
	public static BitmapFactory.Options readOptions(String path) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		return options;
	}

	/**
	 * Load in an bitmap from the given path (can be http or file based).
	 * @param path
	 * @param width
	 * @param height
	 * @return Bitmap
	 */
	public static Bitmap loadBitmapImage(String path, int width, int height) {
		InputStream inputStream = null;
		// decodeStream closes inputstream
		try {
			//Decode image size
			if (path.startsWith("http")) {
				URL url = new URL(path);
				URLConnection urlConnection = url.openConnection();
				inputStream = new BufferedInputStream(urlConnection.getInputStream());
				BitmapFactory.Options options = readOptions(inputStream);
				inputStream.close();
				urlConnection = url.openConnection();
				inputStream = new BufferedInputStream(urlConnection.getInputStream());
				return decodeStream(inputStream, options, width, height);
			} else {
				File file = new File(path);
				if (!file.exists()) {
					Log.e("R1SDKdemo", "File " + path + " does not exist ");
					return null;
				}
				BitmapFactory.Options options = readOptions(path);

				inputStream = new BufferedInputStream(new FileInputStream(file));
				return decodeStream(inputStream, options, width, height);
			}
		} catch (IOException e) {
			Log.e("R1SDKdemo", "Problems loading " + path, e);
		}
		return null;
	}

	/**
	 * Read in the options for a bitmap
	 * @param inputStream
	 * @return BitmapFactory.Options
	 */
	public static BitmapFactory.Options readOptions(InputStream inputStream) throws IOException {
		BitmapFactory.Options options = new BitmapFactory.Options();

		options.inJustDecodeBounds = true;
		BitmapFactory.decodeStream(inputStream, null, options);
		return options;

	}

	/**
	 * Intelligently read in a bitmap by scaling if necessary
	 * @param inputStream
	 * @param options
	 * @param width
	 * @param height
	 * @return Bitmap
	 * @throws IOException
	 */
	public static Bitmap decodeStream(InputStream inputStream, BitmapFactory.Options options, int width, int height) throws IOException {
		int scale = 1;
		try {
			if (options.outHeight > height || options.outWidth > width) {
				scale = getScale(options.outWidth, options.outHeight, width, height);
			}
			//			Logger.debug(this, "getBitmap: scale: " + scale);
			//			Logger.debug(this, "getBitmap: width: " + options.outWidth + " height: " + options.outHeight);

			options.inJustDecodeBounds = false;
			options.inSampleSize = scale;
			Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
			//			Logger.debugLocal(ApplyFilterActivity.class.getSimpleName(),
			//				"Creating Bitmap of size " + ImageUtils.getPrettyAmount(bitmap.getWidth() * bitmap.getHeight() * 4));

			return bitmap;
		}  finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					Log.e("R1SDKdemo", "decodeStream IOException");
					throw e;
				}
			}
		}
	}

	/**
	 * Get the scaling factor for a bitmap
	 *
	 * @return scale number used in inSampleSize
	 */
	public static int getScale(int originalWidth, int originalHeight, int width, int height) {
		int scale = 1;
		int width_tmp = originalWidth, height_tmp = originalHeight;
		if (height_tmp > height || width_tmp > width) {
			int widthScale = 1;
			while ((width_tmp) > width) {
				width_tmp /= 2;
				widthScale *= 2;
			}
			int heightScale = 1;
			while ((height_tmp) > height) {
				height_tmp /= 2;
				heightScale *= 2;
			}
			scale = Math.min(widthScale, heightScale);
		} else {
			scale = 2; // Factor of 2 smaller
		}
		return scale;
	}
}
