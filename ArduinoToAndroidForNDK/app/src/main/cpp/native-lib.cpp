#include <jni.h>
#include <string>
#include <math.h>

extern "C"{
    JNIEXPORT jstring JNICALL
    Java_com_soondori_log_arduinotoandroidforndk_MainFragment_calculateArea( JNIEnv *jenv, jobject self, jdouble radius ) {
        jdouble area = M_PI * radius * radius;

        char output[40];
        sprintf(output, "The area is %f sqm", area);

        return jenv->NewStringUTF(output);
    }
}

