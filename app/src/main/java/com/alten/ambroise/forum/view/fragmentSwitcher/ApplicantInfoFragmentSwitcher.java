package com.alten.ambroise.forum.view.fragmentSwitcher;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;


public class ApplicantInfoFragmentSwitcher  implements FragmentSwitcher, Parcelable {

    private Activity activity;

    public ApplicantInfoFragmentSwitcher(Activity activity) {
        this.activity = activity;
    }

    protected ApplicantInfoFragmentSwitcher(Parcel in) {
    }

    public static final Creator<ApplicantInfoFragmentSwitcher> CREATOR = new Creator<ApplicantInfoFragmentSwitcher>() {
        @Override
        public ApplicantInfoFragmentSwitcher createFromParcel(Parcel in) {
            return new ApplicantInfoFragmentSwitcher(in);
        }

        @Override
        public ApplicantInfoFragmentSwitcher[] newArray(int size) {
            return new ApplicantInfoFragmentSwitcher[size];
        }
    };

    @Override
    public void switchFragment(FragmentManager fm, String tag, Object... args) {
        Fragment fragment = fm.findFragmentByTag(tag);
        FragmentTransaction fTransaction = fm.beginTransaction();
        if (fragment == null) {
            switch (tag) {
                default:
                    break;
            }
            // Définissez les animations
            fTransaction.setCustomAnimations(R.anim.push_left_in
                    , R.anim.push_left_out
                    , R.anim.push_right_in
                    , R.anim.push_right_out
            ).replace(R.id.forum_fragment, fragment, tag);
        } else {
            // Le fragment existe déjà, il suffit de l'afficher
            fTransaction.show(fragment);
        }
        // Remplacez, ajoutez à la backstack et commit
        fTransaction.addToBackStack(tag).commit();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }
}
