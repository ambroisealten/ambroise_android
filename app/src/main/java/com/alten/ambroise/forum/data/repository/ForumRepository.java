package com.alten.ambroise.forum.data.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.beans.Forum;
import com.alten.ambroise.forum.data.dao.ForumDao;
import com.alten.ambroise.forum.data.dao.ForumRoomDatabase;

import java.util.List;

public class ForumRepository {

    private ForumDao mForumDao;
    private LiveData<List<Forum>> mAllForums;

    public ForumRepository(Application application) {
        ForumRoomDatabase db = ForumRoomDatabase.getDatabase(application);
        mForumDao = db.forumRepository();
        mAllForums = mForumDao.getAllForums();
    }

    public LiveData<List<Forum>> getAllForums() {
        return mAllForums;
    }


    public void insert (Forum forum) {
        new insertAsyncTask(mForumDao).execute(forum);
    }

    private static class insertAsyncTask extends AsyncTask<Forum, Void, Void> {

        private ForumDao mAsyncTaskDao;

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

