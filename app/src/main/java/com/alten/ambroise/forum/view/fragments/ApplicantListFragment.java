package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.data.model.viewModel.ForumViewModel;
import com.alten.ambroise.forum.view.activity.ForumActivity;
import com.alten.ambroise.forum.view.adapter.ApplicantRecyclerViewAdapter;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantFragmentSwitcher;

import java.util.List;
import java.util.stream.Collectors;

public class ApplicantListFragment extends Fragment {

    public static final String STATE_COLUMN_COUNT = "column-count";
    private static final String STATE_SWITCHER = "switcher";
    private int mColumnCount = 2;
    private OnListFragmentInteractionListener mListener;
    private ApplicantRecyclerViewAdapter adapter;
    private ApplicantFragmentSwitcher switcher;
    private long forumId = 0;

    public ApplicantListFragment() {
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
    public void onResume(){
        //Instantiate forum view model and add observer
        ApplicantForumViewModel mApplicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);
        mApplicantForumViewModel.getAllApplicants().observe(this, applicants -> {
            final ForumViewModel forumViewModel = ViewModelProviders.of(this).get(ForumViewModel.class);
            Forum forum = forumViewModel.getForum(this.forumId);
            final List<ApplicantForum> applicantsFiltered = applicants.stream().filter(applicant -> {
                //Conversion with Double necessary but for no known reason. List<Long> was a List<double> with no reason.
                for (final Object forumApplicantId : forum.getApplicants()) {
                    final Double id = (Double) forumApplicantId;
                    if(id.longValue() == applicant.get_id()){
                        return true;
                    }
                }
                return false;
            }).collect(Collectors.toList());
            // Update the cached copy of the applicants in the adapter. (with filter them to don't have unrelated forum applicant
            adapter.setApplicants(applicantsFiltered);
        });
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        final Bundle arguments = getArguments();
        if (arguments != null) {
            this.mColumnCount = arguments.getInt(STATE_COLUMN_COUNT);
            this.forumId = arguments.getLong(ForumActivity.STATE_FORUM);
        }
        if (savedInstanceState != null) {
            this.mColumnCount = savedInstanceState.getInt(STATE_COLUMN_COUNT);
            this.switcher = savedInstanceState.getParcelable(STATE_SWITCHER);
        }
        super.onCreate(savedInstanceState);
        this.adapter = new ApplicantRecyclerViewAdapter(this.getContext(), applicant -> {
            if (applicant != null) {
                switcher.onItemClick(applicant);
            }
        });

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
