package com.alten.ambroise.forum.view.fragmentSwitcher;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.data.model.viewModel.ForumViewModel;
import com.alten.ambroise.forum.view.activity.ForumActivity;
import com.alten.ambroise.forum.view.adapter.ForumRecyclerViewAdapter;
import com.alten.ambroise.forum.view.fragments.ApplicantListFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantViewFragment;
import com.alten.ambroise.forum.view.fragments.ForumAddDialogFragment;
import com.alten.ambroise.forum.view.fragments.ForumListFragment;

public class ForumFragmentSwitcher implements FragmentSwitcher, ForumRecyclerViewAdapter.OnItemClickListener, Parcelable {

    public static final String FORUM_LIST_TAG = "forumListTag";
    public static final String ADD_FORUM_TAG = "addForumTag";
    private static final String APPLICANT_VIEW_TAG = "applicantViewTag";
    public static final String APPLICANT_LIST_TAG = "applicantListTag";

    public static final Creator<ForumFragmentSwitcher> CREATOR = new Creator<ForumFragmentSwitcher>() {
        @Override
        public ForumFragmentSwitcher createFromParcel(Parcel in) {
            return new ForumFragmentSwitcher(in);
        }

        @Override
        public ForumFragmentSwitcher[] newArray(int size) {
            return new ForumFragmentSwitcher[size];
        }
    };
    private Activity activity;

    public ForumFragmentSwitcher(Activity activity) {
        this.activity = activity;
    }

    private ForumFragmentSwitcher(Parcel in) {
    }

    @Override
    public void switchFragment(FragmentManager fm, String tag, Object... args) {
        Fragment fragment = fm.findFragmentByTag(tag);
        FragmentTransaction fTransaction = fm.beginTransaction();
        if (fragment == null) {
            switch (tag) {
                case FORUM_LIST_TAG:
                    fragment = switchForumListFragment(fm);
                    break;
                case ADD_FORUM_TAG:
                    switchForumAddFragment(fm);
                    return; //Just in this case because we open a dialog (fragment dialog)
                case APPLICANT_LIST_TAG:
                    fragment = switchApplicantListFragment(fm);
                    break;
                case APPLICANT_VIEW_TAG:
                    fragment = switchApplicantViewFragment(fm, (long) args[0]);
                    break;
                default:
                    fragment = switchForumListFragment(fm);
                    break;
            }
            // Définissez les animations
            fTransaction.setCustomAnimations(R.anim.push_left_in
                    , R.anim.push_left_out
                    , R.anim.push_right_in
                    , R.anim.push_right_out
            ).replace(R.id.main_fragment, fragment, tag);
        } else {
            // Le fragment existe déjà, il suffit de l'afficher
            fTransaction.show(fragment);
        }
        // Remplacez, ajoutez à la backstack et commit
        fTransaction.addToBackStack(tag).commit();
        activity.findViewById(R.id.main_fragment).setTag(tag);
    }

    @Override
    public void onItemClick(ApplicantForum applicant) {
        switchFragment(((AppCompatActivity) activity).getSupportFragmentManager(), APPLICANT_VIEW_TAG, applicant.get_id());
    }

    private ApplicantViewFragment switchApplicantViewFragment(FragmentManager fm, long applicantId) {
        ApplicantViewFragment applicantViewFragment = (ApplicantViewFragment) fm.findFragmentByTag(APPLICANT_VIEW_TAG);
        if (applicantViewFragment == null) {
            applicantViewFragment = new ApplicantViewFragment();
            Bundle bundle = new Bundle();
            bundle.putLong(ApplicantViewFragment.STATE_APPLICANT, applicantId);
            applicantViewFragment.setArguments(bundle);
            applicantViewFragment.setSwitcher(this);
        }
        //Deactivate fab add button
        activity.findViewById(R.id.fab).setVisibility(View.GONE);
        return applicantViewFragment;
    }

    private ApplicantListFragment switchApplicantListFragment(FragmentManager fm) {
        ApplicantListFragment applicantListFragment = (ApplicantListFragment) fm.findFragmentByTag(APPLICANT_LIST_TAG);
        if (applicantListFragment == null) {
            applicantListFragment = new ApplicantListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ApplicantListFragment.STATE_COLUMN_COUNT, 1);
            bundle.putLong(ForumActivity.STATE_FORUM, -1); //-1 because we want all applicant of all forums
            applicantListFragment.setArguments(bundle);
            applicantListFragment.setSwitcher(this);
        }
        activity.findViewById(R.id.fab).setVisibility(View.INVISIBLE);
        return applicantListFragment;
    }

    private ForumListFragment switchForumListFragment(FragmentManager fm) {
        ForumListFragment forumListFragment = (ForumListFragment) fm.findFragmentByTag(FORUM_LIST_TAG);
        if (forumListFragment == null) {
            forumListFragment = new ForumListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ForumListFragment.STATE_COLUMN_COUNT, 1);
            forumListFragment.setArguments(bundle);
            forumListFragment.setSwitcher(this);
        }
        return forumListFragment;
    }

    private void switchForumAddFragment(FragmentManager fm) {
        Fragment frag = fm.findFragmentByTag("fragment_edit_name");
        if (frag != null) {
            fm.beginTransaction().remove(frag).commit();
        }
        ForumAddDialogFragment addForumDialog = ForumAddDialogFragment.newInstance(activity.getString(R.string.dialog_forum));
        addForumDialog.subscribeToSwitcher(this);
        addForumDialog.show(fm, "fragment_edit_name");
    }

    public void addNewForum(Forum newForum) {
        ForumViewModel forumViewModel = new ForumViewModel(activity.getApplication());
        forumViewModel.insert(newForum);
    }

    @Override
    public void onItemClick(Forum forum) {
        Intent intent = new Intent(activity.getBaseContext(), ForumActivity.class);
        intent.putExtra(ForumActivity.STATE_FORUM, forum);
        activity.startActivity(intent);
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
