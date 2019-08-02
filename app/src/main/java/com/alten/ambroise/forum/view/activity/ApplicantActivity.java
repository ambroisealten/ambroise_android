package com.alten.ambroise.forum.view.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.view.adapter.ViewPagerAdapter;
import com.alten.ambroise.forum.view.fragments.ApplicantMobilityFragment;
import com.google.android.material.tabs.TabLayout;

public class ApplicantActivity extends AppCompatActivity implements ApplicantMobilityFragment.OnFragmentInteractionListener {

    public static final String STATE_APPLICANT = "applicant";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewPager = findViewById(R.id.viewpager);
        setViewPager(viewPager);

        tabLayout = findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setIcon();
    }

    private void setIcon() {
        tabLayout.getTabAt(0).setIcon(R.drawable.mobility);
    }

    private void setViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        //Add fragments here
        ApplicantMobilityFragment fragment = new ApplicantMobilityFragment();
        adapter.addFragment(fragment, getString(R.string.mobility));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }
}
