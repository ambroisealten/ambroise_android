package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.fragmentSwitcher.RGPDFragmentSwitcher;

public class RGPDTextFragment extends Fragment {

    private static final String STATE_ACCEPT_ENABLE = "acceptEnable";
    private static final String STATE_ACCEPT_VISIBILITY = "acceptVisibility";
    private OnFragmentInteractionListener mListener;
    private Button accept;

    public RGPDTextFragment() {
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rgpd_text, container, false);
        accept = view.findViewById(R.id.accept_rgpd);
        final ScrollView scrollView = view.findViewById(R.id.scroll_text_rgpd);

        if (savedInstanceState != null) {
            accept.setVisibility(savedInstanceState.getInt(STATE_ACCEPT_VISIBILITY));
            accept.setEnabled(savedInstanceState.getBoolean(STATE_ACCEPT_ENABLE));
        } else {
            accept.setEnabled(false);
            accept.setVisibility(View.GONE);
        }
        Button decline = view.findViewById(R.id.decline_rgpd);


        scrollView.setOnScrollChangeListener((v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            View view1 = scrollView.getChildAt(scrollView.getChildCount() - 1);
            int diff = (view1.getBottom() - (scrollView.getHeight() + scrollView.getScrollY()));

            if (diff == 0) {
                accept.setVisibility(View.VISIBLE);
                accept.setEnabled(true);
            }
        });

        accept.setOnClickListener(v -> mListener.onFragmentInteraction(true, RGPDFragmentSwitcher.RGPD_TEXT_TAG));
        decline.setOnClickListener(v -> mListener.onFragmentInteraction(false, RGPDFragmentSwitcher.RGPD_TEXT_TAG));
        view.post(() -> {
            final int measuredHeight = scrollView.getHeight();
            final int measuredHeight1 = scrollView.getChildAt(0).getHeight();
            if (measuredHeight >= measuredHeight1) {
                accept.setEnabled(true);
                accept.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_ACCEPT_ENABLE, accept.isEnabled());
        savedInstanceState.putInt(STATE_ACCEPT_VISIBILITY, accept.getVisibility());
        super.onSaveInstanceState(savedInstanceState);
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
