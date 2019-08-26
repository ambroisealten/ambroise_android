package com.alten.ambroise.forum.view;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;

public class ApplicantProcessService extends Service {
    private Long applicantId;
    private ApplicantForumViewModel applicantForumViewModel;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Service Started
        this.applicantId = intent.getLongExtra("ApplicantID", -1);
        applicantForumViewModel = new ApplicantForumViewModel(getApplication());
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        //Perfom here want you want to do when App gets kill
        applicantForumViewModel.delete(applicantForumViewModel.getApplicant(applicantId));
        stopSelf();
    }
}