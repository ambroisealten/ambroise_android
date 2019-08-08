package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.view.fragmentSwitcher.RGPDFragmentSwitcher;
import com.github.gcacace.signaturepad.views.SignaturePad;

import static com.alten.ambroise.forum.view.activity.RGPDActivity.STATE_APPLICANT;

public class SignFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SignaturePad mSignaturePad;
    private String signature;
    private ApplicantForum applicant;

    public SignFragment() {
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
        final View view = inflater.inflate(R.layout.fragment_sign, container, false);

        Button decline = view.findViewById(R.id.decline_sign);
        decline.setOnClickListener(v -> mListener.onFragmentInteraction(false, RGPDFragmentSwitcher.RGPD_SIGN_TAG, applicant));

        Button accept = view.findViewById(R.id.accept_sign);
        accept.setVisibility(View.GONE);
        accept.setOnClickListener(v -> {
            signature = mSignaturePad.getSignatureSvg();
            applicant.setSign(signature);
            mListener.onFragmentInteraction(true, RGPDFragmentSwitcher.RGPD_SIGN_TAG, applicant);
        });

        mSignaturePad = view.findViewById(R.id.signature_pad);

        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {

            @Override
            public void onStartSigning() {
                //Event triggered when the pad is touched
                accept.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSigned() {
                //Event triggered when the pad is signed
            }

            @Override
            public void onClear() {
                //Event triggered when the pad is cleared
                accept.setVisibility(View.GONE);
            }
        });
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
