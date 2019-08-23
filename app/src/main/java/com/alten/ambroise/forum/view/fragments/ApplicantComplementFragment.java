package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.utils.InputFilterMinMax;
import com.alten.ambroise.forum.utils.Nationality;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantComplementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ApplicantComplementFragment extends Fragment implements ApplicantInfo {

    private OnFragmentInteractionListener mListener;

    public ApplicantComplementFragment() {
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
        setRetainInstance(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_complement, container, false);

        TextView txtView = view.findViewById(R.id.wage_expectation);
        txtView.setFilters(new InputFilter[]{new InputFilterMinMax(0, 1000000)});

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void saveInformation(ApplicantForum applicant) {
        applicant.setDriverLicense(((CheckBox) getView().findViewById(R.id.license_checkbox)).isChecked());
        applicant.setVehicule(((CheckBox) getView().findViewById(R.id.vehicle_checkbox)).isChecked());

        int id = ((RadioGroup) getView().findViewById(R.id.group_nationality)).getCheckedRadioButtonId();

        if (id != -1) {
            applicant.setNationality(Nationality.getNationality(getView().findViewById(id).getTag().toString()));
        } else {
            applicant.setNationality(Nationality.NONE);
        }

        String wage_expectation = ((EditText) getView().findViewById(R.id.wage_expectation)).getText().toString();

        if (wage_expectation.length() > 0) {
            applicant.setWageExpectations(Integer.parseInt(wage_expectation));
        }

        mListener.onFragmentInteraction(applicant);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
