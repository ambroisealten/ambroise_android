package com.alten.ambroise.forum.data.dao.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.dao.ApplicantForumDao;
import com.alten.ambroise.forum.data.dao.roomDatabase.ApplicantForumRoomDatabase;

import java.util.List;

public class ApplicantForumRepository {

    private ApplicantForumDao applicantForumDao;
    private LiveData<List<ApplicantForum>> mAllApplicants;

    public ApplicantForumRepository(Application application) {
        ApplicantForumRoomDatabase db = ApplicantForumRoomDatabase.getDatabase(application);
        applicantForumDao = db.applicantForumRepository();
        mAllApplicants = applicantForumDao.getAllApplicants();
    }

    public LiveData<List<ApplicantForum>> getAllApplicants() {
        return mAllApplicants;
    }


    public void insert (ApplicantForum applicant) {
        new ApplicantForumRepository.insertAsyncTask(applicantForumDao).execute(applicant);
    }

    private static class insertAsyncTask extends AsyncTask<ApplicantForum, Void, Void> {

        private ApplicantForumDao mAsyncTaskDao;

        insertAsyncTask(ApplicantForumDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final ApplicantForum... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
