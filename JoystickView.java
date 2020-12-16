package com.robotcontroll;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class JoystickView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener
{

    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private JoystickListener joystickCallback;


    //it helps with scaling on different types of screens
    void setupDimensions(){
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 3;
        hatRadius = Math.min(getWidth(), getHeight()) / 5;

    }


    public JoystickView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    //next 3 constructors are just like the ones in super class

    public JoystickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }

    public JoystickView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this); //dodaję to żeby było wiadomo że call back odnoszę do metod stworzoncyh w tej klasie nie w klasie wyżej
        setOnTouchListener(this);
        if(context instanceof JoystickListener)
            joystickCallback = (JoystickListener) context;
    }


    //tak naprawdę nie muszę używać Override bo te metdoy są puste same w ssobie poprzez to że użwyam interfacu dla nich (który tworzy metody ale nie mające środka)


    @Override
    public void surfaceCreated(SurfaceHolder holder){
        setupDimensions();
        drawJoystick(centerX,centerY);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder,int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder){

    }

    //drawing the joystick using canvas
    private void drawJoystick(float positionX, float positionY)
    {
        Canvas myCanvas = this.getHolder().lockCanvas();
        Paint colors = new Paint();
        myCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        colors.setARGB(255, 128,123,  124);
        myCanvas.drawCircle(centerX, centerY, baseRadius, colors);
        colors.setARGB(255,0,50,50);
        myCanvas.drawCircle(positionX,positionY,hatRadius,colors);
        getHolder().unlockCanvasAndPost(myCanvas);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.equals(this))
        {
            //drawing joystick in position where is pressed (event)
            if(event.getAction() != event.ACTION_UP)
            {
                //calculating the displacement between the center of the joystick and the point where u clicked
                float displacement = (float) Math.sqrt(Math.pow(event.getX() - centerX, 2) + Math.pow(event.getY() - centerY, 2));

                //it within the joystick
                if (displacement<baseRadius) {
                    drawJoystick(event.getX(), event.getY());
                    //not sure, to check!
                    joystickCallback.onJoystickMoved((event.getX() - centerX) / baseRadius, (event.getY() - centerY) / baseRadius, getId());
                }

                //its outside of our joystick
                else{
                    //calculating the ratio to calculate the center of the moved hat
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (event.getX() - centerX) * ratio;
                    float constrainedY = centerY + (event.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    //not sure
                    joystickCallback.onJoystickMoved((constrainedX - centerX) / baseRadius, (constrainedY - centerY) / baseRadius, getId());
                }
            }
            //if is not pressed will go back to its' starting position
            else
            {
                drawJoystick(centerX,centerY);
                joystickCallback.onJoystickMoved(0, 0, getId());
            }
            //not sure

        }
        //returning true allowing us to multitouch it
        return true;
    }

    //creating interface so  u can use it outside somewhere? tutaj zmieniłem z public !!!!!!!!!!!!!!!!
    //używam tego żeby później móc odnieść się do joysticka czytając pozycję np
    interface JoystickListener
    {

        void onJoystickMoved(float xPercent, float yPercent, int source);
    }
}


