package com.example.jacobdurrah.keyboardsim;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;


/**
 * Created by jacobdurrah on 2/24/16.
 * //loggs all data required for the app
 */
public class DataLogger extends AppCompatActivity {

   private static String mEmail ="Participant ID,Scenario, " +
           "Checklist ,#,Auto/Man,Vibration,Feedback,time,action,content\n";




    public void addDataPoint(String participantID, int Scenario, int checklistNum, boolean vibration,
                 boolean feedBack, String time, String action,String content, String type, boolean auto_man)
    {
        mEmail += MainActivity.participant_ID + "," + Scenario + "," +type + "," + checklistNum +"," + auto_man + "," + vibration + "," +
                feedBack + "," + time + "," +action + "," + content + "\n";

    }

    public String getMEmail(){return mEmail; }




}
