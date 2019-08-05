package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.utils.Nationality;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantComplementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ApplicantComplementFragment extends Fragment implements ApplicantInfo {


    private boolean hasLicense;
    private boolean hasVehicle;

    private String nationality = "";


    private OnFragmentInteractionListener mListener;

    public ApplicantComplementFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_complement, container, false);

        return view;
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

    @Override
    public void saveInformation(ApplicantForum applicant) {

        applicant.setDriverLicense(((CheckBox) getView().findViewById(R.id.license_checkbox)).isChecked());
        applicant.setVehicule(((CheckBox) getView().findViewById(R.id.vehicle_checkbox)).isChecked());

        int id = ((RadioGroup) getView().findViewById(R.id.group_nationality)).getCheckedRadioButtonId();

        if(id != -1) {
            applicant.setNationality(Nationality.valueOf(getView().findViewById(id).getTag().toString()));
        }
        else{
            applicant.setNationality(Nationality.NONE);
        }

        mListener.onFragmentInteraction(applicant);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
