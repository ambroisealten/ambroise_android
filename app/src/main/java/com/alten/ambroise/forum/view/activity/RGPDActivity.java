package com.alten.ambroise.forum.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.PreferenceManager;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.data.model.viewModel.ApplicantForumViewModel;
import com.alten.ambroise.forum.data.model.viewModel.ForumViewModel;
import com.alten.ambroise.forum.view.fragmentSwitcher.RGPDFragmentSwitcher;
import com.alten.ambroise.forum.view.fragments.GradeAndSendFragment;
import com.alten.ambroise.forum.view.fragments.RGPDTextFragment;
import com.alten.ambroise.forum.view.fragments.SignFragment;
import com.alten.ambroise.forum.view.fragments.ValidationFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

public class RGPDActivity extends AppCompatActivity implements GradeAndSendFragment.OnFragmentInteractionListener, ValidationFragment.OnFragmentInteractionListener, SignFragment.OnFragmentInteractionListener, RGPDTextFragment.OnFragmentInteractionListener {

    public static final String STATE_APPLICANT = "applicant";
    public static final String STATE_RGPD_FRAGMENT_SWITCHER = "rgpdFragmentSwitcher";
    private static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    private static final int MAIL_REQUEST_CODE = 1;
    private ApplicantForum applicant;
    private ApplicantForumViewModel applicantForumViewModel;
    private RGPDFragmentSwitcher rgpdFragmentSwitcher;
    private long forumId;
    private ForumViewModel forumViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicantForumViewModel = ViewModelProviders.of(this).get(ApplicantForumViewModel.class);
        forumViewModel = ViewModelProviders.of(this).get(ForumViewModel.class);
        Intent intent = getIntent();
        this.forumId = intent.getLongExtra(ForumActivity.STATE_FORUM, -1);
        this.applicant = applicantForumViewModel.getApplicant(intent.getLongExtra(STATE_APPLICANT, -1));


        this.rgpdFragmentSwitcher = new RGPDFragmentSwitcher(this);

        this.rgpdFragmentSwitcher.switchFragment(getSupportFragmentManager(), RGPDFragmentSwitcher.RGPD_TEXT_TAG);

        setContentView(R.layout.activity_rgpd);
    }

    private void stopProcess() {
        new AlertDialog.Builder(this)
                .setTitle(R.string.alert_process_title)
                .setMessage(R.string.alert_process_message)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    applicantForumViewModel.delete(applicant);

                    goToListApplicant();
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void goToListApplicant() {
        Intent intent = new Intent(this, ForumActivity.class);
        intent.putExtra(ForumActivity.STATE_FORUM, forumViewModel.getForum(this.forumId));
        startActivity(intent);
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
                this.applicantForumViewModel.update(this.applicant);

                Forum forum = this.forumViewModel.getForum(this.forumId);
                forum.putApplicantId(this.applicant.get_id());
                this.forumViewModel.update(forum);


                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);

                } else {
                    sendMail();
                }
                break;
            default:
                Toast.makeText(this, getString(R.string.not_implemented), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                sendMail();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        }
    }

    private void sendMail() {
        String mail = this.applicant.getPersonInChargeMail();
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        final SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String cc = setToString(preferences.getStringSet("carbon_copied", null));
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

            MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch(NullPointerException e){
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // this will make such that when user returns to your app, your app is displayed, instead of the email app.
        try {
            startActivityForResult(Intent.createChooser(intent, getString(R.string.send_email_using)), MAIL_REQUEST_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, getString(R.string.no_client_mail), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case MAIL_REQUEST_CODE:
                    goToListApplicant();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private String setToString(final Set<String> stringSet) {
        if (stringSet == null || stringSet.size() == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        for (final String s : stringSet) {
            if (builder.length() != 0) {
                builder.append(",");
            }
            builder.append(s);
        }
        return builder.toString();
    }
}

