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
import android.widget.CheckBox;
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

public class ApplicantMobilityFragment extends Fragment implements ApplicantInfo {
    private static final String PRESENT_FRANCE_TAG = "France_present";
    private static final String PRESENT_IDF_TAG = "Idf_present";
    private static final String PRESENT_INTERNATIONAL_TAG = "International_present";
    private static final String STATE_ALL_GEOS = "allGeos";
    private static final String STATE_ALL_GEOS_USED = "allGeosUsed";
    private static final String STATE_ALL_RADIUS = "allRadius";
    private static final String STATE_IDF_CHECKED = "idfChecked";
    private static final String STATE_FRANCE_CHECKED = "franceChecked";
    private ArrayList<Mobility> allGeos = new ArrayList<Mobility>();
    private HashSet<String> allGeographicsUsed = new HashSet<String>();
    private int[] allRadius = {};
    private boolean isIDFChecked = false;
    private boolean isFranceChecked = false;
    private String currentUnit = "mins";
    private TextInputEditText geographicsInput;
    private TextInputEditText radiusInput;
    private Switch unitSwitch;
    private Button addGeographics;
    private Button buttonFrance;
    private Button buttonFranceWithoutIDF;

    private GridView gridView;


    private OnFragmentInteractionListener mListener;

    public ApplicantMobilityFragment() {
        // Required empty public constructor
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_mobility, container, false);
        this.gridView = view.findViewById(R.id.gridView1);

        addGeographics = view.findViewById(R.id.button_add_geographics);
        addGeographics.setEnabled(false);
        addGeographics.setOnClickListener(v -> {
            Mobility createdMobility = createNewMobility(geographicsInput.getText().toString(), Integer.parseInt(radiusInput.getText().toString()), currentUnit);

            if (!allGeographicsUsed.contains(geographicsInput.getText().toString().trim().toLowerCase()) && !geographicsInput.getText().toString().trim().equals(getString(R.string.france)) && !geographicsInput.getText().toString().trim().equals(getString(R.string.franceWithoutIDF)) && !geographicsInput.getText().toString().trim().equals(getString(R.string.international))) {
                allGeos.add(createdMobility);
                allGeographicsUsed.add(geographicsInput.getText().toString().trim().toLowerCase());
            }
            refreshGridView();
            geographicsInput.getText().clear();
            radiusInput.getText().clear();
            unitSwitch.setChecked(false);
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
                if (allGeographicsUsed.contains(s.toString().trim())) {
                    geographicsInput.setError(getString(R.string.invalid_already_existing_diploma));
                    addGeographics.setEnabled(false);
                    radiusInput.setEnabled(false);
                }else {
                    addGeographics.setError(null);
                    radiusInput.setEnabled(true);
                    checkValidity();
                }
            }
        });

        radiusInput = view.findViewById(R.id.radius_input_editText);
        radiusInput.setFilters(new InputFilter[]{new InputFilterMinMax(getResources().getInteger(R.integer.mobility_min_radius), getResources().getInteger(R.integer.mobility_max_radius))});
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
        unitSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> currentUnit = isChecked ? "kms" : "mins");


        buttonFrance = view.findViewById(R.id.buttonFrance);
        if (tagExists(PRESENT_FRANCE_TAG)) {
            buttonFrance.setEnabled(false);
        }
        buttonFrance.setOnClickListener(v -> {
            if (!isFranceChecked && !tagExists(PRESENT_FRANCE_TAG)) {
                Mobility createdMobility = createNewMobility(getString(R.string.france), 0, "kms");
                createdMobility.setTag(PRESENT_FRANCE_TAG);

                if (isIDFChecked) {
                    deleteGeographic(getString(R.string.franceWithoutIDF));
                }

                allGeos.add(createdMobility);
                isFranceChecked = true;
                isIDFChecked = false;
                buttonFrance.setEnabled(false);
                buttonFranceWithoutIDF.setEnabled(isFranceChecked);
                refreshGridView();
            }
        });

        buttonFranceWithoutIDF = view.findViewById(R.id.buttonIDF);
        if (tagExists(PRESENT_IDF_TAG)) {
            buttonFranceWithoutIDF.setEnabled(false);
        }
        buttonFranceWithoutIDF.setOnClickListener(v -> {
            if (!isIDFChecked && !tagExists(PRESENT_IDF_TAG)) {
                Mobility createdMobility = createNewMobility(getString(R.string.franceWithoutIDF), 0, "kms");
                createdMobility.setTag(PRESENT_IDF_TAG);

                if (isFranceChecked) {
                    deleteGeographic(getString(R.string.france));
                }

                allGeos.add(createdMobility);
                isIDFChecked = true;
                isFranceChecked = false;
                buttonFranceWithoutIDF.setEnabled(false);
                buttonFrance.setEnabled(isIDFChecked);
                refreshGridView();
            }
        });

        if (savedInstanceState != null) {
            this.isFranceChecked = savedInstanceState.getBoolean(STATE_FRANCE_CHECKED);
            this.isIDFChecked = savedInstanceState.getBoolean(STATE_IDF_CHECKED);
            this.allRadius = savedInstanceState.getIntArray(STATE_ALL_RADIUS);
            this.allGeos = savedInstanceState.getParcelableArrayList(STATE_ALL_GEOS);
            this.allGeographicsUsed = (HashSet<String>) savedInstanceState.getSerializable(STATE_ALL_GEOS_USED);
            buttonFranceWithoutIDF.setEnabled(!isIDFChecked);
            buttonFrance.setEnabled(!isFranceChecked);
        }
        refreshGridView();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(STATE_IDF_CHECKED, this.isIDFChecked);
        savedInstanceState.putBoolean(STATE_FRANCE_CHECKED, this.isFranceChecked);
        savedInstanceState.putIntArray(STATE_ALL_RADIUS, this.allRadius);
        savedInstanceState.putParcelableArrayList(STATE_ALL_GEOS, this.allGeos);
        savedInstanceState.putSerializable(STATE_ALL_GEOS_USED, this.allGeographicsUsed);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    private void refreshGridView() {
        allGeos.removeIf(mobility -> mobility.getGeographic().equals(getString(R.string.international)));


        CustomGridMobilityAdapter adapter = new CustomGridMobilityAdapter(getActivity(), allGeos, allRadius, this);
        this.gridView.setAdapter(adapter);
    }

    public void deleteGeographic(String geo) {
        int position = findPosition(geo);
        if (position != -1) {
            Mobility tobeDeleted = allGeos.get(position);
            if (tobeDeleted.getGeographic().equals(getString(R.string.france)) || tobeDeleted.getGeographic().equals(getString(R.string.franceWithoutIDF))) {
                buttonFranceWithoutIDF.setEnabled(true);
                buttonFrance.setEnabled(true);
                isFranceChecked = false;
                isIDFChecked = false;
            }
            if (position < allGeos.size()) {
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
        if (((CheckBox) getView().findViewById(R.id.internationalCheckBox)).isChecked()) {
            Mobility createdMobility = createNewMobility(getString(R.string.international), 0, "kms");
            createdMobility.setTag(PRESENT_INTERNATIONAL_TAG);
            allGeos.add(createdMobility);
        }


        applicant.setMobilities(allGeos);

        mListener.onFragmentInteraction(applicant);
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
