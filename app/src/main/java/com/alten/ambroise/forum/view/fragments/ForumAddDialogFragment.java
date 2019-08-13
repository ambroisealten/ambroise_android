package com.alten.ambroise.forum.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Forum;
import com.alten.ambroise.forum.view.fragmentSwitcher.ForumFragmentSwitcher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_HORIZONTAL;
import static android.view.Gravity.HORIZONTAL_GRAVITY_MASK;

public class ForumAddDialogFragment extends DialogFragment {
    private ForumFragmentSwitcher forumFragmentSwitcher;
    private TextInputEditText inputName;
    private TextInputEditText inputPlace;
    private DatePicker inputDate;
    private TextView previewTextView;

    // Empty constructor required for DialogFragment
    public ForumAddDialogFragment() {
    }

    public static ForumAddDialogFragment newInstance(String title) {
        ForumAddDialogFragment frag = new ForumAddDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void subscribeToSwitcher(ForumFragmentSwitcher forumFragmentSwitcher) {
        this.forumFragmentSwitcher = forumFragmentSwitcher;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = getArguments().getString("title");
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle(title);
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateTextPreview();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };

        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        TextInputLayout inputNameLayout = new TextInputLayout(getContext());
        inputNameLayout.setHint(getString(R.string.forum_name));
        inputNameLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));

        inputName = new TextInputEditText(getContext());
        inputName.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));
        inputName.addTextChangedListener(textWatcher);
        inputNameLayout.addView(inputName);
        layout.addView(inputNameLayout);

        TextInputLayout inputPlaceLayout = new TextInputLayout(getContext());
        inputPlaceLayout.setHint(getString(R.string.forum_place));
        inputPlaceLayout.setHintTextColor(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));

        inputPlace = new TextInputEditText(getContext());
        inputPlace.setTextColor(ColorStateList.valueOf(getResources().getColor(R.color.text_color)));
        inputPlace.addTextChangedListener(textWatcher);
        inputPlaceLayout.addView(inputPlace);
        layout.addView(inputPlaceLayout);

        LinearLayout dateLayout = new LinearLayout(getContext());
        dateLayout.setGravity(CENTER);
        dateLayout.setMinimumWidth(layout.getWidth());
        dateLayout.setLayoutMode(HORIZONTAL_GRAVITY_MASK);

        inputDate = new DatePicker(this.getContext());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            inputDate.setOnDateChangedListener((datePicker, year, month, dayOfMonth) -> updateTextPreview());
        }
        dateLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dateLayout.addView(inputDate);
        layout.addView(dateLayout);

        TextView preview = new TextView(this.getContext());
        preview.setText(R.string.preview);
        preview.setGravity(CENTER_HORIZONTAL);
        layout.addView(preview);

        previewTextView = new TextView(this.getContext());
        previewTextView.setGravity(CENTER_HORIZONTAL);
        layout.addView(previewTextView);
        updateTextPreview();

        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setPositiveButton(R.string.save, (dialog, which) -> {
            Forum newForum = new Forum();
            if (ForumAddDialogFragment.this.inputName.length() == 0) {
                ForumAddDialogFragment.this.inputName.setText(R.string.unknown);
            }
            if (inputPlace.length() == 0) {
                inputPlace.setText(R.string.unknown);
            }
            newForum.setName(ForumAddDialogFragment.this.inputName.getText().toString());
            newForum.setPlace(inputPlace.getText().toString());
            // Check if day and month have only one digit. If it's the case, then add 0 before the digit to match with xx format
            String day = inputDate.getDayOfMonth() <= 9 ? "0" + inputDate.getDayOfMonth() : String.valueOf(inputDate.getDayOfMonth());
            String month = inputDate.getMonth() <= 9 ? "0" + (inputDate.getMonth() + 1) : String.valueOf(inputDate.getMonth() + 1);
            newForum.setDate(day + "/" + month + "/" + inputDate.getYear());
            forumFragmentSwitcher.addNewForum(newForum);
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, (dialog, which) -> {
            if (dialog != null) {
                dialog.dismiss();
            }
        });

        return alertDialogBuilder.create();
    }

    private void updateTextPreview() {
        // Check if day and month have only one digit. If it's the case, then add 0 before the digit to match with xx format
        String day = inputDate.getDayOfMonth() <= 9 ? "0" + inputDate.getDayOfMonth() : String.valueOf(inputDate.getDayOfMonth());
        String month = inputDate.getMonth() < 9 ? "0" + (inputDate.getMonth() + 1) : String.valueOf(inputDate.getMonth() + 1);
        String name = inputName.getText().length() == 0 ? getResources().getString(R.string.unknown) : inputName.getText().toString();
        String place = inputPlace.getText().length() == 0 ? getResources().getString(R.string.unknown) : inputPlace.getText().toString();
        String text = new StringBuilder().append(name)
                .append(" - ").append(day)
                .append(" ").append(month)
                .append(" ").append(inputDate.getYear())
                .append(" – ").append(place)
                .toString();
        previewTextView.setText(text);
    }

}

