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
import androidx.lifecycle.ViewModelProviders;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Document;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.data.model.viewModel.DocumentViewModel;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.DocumentDetailFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.HashMap;
import java.util.Objects;

public class ForumActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String STATE_FORUM = "forum";
    private static final String STATE_APPLICANT_FRAGMENT_SWITCHER = "applicantFragmentSwitcher";

    private Forum forum;
    private ApplicantFragmentSwitcher applicantFragmentSwitcher;
    private FloatingActionButton fab;
    private HashMap<Integer, Document> documentsMenu = new HashMap<Integer, Document>();

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
        this.setTitle(Objects.requireNonNull(forum).toString());
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
        DocumentViewModel mDocumentViewModel = ViewModelProviders.of(this).get(DocumentViewModel.class);
        mDocumentViewModel.getAllDocuments().observe(this, documents -> {
            for (int i = 0; i < documents.size(); i++) {
                final Document document = documents.get(i);
                if (!documentsMenu.containsValue(document)) {
                    final int generateViewId = View.generateViewId();
                    documentsMenu.put(generateViewId, document);
                    navigationView.getMenu().add(R.id.nav_document, generateViewId, i, document.title);
                }
            }
        });
        applicantFragmentSwitcher.switchFragment(getSupportFragmentManager(), ApplicantFragmentSwitcher.APPLICANT_LIST_TAG, this.getForumId());
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRestart() {
        //if (findViewById(R._id.forum_fragment).getTag() != null && findViewById(R._id.forum_fragment).getTag() == ApplicantFragmentSwitcher.APPLICANT_LIST_TAG) {
        fab.setVisibility(View.VISIBLE);
        //}
        super.onRestart();
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (findViewById(R.id.forum_fragment).getTag() != null && findViewById(R.id.forum_fragment).getTag() == ApplicantFragmentSwitcher.APPLICANT_LIST_TAG) {
                findViewById(R.id.forum_fragment).setTag(null);
                super.onBackPressed();
                super.onBackPressed();
            } else {
                super.onBackPressed();
                try{
                    findViewById(R.id.forum_fragment).setTag(getSupportFragmentManager().getBackStackEntryAt(getSupportFragmentManager().getBackStackEntryCount() -1).getName());
                    if (findViewById(R.id.forum_fragment).getTag() == null) {
                        this.applicantFragmentSwitcher.switchFragment(getSupportFragmentManager(), ApplicantFragmentSwitcher.APPLICANT_LIST_TAG, this.getForumId());
                    }
                    if (findViewById(R.id.forum_fragment).getTag() == ApplicantFragmentSwitcher.APPLICANT_LIST_TAG) {
                        findViewById(R.id.fab_forum).setVisibility(View.VISIBLE);
                    }
                }
                catch (ArrayIndexOutOfBoundsException e){
                    Intent newIntent = new Intent(this,MainActivity.class);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(newIntent);
                }
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.nav_forum:
                Intent intentForum = new Intent(this, MainActivity.class);
                startActivity(intentForum);
                break;
            case R.id.nav_new_applicant:
                applicantFragmentSwitcher.switchFragment(getSupportFragmentManager(), ApplicantFragmentSwitcher.ADD_APPLICANT_TAG);
                break;
            case R.id.nav_forum_list:
                Intent intentForumList = new Intent(this, MainActivity.class);
                startActivity(intentForumList);
                break;
            case R.id.nav_document_list:
                Intent intent = new Intent(getBaseContext(),DocumentListActivity.class);
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

    public long getForumId() {
        return forum.get_id();
    }
}
