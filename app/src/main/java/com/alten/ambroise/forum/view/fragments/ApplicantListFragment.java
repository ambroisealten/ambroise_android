package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantFragmentSwitcher;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ApplicantListFragment extends Fragment {

    // TODO: Customize parameter argument names
    public static final String STATE_COLUMN_COUNT = "column-count";
    public static final String STATE_SWITCHER = "switcher";
    // TODO: Customize parameters
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private ApplicantRecyclerViewAdapter adapter;
    private ApplicantFragmentSwitcher switcher;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ApplicantListFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ApplicantListFragment newInstance(int columnCount) {
        ApplicantListFragment fragment = new ApplicantListFragment();
        Bundle args = new Bundle();
        args.putInt(STATE_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if(savedInstanceState != null){
            this.mColumnCount = savedInstanceState.getInt(STATE_COLUMN_COUNT);
            this.switcher = savedInstanceState.getParcelable(STATE_SWITCHER);
        }
        super.onCreate(savedInstanceState);
        this.adapter = new ApplicantRecyclerViewAdapter(this.getContext(), new ApplicantRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ApplicantForum applicant) {
                if (applicant != null) {
                    switcher.onItemClick(applicant);
                }
            }
        });
        //Instantiate forum view model and add observer
        ApplicantForumViewModel mApplicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);
        mApplicantForumViewModel.getAllApplicants().observe(this, new Observer<List<ApplicantForum>>() {
            @Override
            public void onChanged(@Nullable final List<ApplicantForum> applicant) {
                // Update the cached copy of the applicant in the adapter.
                adapter.setApplicants(applicant);
            }
        });
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(STATE_COLUMN_COUNT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_SWITCHER,switcher);
        savedInstanceState.putInt(STATE_COLUMN_COUNT,mColumnCount);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_applicant_list, container, false);
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
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setSwitcher(ApplicantFragmentSwitcher switcher) {
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
        void onListFragmentInteraction(ApplicantForum item);
    }
}
