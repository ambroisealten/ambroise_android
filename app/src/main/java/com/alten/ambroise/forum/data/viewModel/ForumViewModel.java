package com.alten.ambroise.forum.data.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.beans.Forum;
import com.alten.ambroise.forum.data.repository.ForumRepository;

import java.util.List;

public class ForumViewModel extends AndroidViewModel {

    private ForumRepository mRepository;

    private LiveData<List<Forum>> mAllForums;

    public ForumViewModel (Application application) {
        super(application);
        mRepository = new ForumRepository(application);
        mAllForums = mRepository.getAllForums();
    }

    LiveData<List<Forum>> getAllWords() { return mAllForums; }

    public void insert(Forum word) { mRepository.insert(word); }
}
