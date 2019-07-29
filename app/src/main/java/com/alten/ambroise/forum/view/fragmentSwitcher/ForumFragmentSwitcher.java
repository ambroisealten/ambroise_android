package com.alten.ambroise.forum.view.fragmentSwitcher;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.data.model.viewModel.ForumViewModel;
import com.alten.ambroise.forum.view.activity.ForumActivity;
import com.alten.ambroise.forum.view.fragments.ForumAddDialogFragment;
import com.alten.ambroise.forum.view.fragments.ForumListFragment;
import com.alten.ambroise.forum.view.fragments.ForumRecyclerViewAdapter;

public class ForumFragmentSwitcher implements FragmentSwitcher, ForumRecyclerViewAdapter.OnItemClickListener, Parcelable {

    public static final String FORUM_LIST_TAG = "forumListTag";
    public static final String ADD_FORUM_TAG = "addForumTag";

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
                    return; //Just in this cas because we open a dialog (fragment dialog)
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
        ForumAddDialogFragment addForumDialog = ForumAddDialogFragment.newInstance("New Forum");
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
