package com.example.jacobdurrah.keyboardsim;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.util.Queue;

public class MainActivity extends AppCompatActivity {

    private Queue<Vibration> mVibrationQueue;
    private Queue<Task> mTaskQueue;
    private VibrationHandler mVibHandler;

    private PendingIntent mFPIntent;
    private PendingIntent mCLIntent;

    private BroadcastReceiver mFPReceiver;
    private BroadcastReceiver mCLReceiver;

    private AlarmManager mNextVibAlarm;
    private AlarmManager mNextTaskAlarm;


    public final static String EXTRA_MESSAGE = "com.example.jacobdurrah.keyboardsim.MESSAGE";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setup();
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void sendMessage(View view)
    {
        Intent intent = new Intent(this, FlightPlanActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void startCheckListActivity(View view)
    {
        Intent intent = new Intent(this, CheckListActivity.class);
        startActivity(intent);
    }

    //Handle private member initialization and callback registration for async callbacks
    private void setup(){
        mVibHandler = new VibrationHandler();

        mFPReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver(mFPReceiver, new IntentFilter("com.example.jacobdurrah.keyboardsim."));

        mCLReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

            }
        };
        registerReceiver();

    }
}
