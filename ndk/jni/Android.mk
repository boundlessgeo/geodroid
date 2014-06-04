LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

TARGET_ARCH_ABI := armeabi

#include $(LOCAL_PATH)/proj.mk
include $(LOCAL_PATH)/gdal.mk
