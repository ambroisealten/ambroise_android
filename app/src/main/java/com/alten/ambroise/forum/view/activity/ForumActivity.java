package com.alten.ambroise.forum.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.ApplicantAddFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

public class ForumActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ApplicantListFragment.OnListFragmentInteractionListener, ApplicantAddFragment.OnFragmentInteractionListener {

    public static final String STATE_FORUM = "forum";
    private static final String STATE_APPLICANT_FRAGMENT_SWITCHER = "applicantFragmentSwitcher";

    private Forum forum;
    private ApplicantFragmentSwitcher applicantFragmentSwitcher;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        Intent intent = getIntent();
        forum = intent.getParcelableExtra(STATE_FORUM);
        //If is not a restored activity
        if (savedInstanceState == null) {
            this.applicantFragmentSwitcher = new ApplicantFragmentSwitcher(this);

        } else {
            this.forum = savedInstanceState.getParcelable(STATE_FORUM);
            this.applicantFragmentSwitcher = savedInstanceState.getParcelable(STATE_APPLICANT_FRAGMENT_SWITCHER) != null
                    ? (ApplicantFragmentSwitcher) savedInstanceState.getParcelable(STATE_APPLICANT_FRAGMENT_SWITCHER)
                    : new ApplicantFragmentSwitcher(this);
            this.applicantFragmentSwitcher.setActivity(this);
        }
        this.setTitle(Objects.requireNonNull(forum).getName());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fab = findViewById(R.id.fab_forum);
        fab.setOnClickListener(view -> applicantFragmentSwitcher.switchFragment(getSupportFragmentManager(), ApplicantFragmentSwitcher.ADD_APPLICANT_TAG));
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        applicantFragmentSwitcher.switchFragment(getSupportFragmentManager(), ApplicantFragmentSwitcher.APPLICANT_LIST_TAG);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_FORUM, this.forum);
        savedInstanceState.putParcelable(STATE_APPLICANT_FRAGMENT_SWITCHER, this.applicantFragmentSwitcher);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.applicantFragmentSwitcher = savedInstanceState.getParcelable(STATE_APPLICANT_FRAGMENT_SWITCHER);
        this.forum = savedInstanceState.getParcelable(STATE_FORUM);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRestart(){
        //if (findViewById(R.id.forum_fragment).getTag() != null && findViewById(R.id.forum_fragment).getTag() == ApplicantFragmentSwitcher.APPLICANT_LIST_TAG) {
            fab.setVisibility(View.VISIBLE);
        //}
        super.onRestart();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (findViewById(R.id.forum_fragment).getTag() != null && findViewById(R.id.forum_fragment).getTag() == ApplicantFragmentSwitcher.APPLICANT_LIST_TAG) {
                findViewById(R.id.forum_fragment).setTag(null);
                super.onBackPressed();
                super.onBackPressed();
            } else {
                super.onBackPressed();
                findViewById(R.id.forum_fragment).setTag(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() - 1).getName());
                if (findViewById(R.id.forum_fragment).getTag() == null) {
                    this.applicantFragmentSwitcher.switchFragment(getSupportFragmentManager(), ApplicantFragmentSwitcher.APPLICANT_LIST_TAG);
                }
                if (findViewById(R.id.forum_fragment).getTag() == ApplicantFragmentSwitcher.APPLICANT_LIST_TAG) {
                    findViewById(R.id.fab_forum).setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.forum, menu);
        ((TextView) findViewById(R.id.forumName)).setText(forum.getName());
        ((TextView) findViewById(R.id.forumDate)).setText(forum.getDate());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(ApplicantForum item) {
    }

    @Override
    public void onFragmentInteraction(ApplicantForum applicant) {
    }

    public long getForumId(){
        return forum.get_id();
    }
}
