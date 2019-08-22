package com.alten.ambroise.forum.view.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.alten.ambroise.forum.utils.UtilsMethods;
import com.alten.ambroise.forum.view.fragmentSwitcher.ApplicantFragmentSwitcher;
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

public class ApplicantAddFragment extends Fragment {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final String STATE_SWITCHER = "switcher";
    private static final String STATE_APPLICANT_CURRENT_PHOTO_PATH = "currentPhotoPath";
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

    private void checkIfStartAllowed() {
        boolean startAllow = cvDisplay.getDrawable() != null || (UtilsMethods.isValidPhoneNumber(phone.getText()) && UtilsMethods.isValidEmail(mail.getText()) && stringNotNullAndEmpty(name.getText().toString()) && stringNotNullAndEmpty(surname.getText().toString()) && stringNotNullAndEmpty(phone.getText().toString()) && stringNotNullAndEmpty(mail.getText().toString()));
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
                    cvDisplay.setTag("CV_PIC_TAKEN");
                    this.temp = null;
                    checkIfStartAllowed();
                } else {
                    if (cvDisplay.getDrawable() != null) {
                        this.currentPhotoPath = this.temp;
                        this.temp = null;
                    }
                }
                break;
        }
        switcher.setFabInvisible();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            this.switcher = savedInstanceState.getParcelable(STATE_SWITCHER);
            this.currentPhotoPath = savedInstanceState.getString(STATE_APPLICANT_CURRENT_PHOTO_PATH);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant_add, container, false);
        //Add take picture action
        view.findViewById(R.id.cv_button).setOnClickListener(v -> dispatchTakePictureIntent());
        button_start = view.findViewById(R.id.button_start);
        button_start.setOnClickListener(v -> {
            ApplicantForum newApplicant = new ApplicantForum();
            long newId = System.currentTimeMillis();

            newApplicant.setSurname(surname.length() == 0 ? getString(R.string.applicant)+"#" + newId : surname.getText().toString());
            newApplicant.setName(name.length() == 0 ? getString(R.string.applicant)+"#" + newId : name.getText().toString());
            newApplicant.setMail(mail.length() == 0 ? getString(R.string.applicant)+"#" + newId + getString(R.string.noMail) : mail.getText().toString());
            newApplicant.setPhoneNumber(phone.length() == 0 ? "0000000000" : phone.getText().toString());

            final Drawable drawable = cvDisplay.getDrawable();
            if (drawable != null && !cvDisplay.getTag().toString().equals("CV_ADD") && drawable.getClass().equals(BitmapDrawable.class)) {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();

                newApplicant.setCvPerson(Base64.encodeToString(bitMapData, Base64.DEFAULT));
            } else {
                newApplicant.setCvPerson(null);
            }
            switcher.startNewApplicantProcess(newApplicant, getActivity().getSupportFragmentManager());
        });

        cvDisplay = view.findViewById(R.id.cv_display);
        surname = view.findViewById(R.id.surname_input_editText);
        name = view.findViewById(R.id.name_input_editText);
        phone = view.findViewById(R.id.phone_input_editText);
        mail = view.findViewById(R.id.mail_input_editText);

        if (savedInstanceState != null && currentPhotoPath != null) {
            cvDisplay.setImageURI(Uri.fromFile(new File(currentPhotoPath)));
        } else {
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
                if (!UtilsMethods.isValidPhoneNumber(s)) {
                    phone.setError(getString(R.string.invalid_phone));
                    button_start.setEnabled(false);
                }
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
                if (!UtilsMethods.isValidEmail(s)) {
                    mail.setError(getString(R.string.invalid_mail));
                    button_start.setEnabled(false);
                }
            }
        });
        //add preview picture action
        cvDisplay.setOnClickListener(v -> {
            if (cvDisplay.getDrawable() != null && !cvDisplay.getTag().toString().equals(getResources().getString(R.string.add_CV))) {
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.alten.ambroise.forum.fileprovider",
                        new File(currentPhotoPath));
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_QUICK_VIEW);
                intent.setDataAndType(uri, "image/*");
                intent.setFlags(FLAG_GRANT_READ_URI_PERMISSION | FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
            } else {
                dispatchTakePictureIntent();
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        switcher.setFabInvisible();
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putParcelable(STATE_SWITCHER, switcher);
        savedInstanceState.putString(STATE_APPLICANT_CURRENT_PHOTO_PATH, currentPhotoPath);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void setSwitcher(ApplicantFragmentSwitcher applicantFragmentSwitcher) {
        this.switcher = applicantFragmentSwitcher;
    }
}
