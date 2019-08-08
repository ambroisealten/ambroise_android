package com.alten.ambroise.forum.data.dao.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.ApplicantForumDao;
import com.alten.ambroise.forum.data.dao.roomDatabase.ApplicantForumRoomDatabase;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class ApplicantForumRepository {

    private final ApplicantForumDao applicantForumDao;
    private final LiveData<List<ApplicantForum>> mAllApplicants;

    public ApplicantForumRepository(Application application) {
        ApplicantForumRoomDatabase db = ApplicantForumRoomDatabase.getDatabase(application);
        applicantForumDao = db.applicantForumRepository();
        mAllApplicants = applicantForumDao.getAllApplicants();
    }

    public LiveData<List<ApplicantForum>> getAllApplicants() {
        return mAllApplicants;
    }


    public Long insert(ApplicantForum applicant) {
        try {
            return new insertAsyncTask(applicantForumDao).execute(applicant).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void update(ApplicantForum... applicants) {
        new ApplicantForumRepository.updateAsyncTask(applicantForumDao).execute(applicants);
    }

    public ApplicantForum getApplicant(final Long id) {
        try {
            return new getAsyncTask(applicantForumDao).execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean delete(ApplicantForum applicant) {
        try {
            return new deleteAsyncTask(applicantForumDao).execute(applicant).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static class deleteAsyncTask extends AsyncTask<ApplicantForum, Void, Boolean> {

        private final ApplicantForumDao mAsyncTaskDao;

        deleteAsyncTask(ApplicantForumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final ApplicantForum... params) {
            return mAsyncTaskDao.deleteApplicantsForum(params);
        }
    }

    private static class getAsyncTask extends AsyncTask<Long, Void, ApplicantForum> {
        private final ApplicantForumDao mAsyncTaskDao;

        getAsyncTask(ApplicantForumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected ApplicantForum doInBackground(Long... id) {
            return mAsyncTaskDao.getApplicant(id[0]);
        }
    }

    private static class insertAsyncTask extends AsyncTask<ApplicantForum, Void, Long> {
        private final ApplicantForumDao mAsyncTaskDao;

        insertAsyncTask(ApplicantForumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Long doInBackground(final ApplicantForum... params) {
            return mAsyncTaskDao.insert(params[0]);
        }
    }

    private static class updateAsyncTask extends AsyncTask<ApplicantForum, Void, Void> {

        private final ApplicantForumDao mAsyncTaskDao;

        updateAsyncTask(ApplicantForumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ApplicantForum... params) {
            mAsyncTaskDao.update(params);
            return null;
        }
    }
}
