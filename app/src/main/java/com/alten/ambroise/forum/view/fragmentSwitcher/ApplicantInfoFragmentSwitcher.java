package com.alten.ambroise.forum.view.fragmentSwitcher;

import android.app.Activity;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.fragments.ApplicantMobilityFragment;


public class ApplicantInfoFragmentSwitcher implements FragmentSwitcher, Parcelable {

    public static final String APPLICANT_INFO_MOBILITY_TAG = "applicantInfoMobilityTag";
    public static final String APPLICANT_INFO_CONTRACT_TAG = "applicantInfoContractTag";
    public static final String APPLICANT_INFO_DIPLOMA_TAG = "applicantInfoDiplomaTag";
    public static final String APPLICANT_INFO_SKILLS_TAG = "applicantInfoSkillsTag";
    public static final String APPLICANT_INFO_MORE_TAG = "applicantInfoMoreTag";

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
    private Activity activity;

    public ApplicantInfoFragmentSwitcher(Activity activity) {
        this.activity = activity;
    }

    protected ApplicantInfoFragmentSwitcher(Parcel in) {
    }

    @Override
    public void switchFragment(FragmentManager fm, String tag, Object... args) {
        Fragment fragment = fm.findFragmentByTag(tag);
        FragmentTransaction fTransaction = fm.beginTransaction();
        if (fragment == null) {
            switch (tag) {
                case APPLICANT_INFO_MOBILITY_TAG:
                    fragment = switchApplicantInfoMobilityFragment(fm, (ApplicantForum) args[0]);
                    break;
                default:
                    break;
            }
            // Définissez les animations
            fTransaction.setCustomAnimations(R.anim.push_left_in
                    , R.anim.push_left_out
                    , R.anim.push_right_in
                    , R.anim.push_right_out
            ).replace(R.id.applicant_info_fragment, fragment, tag);
        } else {
            // Le fragment existe déjà, il suffit de l'afficher
            fTransaction.show(fragment);
        }
        // Remplacez, ajoutez à la backstack et commit
        fTransaction.addToBackStack(tag).commit();
    }

    private ApplicantMobilityFragment switchApplicantInfoMobilityFragment(FragmentManager fm, ApplicantForum applicant) {
        ApplicantMobilityFragment applicantMobilityFragment = (ApplicantMobilityFragment) fm.findFragmentByTag(APPLICANT_INFO_MOBILITY_TAG);
        if (applicantMobilityFragment == null) {
            applicantMobilityFragment = new ApplicantMobilityFragment();
        }
        return applicantMobilityFragment;
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
