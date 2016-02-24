package com.example.jacobdurrah.keyboardsim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jacobdurrah.keyboardsim.R;
import com.example.jacobdurrah.keyboardsim.Vibration;
import com.example.jacobdurrah.keyboardsim.VibrationHandler;

/**
 * Created by jknudson on 2/18/2016.
 */
public class VibrationSetActivity extends AppCompatActivity {
    VibrationHandler mVibrationHandler;

    @Override
    protected  void onCreate(Bundle savedInstanceBundle) {
        super.onCreate(savedInstanceBundle);
        add_Button(true);

        mVibrationHandler = new VibrationHandler();
        mVibrationHandler.init(getApplicationContext());
    }

    public void add_Button(boolean add)
    {
        LinearLayout mainLayout = (LinearLayout)findViewById(R.id.idle_layout);
        if(add) {
            Button addButton = new Button(this);

            addButton.setId(0);
            addButton.setText("Send vibration");
            addButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    View set_vibration_layout = getLayoutInflater().inflate(R.layout.set_vibration_layout, null);
                    EditText efreq = (EditText) set_vibration_layout.findViewById(R.id.freq_text);
                    EditText eampl = (EditText) set_vibration_layout.findViewById(R.id.ampl_text);
                    int freq = Integer.parseInt(efreq.toString());
                    int ampl = Integer.parseInt(eampl.toString());

                    if(freq <0 || freq > 9 || ampl < 0 || ampl > 9)
                        return;

                    mVibrationHandler.changeVibration(freq, ampl);

                }
            });

            mainLayout.addView(addButton);
        }
        else
        {
            mainLayout.removeAllViews();
        }

    }

}
