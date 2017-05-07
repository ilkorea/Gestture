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
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import id.kido1611.arduinoconnect.ArduinoConnect;
import id.kido1611.arduinoconnect.ArduinoConnectCallback;

import android.opengl.GLSurfaceView;

public class MainActivity extends AppCompatActivity {
    private ArduinoConnect mArduinoConnect;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mArduinoConnect = new ArduinoConnect(this, getSupportFragmentManager());
        mArduinoConnect.setSleepTime(500);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


//        root = this;
//        mArduinoConnect = new ArduinoConnect(this, this);
//        mArduinoConnect.setSleepTime(500);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mArduinoConnect.showDialog();
            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new MainFragment()).commit();
    }

    public void hideFAB(int visibility){
        fab.setVisibility(visibility);
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

}
