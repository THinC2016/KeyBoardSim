package com.example.jacobdurrah.keyboardsim;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class ScenarioSelection extends AppCompatActivity {
    public final static String BUNDLE_SCENARIO_KEY = "SCENARIO_ID";
    private ListView mainListView ;
    private ArrayAdapter<String> listAdapter ;

    private List<String> clickedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scenario_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        clickedItems = new ArrayList<>();

        // Find the ListView resource.
        mainListView = (ListView) findViewById( R.id.listScenarios );

        listAdapter = new ArrayAdapter<String>(this, R.layout.simple_row_scenario);
        listAdapter.add("Scenario 1");
        listAdapter.add("Scenario 2");
        listAdapter.add("Scenario 3");
        listAdapter.add("Scenario 4");
        mainListView.setAdapter( listAdapter );

    }

    public void scenarioCLicked(View view) {
        TextView textView = (TextView) view;
        String scenarioFileName = textView.getText().toString();

        if(clickedItems.size() >=1 && !clickedItems.contains(scenarioFileName))
        {
            Context context = getApplicationContext();
            CharSequence text = "Only one Scenario can be checked!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        else if(clickedItems.contains(scenarioFileName))
        {
            clickedItems.remove(scenarioFileName);

            ((TextView) view).setBackgroundColor(Color.parseColor("#FFFFFF")); // custom
        }
        else
        {
            ((TextView) view).setBackgroundColor(Color.parseColor("#008000")); // custom
            clickedItems.add(scenarioFileName);
        }
    }

    public void startCountDownActivity(View view) {
        if (clickedItems.size() == 0) {
            Context context = getApplicationContext();
            CharSequence text = "Please Select a Scenario!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }
        else if(clickedItems.size() > 1)
        {
            Context context = getApplicationContext();
            CharSequence text = "Too many Scenarios selected";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
        //TODO: convert clickedItems to a list

        Intent intent = new Intent(this, CountDown.class);
        intent.putExtra(BUNDLE_SCENARIO_KEY, clickedItems.remove(0));
        startActivity(intent);


    }



}
