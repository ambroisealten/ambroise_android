package com.alten.ambroise.forum.data.dao.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.ForumDao;
import com.alten.ambroise.forum.data.dao.roomDatabase.ForumRoomDatabase;
import com.alten.ambroise.forum.data.model.beans.Forum;

import java.util.List;
import java.util.concurrent.ExecutionException;

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

    public Forum getForum(long forumId) {
        try {
            return new getAsyncTask(mForumDao).execute(forumId).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(final Forum... forums) {
        try {
            new updateAsyncTask(mForumDao).execute(forums).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static class getAsyncTask extends AsyncTask<Long, Void, Forum> {
        private final ForumDao mAsyncTaskDao;

        getAsyncTask(ForumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Forum doInBackground(Long... id) {
            return mAsyncTaskDao.getForum(id[0]);
        }
    }

    private static class updateAsyncTask extends AsyncTask<Forum, Void, Void> {

        private final ForumDao mAsyncTaskDao;

        updateAsyncTask(ForumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Forum... params) {
            mAsyncTaskDao.update(params);
            return null;
        }
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

