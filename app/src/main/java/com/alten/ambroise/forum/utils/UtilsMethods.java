package com.alten.ambroise.forum.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.Calendar;
import java.util.Set;

public class UtilsMethods {

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean isValidPhoneNumber(CharSequence target) {
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

    public static boolean isYearValid(CharSequence target) {
        if (TextUtils.isEmpty(target)) {
            return false;
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        int writtenYear = Integer.parseInt(target.toString());
        return writtenYear <= (currentYear + 10) && writtenYear > (currentYear - 80);
    }

    public static String setToString(final Set<String> stringSet) {
        if (stringSet == null || stringSet.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (final String s : stringSet) {
            if (builder.length() != 0) {
                builder.append(",");
            }
            builder.append(s);
        }
        return builder.toString();
    }
}
