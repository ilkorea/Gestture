package com.soondori.log.arduinotoandroidforndk;

import android.bluetooth.BluetoothDevice;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import id.kido1611.arduinoconnect.ArduinoConnect;
import id.kido1611.arduinoconnect.ArduinoConnectCallback;
import id.kido1611.arduinoconnect.DialogConnect;

import static java.lang.Thread.sleep;

public class MainFragment extends Fragment implements ArduinoConnectCallback {
    private final String TAG = "Soondori";

    private TextView tvRxData;
    public volatile TextView tvBtId;
    //private ImageView ivArrow;
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
        LinearLayout linearLayout = (LinearLayout)rootView.findViewById(R.id.MyLinearLayout);

        GLSurfaceView surfaceView = new TouchGLSurfaceView(rootView.getContext());
        //TouchGLSurfaceView tgls = new TouchGLSurfaceView(rootView.getContext());
        //tgls.setRenderer(new TexturedCubeRenderer(rootView.getContext()));
        TexturedCubeRenderer texturedCubeRenderer = new TexturedCubeRenderer(rootView.getContext());
        surfaceView.setRenderer(texturedCubeRenderer);
        linearLayout.addView(surfaceView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tvBtId = (TextView)rootView.findViewById(R.id.tvBtId);
        //ivArrow = (ImageView)rootView.findViewById(R.id.ivArrow);
        tvRxData = (TextView)rootView.findViewById(R.id.tvRxData);
        //tvRxData.setText(calculateArea(5.5f));
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
                            texturedCubeRenderer.depth += 0.2;
                            break;
                        case "Down":
                            texturedCubeRenderer.depth -= 0.2;
                            break;
                        case "Left":
                            for(int i = 0; i < 90; i++) {
                                texturedCubeRenderer.angleY -= 1;
                                try {
                                    sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case "Right":
                            for(int i = 0; i < 90; i++) {
                                texturedCubeRenderer.angleY += 1;
                                try {
                                    sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case "Forward":
                            for(int i = 0; i < 90; i++){
                                texturedCubeRenderer.angleX -= 1;
                                try {
                                    sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case "Backward":
                            for(int i = 0; i < 90; i++) {
                                texturedCubeRenderer.angleX += 1;
                                try {
                                    sleep(5);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
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
        tvRxData.setText(text);
    }

    @Override
    public void onArduinoConnected(BluetoothDevice device) {
        tvBtId.setText("Bluetooth is connect with " + DialogConnect.strBtName);
        ((MainActivity)getActivity()).SetFabConnectedImage(true);
        if(getArduinoConnect()!=null){
            getArduinoConnect().sendMessage("Connected..");
        }
        //Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        //((MainActivity)getActivity()).hideFAB(View.GONE);
    }

    @Override
    public void onArduinoDisconnected() {
        ((MainActivity)getActivity()).SetFabConnectedImage(false);
        tvBtId.setText("Bluetooth is disconnected");
        if(getArduinoConnect()!=null){
            //Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
            //((MainActivity)getActivity()).hideFAB(View.VISIBLE);
        }
    }

    @Override
    public void onArduinoNotConnected() {
        ((MainActivity)getActivity()).SetFabConnectedImage(false);
        tvBtId.setText("Bluetooth is disconnected");
        // Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArduinoConnectFailed() {
        ((MainActivity)getActivity()).SetFabConnectedImage(false);
        tvBtId.setText("Bluetooth is disconnected");
        // Toast.makeText(this, "Failed to connect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothDeviceNotFound() {
        ((MainActivity)getActivity()).SetFabConnectedImage(false);
        tvBtId.setText("Bluetooth is disconnected");
        // Toast.makeText(this, "Bluetooth device not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothFailedEnabled() {
        ((MainActivity)getActivity()).SetFabConnectedImage(false);
        tvBtId.setText("Bluetooth is disconnected");
        //Toast.makeText(this, "Failed to turn on Bluetooth", Toast.LENGTH_SHORT).show();
    }
}
