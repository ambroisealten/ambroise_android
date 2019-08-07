package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.activity.RGPDActivity;
import com.alten.ambroise.forum.view.fragmentSwitcher.RGPDFragmentSwitcher;
import com.github.gcacace.signaturepad.views.SignaturePad;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SignFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SignFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private SignaturePad mSignaturePad;
    private String signature;
    private ApplicantForum applicant;

    public SignFragment() {
        // Required empty public constructor
    }

    public static SignFragment newInstance(String param1, String param2) {
        SignFragment fragment = new SignFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
            this.applicant = getArguments().getParcelable(RGPDActivity.STATE_APPLICANT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_sign, container, false);
        mSignaturePad = view.findViewById(R.id.signature_pad);

        //To Clear or not to clear, that is the question. Remove if we dont want action on signed
//        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
//
//            @Override
//            public void onStartSigning() {
//                //Event triggered when the pad is touched
//            }
//
//            @Override
//            public void onSigned() {
//                //Event triggered when the pad is signed
//            }
//
//            @Override
//            public void onClear() {
//                //Event triggered when the pad is cleared
//            }
//        });
        Button decline = view.findViewById(R.id.decline_sign);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onFragmentInteraction(false, RGPDFragmentSwitcher.RGPD_SIGN_TAG, applicant);
            }
        });

        Button accept = view.findViewById(R.id.accept_sign);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signature = mSignaturePad.getSignatureSvg();
                applicant.setSign(signature);
                mListener.onFragmentInteraction(true, RGPDFragmentSwitcher.RGPD_SIGN_TAG, applicant);
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
