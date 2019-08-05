package com.alten.ambroise.forum.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.view.adapter.ViewPagerAdapter;
import com.alten.ambroise.forum.view.fragments.ApplicantContractFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantInfo;
import com.alten.ambroise.forum.view.fragments.ApplicantMobilityFragment;
import com.google.android.material.tabs.TabLayout;

public class ApplicantActivity extends AppCompatActivity implements ApplicantMobilityFragment.OnFragmentInteractionListener, ApplicantContractFragment.OnFragmentInteractionListener {

    public static final String STATE_APPLICANT = "applicant";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ApplicantForum applicant;
    private int currentPosition;
    private ApplicantForumViewModel applicantForumViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);

        Intent intent = getIntent();
        this.applicant = intent.getParcelableExtra(STATE_APPLICANT);
        setContentView(R.layout.activity_applicant);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button validateButton = new Button(this);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getBaseContext(), RGPDActivity.class);
                intent.putExtra(RGPDActivity.STATE_APPLICANT, applicant);
                startActivity(intent);
            }
        });
        toolbar.addView(validateButton);

        viewPager = findViewById(R.id.viewpager);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                //Get the view pager adapter to get the corresponding fragment and fire save process. Then, save new position to current
                ((ApplicantInfo) ((ViewPagerAdapter) viewPager.getAdapter()).getItem(currentPosition)).saveInformation(applicant);
                currentPosition = position;
                System.out.println(applicant.toString());
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
        setViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setIcon();
    }

    private void setIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.mobility);
        tabLayout.getTabAt(1).setIcon(R.drawable.contract);
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Fragment fragment;
        //Add fragments here
        fragment = new ApplicantMobilityFragment();
        adapter.addFragment(fragment, getString(R.string.mobility));

        fragment = new ApplicantContractFragment();
        adapter.addFragment(fragment, getString(R.string.contract));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(ApplicantForum applicant) {
        applicantForumViewModel.update(applicant);
    }

}
