package com.example.jacobdurrah.keyboardsim;

import android.content.Intent;
import android.os.Bundle;
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




public class FlightPlanActivity extends AppCompatActivity {
    public final static String BUNDLE_SCENARIO_KEY = "SCENARIO_ID";
    public final static String BUNDLE_PARTICIPANT_KEY = "PARTICIPANT_ID";
    public final static String SCENARIO_FILE_PREFIX = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
        String userName = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        //createFileName
        final String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
        final String fileName = userName + '_' + currentDateTimeString.toString() + ".csv";
        final File file = new File(this.getFilesDir(), fileName);
        FileUtil.writeStringAsFile("Time, Char", file);



        EditText editText = (EditText) findViewById(R.id.sim_editText);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                TextView changed = (TextView) findViewById(R.id.textView);
                changed.setText(s.toString());

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                TextView changed = (TextView) findViewById(R.id.textView);
                //changed.setText(Long.toString(System.currentTimeMillis()));
                String newChar = Util.getNewCharacter(changed.getText().toString(), s.toString());
                changed.setText(newChar);
                FileUtil.appendStringToFile(Long.toString(System.currentTimeMillis()) + "," + newChar , file);
            }
        });



    }


    private void readInScenario(){
        Bundle b = getIntent().getExtras();

        int scenarioNum = b.getInt(BUNDLE_SCENARIO_KEY);
        FlightScenarioReader fsr;
        try {
            new FlightScenarioReader(SCENARIO_FILE_PREFIX + scenarioNum);
        } catch (Exception e){
            Toast.makeText(getApplicationContext(), "Error in initializing scenario reader",
                    Toast.LENGTH_LONG).show();
        }
    }


}
