package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.data.model.viewModel.ForumViewModel;
import com.alten.ambroise.forum.view.adapter.ForumRecyclerViewAdapter;
import com.alten.ambroise.forum.view.fragmentSwitcher.ForumFragmentSwitcher;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ForumListFragment extends Fragment {

    // TODO: Customize parameter argument names
    public static final String STATE_COLUMN_COUNT = "column-count";
    public static final String STATE_SWITCHER = "switcher";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    private ForumRecyclerViewAdapter adapter;
    private ForumFragmentSwitcher switcher;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ForumListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ForumListFragment newInstance(int columnCount) {
        ForumListFragment fragment = new ForumListFragment();
        Bundle args = new Bundle();
        args.putInt(STATE_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            this.mColumnCount = savedInstanceState.getInt(STATE_COLUMN_COUNT);
            this.switcher = savedInstanceState.getParcelable(STATE_SWITCHER);
            this.switcher.setActivity(getActivity());
        }
        this.adapter = new ForumRecyclerViewAdapter(this.getContext(), new ForumRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Forum forum) {
                if (forum != null) {
                    switcher.onItemClick(forum);
                }
            }
        });
        //Instantiate forum view model and add observer
        ForumViewModel mForumViewModel = ViewModelProviders.of(this).get(ForumViewModel.class);
        mForumViewModel.getAllForums().observe(this, new Observer<List<Forum>>() {
            @Override
            public void onChanged(@Nullable final List<Forum> forums) {
                // Update the cached copy of the forums in the adapter.
                adapter.setForums(forums);
            }
        });
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(STATE_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum_list, container, false);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            recyclerView.setAdapter(adapter);
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_SWITCHER, switcher);
        savedInstanceState.putInt(STATE_COLUMN_COUNT, mColumnCount);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setSwitcher(ForumFragmentSwitcher switcher) {
        this.switcher = switcher;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Forum item);
    }
}
