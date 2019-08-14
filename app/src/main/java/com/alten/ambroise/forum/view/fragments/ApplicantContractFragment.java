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
import com.alten.ambroise.forum.utils.InputFilterMinMax;

public class ApplicantContractFragment extends Fragment implements ApplicantInfo {

    private static final String STATE_DATE_DAY = "dateDay";
    private static final String STATE_DATE_MONTH = "dateMonth";
    private static final String STATE_DATE_YEAR = "dateYear";
    private static final String STATE_WITHIN = "within";
    private static final String STATE_DURATION = "duration";

    private Spinner contractSpinner;

    private DatePicker datePicker;

    private TextView abstractTextView;

    private Spinner optionalSpinner;
    private EditText optionalDuration;

    private EditText withinMonths;

    private OnFragmentInteractionListener mListener;
    private boolean configurationChange = false;

    public ApplicantContractFragment() {
        // Required empty public constructor
    }

    private void refreshView() {
        if (this.contractSpinner.getSelectedItem().equals("Internship") || this.contractSpinner.getSelectedItem().equals("CDD")) {
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
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_contract, container, false);

        this.abstractTextView = view.findViewById(R.id.abstractText);

        this.contractSpinner = view.findViewById(R.id.contractSpinner);

        this.withinMonths = view.findViewById(R.id.withinMonths);

        this.optionalSpinner = view.findViewById(R.id.optionalSpinner);
        this.optionalSpinner.setEnabled(false);
        this.optionalSpinner.setFocusable(false);

        this.optionalDuration = view.findViewById(R.id.optionalDuration);
        this.optionalDuration.setInputType(InputType.TYPE_NULL);

        contractSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(configurationChange){
                    configurationChange = false;
                }else{
                    optionalDuration.getText().clear();
                    withinMonths.getText().clear();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        this.datePicker = view.findViewById(R.id.datePicker);

        this.datePicker.setMinDate(System.currentTimeMillis() - 1000);
        this.datePicker.setOnDateChangedListener((datePicker, year, month, dayOfMonth) -> {
            withinMonths.getText().clear();
            setAbstractText();
        });

        this.withinMonths.setFilters(new InputFilter[]{new InputFilterMinMax("1", "24")});
        this.withinMonths.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setAbstractText();
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
                setAbstractText();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.optionalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setAbstractText();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setAbstractText();
            }
        });

        if (savedInstanceState != null) {
            this.configurationChange = true;
            int day = savedInstanceState.getInt(STATE_DATE_DAY);
            int month = savedInstanceState.getInt(STATE_DATE_MONTH);
            int year = savedInstanceState.getInt(STATE_DATE_YEAR);
            this.datePicker.updateDate(year, month, day);
            this.withinMonths.setText(savedInstanceState.getString(STATE_WITHIN));
            this.optionalDuration.setText(savedInstanceState.getString(STATE_DURATION));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putInt(STATE_DATE_DAY, datePicker.getDayOfMonth());
        savedInstanceState.putInt(STATE_DATE_MONTH, datePicker.getMonth());
        savedInstanceState.putInt(STATE_DATE_YEAR, datePicker.getYear());
        savedInstanceState.putString(STATE_WITHIN,withinMonths.getText().toString());
        savedInstanceState.putString(STATE_DURATION,optionalDuration.getText().toString());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private String getBeginningDate() {
        String day = datePicker.getDayOfMonth() <= 9 ? "0" + datePicker.getDayOfMonth() : String.valueOf(datePicker.getDayOfMonth());
        String month = datePicker.getMonth() < 9 ? "0" + (datePicker.getMonth() + 1) : String.valueOf(datePicker.getMonth() + 1);
        return day + "/" + month + "/" + datePicker.getYear();
    }

    private void setAbstractText() {
        final Editable durationString = optionalDuration.getText();
        int duration = durationString.length() > 0 ? Integer.valueOf(durationString.toString()) : 0;
        final Editable withinMonths = this.withinMonths.getText();
        String dateRepresentation;
        if (withinMonths.length() <= 0) {
            dateRepresentation = duration > 0 ? this.contractSpinner.getSelectedItem() + ", beginning the " + getBeginningDate() + ", for " + duration + " " + this.optionalSpinner.getSelectedItem() : this.contractSpinner.getSelectedItem() + ", beginning the " + getBeginningDate();
        } else {
            dateRepresentation = duration > 0 ? this.contractSpinner.getSelectedItem() + ", within " + withinMonths + " months, for " + duration + " " + this.optionalSpinner.getSelectedItem() : this.contractSpinner.getSelectedItem() + ", within " + withinMonths + " months";
        }
        this.abstractTextView.setText(dateRepresentation);
        refreshView();
    }

    @Override
    public void saveInformation(ApplicantForum applicant) {
        applicant.setContractType((String) this.contractSpinner.getSelectedItem());
        applicant.setContractDuration(optionalDuration.getText().toString());
        applicant.setStartAt(this.abstractTextView.getText().toString());
        mListener.onFragmentInteraction(applicant);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
