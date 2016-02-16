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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.HashMap;
import java.util.Map;



public class CheckListActivity extends AppCompatActivity {
    public final static String BUNDLE_SCENARIO_KEY = "SCENARIO_ID";
    public final static String BUNDLE_PARTICIPANT_KEY = "PARTICIPANT_ID";
    public final static String SCENARIO_FILE_PREFIX = "";

    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;
    private TextToSpeech t1;

    private Map clickedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clickedItems = new HashMap();


        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listView );

        // Create and populate a List of planet names.
        String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",
                "Jupiter"};
        ArrayList<String> planetList = new ArrayList<String>();
        planetList.addAll( Arrays.asList(planets) );

        // Create ArrayAdapter using the planet list.
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, planetList);

        // Add more planets. If you passed a String[] instead of a List<String>
        // into the ArrayAdapter constructor, you must not add more items.
        // Otherwise an exception will occur.
        listAdapter.add( "Ceres" );

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
        Intent intent = new Intent(this, IdleScreen.class);
        startActivity(intent);
    }

    public void readText(View view)
    {
        TextView textView = (TextView) view;
        ((TextView) view).setBackgroundColor(Color.parseColor("#008000")); // custom
        String speech = textView.getText().toString();


        if(clickedItems.containsKey(speech))
        {
            clickedItems.remove(speech);

            ((TextView) view).setBackgroundColor(Color.parseColor("#FFFFFF")); // custom
            t1.speak(speech + " " + "Unchecked", TextToSpeech.QUEUE_FLUSH, null, null);

        }
        else
        {
            t1.speak(speech + " " + "checked", TextToSpeech.QUEUE_FLUSH, null, null);
            clickedItems.put(speech, 1);
        }
    }

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


}
