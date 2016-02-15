package com.example.jacobdurrah.keyboardsim;

import android.content.Context;
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
import java.util.Map;




public class ScenarioSelection extends AppCompatActivity {
    public final static String BUNDLE_SCENARIO_KEY = "SCENARIO_ID";
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    private Map clickedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listScenarios );

        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow);

        //Get a list of all names in folder ___ that contain the value val in the filename
        listAdapter.addAll(FileUtil.getFileNamesInFolderContainVal("examFolder", "example"));
        // Set the ArrayAdapter as the ListView's adapter.
        mainListView.setAdapter( listAdapter );

    }

    public void startSimActivity(View view) {
        TextView textView = (TextView) view;
        String scenarioFileName = textView.getText().toString();

        if(clickedItems.size() >=1 && !clickedItems.containsKey(scenarioFileName))
        {
            Context context = getApplicationContext();
            CharSequence text = "Only one Scenario can be checked!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if(clickedItems.containsKey(scenarioFileName))
        {
            clickedItems.remove(scenarioFileName);

            ((TextView) view).setBackgroundColor(Color.parseColor("#FFFFFF")); // custom
        }
        else
        {
            ((TextView) view).setBackgroundColor(Color.parseColor("#008000")); // custom
            clickedItems.put(scenarioFileName, 1);
        }
    }

    public void startCountDownActivity(View view) {
        if (clickedItems.size() == 0) {
            Context context = getApplicationContext();
            CharSequence text = "Please Select a Scenario!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }
        String Scenario_ID = (String)clickedItems.entrySet().iterator().next();
        Intent intent = new Intent(this, CountDown.class);
        intent.putExtra(BUNDLE_SCENARIO_KEY, Scenario_ID);
        startActivity(intent);


    }



}
