package com.alten.ambroise.forum.view.fragmentSwitcher;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.view.activity.ApplicantActivity;
import com.alten.ambroise.forum.view.activity.ForumActivity;
import com.alten.ambroise.forum.view.adapter.ApplicantRecyclerViewAdapter;
import com.alten.ambroise.forum.view.fragments.ApplicantAddFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantListFragment;

public class ApplicantFragmentSwitcher implements FragmentSwitcher, ApplicantRecyclerViewAdapter.OnItemClickListener, Parcelable {

    public static final String APPLICANT_LIST_TAG = "applicantListTag";
    public static final String ADD_APPLICANT_TAG = "addApplicantTag";
    public static final Creator<ApplicantFragmentSwitcher> CREATOR = new Creator<ApplicantFragmentSwitcher>() {
        @Override
        public ApplicantFragmentSwitcher createFromParcel(Parcel in) {
            return new ApplicantFragmentSwitcher(in);
        }

        @Override
        public ApplicantFragmentSwitcher[] newArray(int size) {
            return new ApplicantFragmentSwitcher[size];
        }
    };
    private static final String APPLICANT_TAB_TAG = "applicantTabTag";
    private Activity activity;

    public ApplicantFragmentSwitcher(Activity activity) {
        this.activity = activity;
    }

    private ApplicantFragmentSwitcher(Parcel in) {
    }

    @Override
    public void switchFragment(FragmentManager fm, String tag, Object... args) {
        Fragment fragment = fm.findFragmentByTag(tag);
        FragmentTransaction fTransaction = fm.beginTransaction();
        if (fragment == null) {
            switch (tag) {
                case APPLICANT_LIST_TAG:
                    fragment = switchApplicantListFragment(fm);
                    break;
                case ADD_APPLICANT_TAG:
                    fragment = switchApplicantAddFragment(fm);
                    break;
                default:
                    fragment = switchApplicantListFragment(fm);
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

    private ApplicantAddFragment switchApplicantAddFragment(FragmentManager fm) {
        ApplicantAddFragment applicantAddFragment = (ApplicantAddFragment) fm.findFragmentByTag(ADD_APPLICANT_TAG);
        if (applicantAddFragment == null) {
            applicantAddFragment = new ApplicantAddFragment();
            Bundle bundle = new Bundle();
            applicantAddFragment.setArguments(bundle);
            applicantAddFragment.setSwitcher(this);
        }
        //Deactivate fab add button
        activity.findViewById(R.id.fab_forum).setVisibility(View.GONE);
        activity.findViewById(R.id.forum_fragment).setTag(ADD_APPLICANT_TAG);
        return applicantAddFragment;
    }

    private ApplicantListFragment switchApplicantListFragment(FragmentManager fm) {
        ApplicantListFragment applicantListFragment = (ApplicantListFragment) fm.findFragmentByTag(APPLICANT_LIST_TAG);
        if (applicantListFragment == null) {
            applicantListFragment = new ApplicantListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ApplicantListFragment.STATE_COLUMN_COUNT, 1);
            applicantListFragment.setArguments(bundle);
            applicantListFragment.setSwitcher(this);
        }
        //Be sure to have fab applicant add button visible
        activity.findViewById(R.id.fab_forum).setVisibility(View.VISIBLE);
        activity.findViewById(R.id.forum_fragment).setTag(APPLICANT_LIST_TAG);
        return applicantListFragment;
    }

    @Override
    public void onItemClick(ApplicantForum applicant) {
        Toast.makeText(activity, "CLICK SUR CANDIDAT" + applicant.getMail(), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(activity.getBaseContext(), ForumActivity.class);
//        intent.putExtra("forum",forum);
//        activity.startActivity(intent);
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

    public void startNewApplicantProcess(ApplicantForum applicant, FragmentManager fm) {
        fm.popBackStackImmediate();
        ApplicantForumViewModel applicantForumViewModel = new ApplicantForumViewModel(activity.getApplication());
        Long id = applicantForumViewModel.insert(applicant);
        Intent intent = new Intent(activity.getBaseContext(), ApplicantActivity.class);
        intent.putExtra(ApplicantActivity.STATE_APPLICANT, id);
        intent.putExtra(ForumActivity.STATE_FORUM,( (ForumActivity) activity).getForumId());
        activity.startActivity(intent);
    }

    public void setFabInvisible() {
        activity.findViewById(R.id.fab_forum).setVisibility(View.INVISIBLE);
    }
}
