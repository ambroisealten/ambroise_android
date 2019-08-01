package com.alten.ambroise.forum.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantInfoFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.ApplicantMobilityFragment;
import com.alten.ambroise.forum.view.fragments.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ApplicantActivity extends AppCompatActivity implements ApplicantMobilityFragment.OnFragmentInteractionListener {

    public static final String STATE_APPLICANT = "applicant";
    private static final String STATE_APPLICANT_INFO_FRAGMENT_SWITCHER = "applicantFragmentSwitcher";
    private ApplicantForum applicant;
    private ApplicantInfoFragmentSwitcher applicantInfoFragmentSwitcher;
    private TabLayout tabs;

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
        viewPager.setId(R.id.view_pager);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        for (int i = 0; i < sectionsPagerAdapter.getCount(); i++) {
            tabs.getTabAt(i).setIcon(getTabIcon(SectionsPagerAdapter.getPageTitleId(i)));
        }
        tabs.getTabAt(0).select();
        tabs.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switchTableFromFragment(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                switchTableFromFragment(tab.getPosition());
            }
        });
        tabs.getTabAt(0).select();
    }

    private int getTabIcon(int title) {
        switch (title) {
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

    public void switchTableFromFragment(int index) {
        switch (SectionsPagerAdapter.getPageTitleId(index)) {
            case R.string.mobility:
                applicantInfoFragmentSwitcher.switchFragment(getSupportFragmentManager(),ApplicantInfoFragmentSwitcher.APPLICANT_INFO_MOBILITY_TAG,this.applicant);
                break;
//            case R.string.contract:
//                applicantInfoFragmentSwitcher.switchFragment(getSupportFragmentManager(),ApplicantInfoFragmentSwitcher.APPLICANT_INFO_CONTRACT_TAG,this.applicant);
//                break;
//            case R.string.diploma:
//                applicantInfoFragmentSwitcher.switchFragment(getSupportFragmentManager(),ApplicantInfoFragmentSwitcher.APPLICANT_INFO_DIPLOMA_TAG,this.applicant);
//                break;
//            case R.string.skills:
//                applicantInfoFragmentSwitcher.switchFragment(getSupportFragmentManager(),ApplicantInfoFragmentSwitcher.APPLICANT_INFO_SKILLS_TAG,this.applicant);
//                break;
//            case R.string.more:
//                applicantInfoFragmentSwitcher.switchFragment(getSupportFragmentManager(),ApplicantInfoFragmentSwitcher.APPLICANT_INFO_MORE_TAG,this.applicant);
//                break;
            default:
                Toast.makeText(this, R.string.not_implemented, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}