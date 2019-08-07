package com.alten.ambroise.forum.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.view.fragmentSwitcher.RGPDFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.RGPDTextFragment;
import com.alten.ambroise.forum.view.fragments.SignFragment;
import com.alten.ambroise.forum.view.fragments.ValidationFragment;

public class RGPDActivity extends AppCompatActivity implements ValidationFragment.OnFragmentInteractionListener, SignFragment.OnFragmentInteractionListener, RGPDTextFragment.OnFragmentInteractionListener {

    public static final String STATE_APPLICANT = "applicant";
    public static final String STATE_RGPD_FRAGMENT_SWITCHER = "rgpdFragmentSwitcher";
    private ApplicantForum applicant;
    private ApplicantForumViewModel applicantForumViewModel;
    private RGPDFragmentSwitcher rgpdFragmentSwitcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);

        Intent intent = getIntent();
        this.applicant = intent.getParcelableExtra(STATE_APPLICANT);

        this.rgpdFragmentSwitcher = new RGPDFragmentSwitcher(this);

        this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(), RGPDFragmentSwitcher.RGPD_TEXT_TAG);

        setContentView(R.layout.activity_rgpd);
    }

    private void stopProcess() {

    }

    @Override
    public void onFragmentInteraction(final boolean accept, final String tag, ApplicantForum... applicant) {
        switch (tag) {
            case RGPDFragmentSwitcher.RGPD_TEXT_TAG:
                if (accept) {
                    this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(), RGPDFragmentSwitcher.RGPD_SIGN_TAG, this.applicant);
                } else {
                    stopProcess();
                }
                break;
            case RGPDFragmentSwitcher.RGPD_SIGN_TAG:
                if (accept) {
                    this.applicant = applicant[0];
                    this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(),RGPDFragmentSwitcher.RGPD_VALIDATION_TAG);

//                    //Code only for test. Need to be put on good fragment
//                    final ApplicantForum applicantForum = applicant[0];
//                    String mail = "altenstrasbourg@gmail.com";
//                    Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
//                    intent.setData(Uri.parse("mailto:" + mail)); //If more than 1 receiver, then use , (comma) to separate them
//                    intent.putExtra(Intent.EXTRA_SUBJECT, "Applicant " + applicantForum.getName());
//                    intent.putExtra(Intent.EXTRA_TEXT, applicantForum.toString());
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
//                    try {
//                        startActivity(Intent.createChooser(intent, "Send email using..."));
//                    } catch (android.content.ActivityNotFoundException ex) {
//                        Toast.makeText(this, "No email clients installed.", Toast.LENGTH_SHORT).show();
//                    }
//
//                    //on activity result
                } else {
                    stopProcess();
                }
                break;
            case RGPDFragmentSwitcher.RGPD_VALIDATION_TAG:
//                this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(),RGPDFragmentSwitcher.RGPD_GRADE_AND_SEND_TAG);
                break;
            default:
                Toast.makeText(this, getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

