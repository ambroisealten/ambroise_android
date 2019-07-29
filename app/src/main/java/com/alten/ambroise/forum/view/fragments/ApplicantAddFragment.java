package com.alten.ambroise.forum.view.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantFragmentSwitcher;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ApplicantAddFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ApplicantAddFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ApplicantAddFragment extends Fragment {

    private static final String STATE_SWITCHER = "switcher";
    private static final String STATE_APPLICANT_CURRENT_PHOTO_PATH = "currentPhotoPath";
    private static final String STATE_APPLICANT_SURNAME = "surname";
    private static final String STATE_APPLICANT_NAME = "name";
    private static final String STATE_APPLICANT_PHONE = "phone";
    private static final String STATE_APPLICANT_MAIL = "mail";
    private static final String STATE_BUTTON_START_ENABLE = "button_start_enable";
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private OnFragmentInteractionListener mListener;
    private ApplicantFragmentSwitcher switcher;
    private String currentPhotoPath;
    private Button button_start;
    private ImageView cvDisplay;
    private TextInputEditText surname;
    private TextInputEditText name;
    private TextInputEditText phone;
    private TextInputEditText mail;
    private String temp;


    public ApplicantAddFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ApplicantAddFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ApplicantAddFragment newInstance() {
        ApplicantAddFragment fragment = new ApplicantAddFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null){
            this.switcher = savedInstanceState.getParcelable(STATE_SWITCHER);
            this.currentPhotoPath = savedInstanceState.getString(STATE_APPLICANT_CURRENT_PHOTO_PATH);
            this.mail.setText(savedInstanceState.getString(STATE_APPLICANT_MAIL));
            this.name.setText(savedInstanceState.getString(STATE_APPLICANT_NAME));
            this.phone.setText(savedInstanceState.getString(STATE_APPLICANT_PHONE));
            this.surname.setText(savedInstanceState.getString(STATE_APPLICANT_SURNAME));
            this.button_start.setEnabled(savedInstanceState.getBoolean(STATE_BUTTON_START_ENABLE));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_SWITCHER, switcher);
        savedInstanceState.putString(STATE_APPLICANT_CURRENT_PHOTO_PATH, currentPhotoPath);
        savedInstanceState.putString(STATE_APPLICANT_MAIL, mail.getText().toString());
        savedInstanceState.putString(STATE_APPLICANT_NAME, name.getText().toString());
        savedInstanceState.putString(STATE_APPLICANT_PHONE, phone.getText().toString());
        savedInstanceState.putString(STATE_APPLICANT_SURNAME, surname.getText().toString());
        savedInstanceState.putBoolean(STATE_BUTTON_START_ENABLE, button_start.isEnabled());
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_add, container, false);
        //Add take picture action
        view.findViewById(R.id.cv_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        button_start = view.findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApplicantForum newApplicant = new ApplicantForum();
                long newId = System.currentTimeMillis();

                newApplicant.setSurname(surname.length() == 0 ? "Candidat#" + newId : surname.getText().toString());
                newApplicant.setName(name.length() == 0 ? "Candidat#" + newId : name.getText().toString());
                newApplicant.setMail(mail.length() == 0 ? "Candidat#" + newId + R.string.noMail : surname.getText().toString());

                Bitmap bitmap = ((BitmapDrawable) cvDisplay.getDrawable()).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();

                newApplicant.setCvPerson(Base64.encodeToString(bitMapData, Base64.DEFAULT));

                switcher.startNewApplicantProcess(newApplicant);
            }
        });

        cvDisplay = view.findViewById(R.id.cv_display);
        surname = view.findViewById(R.id.surname_input_editText);
        name = view.findViewById(R.id.name_input_editText);
        phone = view.findViewById(R.id.phone_input_editText);
        mail = view.findViewById(R.id.mail_input_editText);

        if(savedInstanceState != null){
            cvDisplay.setImageURI(Uri.fromFile(new File(currentPhotoPath)));
        }else{
            cvDisplay.setBackground(getActivity().getDrawable(R.drawable.ic_menu_camera));
            button_start.setEnabled(false);
        }

        surname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfStartAllowed();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfStartAllowed();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        phone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfStartAllowed();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkIfStartAllowed();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //add preview picture action
        cvDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cvDisplay.getDrawable() != null) {
                    Uri uri = FileProvider.getUriForFile(getActivity(),
                            "com.alten.ambroise.forum.fileprovider",
                            new File(currentPhotoPath));
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_QUICK_VIEW);
                    intent.setDataAndType(uri, "image/*");
                    intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                    startActivity(intent);
                } else {
                    dispatchTakePictureIntent();
                }
            }
        });
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(ApplicantForum applicant) {
        if (mListener != null) {
            mListener.onFragmentInteraction(applicant);
        }
    }

    private void checkIfStartAllowed() {
        boolean startAllow = cvDisplay.getDrawable() != null || (stringNotNullAndEmpty(name.getText().toString()) && stringNotNullAndEmpty(surname.getText().toString()) && stringNotNullAndEmpty(phone.getText().toString()) && stringNotNullAndEmpty(mail.getText().toString()));
        button_start.setEnabled(startAllow);
    }

    private boolean stringNotNullAndEmpty(String text) {
        return text != null && text.length() > 0;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  //prefix
                ".jpg",         //suffix
                storageDir      //directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        cvDisplay.setBackground(null);
        return image;
    }

    private void dispatchTakePictureIntent() {
        this.temp = this.currentPhotoPath;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this.getContext(), getString(R.string.error_creating_file), Toast.LENGTH_SHORT);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.alten.ambroise.forum.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == Activity.RESULT_OK) {
                    cvDisplay.setImageURI(Uri.fromFile(new File(currentPhotoPath)));
                    this.temp = null;
                    checkIfStartAllowed();
                } else {
                    if (cvDisplay.getDrawable() == null) {
                        cvDisplay.setBackground(getActivity().getDrawable(R.drawable.ic_menu_camera));
                    } else {
                        this.currentPhotoPath = this.temp;
                        this.temp = null;
                    }
                }
                break;
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

    public void setSwitcher(ApplicantFragmentSwitcher applicantFragmentSwitcher) {
        this.switcher = applicantFragmentSwitcher;
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
        void onFragmentInteraction(ApplicantForum applicant);
    }
}
