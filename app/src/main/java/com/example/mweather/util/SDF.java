package com.example.mweather.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SDF {
    // return time
    public static String getTime(long epochTime){

        Date date = new Date(epochTime*1000);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm a");
        return dateFormat.format(date);
    }

    // return Date
    public static String getDate(long dateInSeconds){

        Date date = new Date(dateInSeconds * 1000);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM, yyyy");
        String dateString = dateFormat.format(date);
        //Log.e("getDate : ", dateString);
        return dateString;
    }

    // return Day
    public static String getDay(long timeInSeconds){
        // current date
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("EEE");
        String today = formatter.format(currentDate);

        // api date
        Date day = new Date(timeInSeconds * 1000);
        SimpleDateFormat datFormat = new SimpleDateFormat("EEE");
        //SimpleDateFormat formatterAPIdate = new SimpleDateFormat("dd/MM/yyyy");
        String dayNameString = datFormat.format(day);

        if (today.equals(dayNameString)){
            return "Today";
        }

        //Log.e("getTime : ", dayNameString);

        return dayNameString;
    }
}
