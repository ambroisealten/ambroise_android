package com.alten.ambroise.forum.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Calendar;

public class UtilsMethods {

    public final static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public final static boolean isValidPhoneNumber(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.PHONE.matcher(target).matches();
    }

    public static boolean isValidMultipleEmail(CharSequence targets, final String separator) {
        final String[] targetSpliced = targets.toString().split(separator);
        for (CharSequence target : targetSpliced) {
            if (!isValidEmail(target)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isYearValid(CharSequence target){
        if(TextUtils.isEmpty(target)) {
            return false;
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int writtenYear = Integer.parseInt(target.toString());
        return writtenYear <= (currentYear + 10) && writtenYear > (currentYear - 80);
    }
}
