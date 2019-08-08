package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.adapter.CustomGridStringAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantSkillsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ApplicantSkillsFragment extends Fragment implements ApplicantInfo {

    private AutoCompleteTextView skillsAutoComplete;
    private ArrayList<String> allSkillsRepresentation = new ArrayList<String>();
    private ArrayList<String> allSkills = new ArrayList<String>();

    private Button buttonAddSkill;

    private GridView gridView;

    private OnFragmentInteractionListener mListener;

    public ApplicantSkillsFragment() {
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
        View view = inflater.inflate(R.layout.fragment_applicant_skills, container, false);

        final ApplicantSkillsFragment that = this;

        this.skillsAutoComplete = view.findViewById(R.id.skillsAutoComplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.array_adapter, getResources().getStringArray(R.array.skills));
        this.skillsAutoComplete.setAdapter(adapter);
        this.skillsAutoComplete.addTextChangedListener(new TextWatcher() {
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

        this.buttonAddSkill = view.findViewById(R.id.add_skills_button);
        this.buttonAddSkill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newSkill = that.skillsAutoComplete.getText().toString();

                if( !that.allSkills.contains(newSkill.toLowerCase())){
                    that.allSkillsRepresentation.add(newSkill);
                    that.allSkills.add(newSkill.toLowerCase());
                }

                that.skillsAutoComplete.getText().clear();

                that.refreshGridView();
            }
        });

        this.gridView = view.findViewById(R.id.skills_grid_view);

        this.refreshGridView();

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

    private void refreshGridView() {
        CustomGridStringAdapter adapter = new CustomGridStringAdapter(getActivity(), allSkillsRepresentation, this);
        this.gridView.setAdapter(adapter);
    }

    private void setEnableButton(){
        if (this.skillsAutoComplete.getText().toString().length() > 0){
            this.buttonAddSkill.setEnabled(true);
        }
        else{
            this.buttonAddSkill.setEnabled(false);
        }
    }

    public void deleteSkill(String skillToDelete){
        int position = this.allSkillsRepresentation.indexOf(skillToDelete);
        if (position > -1){
            this.allSkillsRepresentation.remove(position);
            this.allSkills.remove(position);
            this.refreshGridView();
        }
    }

    @Override
    public void saveInformation(ApplicantForum applicant) {
        applicant.setSkills(allSkillsRepresentation);

        mListener.onFragmentInteraction(applicant);
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
