package com.example.user.kumat;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by denail on 17/07/11.
 */

public class IdGen {
    public static Long generateId() {
        return (new GregorianCalendar()).getTimeInMillis();
    }

    public static int[] generateTime() {
        Calendar calendar = new GregorianCalendar();
        int time[] = new int[3];
        time[0] = calendar.get(Calendar.DAY_OF_MONTH);
        time[1] = calendar.get(Calendar.MONTH);
        time[2] = calendar.get(Calendar.YEAR);

        return time;
    }

    public static int generateTimeId() {
        int time[] = IdGen.generateTime();
        return time[2]*10000 + time[1]*100 + time[0];
    }
}
