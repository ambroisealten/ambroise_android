package com.alten.ambroise.forum.view.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.ApplicantForum;

import java.util.List;

public class ApplicantRecyclerViewAdapter extends RecyclerView.Adapter<ApplicantRecyclerViewAdapter.ApplicantListViewHolder> {

    private final ApplicantRecyclerViewAdapter.OnItemClickListener listeners;

    public interface OnItemClickListener {
        void onItemClick(ApplicantForum applicant);
    }

    public class ApplicantListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mSurnameView;
        public final TextView mNameView;
        public final TextView mGradeView;
        public final TextView mHighestDiplomaView;
        public final TextView mHighestDiplomaYearView;
        public final TextView mStartAtView;

        public ApplicantForum mItem;

        public ApplicantListViewHolder(View view) {
            super(view);
            mView = view;
            mSurnameView = view.findViewById(R.id.applicant_surname);
            mNameView = view.findViewById(R.id.applicant_name);
            mGradeView = view.findViewById(R.id.applicant_grade);
            mHighestDiplomaView = view.findViewById(R.id.applicant_diploma_name);
            mHighestDiplomaYearView = view.findViewById(R.id.applicant_diploma_year);
            mStartAtView = view.findViewById(R.id.applicant_start);
        }

        public void bind(final ApplicantForum applicant, final ApplicantRecyclerViewAdapter.OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(applicant);
                }
            });
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mSurnameView.getText() + " " + mNameView + "'";
        }
    }

    private final LayoutInflater mInflater;
    private List<ApplicantForum> mApplicants; // Cached copy of applicants

    ApplicantRecyclerViewAdapter(Context context, ApplicantRecyclerViewAdapter.OnItemClickListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listeners = listener;
    }

    @Override
    public ApplicantRecyclerViewAdapter.ApplicantListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_applicant, parent, false);
        return new ApplicantRecyclerViewAdapter.ApplicantListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ApplicantRecyclerViewAdapter.ApplicantListViewHolder holder, int position) {
        if (mApplicants != null) {
            holder.bind(mApplicants.get(position), listeners);
            holder.mItem = mApplicants.get(position);
            holder.mSurnameView.setText(mApplicants.get(position).getSurname());
            holder.mNameView.setText(mApplicants.get(position).getName());
            holder.mGradeView.setText(mApplicants.get(position).getGrade());
            holder.mHighestDiplomaView.setText(mApplicants.get(position).getHighestDiploma());
            holder.mHighestDiplomaYearView.setText(mApplicants.get(position).getHighestDiplomaYear());
            holder.mStartAtView.setText(mApplicants.get(position).getStartAt());
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
}