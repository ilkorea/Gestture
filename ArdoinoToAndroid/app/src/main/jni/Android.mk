LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE := JNIModule
LOCAL_SRC_FILES := JNI.cpp
LOCAL_LDLIBS := -llog

include $(BUILD_SHARED_LIBRARY)