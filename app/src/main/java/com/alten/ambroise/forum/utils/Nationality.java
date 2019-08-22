package com.alten.ambroise.forum.utils;

import com.alten.ambroise.forum.App;
import com.alten.ambroise.forum.R;

public enum Nationality {
    NONE, FRENCH, EUROPEAN, OTHER;

    public static Nationality getNationality(String nationality) {
        if (nationality.equals(App.getContext().getString(R.string.none))) {
            return NONE;
        } else if (nationality.equals(App.getContext().getString(R.string.french_nationality))) {
            return FRENCH;
        } else if (nationality.equals(App.getContext().getString(R.string.european_citizenship))) {
            return EUROPEAN;
        } else if (nationality.equals(App.getContext().getString(R.string.other_nationality))) {
            return OTHER;
        } else {
            return NONE;
        }
    }
}


