package com.robotcontroll;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity implements JoystickView.JoystickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //creating my joystick(s)
        //JoystickView joy1 = new JoystickView(this);

        //set your joystick as the only view in tablet
        //setContentView(joy1);
        JoystickView joystick = new JoystickView(this);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onJoystickMoved(float xPercent, float yPercent, int id) {

//        Log.d("Main Method", "X percent: " + xPercent + " Y percent: " + yPercent);

        switch(id)
        {

            case R.id.joystickRight:

                Log.d("Right Joystick", "X: " + xPercent + " Y: " + yPercent);

                break;

            case R.id.joystickLeft:

                Log.d("Left Joystick", "X: " + xPercent + " Y: " + yPercent);
                break;

        };

    }
}