//
// Created by Administrator on 2016/8/26.
//


/*
 * Class:     io_github_yanbober_ndkapplication_NdkJniUtils
 * Method:    getCLanguageString
 * Signature: ()Ljava/lang/String;
 */
#include "com_testcore.utils_NdkJniUtils.h"
#include <android/log.h>

JNIEXPORT jstring JNICALL Java_com_testcore_utils_NdkJniUtils_getCLanguageString
  (JNIEnv *env, jobject obj){
     return (*env)->NewStringUTF(env,"This just a test for Android Studio NDK JNI developer!");
  }