package com.example.jacobdurrah.keyboardsim;

import android.content.Context;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class VibrationSetActivity2 extends Activity {

    VibrationHandler mVibrationHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration_set2);



        mVibrationHandler = new VibrationHandler();
        mVibrationHandler.init(getApplicationContext());

    }


    public void startVibration_onClick(View view)
    {
        EditText efreq = (EditText) findViewById(R.id.freq_text);
        EditText eampl = (EditText) findViewById(R.id.ampl_text);
        int freq = Integer.parseInt(efreq.getText().toString());
        int ampl = Integer.parseInt(eampl.getText().toString());

        if(freq <0 || freq > 9 || ampl < 0 || ampl > 9) {
            Context context = getApplicationContext();
            CharSequence text = "Incorrect values: Freq <= 9 , amp <=9 both >0!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            return;
        }

        mVibrationHandler.changeVibration(freq, ampl);

    }

    public void startVibrationRandom_onClick()
    {
        mVibrationHandler.vibrateRandom();
    }

}
