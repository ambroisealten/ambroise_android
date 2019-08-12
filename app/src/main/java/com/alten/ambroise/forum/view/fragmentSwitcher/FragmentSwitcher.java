package com.alten.ambroise.forum.view.fragmentSwitcher;

import android.os.Parcelable;

import androidx.fragment.app.FragmentManager;

import com.alten.ambroise.forum.data.model.beans.ApplicantForum;

public interface FragmentSwitcher extends Parcelable {
    void switchFragment(FragmentManager fm, String tag, Object... args);

    void onItemClick(ApplicantForum applicant);
}
