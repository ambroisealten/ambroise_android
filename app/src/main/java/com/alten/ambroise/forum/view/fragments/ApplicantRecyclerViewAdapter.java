package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;

import java.util.List;

public class ApplicantRecyclerViewAdapter extends RecyclerView.Adapter<ApplicantRecyclerViewAdapter.ApplicantListViewHolder> {

    private final ApplicantRecyclerViewAdapter.OnItemClickListener listeners;
    private List<ApplicantForum> mApplicants; // Cached copy of applicants

    ApplicantRecyclerViewAdapter(Context context, ApplicantRecyclerViewAdapter.OnItemClickListener listener) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        this.listeners = listener;
    }

    @NonNull
    @Override
    public ApplicantRecyclerViewAdapter.ApplicantListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_applicant, parent, false);
        return new ApplicantRecyclerViewAdapter.ApplicantListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicantRecyclerViewAdapter.ApplicantListViewHolder holder, int position) {
        if (mApplicants != null) {
            final ApplicantForum applicant = mApplicants.get(position);
            holder.bind(applicant, listeners);
            holder.mItem = applicant;
            holder.mSurnameView.setText(applicant.getSurname());
            holder.mNameView.setText(applicant.getName());
            holder.mGradeView.setText(applicant.getGrade());
            holder.mHighestDiplomaView.setText(applicant.getHighestDiploma());
            holder.mHighestDiplomaYearView.setText(applicant.getHighestDiplomaYear());
            final String contractType = applicant.getContractType();
            holder.mContractTypeView.setText(contractType);
            if (contractType == null || contractType.equals("CDI")) {
                holder.mStartAtView.setText(applicant.getStartAt());
            } else {
                holder.mStartAtView.setText(applicant.getContractDuration());
            }
        } else {
            // Covers the case of data not being ready yet.
            holder.mSurnameView.setText("No Applicant");
        }
    }

    void setApplicants(List<ApplicantForum> applicants) {
        mApplicants = applicants;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mApplicants has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mApplicants != null)
            return mApplicants.size();
        else return 0;
    }

    public interface OnItemClickListener {
        void onItemClick(ApplicantForum applicant);
    }

    public class ApplicantListViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mSurnameView;
        final TextView mNameView;
        final TextView mGradeView;
        final TextView mHighestDiplomaView;
        final TextView mHighestDiplomaYearView;
        final TextView mContractTypeView;
        final TextView mStartAtView;

        ApplicantForum mItem;

        ApplicantListViewHolder(View view) {
            super(view);
            mView = view;
            mSurnameView = view.findViewById(R.id.applicant_surname);
            mNameView = view.findViewById(R.id.applicant_name);
            mGradeView = view.findViewById(R.id.applicant_grade);
            mHighestDiplomaView = view.findViewById(R.id.applicant_diploma_name);
            mHighestDiplomaYearView = view.findViewById(R.id.applicant_diploma_year);
            mContractTypeView = view.findViewById(R.id.applicant_contract);
            mStartAtView = view.findViewById(R.id.applicant_start);
        }

        void bind(final ApplicantForum applicant, final ApplicantRecyclerViewAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(applicant);
                }
            });
        }

        @NonNull
        @Override
        public String toString() {
            return super.toString() + " '" + mSurnameView.getText() + " " + mNameView + "'";
        }
    }
}