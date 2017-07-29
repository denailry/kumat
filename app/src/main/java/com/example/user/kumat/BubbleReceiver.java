package com.example.user.kumat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import com.example.user.kumat.Database.PengaturanDatabase;
import com.raizlabs.android.dbflow.sql.language.Select;

/**
 * Created by denail on 17/07/27.
 */

public class BubbleReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int width = intent.getIntExtra("width", 0);
        int height = intent.getIntExtra("height", 0);
        int receivedTimeId = intent.getIntExtra("timeId", 0);

        int currentTimeId = IdGen.generateTimeId();
        boolean isAllowed = true;

        PengaturanDatabase setting = new Select()
                .from(PengaturanDatabase.class)
                .querySingle();

        if(setting != null) {
            isAllowed = setting.isNotifHomeAllowed();
        }

        if(isAllowed && receivedTimeId != currentTimeId) {
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