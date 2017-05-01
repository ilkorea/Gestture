package com.soondori.log.ardoinotoandroid;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import id.kido1611.arduinoconnect.ArduinoConnect;
import id.kido1611.arduinoconnect.ArduinoConnectCallback;

public class MainActivity extends AppCompatActivity implements ArduinoConnectCallback {
    private ArduinoConnect mArduinoConnect;
    private TextView tvOutText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mArduinoConnect = new ArduinoConnect(this, getSupportFragmentManager(), this);
        mArduinoConnect.setSleepTime(500);

        tvOutText = (TextView)findViewById(R.id.tvOutText);

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
        //str = tvOutText.getText().toString();
        tvOutText.setText(text);
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
