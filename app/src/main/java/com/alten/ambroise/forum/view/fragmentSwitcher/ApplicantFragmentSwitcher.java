package com.alten.ambroise.forum.view.fragmentSwitcher;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.fragments.ApplicantListFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantRecyclerViewAdapter;
import com.alten.ambroise.forum.view.fragments.ForumListFragment;

public class ApplicantFragmentSwitcher implements FragmentSwitcher, ApplicantRecyclerViewAdapter.OnItemClickListener {

    public static final String APPLICANT_LIST_TAG = "applicantListTag";
    public static final String ADD_APPLICANT_TAG = "addApplicantTag";
    private final Activity activity;

    public ApplicantFragmentSwitcher(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void switchFragment(FragmentManager fm, String tag) {
        Fragment fragment = fm.findFragmentByTag(tag);
        FragmentTransaction fTransaction = fm.beginTransaction();
        if (fragment == null) {
            switch (tag) {
                case APPLICANT_LIST_TAG:
                    fragment = switchApplicantListFragment(fm);
                    break;
                case ADD_APPLICANT_TAG:
//                switchForumAddFragment(fm);
                    return;
                default:
                    fragment = switchApplicantListFragment(fm);
                    break;
            }
            // Définissez les animations
            fTransaction.setCustomAnimations(R.anim.push_left_in
                    , R.anim.push_left_out
                    , R.anim.push_right_in
                    , R.anim.push_right_out
            );
            fTransaction.replace(R.id.fragment, fragment, tag);
        } else {
            // Le fragment existe déjà, il suffit de l'afficher
            fTransaction.show(fragment);
        }
        // Remplacez, ajoutez à la backstack et commit
        fTransaction.addToBackStack(tag);
        fTransaction.commit();
    }

    private ApplicantListFragment switchApplicantListFragment(FragmentManager fm) {
        ApplicantListFragment applicantListFragment = (ApplicantListFragment) fm.findFragmentByTag(APPLICANT_LIST_TAG);
        if (applicantListFragment == null) {
            applicantListFragment = new ApplicantListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ForumListFragment.ARG_COLUMN_COUNT, 1);
            applicantListFragment.setArguments(bundle);
            applicantListFragment.setSwitcher(this);
        }
        return applicantListFragment;
    }

    @Override
    public void onItemClick(ApplicantForum applicant) {
        Toast.makeText(activity, "CLICK SUR CANDIDAT" + applicant.getMail(), Toast.LENGTH_SHORT).show();
//        Intent intent = new Intent(activity.getBaseContext(), ForumActivity.class);
//        intent.putExtra("forum",forum);
//        activity.startActivity(intent);
    }
}
