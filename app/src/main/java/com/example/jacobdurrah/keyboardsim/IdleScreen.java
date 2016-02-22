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
import android.widget.TextView;
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


        setup();
        if(MainActivity.Vib_connected_toggle)
            setupVibration();

        //remove the first task
        currentTask = mTaskQueue.remove();

        if(currentTask != null) {
            //start logging time
            countDownTimer = new MyCountDownTimer(startTime * currentTask.getmSecondsToWait(),interval);
            countDownTimer.start();
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Error in starting task que", Toast.LENGTH_LONG)
                   .show();
        }



    }

    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {

            add_Button(true); //add a button that will be used to start task to the idle screen

        }

        @Override
        public void onTick(long millisUntilFinished) {
            //Tick every second

    /*        Vibration vib = mVibQueue.remove();
            mVibrationHandler.changeVibration(vib.getFreq(), vib.getAmplitude());
            Toast.makeText(getApplicationContext(), "f: " + Integer.toString(vib.getFreq()) + "a: "
                    + Integer.toString(vib.getAmplitude()), Toast.LENGTH_SHORT)
                    .show();

                    */

        }
    }
        //used in MyCountDownTimer.onFInish - adds a button to the idle screen for starting task
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
    //used in idle screen. Called when button on idle screen is pushed
    public void startTask()
    {
        //start vibration
        //currentTask.amp , //currentTask.freq
        if(MainActivity.Vib_connected_toggle)
            mVibrationHandler.changeVibration(currentTask.getMvibration_freq(),currentTask.getMvibration_amp());

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

    //called when task is completed. If all task done displays Done to idle screen
    @Override
    protected void onRestart() {
        super.onRestart();  // Always call the superclass method first

        //stop vibration
        if(MainActivity.Vib_connected_toggle)
            mVibrationHandler.stopVibration();
        // Activity being restarted from stopped state
        countDownTimer.cancel();
        if(currentTask != null) {
            countDownTimer = new MyCountDownTimer(startTime * currentTask.getmSecondsToWait(), interval);
            countDownTimer.start();
            add_Button(false); //remove the button from the screen
        }
        else
        {
            //Change idle screen to show Experiment Complete
            //clear all buttons
            add_Button(false);
            LinearLayout mainLayout = (LinearLayout)findViewById(R.id.idle_layout);
            TextView ExperimentComplete = new TextView(this);
            ExperimentComplete.setText("Experiment Complete");
            ExperimentComplete.setTextSize(40);
            mainLayout.addView(ExperimentComplete);

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
        else {
            currentTask = null;
        }
        startActivity(intent);
    }


    public void setup()
    {
        mTaskQueue = new LinkedList<Task>();
//scenario1

        if(ScenarioSelection.selectedScenario.equals("Scenario 1")) {
            mTaskQueue.add(new Task(
                    1,      //scenario
                    1,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "SPD MODE .................. ON",
                            "STALL LIGHT ............... OFF",
                            "TERRAIN LIGHT ............. OFF",
                            "MASTER WARNING ............ OFF",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(1, 1, true, "FP", 90, false, false
                    , false, 9, 3, "TAMEV"));

            mTaskQueue.add(new Task(1, 2, true, "FP", 90, false, false
                    , true, 9, 3, "BNEOG"));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    2,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    false,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "HDG SEL MODE .............. ON",
                            "AP DISCON ................. OFF",
                            "PITOT HEAT ................ OFF",
                            "MASTER CAUTION ............ OFF",
                            "THRUST REVERSE ............ DISENGAGED"}));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    3,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALT MODE .................. ON",
                            "LOW FUEL LIGHT ............ OFF",
                            "ICE DETECT ................ OFF",
                            "THRUST LEVELS ............. BALANCED",
                            "SPEED BRAKES .............. RETRACTED"}));

            mTaskQueue.add(new Task(1, 3, true, "FP", 90, false, true
                    , false, 9, 3, "PAVQK"));

            mTaskQueue.add(new Task(1, 4, true, "FP", 90, false, true
                    , true, 9, 3, "WUPSK"));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    4,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALTIMETER ................. STD",
                            "STALL LIGHT ............... OFF",
                            "LOW FUEL LIGHT ............ OFF",
                            "HYDR. PRESS. .............. BALANCED",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    5,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "SPD MODE .................. ON",
                            "TERRAIN LIGHT ............. OFF",
                            "PITOT HEAT ................ OFF",
                            "N1 vs. N1 LIMIT ........... CHECK",
                            "THRUST REVERSE ............ DISENGAGED"}));

            mTaskQueue.add(new Task(1, 5, true, "FP", 90, true, false
                    , false, 9, 3, "TEOZU"));

            mTaskQueue.add(new Task(1, 6, true, "FP", 90, true, false
                    , true, 9, 3, "LYZOC"));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    6,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "HDG SEL MODE .............. ON",
                            "PITOT HEAT ................ OFF",
                            "ICE DETECT ................ OFF",
                            "MASTER WARNING ............ OFF",
                            "SPEED BRAKES .............. RETRACTED"}));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    7,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALT MODE .................. ON",
                            "AP DISCON ................. OFF",
                            "STALL LIGHT ............... OFF",
                            "THRUST LEVELS ............. BALANCED",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(1, 7, false, "FP", 90, true, false
                    , false, 9, 3, "DIFEO"));

            mTaskQueue.add(new Task(1, 8, false, "FP", 90, true, false
                    , true, 9, 3, "KENJS"));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    8,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALTIMETER ................. STD",
                            "LOW FUEL LIGHT ............ OFF",
                            "TERRAIN LIGHT ............. OFF",
                            "N1 vs. N1 LIMI............. CHECK",
                            "THRUST REVERSE ............ DISENGAGED"}));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    9,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "SPD MODE .................. ON",
                            "STALL LIGHT ............... OFF", "TERRAIN LIGHT ............. OFF",
                            "MASTER CAUTION ............ OFF",
                            "THRUST REVERSE ............ DISENGAGED"}));

            mTaskQueue.add(new Task(1, 9, false, "FP", 90, false, true
                    , false, 9, 3, "WOAGM"));

            mTaskQueue.add(new Task(1, 10, false, "FP", 90, false, true
                    , true, 9, 3, "ROZIT"));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    10,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "HDG SEL MODE .............. ON",
                            "AP DISCON ................. OFF",
                            "PITOT HEAT ................ OFF",
                            "HYDR. PRESS. .............. BALANCED",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    11,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALT MODE .................. ON",
                            "LOW FUEL LIGHT ............ OFF",
                            "AP DISCON ................. OFF",
                            "MASTER WARNING ............ OFF",
                            "SPEED BRAKES .............. RETRACTED"}));

            mTaskQueue.add(new Task(1, 11, false, "FP", 90, false, false
                    , false, 9, 3, "EYMAO"));

            mTaskQueue.add(new Task(1, 12, false, "FP", 90, false, false
                    , true, 9, 3, "LFQIX"));

            mTaskQueue.add(new Task(
                    1,      //scenario
                    12,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    false,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALT MODE .................. ON",
                            "ALTIMETER ................. STD",
                            "LOW FUEL LIGHT ............ OFF",
                            "ICE DETECT ................ OFF",
                            "N1 vs. N1 LIMIT ........... CHECK",
                            "FLAPS ..................... UP"}));


        }



//scenario2
else if(ScenarioSelection.selectedScenario.equals("Scenario 2")) {
            mTaskQueue.add(new Task(
                    2,      //scenario
                    1,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    false,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "SPD MODE .................. ON",
                            "STALL LIGHT ............... OFF",
                            "TERRAIN LIGHT ............. OFF",
                            "MASTER WARNING ............ OFF",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(2, 1, false, "FP", 90, false, false
                    , true, 9, 3, "TAMEV"));

            mTaskQueue.add(new Task(1, 2, false, "FP", 90, false, false
                    , false, 9, 3, "BNEOG"));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    2,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "HDG SEL MODE .............. ON",
                            "AP DISCON ................. OFF",
                            "PITOT HEAT ................ OFF",
                            "MASTER CAUTION ............ OFF",
                            "THRUST REVERSE ............ DISENGAGED"}));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    3,     //Check list number
                    false,  //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALT MODE .................. ON",
                            "LOW FUEL LIGHT ............ OFF",
                            "ICE DETECT ................ OFF",
                            "THRUST LEVELS ............. BALANCED",
                            "SPEED BRAKES .............. RETRACTED"}));

            mTaskQueue.add(new Task(2, 3, false, "FP", 90, false, true
                    , true, 9, 3, "PAVQK"));

            mTaskQueue.add(new Task(1, 4, false, "FP", 90, false, true
                    , false, 9, 3, "WUPSK"));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    4,        //Check list number
                    false,    //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALTIMETER ................. STD",
                            "STALL LIGHT ............... OFF",
                            "LOW FUEL LIGHT ............ OFF",
                            "HYDR. PRESS. .............. BALANCED",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    5,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "SPD MODE .................. ON",
                            "TERRAIN LIGHT ............. OFF",
                            "PITOT HEAT ................ OFF",
                            "N1 vs. N1 LIMIT ........... CHECK",
                            "THRUST REVERSE ............ DISENGAGED"}));

            mTaskQueue.add(new Task(2, 5, false, "FP", 90, true, false
                    , true, 9, 3, "TEOZU"));

            mTaskQueue.add(new Task(2, 6, false, "FP", 90, true, false
                    , false, 9, 3, "LYZOC"));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    6,     //Check list number
                    false,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "HDG SEL MODE .............. ON",
                            "PITOT HEAT ................ OFF",
                            "ICE DETECT ................ OFF",
                            "MASTER WARNING ............ OFF",
                            "SPEED BRAKES .............. RETRACTED"}));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    7,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALT MODE .................. ON",
                            "AP DISCON ................. OFF",
                            "STALL LIGHT ............... OFF",
                            "THRUST LEVELS ............. BALANCED",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(2, 7, true, "FP", 90, true, false
                    , true, 9, 3, "DIFEO"));

            mTaskQueue.add(new Task(2, 8, true, "FP", 90, true, false
                    , false, 9, 3, "KENJS"));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    8,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALTIMETER ................. STD",
                            "LOW FUEL LIGHT ............ OFF",
                            "TERRAIN LIGHT ............. OFF",
                            "N1 vs. N1 LIMI............. CHECK",
                            "THRUST REVERSE ............ DISENGAGED"}));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    9,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "SPD MODE .................. ON",
                            "STALL LIGHT ............... OFF", "TERRAIN LIGHT ............. OFF",
                            "MASTER CAUTION ............ OFF",
                            "THRUST REVERSE ............ DISENGAGED"}));

            mTaskQueue.add(new Task(2, 9, true, "FP", 90, false, true
                    , true, 9, 3, "WOAGM"));

            mTaskQueue.add(new Task(2, 10, true, "FP", 90, false, true
                    , false, 9, 3, "ROZIT"));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    10,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "HDG SEL MODE .............. ON",
                            "AP DISCON ................. OFF",
                            "PITOT HEAT ................ OFF",
                            "HYDR. PRESS. .............. BALANCED",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    11,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    false,   //audio feedback
                    false,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALT MODE .................. ON",
                            "LOW FUEL LIGHT ............ OFF",
                            "AP DISCON ................. OFF",
                            "MASTER WARNING ............ OFF",
                            "SPEED BRAKES .............. RETRACTED"}));

            mTaskQueue.add(new Task(2, 11, false, "FP", 90, true, false
                    , true, 9, 3, "EYMAO"));

            mTaskQueue.add(new Task(2, 12, false, "FP", 90, true, false
                    , false, 9, 3, "LFQIX"));

            mTaskQueue.add(new Task(
                    2,      //scenario
                    12,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    90,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "ALT MODE .................. ON",
                            "ALTIMETER ................. STD",
                            "LOW FUEL LIGHT ............ OFF",
                            "ICE DETECT ................ OFF",
                            "N1 vs. N1 LIMIT ........... CHECK",
                            "FLAPS ..................... UP"}));
        }
        else
        {
            mTaskQueue.add(new Task(
                    1,      //scenario
                    1,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    5,      //seconds to wait after previous task complete
                    false,   //visual feedback
                    true,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "SPD MODE .................. ON",
                            "STALL LIGHT ............... OFF",
                            "TERRAIN LIGHT ............. OFF",
                            "MASTER WARNING ............ OFF",
                            "FLAPS ..................... UP"}));
            mTaskQueue.add(new Task(
                    1,      //scenario
                    1,     //Check list number
                    true,   //autopilot (true, fals
                    "CL",   //type, checklist or flight plan
                    5,      //seconds to wait after previous task complete
                    true,   //visual feedback
                    false,   //audio feedback
                    true,  //vibration
                    9,      //vibration amp
                    3,      //vibration freq
                    new String[]{   //checklist
                            "SPD MODE .................. ON",
                            "STALL LIGHT ............... OFF",
                            "TERRAIN LIGHT ............. OFF",
                            "MASTER WARNING ............ OFF",
                            "FLAPS ..................... UP"}));

            mTaskQueue.add(new Task(1, 1, true, "FP", 10, false, false
                    , false, 9, 3, "TAMEV"));
            mTaskQueue.add(new Task(1, 1, true, "FP", 10, false, true
                    , false, 9, 3, "TAMEV"));


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
