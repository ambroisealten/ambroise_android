package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.utils.InputFilterMinMax;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantContractFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApplicantContractFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicantContractFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private Spinner contractSpinner;
    private String contractType = "Internship";

    private DatePicker datePicker;
    private String beginningDate = "";

    private TextView abstractTextView;

    private Spinner optionnalSpinner;
    private EditText optionnalDuration;
    private Integer duration = null;
    private String durationType = "days";

    private EditText withinMonths;
    private Integer monthsUntil = null;



    private OnFragmentInteractionListener mListener;

    public ApplicantContractFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ApplicantContractFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplicantContractFragment newInstance(String param1, String param2) {
        ApplicantContractFragment fragment = new ApplicantContractFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_contract, container, false);

        this.abstractTextView = view.findViewById(R.id.abstractText);

        final String[] contractArray = getResources().getStringArray(R.array.contract_type);
        final String[] contractDurationArray = getResources().getStringArray(R.array.contract_duration_type);

        this.contractSpinner = (Spinner) view.findViewById(R.id.contractSpinner);

        this.withinMonths = (EditText) view.findViewById(R.id.withinMonths);

        this.optionnalSpinner = (Spinner) view.findViewById(R.id.optionnalSpinner);
        this.optionnalSpinner.setEnabled(false);
        this.optionnalSpinner.setFocusable(false);

        this.optionnalDuration = (EditText) view.findViewById(R.id.optionnalDuration);
        this.optionnalDuration.setInputType(InputType.TYPE_NULL);

        final ApplicantContractFragment that = this;

        contractSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                that.contractType = contractArray[position];
                that.refreshAllVars();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                that.contractType = "";
                that.refreshAllVars();
            }
        });

        this.datePicker = (DatePicker) view.findViewById(R.id.datePicker);

        this.datePicker.setMinDate(System.currentTimeMillis() - 1000);
        this.datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener(){
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth){
                that.beginningDate = dayOfMonth+"/"+(month+1)+"/"+year;
                that.monthsUntil = null;
                that.withinMonths.clearComposingText();
                that.setAbstractText();
            }
        });

        this.withinMonths.setFilters(new InputFilter[]{new InputFilterMinMax("1","24")});
        this.withinMonths.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    that.monthsUntil = null;
                }
                else {
                    that.monthsUntil = Integer.parseInt((s.toString()));
                    that.beginningDate = "";
                }
                that.setAbstractText();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.optionnalDuration.setFilters(new InputFilter[]{new InputFilterMinMax("1","31")});
        this.optionnalDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    that.duration = null;
                }
                else if (that.contractType.equals("Internship") || that.contractType.equals("CDD")) that.duration = Integer.parseInt(s.toString());
                else that.duration = null;
                that.setAbstractText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.optionnalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                that.durationType = contractDurationArray[position].toLowerCase();
                that.setAbstractText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                that.durationType = "days";
                that.setAbstractText();
            }
        });

        return view;
    }

    private void refreshAllVars(){
        this.beginningDate = "";
        this.monthsUntil = null;
        this.duration = null;
        this.withinMonths.clearFocus();
        this.withinMonths.getText().clear();
        this.optionnalDuration.clearFocus();
        this.optionnalDuration.getText().clear();
        this.setAbstractText();
    }

    private void refreshView() {
            if (this.contractType.equals("Internship") || this.contractType.equals("CDD")) {
                this.optionnalSpinner.setFocusable(true);
                this.optionnalSpinner.setEnabled(true);
                this.optionnalDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else {
                this.optionnalSpinner.setFocusable(false);
                this.optionnalSpinner.setEnabled(false);
                this.optionnalDuration.setInputType(InputType.TYPE_NULL);

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

    public void setAbstractText(){
        String dateRepresentation = "";
        if(this.monthsUntil == null){
            if(this.beginningDate.length() > 0){
                if(this.duration != null){
                    dateRepresentation = this.contractType+", beginning the "+this.beginningDate+", for "+this.duration+" "+this.durationType;
                }
                else{
                    dateRepresentation = this.contractType+", beginning the "+this.beginningDate;
                }
            }
            else{
                if(this.duration != null){
                    dateRepresentation = this.contractType+", for "+this.duration+" "+this.durationType;
                }
                else{
                    dateRepresentation = this.contractType;
                }
            }
        }
        else{
            if(this.duration != null){
                dateRepresentation = this.contractType+", within "+this.monthsUntil+" months, for "+this.duration+" "+this.durationType;
            }
            else{
                dateRepresentation = this.contractType+", within "+this.monthsUntil+" months";
            }
        }

        this.abstractTextView.setText(dateRepresentation);
        refreshView();
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
