#include <jni.h>
#include <string>
#include <math.h>
#include <GLES2/gl2.h>
#include <android/bitmap.h>

extern "C"{
    jobject bitmap;// = new jobject[6];

    JNIEXPORT jstring JNICALL
    Java_com_soondori_log_arduinotoandroidforndk_MainFragment_calculateArea( JNIEnv *jenv, jobject self, jdouble radius ) {
        jdouble area = M_PI * radius * radius;

        char output[40];
        sprintf(output, "The area is %f sqm", area);

        return jenv->NewStringUTF(output);
    }

    JNIEXPORT void JNICALL
    Java_com_soondori_log_arduinotoandroidforndk_TexturedCubeRenderer_saveBitmap( JNIEnv *jenv, jobject pBitmap) {
        bitmap = pBitmap;
        return;// jenv->NewStringUTF("true");
    }
}