package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RadioGroup;

import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.adapter.CustomGridStringAdapter;

import java.util.ArrayList;

public class ApplicantSkillsFragment extends Fragment implements ApplicantInfo {
    private static final String STATE_ALL_SKILLS_REPRESENTATION = "allSkillsRepresentation";
    private static final String STATE_ALL_SKILLS = "allSkills";

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setRetainInstance(true);
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
                String testSkill = s.toString().trim();

                int id = ((RadioGroup) getView().findViewById(R.id.skill_type)).getCheckedRadioButtonId();

                String skillType = getView().findViewById(id).getTag().toString();

                testSkill += " - " + skillType;

                if (allSkills.contains(testSkill.trim().toLowerCase())) {
                    skillsAutoComplete.setError(getString(R.string.invalid_already_existing_skill));
                    buttonAddSkill.setEnabled(false);
                } else {
                    skillsAutoComplete.setError(null);
                    setEnableButton();
                }
            }
        });

        RadioGroup rdGroup = view.findViewById(R.id.skill_type);

        rdGroup.setOnCheckedChangeListener((group, checkedId) -> {
            String testSkill = skillsAutoComplete.getText().toString().trim();

            int id = ((RadioGroup) getView().findViewById(R.id.skill_type)).getCheckedRadioButtonId();

            String skillType = getView().findViewById(id).getTag().toString();

            testSkill += " - " + skillType;

            if (allSkills.contains(testSkill.trim().toLowerCase())) {
                skillsAutoComplete.setError(getString(R.string.invalid_already_existing_skill));
                buttonAddSkill.setEnabled(false);
            } else {
                skillsAutoComplete.setError(null);
                setEnableButton();
            }
        });


        this.buttonAddSkill = view.findViewById(R.id.add_skills_button);
        this.buttonAddSkill.setEnabled(false);
        this.buttonAddSkill.setOnClickListener(v -> {
            String newSkill = that.skillsAutoComplete.getText().toString();

            int id = ((RadioGroup) getView().findViewById(R.id.skill_type)).getCheckedRadioButtonId();

            String skillType = getView().findViewById(id).getTag().toString();

            newSkill += " - " + skillType;


            if (!that.allSkills.contains(newSkill.toLowerCase())) {
                that.allSkillsRepresentation.add(newSkill);
                that.allSkills.add(newSkill.trim().toLowerCase());
            }

            that.skillsAutoComplete.getText().clear();
            that.skillsAutoComplete.setError(null);

            that.refreshGridView();
        });

        if (savedInstanceState != null) {
            this.allSkills = savedInstanceState.getStringArrayList(STATE_ALL_SKILLS);
            this.allSkillsRepresentation = savedInstanceState.getStringArrayList(STATE_ALL_SKILLS_REPRESENTATION);
        }
        this.gridView = view.findViewById(R.id.skills_grid_view);
        this.refreshGridView();

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putStringArrayList(STATE_ALL_SKILLS, allSkills);
        savedInstanceState.putStringArrayList(STATE_ALL_SKILLS_REPRESENTATION, allSkillsRepresentation);
        super.onSaveInstanceState(savedInstanceState);
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

    private void setEnableButton() {
        if (this.skillsAutoComplete.getText().toString().length() > 0) {
            this.buttonAddSkill.setEnabled(true);
        } else {
            this.buttonAddSkill.setEnabled(false);
        }
    }

    public void deleteSkill(String skillToDelete) {
        int position = this.allSkillsRepresentation.indexOf(skillToDelete);
        if (position > -1) {
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
