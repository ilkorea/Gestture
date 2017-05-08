package com.soondori.log.arduinotoandroidforndk;

import android.bluetooth.BluetoothDevice;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.kido1611.arduinoconnect.ArduinoConnect;
import id.kido1611.arduinoconnect.ArduinoConnectCallback;

public class MainFragment extends Fragment implements ArduinoConnectCallback {
    private final String TAG = "Soondori";

    private TextView tvRxData;
    private ImageView ivArrow;
    MainActivity root;

    static {
        System.loadLibrary("native-lib");
    }
    private native String calculateArea(double radius);

    public MainFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity)getActivity()).getArduinoConnect().setCallback(this);
        getArduinoConnect().setSleepTime(1000);
    }

    private ArduinoConnect getArduinoConnect(){
        if((MainActivity)getActivity()==null) return null;

        return ((MainActivity)getActivity()).getArduinoConnect();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_main, container, false);
        Log.d(TAG, calculateArea(5.5f));
        LinearLayout l = (LinearLayout)rootView.findViewById(R.id.MyLinearLayout);
        GLSurfaceView s = new GLSurfaceView(rootView.getContext());
        s.setRenderer(new ParticleRenderer(rootView.getContext()));
        l.addView(s, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        ivArrow = (ImageView)rootView.findViewById(R.id.ivArrow);
        tvRxData = (TextView)rootView.findViewById(R.id.tvRxData);
        tvRxData.setText(calculateArea(5.5f));
        tvRxData.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count >1){
                    String str = s.toString();
                    str = str.substring(0, str.indexOf("\r"));

                    switch (str){
                        case "Up":
                            ivArrow.setBackgroundResource(R.drawable.up);
                            break;
                        case "Down":
                            ivArrow.setBackgroundResource(R.drawable.down);
                            break;
                        case "Left":
                            ivArrow.setBackgroundResource(R.drawable.left);
                            break;
                        case "Right":
                            ivArrow.setBackgroundResource(R.drawable.right);
                            break;
                        case "Forward":
                            ivArrow.setBackgroundResource(R.drawable.forward);
                            break;
                        case "Backward":
                            ivArrow.setBackgroundResource(R.drawable.backward);
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        return rootView;
    }

    @Override
    public void onSerialTextReceived(String text) {
        String str;
        //str = tvRxData.getText().toString();
        tvRxData.setText(text);
    }

    @Override
    public void onArduinoConnected(BluetoothDevice device) {
        if(getArduinoConnect()!=null){
            getArduinoConnect().sendMessage("Connected..");
        }
        //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        //((MainActivity)getActivity()).hideFAB(View.GONE);
    }

    @Override
    public void onArduinoDisconnected() {
        if(getArduinoConnect()!=null){
            //Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
            //((MainActivity)getActivity()).hideFAB(View.VISIBLE);
        }
    }

    @Override
    public void onArduinoNotConnected() {
        // Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArduinoConnectFailed() {
        // Toast.makeText(this, "Failed to connect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothDeviceNotFound() {
        // Toast.makeText(this, "Bluetooth device not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothFailedEnabled() {
        //Toast.makeText(this, "Failed to turn on Bluetooth", Toast.LENGTH_SHORT).show();
    }
}
