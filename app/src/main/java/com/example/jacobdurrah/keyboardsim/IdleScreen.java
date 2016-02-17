package com.example.jacobdurrah.keyboardsim;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class IdleScreen extends AppCompatActivity {
 private Queue<Task> mTaskQueue;
    private Queue<Vibration> mVibQueue;
    private Task currentTask;
    private CountDownTimer countDownTimer;
    private final long startTime =  1000;
    private final long interval = 1 * 1000;
    public static ArrayList<String> planets;
    private boolean CheckListDone = false;
    private VibrationHandler mVibrationHandler;

    String waypoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idle_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setup();
        setupVibration();
        //mVibrationHandler.changeVibration(1, 9);
        //currentTask = mTaskQueue.remove();

        //if(currentTask != null) {
            //start logging time
            countDownTimer = new MyCountDownTimer(startTime * 101, interval);
            countDownTimer.start();
        //}
        //else
        //{
        //Toast.makeText(getApplicationContext(), "Error in starting task que", Toast.LENGTH_LONG)
        //            .show();
        //}



    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            //add_Button(true);

        }

        @Override
        public void onTick(long millisUntilFinished) {
            //Tick every second

            Vibration vib = mVibQueue.remove();
            mVibrationHandler.changeVibration(vib.getFreq(), vib.getAmplitude());
            Toast.makeText(getApplicationContext(), "f: " + Integer.toString(vib.getFreq()) + "a: "
                    + Integer.toString(vib.getAmplitude()), Toast.LENGTH_SHORT)
                    .show();

        }
    }
        public void add_Button(boolean add)
        {
            LinearLayout mainLayout = (LinearLayout)findViewById(R.id.idle_layout);
            if(add) {
                Button addButton = new Button(this);

                addButton.setId(0);
                if (currentTask.getType() == "FP") {
                    addButton.setText("Start FlightPlan Task");
                } else if (currentTask.getType() == "CL") {
                    addButton.setText("Start CheckList Task");
                }
                addButton.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        startTask();
                    }
                });

                mainLayout.addView(addButton);
            }
            else
            {
               mainLayout.removeAllViews();
            }

        }
    public void startTask()
    {
        if(currentTask.getType() == "CL")
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
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        // Activity being restarted from stopped state
        countDownTimer.cancel();
        if(currentTask != null) {
            countDownTimer = new MyCountDownTimer(startTime * currentTask.getmSecondsToWait(), interval);
            countDownTimer.start();
            add_Button(false); //remove the button from the screen
        }
        else
        {
            finish();
        }
    }

    public void startCheckListActivity() {
        Intent intent = new Intent(this, CheckListActivity.class);
        intent.putExtra(Bundle_Keys.BUNDLE_Checklist_KEY, currentTask.getmCheckList());
        intent.putExtra(Bundle_Keys.BUNDLE_Waypoint_KEY,currentTask.getmWaypoint());
        intent.putExtra(Bundle_Keys.BUNDLE_Audio_KEY, currentTask.getmAudioFeedBack());
        intent.putExtra(Bundle_Keys.BUNDLE_Visual_KEY, currentTask.getmVisualFeedBack());
        intent.putExtra(Bundle_Keys.BUNDLE_Vibrate_KEY, currentTask.getmVibration());
        if(!mTaskQueue.isEmpty()) {
            currentTask = mTaskQueue.remove();
        }
        else
        {
            currentTask = null;
        }
        startActivity(intent);
    }

    public void startFlightplanActivity() {
        Intent intent = new Intent(this, FlightPlanActivity.class);
        intent.putExtra(Bundle_Keys.BUNDLE_Waypoint_KEY,currentTask.getmWaypoint());
        intent.putExtra(Bundle_Keys.BUNDLE_Audio_KEY, currentTask.getmAudioFeedBack());
        intent.putExtra(Bundle_Keys.BUNDLE_Visual_KEY, currentTask.getmVisualFeedBack());
        intent.putExtra(Bundle_Keys.BUNDLE_Vibrate_KEY, currentTask.getmVibration());

        if(!mTaskQueue.isEmpty()) {
            currentTask = mTaskQueue.remove();
        }
        else
        {
            currentTask = null;
        }
        startActivity(intent);
    }


    public void setup()
    {
        mTaskQueue = new LinkedList<Task>();

        mTaskQueue.add(new Task(0, "FP", 3, true, true
                , false, "DTW"));

        mTaskQueue.add(new Task(
                0,  //scenario
                "CL",   //type, checklist or flight plan
                4,      //seconds to wait after previous task complete
                true,   //visual feedback
                true,   //audio feedback
                false,  //vibration
                new String[]{   //checklist
                        "list one",
                        "list two",
                        "List three",
                        "List four",
                        "List five",
                        "List six"}));
        mTaskQueue.add(new Task(
                0,  //scenario
                "CL",   //type, checklist or flight plan
                10,      //seconds to wait after previous task complete
                true,   //visual feedback
                true,   //audio feedback
                false,  //vibration
                new String[]{   //checklist
                        "list hello",
                        "list world",
                        "List  you",
                        "List four",
                        "List five",
                        "List six"}));

        mTaskQueue.add(new Task(0, "FP", 1, true, true
                , false, "BME"));
        mVibQueue = new LinkedList<>();
        for(int i = 0; i < 10; i++){
            for(int j = 0; j < 10; j++){
                mVibQueue.add(new Vibration(i,j, "00:00:00") );
            }
        }


    }

    public void setupVibration(){
        //mVibQueue = new LinkedList<>();
        mVibrationHandler = new VibrationHandler();
        try {
            mVibrationHandler.init(getApplicationContext());
        } catch (Exception e){

        }
    }

}
