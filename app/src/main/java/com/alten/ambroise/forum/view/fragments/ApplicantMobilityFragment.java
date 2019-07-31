package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.Toast;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.view.CustomGrid;
import com.alten.ambroise.forum.view.activity.MainActivity;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantMobilityFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApplicantMobilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicantMobilityFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final int minRadius = 0;
    private static final int maxRadius = 500;
    private ArrayList<String> allGeos = new ArrayList<String>();
    private int[] allRadius = {};
    private String currentUnit = "mins";

    private Button addGeographics;
    private boolean isEnabled = false;


    private TextInputEditText geographicsInput;
    private TextInputEditText radiusInput;
    private Switch unitSwitch;

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

        addGeographics = view.findViewById(R.id.button_add_geographics);
        addGeographics.setEnabled(isEnabled);
        addGeographics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    allGeos.add(geographicsInput.getText().toString()+" : "+radiusInput.getText().toString()+currentUnit);
                    gridView.setAdapter(new CustomGrid(getActivity(), allGeos, allRadius));
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

        CustomGrid adapter = new CustomGrid(getActivity(), allGeos, allRadius);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getActivity(), "Click detected", Toast.LENGTH_SHORT);
            }
        });

        return view;
    }

    public void checkValidity() {
        if (geographicsInput.getText().length() > 0 && radiusInput.getText().length() > 0 )  {
            addGeographics.setEnabled(true);
        } else {
            addGeographics.setEnabled(false);
        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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

    public void addGeographic(View v) {

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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
