package com.example.jacobdurrah.keyboardsim;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class IdleScreen extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private final long startTime = 5 * 1000;
    private final long interval = 1 * 1000;
    public static ArrayList<String> planetList;
    private boolean CheckListDone = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //start logging time
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();
        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll(Arrays.asList(planets));

    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            if(!CheckListDone)
            {
                //start next activity
                startCheckListActivity();
                CheckListDone = true;
            }
            else
            {
                startFlightplanActivity();
                CheckListDone = false;

            }

        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
        countDownTimer.cancel();
        countDownTimer.start();
    }

    public void startCheckListActivity() {
        Intent intent = new Intent(this, CheckListActivity.class);
        startActivity(intent);
    }

    public void startFlightplanActivity() {
        Intent intent = new Intent(this, FlightPlanActivity.class);
        startActivity(intent);
    }

}
