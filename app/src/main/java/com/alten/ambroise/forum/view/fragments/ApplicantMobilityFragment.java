package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.utils.InputFilterMinMax;
import com.alten.ambroise.forum.view.adapter.CustomGridMobilityAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;

public class ApplicantMobilityFragment extends Fragment implements ApplicantInfo{
    private static final String PRESENT_FRANCE_TAG = "France_present";
    private static final String PRESENT_IDF_TAG = "Idf_present";
    private static final String PRESENT_INTERNATIONAL_TAG = "International_present";
    private ArrayList<Mobility> allGeos = new ArrayList<Mobility>();
    private HashSet<String> allGeographicsUsed = new HashSet<String>();
    private int[] allRadius = {};
    private String currentUnit = "mins";
    private TextInputEditText geographicsInput;
    private TextInputEditText radiusInput;
    private Switch unitSwitch;
    private Button addGeographics;
    private Button buttonFrance;
    private boolean isFranceChecked = false;
    private Button buttonFranceWithoutIDF;
    private boolean isIDFChecked = false;
    private Switch internationalSwitch;
    private Integer internationalId;

    private GridView gridView;


    private OnFragmentInteractionListener mListener;

    public ApplicantMobilityFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_mobility, container, false);
        this.gridView = view.findViewById(R.id.gridView1);

        addGeographics = view.findViewById(R.id.button_add_geographics);
        addGeographics.setEnabled(false);
        addGeographics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Mobility createdMobility = createNewMobility(geographicsInput.getText().toString(), Integer.parseInt(radiusInput.getText().toString()), currentUnit);

                if (!allGeographicsUsed.contains(geographicsInput.getText().toString().toLowerCase())) {
                    allGeos.add(createdMobility);
                    allGeographicsUsed.add(geographicsInput.getText().toString().toLowerCase());
                }
                refreshGridView();
                geographicsInput.getText().clear();
                radiusInput.getText().clear();
                unitSwitch.setChecked(false);
            }
        });

        geographicsInput = view.findViewById(R.id.geographics_input_editText);
        geographicsInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        radiusInput = view.findViewById(R.id.radius_input_editText);
        radiusInput.setFilters(new InputFilter[]{new InputFilterMinMax(getResources().getInteger(R.integer.mobility_min_radius),getResources().getInteger(R.integer.mobility_max_radius))});
        radiusInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkValidity();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        unitSwitch = view.findViewById(R.id.switch2);
        unitSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                currentUnit = isChecked ? "kms" : "mins";
            }
        });



        buttonFrance = view.findViewById(R.id.buttonFrance);
        buttonFrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFranceChecked && !tagExists(PRESENT_FRANCE_TAG)) {
                    Mobility createdMobility = createNewMobility("France", 0, "kms");
                    createdMobility.setTag(PRESENT_FRANCE_TAG);

                    if (isIDFChecked) {
                        deleteGeographic("France without IDF");
                    }

                    allGeos.add(createdMobility);
                    isFranceChecked = true;
                    isIDFChecked = false;
                    buttonFrance.setEnabled(false);
                    buttonFranceWithoutIDF.setEnabled(isFranceChecked);
                    refreshGridView();
                }
            }
        });

        buttonFranceWithoutIDF = view.findViewById(R.id.buttonIDF);
        buttonFranceWithoutIDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isIDFChecked && !tagExists(PRESENT_IDF_TAG)) {
                    Mobility createdMobility = createNewMobility("France without IDF", 0, "kms");
                    createdMobility.setTag(PRESENT_IDF_TAG);

                    if (isFranceChecked) {
                        deleteGeographic("France");
                    }

                    allGeos.add(createdMobility);
                    isIDFChecked = true;
                    isFranceChecked = false;
                    buttonFranceWithoutIDF.setEnabled(false);
                    buttonFrance.setEnabled(isIDFChecked);
                    refreshGridView();
                }
            }
        });

        internationalSwitch = view.findViewById(R.id.switchInternational);

        internationalSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !tagExists(PRESENT_INTERNATIONAL_TAG)) {
                    internationalId = allGeos.size();

                    Mobility createdMobility = createNewMobility("International", 0, "kms");
                    createdMobility.setTag(PRESENT_INTERNATIONAL_TAG);
                    allGeos.add(createdMobility);
                    refreshGridView();
                } else {
                    if (internationalId != null && tagExists(PRESENT_INTERNATIONAL_TAG)) {
                        deleteGeographic("International");
                    }

                    internationalId = null;
                }

            }
        });

        refreshGridView();

        return view;
    }

    private boolean tagExists(String presenceTag) {
        for (Mobility mobility : allGeos) {
            if (mobility.getTag() != null && mobility.getTag().equals(presenceTag)) {
                return true;
            }
        }
        return false;
    }

    private Mobility createNewMobility(String geographic, int radius, String unit) {
        Mobility createdMobility = new Mobility();
        createdMobility.setGeographic(geographic);
        createdMobility.setRadius(radius);
        createdMobility.setUnit(unit);
        return createdMobility;
    }


    private void checkValidity() {
        addGeographics.setEnabled(geographicsInput.getText().length() > 0 && radiusInput.getText().length() > 0);
    }

    @Override
    public void onAttach(@NonNull Context context) {
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



    private void refreshGridView() {
        CustomGridMobilityAdapter adapter = new CustomGridMobilityAdapter(getActivity(), allGeos, allRadius, this);
        this.gridView.setAdapter(adapter);
    }

    public void deleteGeographic(String geo) {
        int position = findPosition(geo);
        if (position != -1) {
            Mobility tobeDeleted = allGeos.get(position);
            if (tobeDeleted.getGeographic().equals("France") || tobeDeleted.getGeographic().equals("France without IDF")) {
                buttonFranceWithoutIDF.setEnabled(true);
                buttonFrance.setEnabled(true);
                isFranceChecked = false;
                isIDFChecked = false;
            } else if (tobeDeleted.getGeographic().equals("International")) {
                internationalSwitch.setChecked(false);
            }
            if (position < allGeos.size()){
                allGeos.remove(position);
                allGeographicsUsed.remove(geo.toLowerCase());
            }
            refreshGridView();
        }
    }

    private int findPosition(String geo) {
        for (Mobility mobility : allGeos) {
            if (mobility.getGeographic().equals(geo)) {
                return allGeos.indexOf(mobility);
            }
        }
        return -1;
    }

    @Override
    public void saveInformation(ApplicantForum applicant) {
        applicant.setMobilities(allGeos);

        mListener.onFragmentInteraction(applicant);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
