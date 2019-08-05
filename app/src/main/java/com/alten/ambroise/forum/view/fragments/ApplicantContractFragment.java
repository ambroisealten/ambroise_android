package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.utils.InputFilterMinMax;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantContractFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApplicantContractFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicantContractFragment extends Fragment implements ApplicantInfo {

    private Spinner contractSpinner;
    private String contractType = "Internship";

    private DatePicker datePicker;
    private String beginningDate = "";

    private TextView abstractTextView;

    private Spinner optionalSpinner;
    private EditText optionalDuration;
    private Integer duration = null;
    private String durationType = "days";

    private EditText withinMonths;
    private Integer monthsUntil = null;

    private String dateRepresentation = "";


    private OnFragmentInteractionListener mListener;

    public ApplicantContractFragment() {
        // Required empty public constructor
    }

    public static ApplicantContractFragment newInstance(String param1, String param2) {
        ApplicantContractFragment fragment = new ApplicantContractFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void refreshAllVars() {
        this.beginningDate = "";
        this.monthsUntil = null;
        this.duration = null;
        this.withinMonths.clearFocus();
        this.withinMonths.getText().clear();
        this.optionalDuration.clearFocus();
        this.optionalDuration.getText().clear();
        this.setAbstractText();
    }

    private void refreshView() {
        if (this.contractType.equals("Internship") || this.contractType.equals("CDD")) {
            this.optionalSpinner.setFocusable(true);
            this.optionalSpinner.setEnabled(true);
            this.optionalDuration.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
            this.optionalSpinner.setFocusable(false);
            this.optionalSpinner.setEnabled(false);
            this.optionalDuration.setInputType(InputType.TYPE_NULL);

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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
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

        this.contractSpinner = view.findViewById(R.id.contractSpinner);

        this.withinMonths = view.findViewById(R.id.withinMonths);

        this.optionalSpinner = view.findViewById(R.id.optionnalSpinner);
        this.optionalSpinner.setEnabled(false);
        this.optionalSpinner.setFocusable(false);

        this.optionalDuration = view.findViewById(R.id.optionnalDuration);
        this.optionalDuration.setInputType(InputType.TYPE_NULL);

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

        this.datePicker = view.findViewById(R.id.datePicker);

        this.datePicker.setMinDate(System.currentTimeMillis() - 1000);
        this.datePicker.setOnDateChangedListener(new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {
                that.beginningDate = dayOfMonth + "/" + (month + 1) + "/" + year;
                that.monthsUntil = null;
                that.withinMonths.clearComposingText();
                that.setAbstractText();
            }
        });

        this.withinMonths.setFilters(new InputFilter[]{new InputFilterMinMax("1", "24")});
        this.withinMonths.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    that.monthsUntil = null;
                } else {
                    that.monthsUntil = Integer.parseInt((s.toString()));
                    that.beginningDate = "";
                }
                that.setAbstractText();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        this.optionalDuration.setFilters(new InputFilter[]{new InputFilterMinMax("1", "31")});
        this.optionalDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")) {
                    that.duration = null;
                } else if (that.contractType.equals("Internship") || that.contractType.equals("CDD")) {
                    that.duration = Integer.parseInt(s.toString());
                } else {
                    that.duration = null;
                }
                that.setAbstractText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.optionalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void setAbstractText() {
        if (this.monthsUntil == null) {
            if (this.beginningDate.length() > 0) {
                if (this.duration != null) {
                    this.dateRepresentation = this.contractType + ", beginning the " + this.beginningDate + ", for " + this.duration + " " + this.durationType;
                } else {
                    this.dateRepresentation = this.contractType + ", beginning the " + this.beginningDate;
                }
            } else {
                if (this.duration != null) {
                    this.dateRepresentation = this.contractType + ", for " + this.duration + " " + this.durationType;
                } else {
                    this.dateRepresentation = this.contractType;
                }
            }
        } else {
            if (this.duration != null) {
                this.dateRepresentation = this.contractType + ", within " + this.monthsUntil + " months, for " + this.duration + " " + this.durationType;
            } else {
                this.dateRepresentation = this.contractType + ", within " + this.monthsUntil + " months";
            }
        }

        this.abstractTextView.setText(dateRepresentation);
        refreshView();
    }

    @Override
    public void saveInformation(ApplicantForum applicant) {
        applicant.setContractType(contractType);
        applicant.setContractDuration(duration.toString());
        applicant.setStartAt(this.dateRepresentation);
        mListener.onFragmentInteraction(applicant);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
