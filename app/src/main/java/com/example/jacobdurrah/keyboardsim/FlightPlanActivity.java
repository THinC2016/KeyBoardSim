package com.example.jacobdurrah.keyboardsim;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;


public class FlightPlanActivity extends AppCompatActivity {

    private String oldText;
    private TextToSpeech t1;

    public boolean audioFeedBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        //log start time
        IdleScreen.dataLogger.addDataPoint(
                getIntent().getStringExtra(Bundle_Keys.BUNDLE_PARTICIPANT_KEY),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_SCENARIO_KEY, 0),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_CL_WP_Num_KEY, 0),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Vibrate_KEY, true),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Audio_KEY, true),
                Long.toString(System.currentTimeMillis()) ,
                "start",
                "",
                "FP");




        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });

        audioFeedBack = getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Audio_KEY, true);

/*

        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        TextView textView = new TextView(this);
        textView.setTextSize(40);
        textView.setText(message);
        RelativeLayout layout = (RelativeLayout) findViewById(R.id.content);
        layout.addView(textView);
        */


        //get username from intent
        Intent intent = getIntent();
        String userName = intent.getStringExtra(Bundle_Keys.BUNDLE_PARTICIPANT_KEY);
        String waypoint = intent.getStringExtra(Bundle_Keys.BUNDLE_Waypoint_KEY);

        //createFileName
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        final String fileName = userName + '_' + currentDateTimeString.toString() + ".csv";
        final File file = new File(this.getFilesDir(), fileName);
        FileUtil.writeStringAsFile("Time, Char", file);

        TextView instructionView = (TextView) findViewById(R.id.textView);
        instructionView.setText("Enter Waypoint: "+ waypoint);

        EditText editText = (EditText) findViewById(R.id.sim_editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                TextView changed = (TextView) findViewById(R.id.textView);
                //changed.setText(s.toString());
                oldText = s.toString();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                TextView changed = (TextView) findViewById(R.id.textView);
                //changed.setText(Long.toString(System.currentTimeMillis()));
                String newChar = Util.getNewCharacter(oldText, s.toString());
                //.setText(newChar);
                String f =  s.toString();
                oldText = newChar;
                if(audioFeedBack) {
                    t1.speak(newChar, TextToSpeech.QUEUE_FLUSH, null, null);
                }
                FileUtil.appendStringToFile(Long.toString(System.currentTimeMillis()) + "," + newChar , file);

                //log clicked button
                IdleScreen.dataLogger.addDataPoint(
                        getIntent().getStringExtra(Bundle_Keys.BUNDLE_PARTICIPANT_KEY),
                        getIntent().getIntExtra(Bundle_Keys.BUNDLE_SCENARIO_KEY, 0),
                        getIntent().getIntExtra(Bundle_Keys.BUNDLE_CL_WP_Num_KEY, 0),
                        getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Vibrate_KEY, true),
                        getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Audio_KEY, true),
                        Long.toString(System.currentTimeMillis()),
                        "BP",
                        newChar,
                        "FP");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    public void startIdleActivity(View view) {

        TextView textView = (TextView) findViewById(R.id.textView);
        //log completed fp task
        IdleScreen.dataLogger.addDataPoint(
                getIntent().getStringExtra(Bundle_Keys.BUNDLE_PARTICIPANT_KEY),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_SCENARIO_KEY, 0),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_CL_WP_Num_KEY, 0),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Vibrate_KEY, true),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Audio_KEY, true),
                Long.toString(System.currentTimeMillis()),
                "End_FP",
                textView.getText().toString(),
                "FP");


        finish();
    }
/*
    private void readInScenario(){
        Bundle b = getIntent().getExtras();

        int scenarioNum = b.getInt(BUNDLE_SCENARIO_KEY);
        FlightScenarioReader fsr;
        try {
            fsr = new FlightScenarioReader(SCENARIO_FILE_PREFIX + scenarioNum);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error in initializing scenario reader",
                    Toast.LENGTH_LONG).show();
            return;
        }

        String fb = fsr.getScenarioFeedback();
        if(fb.equals("none")){
            Toast.makeText(getApplicationContext(), "Feedback is not provided",
                    Toast.LENGTH_LONG).show();
        }
        else if(fb.equals("vis")){
            Toast.makeText(getApplicationContext(), "Visual feedback is provided",
                    Toast.LENGTH_LONG).show();
        }
        else if(fb.equals("aud")){
            Toast.makeText(getApplicationContext(), "Audible feedback is provided",
                    Toast.LENGTH_LONG).show();
        }
    }
    */
}
