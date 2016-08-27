//
// Created by Administrator on 2016/8/26.
//
#include <jni.h>

#ifdef __cplusplus
extern "C" {
#endif
/*
 * Method:    getCLanguageString
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_com_testcore_utils_NdkJniUtils_getCLanguageString
  (JNIEnv *, jobject);

#ifdef __cplusplus
}
#endif

