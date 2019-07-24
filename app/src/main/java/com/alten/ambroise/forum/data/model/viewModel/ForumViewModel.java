package com.alten.ambroise.forum.data.model.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.repository.ForumRepository;
import com.alten.ambroise.forum.data.model.beans.Forum;

import java.util.List;

public class ForumViewModel extends AndroidViewModel {

    private final ForumRepository mRepository;

    private final LiveData<List<Forum>> mAllForums;

    public ForumViewModel(Application application) {
        super(application);
        mRepository = new ForumRepository(application);
        mAllForums = mRepository.getAllForums();
    }

    public LiveData<List<Forum>> getAllForums() {
        return mAllForums;
    }

    public void insert(Forum forum) {
        mRepository.insert(forum);
    }
}
