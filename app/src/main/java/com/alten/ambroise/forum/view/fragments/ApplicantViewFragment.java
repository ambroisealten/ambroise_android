package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.utils.UtilsMethods;
import com.alten.ambroise.forum.view.adapter.CustomGridMobilityAdapter;
import com.alten.ambroise.forum.view.adapter.CustomGridStringAdapter;
import com.alten.ambroise.forum.view.fragmentSwitcher.FragmentSwitcher;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;
import static com.alten.ambroise.forum.view.fragments.ApplicantAddFragment.REQUEST_IMAGE_CAPTURE;


public class ApplicantViewFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    public static final String STATE_APPLICANT = "applicant";
    private static final int MAIL_REQUEST_CODE = 1;
    private FragmentSwitcher switcher;
    private GridView skillsGridView;
    private GridView mobilityGridView;
    private ImageView cvDisplay;

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
        ((TextView)view.findViewById(R.id.applicant_view_nationality)).setText(this.applicant.getNationality() == null ? "" : "Nationality : " + this.applicant.getNationality().toString());
        // Skills list
        this.skillsGridView = view.findViewById(R.id.applicant_view_skills_grid_view);
        refreshSkillsGridView();
        // Mobility list
        this.mobilityGridView = view.findViewById(R.id.applicant_view_mobility_grid_view);
        refreshMobilityGridView();
        // Share button
        Button shareApplicant = view.findViewById(R.id.applicant_view_share);
        shareApplicant.setOnClickListener(v -> {
            this.sendMail();
        });
        // CV display
        cvDisplay = view.findViewById(R.id.applicant_view_cv_display);
        byte[] decodedString = Base64.decode(this.applicant.getCvPerson(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        cvDisplay.setImageBitmap(decodedByte);
        //add preview picture action
        cvDisplay.setOnClickListener(v -> {
            if (cvDisplay.getDrawable() != null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                decodedByte.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(getContext().getContentResolver(), decodedByte, "Title", null);
                Uri uri = Uri.parse(path);
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_QUICK_VIEW);
                intent.setDataAndType(uri, "image/*");
                intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            }
        });

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

    private void sendMail() {
        String mail = this.applicant.getPersonInChargeMail();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        String cc = UtilsMethods.setToString(preferences.getStringSet("carbon_copied", null));
        intent.setData(Uri.parse("mailto:" + mail + "?cc=" + cc)); //If more than 1 receiver, then use , (comma) to separate them, same for cc (carbon copied because EXTRA_CC not supported by SENDTO
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.cv_that_will_interest_you));
        String body = new StringBuilder().append(getString(R.string.mail_hello_text)).append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(this.applicant.toString())
                .append(System.lineSeparator())
                .append(System.lineSeparator())
                .append(getString(R.string.mail_bye_text))
                .append(System.lineSeparator())
                .append(preferences.getString("signature", ""))
                .toString();
        intent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            final byte[] CvBytes = Base64.decode(this.applicant.getCvPerson(), Base64.DEFAULT);

            Bitmap cvBitmap = BitmapFactory.decodeByteArray(CvBytes, 0, CvBytes.length);

            String path = Environment.getExternalStorageDirectory().toString();
            File file = new File(path, "cv.jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.

            FileOutputStream fOut = new FileOutputStream(file);
            cvBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
            fOut.flush(); // Not really required
            fOut.close(); // do not forget to close the stream

            MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.send_email_using)), MAIL_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), getString(R.string.no_client_mail), Toast.LENGTH_SHORT).show();
        }
    }
}
