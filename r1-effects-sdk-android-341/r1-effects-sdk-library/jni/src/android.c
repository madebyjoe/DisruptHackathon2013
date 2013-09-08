#include <jni.h>
#include <stdlib.h>
#include <android/bitmap.h>
#include <GLES2/gl2.h>
#include "logging.h"

JNIEXPORT jint JNICALL JNI_OnLoad( JavaVM *vm, void *pvt )
{
	LOGD( "** Loading ViaMe magic subsystem ***" );
	return JNI_VERSION_1_6;
}


static char *bmFormatStr( int format ){
	switch( format ){
	case ANDROID_BITMAP_FORMAT_NONE: return "NONE";
	case ANDROID_BITMAP_FORMAT_RGBA_8888: return "RGBA_8888";
	case ANDROID_BITMAP_FORMAT_RGB_565: return "RGB_565";
	case ANDROID_BITMAP_FORMAT_RGBA_4444: return "RGBA_4444";
	case ANDROID_BITMAP_FORMAT_A_8: return "A_8";
	}

	return "Invalid";
}


JNIEXPORT jint Java_com_radiumone_viamenative_JNIBridge_glReadPixelsToBitmap( JNIEnv* env, jobject thiz, jobject bitmap ) {
	int ret = 0;

	AndroidBitmapInfo info;
	void *pixels;

	ret = AndroidBitmap_getInfo(env, bitmap, &info);

	if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
		LOGD("AndroidBitmap_lockPixels() failed ! error=%d", ret);
		return -1;
	}

	glReadPixels (0, 0, info.width, info.height, GL_RGBA, GL_UNSIGNED_BYTE, pixels);

	AndroidBitmap_unlockPixels(env, bitmap);

	return 0;
}


static void *texbuffer = NULL;

JNIEXPORT jint Java_com_radiumone_viamenative_JNIBridge_createAndTextureBuffers( JNIEnv* env, jobject thiz, jint width, jint height ){

//	LOGD("createAndTextureBuffers(%d,%d)", width, height);


	if( NULL == texbuffer) {
		LOGD( "new texbuffer");
		int size = 1024 * 1024 * 4;
		texbuffer = malloc( size );
	}

//	LOGD( "texbuffer @ %p", texbuffer );

	glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, texbuffer);

	//LOGD( "glTexImage2D returned %d", glGetError());


	return 0;
}




