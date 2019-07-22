package com.alten.ambroise.forum.view.fragmentSwitcher;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.view.fragments.ForumAddFragment;
import com.alten.ambroise.forum.view.fragments.ForumListFragment;

public class ForumFragmentSwitcher {

    public static final String forumListTag = "forumListTag";
    public static final String addForumTag = "addForumTag";

    public void switchFragment(FragmentManager fm, String tag) {
        Fragment fragment;
        switch (tag) {
            case forumListTag:
                fragment = switchForumListFragment(fm);
                break;
            case addForumTag:
                fragment = switchForumAddFragment(fm);
                break;
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

    private ForumAddFragment switchForumAddFragment(FragmentManager fm) {
        ForumAddFragment forumAddFragment = (ForumAddFragment) fm.findFragmentByTag(addForumTag);
        if (forumAddFragment == null) {
            forumAddFragment = new ForumAddFragment();
            Bundle bundle = new Bundle();
            forumAddFragment.setArguments(bundle);
        }
        return forumAddFragment;
    }
}
