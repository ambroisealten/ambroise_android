package com.alten.ambroise.forum.view.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.viewModel.DocumentViewModel;
import com.alten.ambroise.forum.view.adapter.SimpleItemRecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

/**
 * An activity representing a list of documents. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link DocumentDetailActivity} representing
 * item uri. On tablets, the activity presents the list of items and
 * item uri side-by-side using two vertical panes.
 */
public class DocumentListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SimpleItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        if (findViewById(R.id.document_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        adapter = new SimpleItemRecyclerViewAdapter(this, mTwoPane);
        
        DocumentViewModel mDocumentViewModel = ViewModelProviders.of(this).get(DocumentViewModel.class);
        mDocumentViewModel.getAllDocuments().observe(this, documents -> this.adapter.setDocuments(documents));

        RecyclerView recyclerView = findViewById(R.id.document_list);
        assert recyclerView != null;
        recyclerView.setAdapter(adapter);
    }

}
