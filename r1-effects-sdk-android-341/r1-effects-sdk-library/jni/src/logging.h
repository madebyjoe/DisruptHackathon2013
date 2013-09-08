#ifndef LOGGING_H_
#define LOGGING_H_

#define DEBUG_LOGGING_ENABLED 1


#if TARGET_OS_ANDROID
	#include <android/log.h>

	#define  LOG_TAG    "viame-ndk"
	#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)
	#define  LOGI(...)  __android_log_print(ANDROID_LOG_INFO,LOG_TAG,__VA_ARGS__)
	#define  LOGE(...)  __android_log_print(ANDROID_LOG_ERROR,LOG_TAG,__VA_ARGS__)
#else
#define	LOGD(...)
#define	LOGI(...)
#define	LOGE(...)
#endif

#ifndef DEBUG_LOGGING_ENABLED
#define LOGD(...)
#endif


#endif
