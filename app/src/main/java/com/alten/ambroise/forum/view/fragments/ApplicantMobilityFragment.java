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

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.utils.InputFilterMinMax;
import com.alten.ambroise.forum.view.adapter.CustomGridAdapter;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantMobilityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApplicantMobilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicantMobilityFragment extends Fragment implements ApplicantInfo{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int minRadius = 0;
    private static final int maxRadius = 500;
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
    private boolean isEnabled = false;
    private Button buttonFrance;
    private boolean isFranceChecked = false;
    private int franceMobilityID;
    private Button buttonFranceWithoutIDF;
    private boolean isIDFChecked = false;
    private int idfMobilityID;
    private Switch internationalSwitch;
    private Integer internationalId;

    // TODO: Rename and change types of parameters
    private GridView gridView;


    private OnFragmentInteractionListener mListener;

    public ApplicantMobilityFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApplicantMobilityFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplicantMobilityFragment newInstance(String param1, String param2) {
        ApplicantMobilityFragment fragment = new ApplicantMobilityFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_mobility, container, false);
        gridView = view.findViewById(R.id.gridView1);

        final ApplicantMobilityFragment that = this;

        addGeographics = view.findViewById(R.id.button_add_geographics);
        addGeographics.setEnabled(isEnabled);
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
        radiusInput.setFilters(new InputFilter[]{new InputFilterMinMax("1","150")});
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

        refreshGridView();

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

                    franceMobilityID = allGeos.size();
                    allGeos.add(createdMobility);
                    isFranceChecked = true;
                    isIDFChecked = false;
                    buttonFrance.setEnabled(isIDFChecked);
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

                    idfMobilityID = allGeos.size();
                    allGeos.add(createdMobility);
                    isIDFChecked = true;
                    isFranceChecked = false;
                    buttonFranceWithoutIDF.setEnabled(isFranceChecked);
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

        return view;
    }

    private boolean tagExists(String presenceTag) {
        for (Mobility mobility_pour_faire_plaisir_a_andy : allGeos) {
            if (mobility_pour_faire_plaisir_a_andy.getTag() == presenceTag) {
                return true;
            }
        }
        return false;
    }

    public Mobility createNewMobility(String geographic, int radius, String unit) {
        Mobility createdMobility = new Mobility();
        createdMobility.setGeographic(geographic);
        createdMobility.setRadius(radius);
        createdMobility.setUnit(unit);
        return createdMobility;
    }


    public void checkValidity() {
        if (geographicsInput.getText().length() > 0 && radiusInput.getText().length() > 0) {
            addGeographics.setEnabled(true);
        } else {
            addGeographics.setEnabled(false);
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

    public void refreshGridView() {
        CustomGridAdapter adapter = new CustomGridAdapter(getActivity(), allGeos, allRadius, this);
        gridView.setAdapter(adapter);
    }

    public void deleteGeographic(String geo) {
        int position = findPosition(geo);
        if (position != -1) {
            Mobility tobeDeleted = allGeos.get(position);
            if (tobeDeleted.getGeographic() == "France" || tobeDeleted.getGeographic() == "France without IDF") {
                buttonFranceWithoutIDF.setEnabled(true);
                buttonFrance.setEnabled(true);
                isFranceChecked = false;
                isIDFChecked = false;
            } else if (tobeDeleted.getGeographic() == "International") {
                internationalSwitch.setChecked(false);
            } else if (position < allGeos.size()) {
                allGeos.remove(position);
                allGeographicsUsed.remove(geo.toLowerCase());
            }
            refreshGridView();
        }
    }

    public int findPosition(String geo) {
        for (Mobility mobility : allGeos) {
            if (mobility.getGeographic() == geo) {
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
