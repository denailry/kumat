package com.example.user.kumat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

/**
 * Created by denail on 17/07/27.
 */

public class BubleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int width = intent.getIntExtra("width", 0);
        int height = intent.getIntExtra("height", 0);

        boolean isOpenInTheDay = intent.getBooleanExtra("isOpenInTheDay", false);

        if(!isOpenInTheDay) {
            try {
                Intent bubbleIntent = new Intent(context, Bubble.class);
                bubbleIntent.putExtra("width", width);
                bubbleIntent.putExtra("height", height);
                context.startService(bubbleIntent);
            } catch (SecurityException e) {
                Log.e(getClass().getSimpleName(), e.toString());
            }
        }
    }
}