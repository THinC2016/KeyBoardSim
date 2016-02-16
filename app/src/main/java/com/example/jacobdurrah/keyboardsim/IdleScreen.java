package com.example.jacobdurrah.keyboardsim;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class IdleScreen extends AppCompatActivity {
    private CountDownTimer countDownTimer;
    private final long startTime = 5 * 1000;
    private final long interval = 1 * 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //start logging time
        countDownTimer = new MyCountDownTimer(startTime, interval);
        countDownTimer.start();

    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            //start next activity
            startCheckListActivity();
        }

        @Override
        public void onTick(long millisUntilFinished) {
        }
    }

    public void startCheckListActivity() {
        Intent intent = new Intent(this, CheckListActivity.class);
        startActivity(intent);
    }

}
