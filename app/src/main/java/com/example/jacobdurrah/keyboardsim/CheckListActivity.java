package com.example.jacobdurrah.keyboardsim;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;



public class CheckListActivity extends AppCompatActivity {
    public final static String SCENARIO_FILE_PREFIX = "";

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private TextToSpeech t1;

    //Information needed
    //Audio Feedback
    private boolean audioFeedBack;
    //Visual Feed
    private boolean visualFeedBack;
    //List of items to be displayed


    private Map clickedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);

        //log start time
        IdleScreen.dataLogger.addDataPoint(
                getIntent().getStringExtra(Bundle_Keys.BUNDLE_PARTICIPANT_KEY),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_SCENARIO_KEY, 0),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_CL_WP_Num_KEY, 0),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Vibrate_KEY, true),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Audio_KEY, true),
                Long.toString(System.currentTimeMillis()),
                "start",
                "",
                "CL",
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Auto_Manu_KEY, true));


        clickedItems = new HashMap();

        //Information needed
        //Audio Feedback
        audioFeedBack = getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Audio_KEY, true);
        //Visual Feed
        visualFeedBack = getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Visual_KEY, true);

        //List of items to be displayed

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listView );

        Intent intent = getIntent();
        ArrayList<String> list = new ArrayList<String>(intent.getStringArrayListExtra(Bundle_Keys.BUNDLE_Checklist_KEY));

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, list);

        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );

        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                }
            }
        });
    }
    public void startIdleActivity(View view) {
        ArrayList<String> list = new ArrayList<String>(getIntent().getStringArrayListExtra(Bundle_Keys.BUNDLE_Checklist_KEY));
        int numUnchecked = list.size() - clickedItems.size();

        //log start time
        IdleScreen.dataLogger.addDataPoint(
                getIntent().getStringExtra(Bundle_Keys.BUNDLE_PARTICIPANT_KEY),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_SCENARIO_KEY, 0),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_CL_WP_Num_KEY, 0),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Vibrate_KEY, true),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Audio_KEY, true),
                Long.toString(System.currentTimeMillis()),
                "End_CL",
                String.valueOf(numUnchecked),//number of unchecked items
                "CL",
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Auto_Manu_KEY, true));

        finish();
    }

    public void readText(View view)
    {
        String clicked = "click";

        CheckedTextView textView = (CheckedTextView) view;
        if(visualFeedBack) {
            textView.setBackgroundColor(Color.parseColor("#008000")); // custom
            ((CheckedTextView) view).setChecked(true);
        }
        else
        {
            ((CheckedTextView) view).setChecked(true);
        }

        String speech = textView.getText().toString();


        if(clickedItems.containsKey(speech))
        {
            clickedItems.remove(speech);
            if(visualFeedBack) {
                ((CheckedTextView) view).setChecked(false);
                ((CheckedTextView) view).setBackgroundColor(Color.parseColor("#FFFFFF")); // custom
            }
            else
            {
                ((CheckedTextView) view).setChecked(false);
            }
            if(audioFeedBack) {
                t1.speak(speech + ". " + "Unchecked", TextToSpeech.QUEUE_FLUSH, null, null);
            }
            clicked = "unclicked";
        }
        else
        {
            if(audioFeedBack) {
                t1.speak(speech + ". " + "checked", TextToSpeech.QUEUE_FLUSH, null, null);
            }
            clickedItems.put(speech, 1);

        }


        //log start time
        IdleScreen.dataLogger.addDataPoint(
                getIntent().getStringExtra(Bundle_Keys.BUNDLE_PARTICIPANT_KEY),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_SCENARIO_KEY, 0),
                getIntent().getIntExtra(Bundle_Keys.BUNDLE_CL_WP_Num_KEY, 0),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Vibrate_KEY, true),
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Audio_KEY, true),
                Long.toString(System.currentTimeMillis()),
                clicked,
                speech,//checklist value
                "CL",
                getIntent().getBooleanExtra(Bundle_Keys.BUNDLE_Auto_Manu_KEY, true));
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

