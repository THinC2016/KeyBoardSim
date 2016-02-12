package com.example.jacobdurrah.keyboardsim;

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
    private String mStartTime;

    public Task(int scen, String type, String start){
        mScenario = scen;
        mType = type;
        mStartTime = start;
    }

    public int getScenario(){
        return mScenario;
    }

    public String getType(){
        return mType;
    }

    public String getStartTime(){
        return mStartTime;
    }
}
