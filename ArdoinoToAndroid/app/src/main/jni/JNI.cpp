//
// Created by Log on 2017-05-04.
//
#include <com_soondori_log_ardoinotoandroid_MainActivity.h>

JNIEXPORT jstring JNICALL Java_com_soondori_log_ardoinotoandroid_MainActivity_makeString(JNIEnv *env, jobject obj){
    return env->NewStringUTF("Message");
}