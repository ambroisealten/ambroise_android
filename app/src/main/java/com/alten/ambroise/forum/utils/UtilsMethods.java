package com.alten.ambroise.forum.utils;

import android.text.TextUtils;
import android.util.Patterns;

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
}
