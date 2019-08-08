package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.view.fragmentSwitcher.RGPDFragmentSwitcher;

import static com.alten.ambroise.forum.view.activity.RGPDActivity.STATE_APPLICANT;

public class GradeAndSendFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ApplicantForum applicant;

    public GradeAndSendFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            final ApplicantForumViewModel applicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);
            this.applicant = applicantForumViewModel.getApplicant(getArguments().getLong(STATE_APPLICANT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_grade_and_send, container, false);

        Toolbar toolbar = view.findViewById(R.id.toolbar_grade_send);
        toolbar.setTitle(getString(R.string.end_of_process));
        toolbar.setSubtitle(this.applicant.getName() + " " + this.applicant.getSurname());
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        Button validate = view.findViewById(R.id.validation_and_send);
        validate.setOnClickListener(v -> {
            applicant.setGrade(((Spinner) view.findViewById(R.id.spinner_grade)).getSelectedItem().toString());
            applicant.setPersonInChargeMail(((AutoCompleteTextView) view.findViewById(R.id.send_to)).getText().toString());

            mListener.onFragmentInteraction(true, RGPDFragmentSwitcher.RGPD_GRADE_AND_SEND_TAG, applicant);
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.array_adapter, getResources().getStringArray(R.array.managers));
        AutoCompleteTextView textView = view.findViewById(R.id.send_to);
        textView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(boolean accept, String tag, ApplicantForum... applicant);
    }
}
