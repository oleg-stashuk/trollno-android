package com.apps.trollino.utils;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.apps.trollino.utils.data.Const.LOG_TAG;

public class ShowTimeAgoHelper {

    public static String showTimeAgo(String postTime) {
        DateFormat dateFormat;
        if(postTime.length() > 18) {
            postTime = postTime.substring(4);
            dateFormat = new SimpleDateFormat("MM/dd/yyyy - hh:mm");
        } else {
            dateFormat = new SimpleDateFormat("dd.MM.yyyy - hh:mm");
        }

        try {
            Date date = dateFormat.parse(postTime);
            GregorianCalendar dataFromApi  = new GregorianCalendar();
            dataFromApi.setTime(date);

            return choiceCorrectDataMessage(dataFromApi.getTimeInMillis());
        } catch (ParseException e) {
            Log.d(LOG_TAG, "!!!!!!!!!!!! ParseException " + e.getLocalizedMessage());
            e.printStackTrace();
            return postTime;
        }
    }

    private static String choiceCorrectDataMessage(long longPostDate) {
        GregorianCalendar cal = new GregorianCalendar();
        long longCurrentDate = cal.getTimeInMillis();

        long yearAgo = (longCurrentDate - longPostDate) / 1000 / 3600 / 8760;
        long monthAgo = (longCurrentDate - longPostDate) / 1000 / 3600 / 730;
        long dayAgo = (longCurrentDate - longPostDate) / 1000 / 86400;
        long hourAgo = (longCurrentDate - longPostDate) / 1000 / 3600;
        long minAgo = (longCurrentDate - longPostDate) / 1000 / 60;

        if(yearAgo > 0) {
            return yearString((int) yearAgo);
        } else if(monthAgo > 0){
            return monthString((int) monthAgo);
        } else if(dayAgo > 0) {
            return dayAgo < 7 ? dayString((int) dayAgo) : weekString((int) dayAgo);
        } else if(hourAgo > 0) {
            return hourString((int) hourAgo);
        } else if(minAgo > 0) {
            return minutesString((int) minAgo);
        } else {
            return "Несколько секунд назад";
        }
    }

    private static String yearString(int postYear) {
        return postYear + ((postYear / 10 % 10 == 1 ||  postYear % 10 > 5) ?
                " лет назад" :
                (postYear % 10 == 1 ? " год назад" : " года назад"));
    }

    private static String monthString(int postMonth) {
        return postMonth + (postMonth % 10 == 1 ?
                " месяц назад" :
                (postMonth % 10 <= 4 ? " месяца назад" : " месяцев назад"));
    }

    private static String weekString(int postDay) {
        return (postDay / 7) + ((postDay / 7 == 1) ?
                " неделя назад" :
                (postDay / 7 < 5 ? " недели назад" : " недель назад"));
    }

    private static String dayString(int postDay) {
        return postDay + ((postDay / 10 % 10 == 1 ||  postDay % 10 > 5) ?
                " дней назад" :
                (postDay % 10 == 1 ? " день назад" : " дня назад"));
    }

    private static String hourString(int postHour) {
        return postHour + ((postHour / 10 % 10 == 1 ||  postHour % 10 > 4) ?
                " часов назад" :
                (postHour % 10 == 1 ? " час назад" : " часа назад"));
    }

    private static String minutesString(int postMinutes) {
        return postMinutes + ((postMinutes / 10 % 10 == 1 ||  postMinutes % 10 > 5) ?
                " минут назад" :
                (postMinutes % 10 == 1 ? " минута назад" : " минуты назад"));
    }
}
