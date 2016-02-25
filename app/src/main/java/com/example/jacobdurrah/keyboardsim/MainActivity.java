package com.example.jacobdurrah.keyboardsim;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
/*
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
*/
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    private Queue<Task> mTaskQueue;
    private Queue<Vibration> mVibrationQueue;

    private VibrationHandler mVibHandler;

    private PendingIntent mTaskIntent;
    private PendingIntent mVibIntent;

    private BroadcastReceiver mTaskReceiver;
    private BroadcastReceiver mVibReceiver;

    private AlarmManager mNextTaskAlarm;
    private AlarmManager mNextVibAlarm;

    private Task mNextTask;
    private Vibration mNextVibration;

    private String mParticipantID;
    private int mNextCLScenario;
    private int mNextFPScenario;

    public final static String FP_TASK = "FP";
    public final static String CL_TASK = "CL";

    public final static String EXTRA_MESSAGE = "com.example.jacobdurrah.keyboardsim.MESSAGE";
    public final static String VIB_HANDLER = "com.example.jacobdurrah.keyboardsim.VibHandler";
    public final static String TASK_HANDLER = "com.example.jacobdurrah.keyboardsim.TaskHandler";

    public final static String BUNDLE_SCENARIO_KEY = "SCENARIO_ID";
    public final static String BUNDLE_PARTICIPANT_KEY = "PARTICIPANT_ID";

    public static boolean Vib_connected_toggle = true;

    public static String participant_ID;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // setup();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    //TODO delete this ish


    public void startFlightplanActivity(View view) {
        Intent intent = new Intent(this, FlightPlanActivity.class);
        EditText editText = (EditText) findViewById(R.id.participant_id);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        intent.putExtra(BUNDLE_SCENARIO_KEY, mNextFPScenario);
        intent.putExtra(BUNDLE_PARTICIPANT_KEY, mParticipantID);
        startActivity(intent);
    }

    public void startCheckListActivity(View view) {
        Intent intent = new Intent(this, CheckListActivity.class);
        intent.putExtra(BUNDLE_SCENARIO_KEY, mNextCLScenario);
        intent.putExtra(BUNDLE_PARTICIPANT_KEY, mParticipantID);
        startActivity(intent);
    }

    public void startScenarioSelectionActivity(View view) {
        Intent intent = new Intent(this, ScenarioSelection.class);
        EditText editText = (EditText) findViewById(R.id.participant_id);
        String participantId = editText.getText().toString();
        intent.putExtra(BUNDLE_PARTICIPANT_KEY, participantId);
        participant_ID = participantId;
        intent.putExtra(Bundle_Keys.BUNDLE_Vibration_Status_KEY, Vib_connected_toggle);
        startActivity(intent);
    }

    //used with toggle button on main screen. turns virbation on or off depending on rather
    //or not tablet is connected to the vibrator
    public void setVibrationStatus(View view)
    {
        Switch vib_connected_toggle = (Switch) findViewById(R.id.switch1);
        if(Vib_connected_toggle)
        {
            Context context = getApplicationContext();
            CharSequence text = "switch off";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Vib_connected_toggle = false;
        }
        else {

            Context context = getApplicationContext();
            CharSequence text = "switch on";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            Vib_connected_toggle = true;
        }

    }

    /*

    //Handle private member initialization and callback registration for async callbacks
    private void setup() {

        mTaskQueue = new LinkedList<>();
        mVibrationQueue = new LinkedList<>();

        mVibHandler = new VibrationHandler();
        Bundle b = getIntent().getExtras();
        String scenario = b.getString(BUNDLE_SCENARIO_KEY);
        mParticipantID = b.getString(BUNDLE_PARTICIPANT_KEY);

        FlightScenarioReader fsr;
        try {
            fsr = new FlightScenarioReader(scenario);
            fsr.updateQueues(mVibrationQueue, mTaskQueue);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "IOException on scenario file read",
                    Toast.LENGTH_LONG);
        }

        //TASKS
        mTaskReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //Toast.makeText(context, "Starting flightplan task", Toast.LENGTH_SHORT).show();
                handleTask();
            }
        };
        registerReceiver(mTaskReceiver, new IntentFilter(TASK_HANDLER));
        Intent taskIntent = new Intent(TASK_HANDLER);
        mTaskIntent = PendingIntent.getBroadcast(this, 0, taskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //VIBRATIONS
        mVibReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Toast.makeText(context, "Starting vibration", Toast.LENGTH_SHORT).show();
                handleVibration();
            }
        };
        registerReceiver(mVibReceiver, new IntentFilter(VIB_HANDLER));
        Intent vibIntent = new Intent(VIB_HANDLER);
        mVibIntent = PendingIntent.getBroadcast(this, 0, vibIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //INITIALIZE ALARMS
        mNextVibAlarm = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
        mNextTaskAlarm = (AlarmManager) (this.getSystemService(Context.ALARM_SERVICE));
    }

    private void handleVibration() {
        //Update member data to reflect next event
        Vibration thisVib = mNextVibration;
        mNextVibration = mVibrationQueue.remove();
        //Change the vibration
        boolean success = true;
        if (thisVib.getAmplitude() == -1)
            success = mVibHandler.stopVibration();
        else if (thisVib.getAmplitude() > 9 || thisVib.getFreq() < 0 || thisVib.getFreq() > 9) {
            Toast.makeText(getApplicationContext(), "Bad vibration in XML file, turning off" +
                    " vibrations", Toast.LENGTH_LONG).show();
            success = mVibHandler.stopVibration();
        } else
            success = mVibHandler.changeVibration(thisVib.getFreq(), thisVib.getAmplitude());

        if (!success) {
            Toast.makeText(getApplicationContext(), "Error in setting vibration", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        //Schedule next vibration
        Date cur;
        Date next;
        try {
            cur = new SimpleDateFormat("HH:mm:ss").parse(thisVib.getStartTime());
            next = new SimpleDateFormat("HH:mm:ss").parse(thisVib.getStartTime());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error in parsing vibration time", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        long diff = next.getTime() - cur.getTime();
        mNextVibAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                diff, mVibIntent);
    }

    private void handleTask() {
        //Update member data to reflect next event
        Task thisTask = mNextTask;
        mNextTask = mTaskQueue.remove();
        //Update display and notify user of event (call Jacob's function)
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error in sound notification", Toast.LENGTH_LONG)
                    .show();
            return;
        }

        if (thisTask.getType().equals(FP_TASK)) {
            //Call Jacob's function to display notification & button
            mNextFPScenario = thisTask.getScenario();
        } else if (thisTask.getType().equals(CL_TASK)) {
            //Call Jacob's function to display notification & button
            mNextCLScenario = thisTask.getScenario();
        } else {
            Toast.makeText(getApplicationContext(), "Bad task in XML file, not updating",
                    Toast.LENGTH_LONG).show();
            return;
        }


        //Schedule next vibration
        Date cur;
        Date next;
        try {
          //TODO: Fix broken cur = new SimpleDateFormat("HH:mm:ss").parse(thisTask.getStartTime());
          //TODO:Fix broken  next = new SimpleDateFormat("HH:mm:ss").parse(thisTask.getStartTime());
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error in parsing vibration time", Toast.LENGTH_LONG)
                    .show();
            return;
        }
       //TODO: Fix Broken long diff = next.getTime() - cur.getTime();
     //   mNextTaskAlarm.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
//                diff, mTaskIntent);
    }
    */

}
