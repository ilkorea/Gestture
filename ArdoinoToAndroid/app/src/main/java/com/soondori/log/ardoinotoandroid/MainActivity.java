package com.soondori.log.ardoinotoandroid;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import id.kido1611.arduinoconnect.ArduinoConnect;
import id.kido1611.arduinoconnect.ArduinoConnectCallback;

public class MainActivity extends AppCompatActivity implements ArduinoConnectCallback {
    private ArduinoConnect mArduinoConnect;
    private TextView tvRxData;
    private ImageView ivArrow;
    MainActivity root;

    static {
        System.loadLibrary("JNIModule");
    }
    public native String makeString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        root = this;
        mArduinoConnect = new ArduinoConnect(this, this);
        mArduinoConnect.setSleepTime(500);

        ivArrow = (ImageView)findViewById(R.id.ivArrow);
        tvRxData = (TextView)findViewById(R.id.tvRxData);
        tvRxData.setText(makeString());

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
                            ivArrow.setBackground(ContextCompat.getDrawable(root, R.drawable.up));
                            break;
                        case "Down":
                            ivArrow.setBackground(ContextCompat.getDrawable(root, R.drawable.down));
                            break;
                        case "Left":
                            ivArrow.setBackground(ContextCompat.getDrawable(root, R.drawable.left));
                            break;
                        case "Right":
                            ivArrow.setBackground(ContextCompat.getDrawable(root, R.drawable.right));
                            break;
                        case "Forward":
                            ivArrow.setBackground(ContextCompat.getDrawable(root, R.drawable.forward));
                            break;
                        case "Backward":
                            ivArrow.setBackground(ContextCompat.getDrawable(root, R.drawable.backward));
                            break;
                        default:
                            break;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mArduinoConnect.showDialog();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * Must added to code
         */
        if(mArduinoConnect!=null)
            mArduinoConnect.onActivityResult(requestCode, resultCode, data);
    }

    public ArduinoConnect getArduinoConnect(){
        return mArduinoConnect;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.action_disconnected) {
//            if(getArduinoConnect()!=null){
//                if(getArduinoConnect().isConnected()){
//                    getArduinoConnect().disconnected();
//                }else{
//                    Toast.makeText(this, "Failed to disconnect", Toast.LENGTH_SHORT).show();
//                }
//            }else{
//                Toast.makeText(this, "Failed to disconnect", Toast.LENGTH_SHORT).show();
//            }
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        /**
         * Add this to fix memory leak
         */
        if(mArduinoConnect!=null)
            mArduinoConnect.disconnected();

        super.onDestroy();
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
        Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        //((MainActivity)getActivity()).hideFAB(View.GONE);
    }

    @Override
    public void onArduinoDisconnected() {
        if(getArduinoConnect()!=null){
            Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
            //((MainActivity)getActivity()).hideFAB(View.VISIBLE);
        }
    }

    @Override
    public void onArduinoNotConnected() {
        Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArduinoConnectFailed() {
        Toast.makeText(this, "Failed to connect", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothDeviceNotFound() {
        Toast.makeText(this, "Bluetooth device not found", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBluetoothFailedEnabled() {
        Toast.makeText(this, "Failed to turn on Bluetooth", Toast.LENGTH_SHORT).show();
    }
}
