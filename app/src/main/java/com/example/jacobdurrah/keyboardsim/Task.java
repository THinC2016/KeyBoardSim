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
    private int  mvibration_amp;
    private int  mvibration_freq;
    private int check_list_way_point_number;
    private boolean mAutopilot;

    public Task(int scen, int num, boolean auto, String type, int secondsToWait, boolean visualFeedBack, boolean audioFeedBack
                , boolean vibration, int vibration_amp , int vibration_freq, String waypoint){//waypoint constructor
        mScenario = scen;
        mType = type;
        mSecondsToWait = secondsToWait;//number of seconds after previous task completed
        mWaypoint = waypoint;
        mVibration = vibration;
        mVisualFeedBack = visualFeedBack;
        mAudioFeedBack = audioFeedBack;
        mvibration_amp = vibration_amp;
        mvibration_freq = vibration_freq;
        check_list_way_point_number = num;
        mAutopilot = auto;



    }
    public Task(int scen, int num, boolean auto, String type, int secondsToWait, boolean visualFeedBack, boolean audioFeedBack
            , boolean vibration, int vibration_amp , int vibration_freq, String[] checkList){//checklist constructor
        mScenario = scen;
        mType = type;
        mSecondsToWait = secondsToWait;//number of seconds after previous task completed
        mCheckList = new ArrayList<String>(Arrays.asList(checkList));
        mVibration = vibration;
        mVisualFeedBack = visualFeedBack;
        mAudioFeedBack = audioFeedBack;
        mvibration_amp = vibration_amp;
        mvibration_freq = vibration_freq;
        check_list_way_point_number = num;
        mAutopilot = auto;


    }


    public int getScenario(){return mScenario;}
    public boolean getmAutopilot(){return mAutopilot;}
    public  int getCheck_list_way_point_number(){return check_list_way_point_number;}
    public int getMvibration_amp(){return mvibration_amp;}
    public int getMvibration_freq(){return mvibration_freq;}
    public String getType(){return mType;}
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
        return mAudioFeedBack;
    }
    public boolean getmVibration(){
        return mVibration;
    }


}
