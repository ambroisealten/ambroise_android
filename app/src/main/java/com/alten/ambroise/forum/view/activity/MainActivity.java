package com.alten.ambroise.forum.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProviders;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Document;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.data.model.viewModel.DocumentViewModel;
import com.alten.ambroise.forum.view.fragmentSwitcher.ForumFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.DocumentDetailFragment;
import com.alten.ambroise.forum.view.fragments.ForumListFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ForumListFragment.OnListFragmentInteractionListener {

    private static final String STATE_FORUM_FRAGMENT_SWITCHER = "forumFragmentSwitcher";

    private ForumFragmentSwitcher forumFragmentSwitcher;
    private HashMap<Integer, Document> documentsMenu = new HashMap<Integer, Document>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //If is not a restored activity
        this.forumFragmentSwitcher = new ForumFragmentSwitcher(this);
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

        DocumentViewModel mDocumentViewModel = ViewModelProviders.of(this).get(DocumentViewModel.class);
        mDocumentViewModel.getAllDocuments().observe(this, documents -> {
            documentsMenu.keySet().forEach(id -> navigationView.getMenu().removeItem(id));
            documentsMenu.clear();
            for (int i = 0; i < documents.size(); i++) {
                final Document document = documents.get(i);
                if (!documentsMenu.containsValue(document)) {
                    final int generateViewId = View.generateViewId();
                    documentsMenu.put(generateViewId, document);
                    navigationView.getMenu().add(R.id.nav_document, generateViewId, i, document.title);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        getExternalFilesDir(Environment.DIRECTORY_PICTURES).deleteOnExit();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_FORUM_FRAGMENT_SWITCHER, this.forumFragmentSwitcher);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        this.forumFragmentSwitcher = savedInstanceState.getParcelable(STATE_FORUM_FRAGMENT_SWITCHER) != null
                ? (ForumFragmentSwitcher) savedInstanceState.getParcelable(STATE_FORUM_FRAGMENT_SWITCHER)
                : new ForumFragmentSwitcher(this);
        this.forumFragmentSwitcher.setActivity(this);
        super.onRestoreInstanceState(savedInstanceState);
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

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        findViewById(R.id.fab).setVisibility(View.VISIBLE);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            Object tag = findViewById(R.id.main_fragment).getTag();
            //We pop the backstack.
            super.onBackPressed();
            //If when we pop the first element of backstack, this activity don't have fragment, we add List fragment to dont have empty activity
            if (tag == ForumFragmentSwitcher.APPLICANT_LIST_TAG) {
                forumFragmentSwitcher.switchFragment(getSupportFragmentManager(), ForumFragmentSwitcher.FORUM_LIST_TAG);
            }
            //If we are on the main fragment, quit the app
            else if (tag == ForumFragmentSwitcher.FORUM_LIST_TAG) {
                super.onBackPressed();
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_forum:
                Intent intentForum = new Intent(this, MainActivity.class);
                startActivity(intentForum);
                break;
            case R.id.nav_new_forum:
                forumFragmentSwitcher.switchFragment(getSupportFragmentManager(), ForumFragmentSwitcher.ADD_FORUM_TAG);
                break;
            case R.id.nav_applicant_list:
                forumFragmentSwitcher.switchFragment(getSupportFragmentManager(), ForumFragmentSwitcher.APPLICANT_LIST_TAG);
                break;
            case R.id.nav_document_list:
                Intent intent = new Intent(getBaseContext(), DocumentListActivity.class);
                startActivity(intent);
                break;
            default:
                Document document = documentsMenu.get(item.getItemId());
                Intent intent1 = new Intent(getBaseContext(), DocumentDetailActivity.class);
                intent1.putExtra(DocumentDetailFragment.ARG_ITEM, document);
                startActivity(intent1);
                break;
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onListFragmentInteraction(Forum item) {
    }
}
