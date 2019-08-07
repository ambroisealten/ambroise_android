package com.alten.ambroise.forum.view.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.view.fragmentSwitcher.RGPDFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.GradeAndSendFragment;
import com.alten.ambroise.forum.view.fragments.RGPDTextFragment;
import com.alten.ambroise.forum.view.fragments.SignFragment;
import com.alten.ambroise.forum.view.fragments.ValidationFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class RGPDActivity extends AppCompatActivity implements GradeAndSendFragment.OnFragmentInteractionListener, ValidationFragment.OnFragmentInteractionListener, SignFragment.OnFragmentInteractionListener, RGPDTextFragment.OnFragmentInteractionListener {

    public static final String STATE_APPLICANT = "applicant";
    public static final String STATE_RGPD_FRAGMENT_SWITCHER = "rgpdFragmentSwitcher";
    private ApplicantForum applicant;
    private ApplicantForumViewModel applicantForumViewModel;
    private RGPDFragmentSwitcher rgpdFragmentSwitcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);

        Intent intent = getIntent();
        this.applicant = applicantForumViewModel.getApplicant(intent.getLongExtra(STATE_APPLICANT, -1));


        this.rgpdFragmentSwitcher = new RGPDFragmentSwitcher(this);

        this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(), RGPDFragmentSwitcher.RGPD_TEXT_TAG);

        setContentView(R.layout.activity_rgpd);
    }

    private void stopProcess() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onFragmentInteraction(final boolean accept, final String tag, ApplicantForum... applicant) {
        switch (tag) {
            case RGPDFragmentSwitcher.RGPD_TEXT_TAG:
                if (accept) {
                    this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(), RGPDFragmentSwitcher.RGPD_SIGN_TAG, this.applicant);
                } else {
                    stopProcess();
                }
                break;
            case RGPDFragmentSwitcher.RGPD_SIGN_TAG:
                if (accept) {
                    this.applicant = applicant[0];
                    this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(), RGPDFragmentSwitcher.RGPD_VALIDATION_TAG);
                } else {
                    stopProcess();
                }
                break;
            case RGPDFragmentSwitcher.RGPD_VALIDATION_TAG:
                this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(), RGPDFragmentSwitcher.RGPD_GRADE_AND_SEND_TAG, this.applicant);
                break;
            case RGPDFragmentSwitcher.RGPD_GRADE_AND_SEND_TAG:
                this.applicant = applicant[0];
                String mail = this.applicant.getPersonInChargeMail();
                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.setData(Uri.parse("mailto:" + mail)); //If more than 1 receiver, then use , (comma) to separate them
                intent.putExtra(Intent.EXTRA_SUBJECT, "Applicant " + this.applicant.getName().toUpperCase() + " " + this.applicant.getSurname());
                intent.putExtra(Intent.EXTRA_TEXT, this.applicant.toString());

                //intent.putExtra(Intent.EXTRA_CC,"caboncopiedMail");

                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Permission is not granted
                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Show an explanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.
                    } else {
                        // No explanation needed; request the permission
                        ActivityCompat.requestPermissions(this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                1);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

                final byte[] CvBytes = Base64.decode(this.applicant.getCvPerson(), Base64.DEFAULT);

                Bitmap cvBitmap = BitmapFactory.decodeByteArray(CvBytes, 0, CvBytes.length);

                String path = Environment.getExternalStorageDirectory().toString();
                File file = new File(path, "cv.jpg"); // the File to save , append increasing numeric counter to prevent files from getting overwritten.
                try {
                    FileOutputStream fOut = new FileOutputStream(file);
                    cvBitmap.compress(Bitmap.CompressFormat.JPEG, 85, fOut); // saving the Bitmap to a file compressed as a JPEG with 85% compression rate
                    fOut.flush(); // Not really required
                    fOut.close(); // do not forget to close the stream

                    MediaStore.Images.Media.insertImage(getContentResolver(),file.getAbsolutePath(),file.getName(),file.getName());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
                try {
                    startActivity(Intent.createChooser(intent, getString(R.string.send_email_using)));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(this, getString(R.string.no_client_mail), Toast.LENGTH_SHORT).show();
                }

                //on activity result continue process
                break;
            default:
                Toast.makeText(this, getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

