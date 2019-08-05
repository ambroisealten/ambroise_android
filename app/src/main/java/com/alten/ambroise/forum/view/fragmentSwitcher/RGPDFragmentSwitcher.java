package com.alten.ambroise.forum.view.fragmentSwitcher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.view.fragments.ForumListFragment;
import com.alten.ambroise.forum.view.fragments.RGPDTextFragment;
import com.alten.ambroise.forum.view.fragments.SignFragment;

public class RGPDFragmentSwitcher implements FragmentSwitcher, Parcelable {

    public static final String RGPD_TEXT_TAG = "rgpdTextTag";
    public static final String RGPD_SIGN_TAG = "rgpdSignTag";

    public static final Creator<RGPDFragmentSwitcher> CREATOR = new Creator<RGPDFragmentSwitcher>() {
        @Override
        public RGPDFragmentSwitcher createFromParcel(Parcel in) {
            return new RGPDFragmentSwitcher(in);
        }

        @Override
        public RGPDFragmentSwitcher[] newArray(int size) {
            return new RGPDFragmentSwitcher[size];
        }
    };
    private Activity activity;

    public RGPDFragmentSwitcher(Activity activity) {
        this.activity = activity;
    }

    private RGPDFragmentSwitcher(Parcel in) {
    }

    @Override
    public void switchFragment(FragmentManager fm, String tag, Object... args) {
        Fragment fragment = fm.findFragmentByTag(tag);
        FragmentTransaction fTransaction = fm.beginTransaction();
        if (fragment == null) {
            switch (tag) {
                case RGPD_TEXT_TAG:
                    fragment = switchRGPDTextFragment(fm);
                    break;
                case RGPD_SIGN_TAG:
                    fragment = switchRGPDSignFragment(fm);
                    break;
                default:
                    fragment = switchRGPDTextFragment(fm);
                    break;
            }
            // Définissez les animations
            fTransaction.setCustomAnimations(R.anim.push_left_in
                    , R.anim.push_left_out
                    , R.anim.push_right_in
                    , R.anim.push_right_out
            ).replace(R.id.rgpd_fragment, fragment, tag);
        } else {
            // Le fragment existe déjà, il suffit de l'afficher
            fTransaction.show(fragment);
        }
        // Remplacez, ajoutez à la backstack et commit
        fTransaction.addToBackStack(tag).commit();
    }

    private RGPDTextFragment switchRGPDTextFragment(FragmentManager fm) {
        RGPDTextFragment rgpdTextFragment = (RGPDTextFragment) fm.findFragmentByTag(RGPD_TEXT_TAG);
        if (rgpdTextFragment == null) {
            rgpdTextFragment = new RGPDTextFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ForumListFragment.STATE_COLUMN_COUNT, 1);
            rgpdTextFragment.setArguments(bundle);
        }
        return rgpdTextFragment;
    }

    private SignFragment switchRGPDSignFragment(FragmentManager fm) {
        SignFragment signFragment = (SignFragment) fm.findFragmentByTag(RGPD_SIGN_TAG);
        if (signFragment == null) {
            signFragment = new SignFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ForumListFragment.STATE_COLUMN_COUNT, 1);
            signFragment.setArguments(bundle);
        }
        return signFragment;
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

