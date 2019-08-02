package com.alten.ambroise.forum.view.activity;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.view.adapter.ViewPagerAdapter;
import com.alten.ambroise.forum.view.fragments.ApplicantMobilityFragment;
import com.alten.ambroise.forum.view.fragments.SignFragment;
import com.google.android.material.tabs.TabLayout;

public class ApplicantActivity extends AppCompatActivity implements ApplicantMobilityFragment.OnFragmentInteractionListener, SignFragment.OnFragmentInteractionListener {

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

        Fragment fragment;
        //Add fragments here
        fragment = new ApplicantMobilityFragment();
        adapter.addFragment(fragment, getString(R.string.mobility));

        fragment = new SignFragment();
        adapter.addFragment(fragment, getString(R.string.sign));

        viewPager.setAdapter(adapter);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        
    }
}
