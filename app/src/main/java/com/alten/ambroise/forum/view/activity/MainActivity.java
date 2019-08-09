package com.alten.ambroise.forum.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.view.fragmentSwitcher.ForumFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.ForumListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ForumListFragment.OnListFragmentInteractionListener {

    private static final String STATE_FORUM_FRAGMENT_SWITCHER = "forumFragmentSwitcher";

    private ForumFragmentSwitcher forumFragmentSwitcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //If is not a restored activity
        if (savedInstanceState == null) {
            this.forumFragmentSwitcher = new ForumFragmentSwitcher(this);
        } else {
            this.forumFragmentSwitcher = savedInstanceState.getParcelable(STATE_FORUM_FRAGMENT_SWITCHER) != null
                    ? (ForumFragmentSwitcher) savedInstanceState.getParcelable(STATE_FORUM_FRAGMENT_SWITCHER)
                    : new ForumFragmentSwitcher(this);
            this.forumFragmentSwitcher.setActivity(this);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        forumFragmentSwitcher.switchFragment(getSupportFragmentManager(), ForumFragmentSwitcher.FORUM_LIST_TAG);
        fab.setOnClickListener(view -> forumFragmentSwitcher.switchFragment(getSupportFragmentManager(), ForumFragmentSwitcher.ADD_FORUM_TAG));
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_FORUM_FRAGMENT_SWITCHER, this.forumFragmentSwitcher);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.forumFragmentSwitcher = savedInstanceState.getParcelable(STATE_FORUM_FRAGMENT_SWITCHER);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //We pop the backstack.
            super.onBackPressed();
            //If when we pop the first element of backstack, this activity don't have fragment, we add List fragment to dont have empty activity
            if (findViewById(R.id.main_fragment).getTag() == null) {
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onDestroy(){
        getExternalFilesDir(Environment.DIRECTORY_PICTURES).deleteOnExit();
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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
    public boolean onNavigationItemSelected(MenuItem item) {
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
    public void onListFragmentInteraction(Forum item) {
    }
}
