package com.alten.ambroise.forum.data.model.viewModel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.alten.ambroise.forum.data.dao.repository.ApplicantForumRepository;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;

import java.util.List;

public class ApplicantForumViewModel extends AndroidViewModel {

    private final ApplicantForumRepository mApplicantForumRepository;

    private final LiveData<List<ApplicantForum>> mAllApplicantsForum;

    public ApplicantForumViewModel(Application application) {
        super(application);
        mApplicantForumRepository = new ApplicantForumRepository(application);
        mAllApplicantsForum = mApplicantForumRepository.getAllApplicants();
    }

    public LiveData<List<ApplicantForum>> getAllApplicants() {
        return mAllApplicantsForum;
    }

    public Long insert(ApplicantForum applicant) {
         return mApplicantForumRepository.insert(applicant);
    }

    public void update(ApplicantForum applicant) { mApplicantForumRepository.update(applicant); }
}
