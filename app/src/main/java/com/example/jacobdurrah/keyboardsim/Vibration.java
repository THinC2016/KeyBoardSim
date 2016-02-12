package com.example.jacobdurrah.keyboardsim;

/**
 * Created by John on 2/12/2016.
 *
 * Simple class to store a vibration event read in from an XML file
 */
public class Vibration {
    private int mFrequency;
    private int mAmplitude;
    private String mStartTime;

    public Vibration(int freq, int ampl, String start){
        mFrequency = freq;
        mAmplitude = ampl;
        mStartTime = start;
    }

    public int getFreq(){
        return mFrequency;
    }

    public int getAmplitude(){
        return mAmplitude;
    }

    public String getStartTime(){
        return mStartTime;
    }
}
