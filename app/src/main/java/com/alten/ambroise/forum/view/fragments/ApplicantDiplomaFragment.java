package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.utils.InputFilterMinMax;
import com.alten.ambroise.forum.view.adapter.CustomGridStringAdapter;

import java.util.ArrayList;

public class ApplicantDiplomaFragment extends Fragment implements ApplicantInfo {

    private static final String STATE_ALL_DIPLOMAS_REPRESENTATION = "allDiplomasRepresentation";
    private static final String STATE_ALL_DIPLOMA = "allDiplomas";
    private AutoCompleteTextView diplomaAutoComplete;
    private ArrayList<String> allDiplomasRepresentation = new ArrayList<String>();
    private ArrayList<String> allDiplomas = new ArrayList<String>();
    private String highestDiploma;

    private EditText inputEditDate;
    private Integer highestDiplomaYear = 0;

    private Button addButton;

    private GridView gridView;

    private OnFragmentInteractionListener mListener;

    public ApplicantDiplomaFragment() {
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
        View view = inflater.inflate(R.layout.fragment_applicant_diploma, container, false);

        final ApplicantDiplomaFragment that = this;


        this.diplomaAutoComplete = view.findViewById(R.id.diplomaAutocomplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.array_adapter, getResources().getStringArray(R.array.diplomas));
        this.diplomaAutoComplete.setAdapter(adapter);
        this.diplomaAutoComplete.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                that.setEnableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        this.inputEditDate = view.findViewById(R.id.date_edit_text);
        this.inputEditDate.setFilters(new InputFilter[]{new InputFilterMinMax(0,2030)});
        this.inputEditDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                that.setEnableButton();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        this.addButton = view.findViewById(R.id.add_skills_button);
        this.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String diploma = that.diplomaAutoComplete.getText().toString();
                String diplomaYear = that.inputEditDate.getText().toString();

                that.saveNewDiploma(diploma, diplomaYear);

                that.inputEditDate.getText().clear();
                that.diplomaAutoComplete.getText().clear();

                that.refreshGridView();
            }
        });


        if(savedInstanceState != null){
            this.allDiplomas = savedInstanceState.getStringArrayList(STATE_ALL_DIPLOMA);
            this.allDiplomasRepresentation = savedInstanceState.getStringArrayList(STATE_ALL_DIPLOMAS_REPRESENTATION);
        }
        this.gridView = view.findViewById(R.id.diplomas_grid_view);
        this.refreshGridView();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList(STATE_ALL_DIPLOMA,allDiplomas);
        savedInstanceState.putStringArrayList(STATE_ALL_DIPLOMAS_REPRESENTATION,allDiplomasRepresentation);
        super.onSaveInstanceState(savedInstanceState);
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
        applicant.setHighestDiploma(this.highestDiploma);
        applicant.setHighestDiplomaYear(this.highestDiplomaYear.toString());

        mListener.onFragmentInteraction(applicant);
    }

    private void saveNewDiploma(String diploma, String diplomaYear){
        if(! this.allDiplomas.contains(diploma.toLowerCase()) && Integer.parseInt(diplomaYear) > 1930){
            this.allDiplomasRepresentation.add(diploma+" - "+diplomaYear);
            this.allDiplomas.add(diploma.toLowerCase());
        }

        if(Integer.parseInt(diplomaYear) >= this.highestDiplomaYear){
            this.highestDiploma = diploma;
            this.highestDiplomaYear = Integer.parseInt(diplomaYear);
        }

    }

    private void refreshGridView() {
        CustomGridStringAdapter adapter = new CustomGridStringAdapter(getActivity(), allDiplomasRepresentation, this);
        this.gridView.setAdapter(adapter);
    }

    private void setEnableButton(){
        String currentDiploma = this.diplomaAutoComplete.getText().toString();
        String currentDiplomaYear = this.inputEditDate.getText().toString();

        if(currentDiploma.length() > 0 && currentDiplomaYear.length() > 0  && Integer.parseInt(currentDiplomaYear.toString()) > 1930) {
            this.addButton.setEnabled(true);
        }
        else{
            this.addButton.setEnabled(false);
        }
    }

    public void deleteDiploma(String diplomaToDelete){
        int position = allDiplomasRepresentation.indexOf(diplomaToDelete);
        if(position != -1){
            allDiplomasRepresentation.remove(position);
            allDiplomas.remove(position);
            refreshGridView();
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
