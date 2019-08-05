package com.alten.ambroise.forum.data.dao.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.ApplicantForumDao;
import com.alten.ambroise.forum.data.dao.roomDatabase.ApplicantForumRoomDatabase;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;

import java.util.List;

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
        new ApplicantForumRepository.insertAsyncTask(applicantForumDao).execute(applicant);
        return applicant.get_id();
    }

    public void update(ApplicantForum... applicants) {
        new ApplicantForumRepository.updateAsyncTask(applicantForumDao).execute(applicants);
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
