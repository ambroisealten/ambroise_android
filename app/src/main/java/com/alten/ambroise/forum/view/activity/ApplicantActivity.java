package com.alten.ambroise.forum.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.data.model.viewModel.ForumViewModel;
import com.alten.ambroise.forum.view.ApplicantProcessService;
import com.alten.ambroise.forum.view.adapter.ViewPagerAdapter;
import com.alten.ambroise.forum.view.fragments.ApplicantComplementFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantContractFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantDiplomaFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantInfo;
import com.alten.ambroise.forum.view.fragments.ApplicantMobilityFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantSkillsFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class ApplicantActivity extends AppCompatActivity implements ApplicantMobilityFragment.OnFragmentInteractionListener, ApplicantDiplomaFragment.OnFragmentInteractionListener, ApplicantSkillsFragment.OnFragmentInteractionListener, ApplicantComplementFragment.OnFragmentInteractionListener, ApplicantContractFragment.OnFragmentInteractionListener {

    public static final String STATE_APPLICANT = "applicant";
    public static final String STATE_CURRENT_POSITION = "currentPosition";
    public static final String STATE_FORUM_ID = "forumId";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ApplicantForum applicant;
    private int currentPosition;
    private Fragment fragment;
    private ApplicantForumViewModel applicantForumViewModel;
    private long forumId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        applicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);

        Intent intent = getIntent();

        this.forumId = intent.getLongExtra(ForumActivity.STATE_FORUM, -1);
        this.applicant = applicantForumViewModel.getApplicant(intent.getLongExtra(STATE_APPLICANT, -1));

        Intent serviceIntent = new Intent(this, ApplicantProcessService.class);
        serviceIntent.putExtra("ApplicantID", applicant.get_id());
        startService(serviceIntent);

        setContentView(R.layout.activity_applicant);

        Toolbar mToolbar = findViewById(R.id.toolbar);
        mToolbar.setTitle(applicant.getSurname() + " " + applicant.getName());
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        mToolbar.setNavigationOnClickListener(view -> stopProcess());
        setSupportActionBar(mToolbar);

        FloatingActionButton validateButton = findViewById(R.id.save_applicant);
        validateButton.setOnClickListener(v -> {
            //The current fragment will save automatically its content
            askToSave();

            Intent intent1 = new Intent(getBaseContext(), RGPDActivity.class);
            intent1.putExtra(RGPDActivity.STATE_APPLICANT, applicant.get_id());
            intent1.putExtra(ForumActivity.STATE_FORUM, this.forumId);
            startActivity(intent1);
        });

        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                //Get the view pager adapter to get the corresponding fragment and fire save process. Then, save new position to current
                askToSave();
                currentPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        setViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
                fragment = ((ViewPagerAdapter) viewPager.getAdapter()).getItem(currentPosition);

                fTransaction.attach(fragment);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                FragmentTransaction fTransaction = getSupportFragmentManager().beginTransaction();
                if (fragment != null) {
                    fTransaction.detach(fragment);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        tabLayout.setupWithViewPager(viewPager);

        setIcon();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_CURRENT_POSITION, currentPosition);
        savedInstanceState.putLong(STATE_FORUM_ID, this.forumId);
        savedInstanceState.putParcelable(STATE_APPLICANT, this.applicant);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.currentPosition = savedInstanceState.getInt(STATE_CURRENT_POSITION);
        this.forumId = savedInstanceState.getLong(STATE_FORUM_ID);
        this.applicant = savedInstanceState.getParcelable(STATE_APPLICANT);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void askToSave() {
        ((ApplicantInfo) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(currentPosition)).saveInformation(applicant);
    }

    private void setIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.mobility);
        tabLayout.getTabAt(1).setIcon(R.drawable.contract);
        tabLayout.getTabAt(2).setIcon(R.drawable.diploma);
        tabLayout.getTabAt(3).setIcon(R.drawable.skills);
        tabLayout.getTabAt(4).setIcon(R.drawable.more);
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragment;
        //Add fragments here
        fragment = new ApplicantMobilityFragment();
        adapter.addFragment(fragment, getString(R.string.mobility));

        fragment = new ApplicantContractFragment();
        adapter.addFragment(fragment, getString(R.string.contract));

        fragment = new ApplicantDiplomaFragment();
        adapter.addFragment(fragment, getString(R.string.diploma));

        fragment = new ApplicantSkillsFragment();
        adapter.addFragment(fragment, getString(R.string.skills));

        fragment = new ApplicantComplementFragment();
        adapter.addFragment(fragment, getString(R.string.more));

        viewPager.setAdapter(adapter);
    }

    private void stopProcess() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_process_title)
                .setMessage(R.string.alert_process_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    applicantForumViewModel.delete(applicant);

                    Intent intent = new Intent(this, ForumActivity.class);
                    ForumViewModel mForumViewModel = ViewModelProviders.of(this).get(ForumViewModel.class);
                    mForumViewModel.getForum(this.forumId);
                    intent.putExtra(ForumActivity.STATE_FORUM, mForumViewModel.getForum(this.forumId));
                    startActivity(intent);
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    @Override
    public void onFragmentInteraction(ApplicantForum applicant) {
        applicantForumViewModel.update(applicant);
    }

    @Override
    public void onBackPressed() {
        this.stopProcess();
    }
}
