package com.alten.ambroise.forum.data.dao.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.ForumDao;
import com.alten.ambroise.forum.data.dao.roomDatabase.ForumRoomDatabase;
import com.alten.ambroise.forum.data.model.beans.Forum;

import java.util.List;

public class ForumRepository {

    private final ForumDao mForumDao;
    private final LiveData<List<Forum>> mAllForums;

    public ForumRepository(Application application) {
        ForumRoomDatabase db = ForumRoomDatabase.getDatabase(application);
        mForumDao = db.forumRepository();
        mAllForums = mForumDao.getAllForums();
    }

    public LiveData<List<Forum>> getAllForums() {
        return mAllForums;
    }


    public void insert(Forum forum) {
        new insertAsyncTask(mForumDao).execute(forum);
    }

    private static class insertAsyncTask extends AsyncTask<Forum, Void, Void> {

        private final ForumDao mAsyncTaskDao;

        insertAsyncTask(ForumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Forum... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}

