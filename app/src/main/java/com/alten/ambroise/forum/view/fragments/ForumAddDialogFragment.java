package com.alten.ambroise.forum.view.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.beans.Forum;
import com.alten.ambroise.forum.view.fragmentSwitcher.ForumFragmentSwitcher;

public class ForumAddDialogFragment extends DialogFragment {
    private EditText mEditText;
    private ForumFragmentSwitcher forumFragmentSwitcher;
    private EditText inputName;
    private EditText inputPlace;
    private DatePicker inputDate;

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

        LinearLayout layout = new LinearLayout(this.getContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setGravity(Gravity.CENTER);

        inputName = new EditText(this.getContext());
        inputName.setHint(R.string.forum_name);
        inputName.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputName);

        inputPlace = new EditText(this.getContext());
        inputPlace.setHint(R.string.forum_place);
        inputPlace.setInputType(InputType.TYPE_CLASS_TEXT);
        layout.addView(inputPlace);

        inputDate = new DatePicker(this.getContext());
        layout.addView(inputDate);

        alertDialogBuilder.setView(layout);

        alertDialogBuilder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Forum newForum = new Forum();
                if(inputName.length() == 0){
                    inputName.setText(R.string.unknown);
                }
                if(inputPlace.length() == 0){
                    inputPlace.setText(R.string.unknown);
                }
                newForum.setName(inputName.getText().toString());
                newForum.setPlace(inputPlace.getText().toString());
                // Check if day and month have only one digit. If it's the case, then add 0 before the digit to match with xx format
                String day = inputDate.getDayOfMonth() <= 9 ? "0" + inputDate.getDayOfMonth() : String.valueOf(inputDate.getDayOfMonth());
                String month = inputDate.getMonth() <= 9 ? "0" + inputDate.getMonth() : String.valueOf(inputDate.getMonth());
                newForum.setDate(day + "/" + month + "/" + inputDate.getYear());
                forumFragmentSwitcher.addNewForum(newForum);
            }
        });
        alertDialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        return alertDialogBuilder.create();
    }

}

