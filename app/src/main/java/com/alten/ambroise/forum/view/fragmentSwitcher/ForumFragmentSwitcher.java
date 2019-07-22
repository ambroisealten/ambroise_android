package com.alten.ambroise.forum.view.fragmentSwitcher;


import android.app.Activity;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.beans.Forum;
import com.alten.ambroise.forum.data.viewModel.ForumViewModel;
import com.alten.ambroise.forum.view.fragments.ForumAddDialogFragment;
import com.alten.ambroise.forum.view.fragments.ForumListFragment;

public class ForumFragmentSwitcher {

    public static final String forumListTag = "forumListTag";
    public static final String addForumTag = "addForumTag";
    private final Activity activity;

    public ForumFragmentSwitcher(Activity activity){
        this.activity = activity;
    }

    public void switchFragment(FragmentManager fm, String tag) {
        Fragment fragment;
        switch (tag) {
            case forumListTag:
                fragment = switchForumListFragment(fm);
                break;
            case addForumTag:
                switchForumAddFragment(fm);
                return;
            default:
                fragment = switchForumListFragment(fm);
                break;
        }
        FragmentTransaction fTransaction = fm.beginTransaction();
        // Définissez les animations
            /*fTransaction.setCustomAnimations(R.anim.anim_push_left_in,
                    R.anim.anim_push_left_out,
                    R.anim.anim_push_right_in,
                    R.anim.anim_push_right_out);*/
        // Remplacez, ajoutez à la backstack et commit
        fTransaction.replace(R.id.fragment, fragment, tag);
        fTransaction.addToBackStack(tag);
        fTransaction.commit();
    }

    private ForumListFragment switchForumListFragment(FragmentManager fm) {
        ForumListFragment forumListFragment = (ForumListFragment) fm.findFragmentByTag(forumListTag);
        if (forumListFragment == null) {
            forumListFragment = new ForumListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(ForumListFragment.ARG_COLUMN_COUNT, 2);
            forumListFragment.setArguments(bundle);
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
}
