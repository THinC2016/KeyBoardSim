package com.example.jacobdurrah.keyboardsim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by John on 2/12/2016.
 *
 * Simple class meant to hold information related to flightplan
 * and checklist tasks.
 *
 * This is meant to be held in a queue by the main activity
 */
public class Task {
    private int mScenario;
    private String mType;
    private int mSecondsToWait;
    private ArrayList<String> mCheckList;
    private String mWaypoint;
    private boolean mVisualFeedBack;
    private boolean mAudioFeedBack;
    private boolean mVibration;

    public Task(int scen, String type, int secondsToWait, boolean visualFeedBack, boolean audioFeedBack
                , boolean vibration, String waypoint ){//waypoint constructor
        mScenario = scen;
        mType = type;
        mSecondsToWait = secondsToWait;//number of seconds after previous task completed
        mWaypoint = waypoint;
        mVibration = vibration;
        mVisualFeedBack = visualFeedBack;
        mAudioFeedBack = audioFeedBack;


    }
    public Task(int scen, String type, int secondsToWait, boolean visualFeedBack, boolean audioFeedBack
            , boolean vibration, String[] checkList){//checklist constructor
        mScenario = scen;
        mType = type;
        mSecondsToWait = secondsToWait;//number of seconds after previous task completed
        mCheckList = new ArrayList<String>(Arrays.asList(checkList));
        mVibration = vibration;
        mVisualFeedBack = visualFeedBack;
        mAudioFeedBack = audioFeedBack;

    }

    public int getScenario(){
        return mScenario;
    }

    public String getType(){
        return mType;
    }
    public int getmSecondsToWait(){
        return mSecondsToWait;
    }
    public ArrayList<String> getmCheckList(){
        return mCheckList;
    }
    public String getmWaypoint(){
        return mWaypoint;
    }
    public boolean getmVisualFeedBack(){
        return mVisualFeedBack;
    }
    public boolean getmAudioFeedBack(){
        return mVisualFeedBack;
    }
    public boolean getmVibration(){
        return mVibration;
    }
}
