package com.alten.ambroise.forum.data.model.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.repository.ForumRepository;
import com.alten.ambroise.forum.data.model.beans.Forum;

import java.util.List;

public class ForumViewModel extends AndroidViewModel {

    private final ForumRepository mForumRepository;

    private final LiveData<List<Forum>> mAllForums;

    public ForumViewModel(Application application) {
        super(application);
        mForumRepository = new ForumRepository(application);
        mAllForums = mForumRepository.getAllForums();
    }

    public LiveData<List<Forum>> getAllForums() {
        return mAllForums;
    }

    public void insert(Forum forum) {
        mForumRepository.insert(forum);
    }

    public Forum getForum(long forumId) {
        return mForumRepository.getForum(forumId);
    }

    public void update(final Forum forum) {
        mForumRepository.update(forum);
    }
}
