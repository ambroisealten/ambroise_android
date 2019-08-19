package com.alten.ambroise.forum.view.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Document;
import com.alten.ambroise.forum.view.activity.DocumentDetailActivity;
import com.alten.ambroise.forum.view.activity.DocumentListActivity;
import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * A fragment representing a single document detail screen.
 * This fragment is either contained in a {@link DocumentListActivity}
 * in two-pane mode (on tablets) or a {@link DocumentDetailActivity}
 * on handsets.
 */
public class DocumentDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item that this fragment
     * represents.
     */
    public static final String ARG_ITEM = "item";

    /**
     * The dummy title this fragment is presenting.
     */
    private Document mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DocumentDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy title specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load title from a title provider.
            mItem = getArguments().getParcelable(ARG_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.title);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.document_detail, container, false);

        // Show the dummy title as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.document_detail)).setText(mItem.uri);
        }

        return rootView;
    }
}
