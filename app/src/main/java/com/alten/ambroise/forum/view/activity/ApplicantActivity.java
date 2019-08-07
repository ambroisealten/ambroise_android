package com.alten.ambroise.forum.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
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
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ApplicantForum applicant;
    private int currentPosition;
    private Fragment fragment;
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

        FloatingActionButton validateButton = findViewById(R.id.save_applicant);
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Intent intent = new Intent(getBaseContext(), RGPDActivity.class);
                intent.putExtra(RGPDActivity.STATE_APPLICANT, applicant);
                startActivity(intent);
            }
        });

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
                fragment = ((ViewPagerAdapter)viewPager.getAdapter()).getItem(currentPosition);

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

    @Override
    public void onFragmentInteraction(ApplicantForum applicant) {
        applicantForumViewModel.update(applicant);
    }


}
