package com.alten.ambroise.forum.view.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantInfoFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ApplicantActivity extends AppCompatActivity {

    private static final String STATE_APPLICANT_INFO_FRAGMENT_SWITCHER = "applicantFragmentSwitcher";
    public static final String STATE_APPLICANT = "applicant";

    private ApplicantForum applicant;
    private ApplicantInfoFragmentSwitcher applicantInfoFragmentSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            this.applicantInfoFragmentSwitcher = new ApplicantInfoFragmentSwitcher(this);
            Intent intent = getIntent();
            this.applicant = intent.getParcelableExtra(STATE_APPLICANT);
        } else {
            this.applicant = savedInstanceState.getParcelable(STATE_APPLICANT);
            this.applicantInfoFragmentSwitcher = savedInstanceState.getParcelable(STATE_APPLICANT_INFO_FRAGMENT_SWITCHER) != null
                    ? (ApplicantInfoFragmentSwitcher) savedInstanceState.getParcelable(STATE_APPLICANT_INFO_FRAGMENT_SWITCHER)
                    : new ApplicantInfoFragmentSwitcher(this);
            this.applicantInfoFragmentSwitcher.setActivity(this);
        }
        setContentView(R.layout.activity_applicant);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        for (int i = 0; i < sectionsPagerAdapter.getCount() ; i++) {
            tabs.getTabAt(i).setIcon(getTabIcon(sectionsPagerAdapter.getPageTitleId(i)));
        }

    }

    private int getTabIcon(int title) {
        switch (title){
            case R.string.mobility:
                return R.drawable.mobility;
            case R.string.contract:
                return R.drawable.contract;
            case R.string.diploma:
                return R.drawable.diploma;
            case R.string.skills:
                return R.drawable.skills;
            case R.string.more:
                return R.drawable.more;
            default:
               return R.drawable.googleg_standard_color_18;
        }
    }
}