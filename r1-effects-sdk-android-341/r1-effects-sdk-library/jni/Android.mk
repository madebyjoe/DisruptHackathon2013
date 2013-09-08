LOCAL_PATH := $(call my-dir)/src

include $(CLEAR_VARS)
APP_PLATFORM := android-8

#SRC := YooLottoOCR
LOCAL_LDLIBS	:= -L$(SYSROOT)/usr/lib -llog -ljnigraphics -lGLESv2 -lEGL
LOCAL_CFLAGS    := -DTARGET_OS_ANDROID 
LOCAL_MODULE    := viame
LOCAL_SRC_FILES :=	android.c

# http://stackoverflow.com/questions/5089783/producing-optimised-ndk-code-for-multiple-architectures
APP_ABI := armeabi armeabi-v7a
LOCAL_CFLAGS    += -O3
LOCAL_CFLAGS	+= -std=c99

#LOCAL_CFLAGS    += -DRAPIDXML_NO_EXCEPTIONS
#LOCAL_CFLAGS    += -g

include $(BUILD_SHARED_LIBRARY)
	

