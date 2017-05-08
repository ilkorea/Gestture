package com.soondori.log.arduinotoandroidforndk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    private  final String TAG = "Soondori";
    //////////////////////////////////////////////////////////////////////////////////////
    // NDK 선언
    //////////////////////////////////////////////////////////////////////////////////////
    static {
        System.loadLibrary("native-lib");
    }
    private native String calculateArea(double radius);
    //////////////////////////////////////////////////////////////////////////////////////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, calculateArea(5.5f));
    }
}
