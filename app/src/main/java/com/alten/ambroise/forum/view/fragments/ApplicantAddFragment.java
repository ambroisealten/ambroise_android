package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantFragmentSwitcher;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApplicantAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicantAddFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private ApplicantFragmentSwitcher applicantFragmentSwitcher;

    public ApplicantAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ApplicantAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplicantAddFragment newInstance() {
        ApplicantAddFragment fragment = new ApplicantAddFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_applicant_add, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(ApplicantForum applicant) {
        if (mListener != null) {
            mListener.onFragmentInteraction(applicant);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setSwitcher(ApplicantFragmentSwitcher applicantFragmentSwitcher) {
        this.applicantFragmentSwitcher = applicantFragmentSwitcher;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
