package com.example.jacobdurrah.keyboardsim;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.hoho.android.usbserial.driver.UsbSerialPort;
import com.hoho.android.usbserial.util.HexDump;
import com.hoho.android.usbserial.util.SerialInputOutputManager;

import java.io.IOException;
import java.util.List;

/**
 * Created by John on 2/10/2016.
 *
 * Class meant to drive & handle vibration output to an Arduino
 *
 * Requires usbserial library:
 * https://github.com/mik3y/usb-serial-for-android
 *
 * Follow steps on readme page to install & link
 */
public class VibrationHandler {

    private static final int BAUD_RATE      = 9600;
    private static final int DATA_BITS      = UsbSerialPort.DATABITS_8;
    private static final int STOP_BITS      = UsbSerialPort.STOPBITS_1;
    private static final int PARITY         = UsbSerialPort.PARITY_NONE;

    private static final int TIMEOUT        = 1000;

    private static final char HALT          = 'h';
    private static final char RUN           = 'r';
    private static final char FREQ          = 'f';
    private static final char AMPL          = 'a';
    private static final char CARR          = '\r';

    private static final String LOG_TAG     = "VibrationHandler";

    private int mAmpl;
    private int mFreq;

    private UsbSerialPort mPort;
    private UsbManager mUSBManager;
    private SerialInputOutputManager mIOManager;
    private Context             ctxt;
    public VibrationHandler(){
        mAmpl = -1;
        mFreq = -1;
        ctxt = null;
    }

    public boolean init(Context ctxt){
        this.ctxt = ctxt;
        mUSBManager = (UsbManager) ctxt.getSystemService(Context.USB_SERVICE);
        List<UsbSerialDriver> availDrivers = UsbSerialProber.getDefaultProber().findAllDrivers(mUSBManager);
        if(availDrivers.isEmpty()) {
            Log.i(LOG_TAG, "No available drivers");
            return false;
        }
        UsbSerialDriver driver = availDrivers.get(0);
        UsbDeviceConnection cnxn = mUSBManager.openDevice(driver.getDevice());
        if(cnxn == null) {
            Log.i(LOG_TAG, "Null connection");
            return false;
        }

        mPort = driver.getPorts().get(0);

        try {
            mPort.open(cnxn);
            mPort.setParameters(BAUD_RATE, DATA_BITS, STOP_BITS, PARITY);
        } catch (IOException e){
            return false;
        }

        return true;
    }

    public boolean stopVibration(){
        String msg = new StringBuilder().append(HALT).append(CARR).toString();

        boolean success = true;
        try {
            mPort.write(msg.getBytes(), TIMEOUT);
            mAmpl = -1;
            mFreq = -1;
        } catch (IOException e) {
            success = false;
            Log.i(LOG_TAG, "IOError in stopVibration");
        }
        return success;
    }

    public boolean changeVibration(int freq, int ampl){
        if(freq > 9 || freq < 0 || ampl > 9 || ampl < 0)
            return false;

        boolean success = true;


        success = success && setFreq(freq);
        success = success && setAmpl(ampl);

        String msg = new StringBuilder().append(RUN).toString();

        try {
            mPort.write(msg.getBytes(), TIMEOUT);
        } catch (IOException e) {
            Log.i(LOG_TAG, "IOError in changeVibration");
            success = false;
        }

        return success;
    }

    private boolean setAmpl(int ampl){
        boolean success = true;
        String msg = new StringBuilder().append(AMPL).append(ampl).toString();

        try {
            mPort.write(msg.getBytes(), TIMEOUT);
            mAmpl = ampl;
        } catch (IOException e){
            Log.i(LOG_TAG, "IOError in setAmplitude");
            success = false;
        }
        return success;
    }

    private boolean setFreq(int freq){
        boolean success = true;
        String msg = new StringBuilder().append(FREQ).append(freq).toString();

        try {
            mPort.write(msg.getBytes(), TIMEOUT);
            mFreq = freq;
        } catch (IOException e){
            Log.i(LOG_TAG, "IOError in setFrequency");
            success = false;
        }
        return success;
    }
}
