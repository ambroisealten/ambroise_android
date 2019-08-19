package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.room.util.StringUtil;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.view.adapter.CustomGridMobilityAdapter;
import com.alten.ambroise.forum.view.adapter.CustomGridStringAdapter;
import com.alten.ambroise.forum.view.fragmentSwitcher.FragmentSwitcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


public class ApplicantViewFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String STATE_APPLICANT = "applicant";
    private FragmentSwitcher switcher;
    private GridView skillsGridView;
    private GridView mobilityGridView;

    private ApplicantForum applicant;
    private long applicantId = 0;

    public ApplicantViewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        //Instantiate applicant forum view model and add observer
        ApplicantForumViewModel mApplicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);
        this.applicant = mApplicantForumViewModel.getApplicant(this.applicantId);
        super.onResume();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.applicantId = getArguments().getLong(STATE_APPLICANT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Instantiate applicant forum view model
        ApplicantForumViewModel mApplicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);
        this.applicant = mApplicantForumViewModel.getApplicant(this.applicantId);
        //System.out.println(this.applicant);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_view, container, false);
        ((TextView)view.findViewById(R.id.applicant_view_surname_name)).setText(this.applicant.getSurname() + " " + this.applicant.getName());
        ((TextView)view.findViewById(R.id.applicant_view_startAt)).setText(this.applicant.getStartAt());
        ((TextView)view.findViewById(R.id.applicant_view_phoneNumber)).setText(this.applicant.getPhoneNumber());
        ((TextView)view.findViewById(R.id.applicant_view_mail)).setText(this.applicant.getMail());
        ((TextView)view.findViewById(R.id.applicant_view_grade)).setText(this.applicant.getGrade());
        ((TextView)view.findViewById(R.id.applicant_view_highestDiploma)).setText(this.applicant.getHighestDiploma());
        ((TextView)view.findViewById(R.id.applicant_view_highestDiplomaYear)).setText(this.applicant.getHighestDiplomaYear());
        ((TextView)view.findViewById(R.id.applicant_view_personInChargeMail)).setText(this.applicant.getPersonInChargeMail());
        ((TextView)view.findViewById(R.id.applicant_view_driverLicense)).setText(this.applicant.isDriverLicense() ? "Driver license" : "");
        ((TextView)view.findViewById(R.id.applicant_view_vehicule)).setText(this.applicant.isVehicule() ? "Own vehicule" : "");
        ((TextView)view.findViewById(R.id.applicant_view_nationality)).setText(this.applicant.getNationality() == null ? "" : this.applicant.getNationality().toString());
        // Skills list
        this.skillsGridView = view.findViewById(R.id.applicant_view_skills_grid_view);
        refreshSkillsGridView();
        // Mobility list
        this.mobilityGridView = view.findViewById(R.id.applicant_view_mobility_grid_view);
        refreshMobilityGridView();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setSwitcher(FragmentSwitcher switcher) {
        this.switcher = switcher;
    }

    private void refreshSkillsGridView() {
        CustomGridStringAdapter adapter = new CustomGridStringAdapter(getActivity(), (ArrayList<String>)this.applicant.getSkills(), this);
        this.skillsGridView.setAdapter(adapter);
    }

    private void refreshMobilityGridView() {
        int[] allRadius = {};
        List<Mobility> mobilities = new ArrayList<>();
        List<LinkedTreeMap> mobilitiesTreemap = (List<LinkedTreeMap>)((Object)this.applicant.getMobilities());
        mobilitiesTreemap.forEach(mobility -> {
            Gson gson = new GsonBuilder().create();
            JsonObject jsonMobility = gson.toJsonTree(mobility).getAsJsonObject();

            mobilities.add(gson.fromJson(jsonMobility, Mobility.class));
        });
        CustomGridMobilityAdapter adapter = new CustomGridMobilityAdapter(getActivity(),(ArrayList<Mobility>) mobilities, allRadius, this);
        this.mobilityGridView.setAdapter(adapter);
    }
}
