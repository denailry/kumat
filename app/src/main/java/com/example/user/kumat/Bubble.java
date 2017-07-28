package com.example.user.kumat;

import android.app.Service;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by denail on 17/07/25.
 */

public class Bubble extends Service {

    WindowManager windowManager, trashManager;
    ImageView bubble;
    Button trash;
    RelativeLayout relativeLayout;

    int displayWidth, displayHeight;
    float halfDisplayWidth;
    boolean isOnTouch = false;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        displayWidth = intent.getIntExtra("width", 0);
        halfDisplayWidth = displayWidth / 2;
        displayHeight = intent.getIntExtra("height", 0);

        relativeLayout = new RelativeLayout(this);
        relativeLayout.setLayoutParams(new RelativeLayout.LayoutParams(150, displayHeight));
        relativeLayout.setBackgroundResource(R.drawable.bubble_trash);
//        relativeLayout.setBackgroundColor(Color.parseColor("#00FF00"));
        relativeLayout.setGravity(Gravity.CENTER);
//        trash = new Button(this);
//        trash.setLayoutParams(new ViewGroup.LayoutParams(50, 50));
//        trash.setBackgroundColor(Color.parseColor("#FF00FF"));
//        relativeLayout.addView(trash);
        relativeLayout.setVisibility(View.GONE);

        bubble = new ImageView(this);
        //a face floating bubble as imageView
        bubble.setImageResource(R.drawable.bubble_icon);

        trashManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams trashParams = new WindowManager.LayoutParams(
                400,
                displayHeight,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        trashParams.x = displayWidth/2;
        trashParams.y = 0;
        try {
            trashManager.addView(relativeLayout, trashParams);
        } catch (WindowManager.BadTokenException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams myParams = new WindowManager.LayoutParams(
                300,
                300,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        myParams.x = -displayWidth/2;
        myParams.y = displayHeight/2;
        try {
            windowManager.addView(bubble, myParams);
        } catch (WindowManager.BadTokenException e) {
            Log.e(getClass().getSimpleName(), e.toString());
        }

        try{
            //for moving the picture on touch and slide
            bubble.setOnTouchListener(new View.OnTouchListener() {
                WindowManager.LayoutParams paramsT = myParams;

                private int initialX;
                private int initialY;
                private float initialTouchX;
                private float initialTouchY;
                private int boundary = displayWidth - 400;
                private long initialTime;

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int coordinate[] = new int[2];

                    v.getLocationOnScreen(coordinate);

                    //remove face bubble on long press
                    switch(event.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            relativeLayout.setVisibility(View.VISIBLE);
                            initialX = paramsT.x;
                            initialY = paramsT.y;
                            initialTime = System.currentTimeMillis();
                            initialTouchX = event.getRawX();
                            initialTouchY = event.getRawY();
                            isOnTouch = true;
                            break;
                        case MotionEvent.ACTION_UP:
                            if(System.currentTimeMillis() - initialTime < ViewConfiguration.getLongPressTimeout()) {
                                Intent activityStart = new Intent(getApplicationContext(), MainActivity.class);
                                activityStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(activityStart);
                                windowManager.removeView(bubble);
                                trashManager.removeView(relativeLayout);
                                stopSelf();
                            }
                            relativeLayout.setVisibility(View.GONE);
                            if(coordinate[0] > boundary) {
                                windowManager.removeView(bubble);
                                trashManager.removeView(relativeLayout);
                                stopSelf();
                            }
                            isOnTouch = false;
                            break;
                        case MotionEvent.ACTION_MOVE:
                            if(event.getRawX() + halfDisplayWidth < boundary+500) {
                                paramsT.x = initialX + (int) (event.getRawX() - initialTouchX);
                            } else {
                                paramsT.x = displayWidth;
                            }
                            paramsT.y = initialY + (int) (event.getRawY() - initialTouchY);
                            windowManager.updateViewLayout(v, paramsT);
                            break;
                    }
                    return false;
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }

        bubble.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View view, int i, int i1, int i2, int i3, int i4, int i5, int i6, int i7) {
                if(!isOnTouch) {
                    int coordinate[] = new int[2];
                    view.getLocationOnScreen(coordinate);
                    int boundary = displayWidth - 200;
                    if(coordinate[0] > boundary) {
                        windowManager.removeView(bubble);
                        stopSelf();
                    }
                }
            }
        });

//        bubble.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent activityStart = new Intent(getApplicationContext(), MainActivity.class);
//                activityStart.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(activityStart);
//                windowManager.removeView(bubble);
//                trashManager.removeView(relativeLayout);
//                stopSelf();
//            }
//        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
