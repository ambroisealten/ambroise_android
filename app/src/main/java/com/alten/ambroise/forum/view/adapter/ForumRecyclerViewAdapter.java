package com.alten.ambroise.forum.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Forum;

import java.util.List;

public class ForumRecyclerViewAdapter extends RecyclerView.Adapter<ForumRecyclerViewAdapter.ForumListViewHolder> {

    private final OnItemClickListener listeners;
    private List<Forum> mForums; // Cached copy of forums

    public ForumRecyclerViewAdapter(Context context, OnItemClickListener listener) {
        LayoutInflater mInflater = LayoutInflater.from(context);
        this.listeners = listener;
    }

    @Override
    public ForumListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_forum, parent, false);
        return new ForumListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ForumListViewHolder holder, int position) {
        if (mForums != null) {
            final Forum forum = mForums.get(position);
            holder.bind(forum, listeners);
            holder.mItem = forum;
            holder.mNameView.setText(forum.getName());
            holder.mPlaceView.setText(forum.getPlace());
            holder.mDateView.setText(forum.getDate());
        } else {
            // Covers the case of data not being ready yet.
            holder.mPlaceView.setText("No forum");
        }
    }

    // getItemCount() is called many times, and when it is first called,
    // mForums has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mForums != null) {
            return mForums.size();
        } else {
            return 0;
        }
    }

    public void setForums(List<Forum> forums) {
        mForums = forums;
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Forum forum);
    }

    public class ForumListViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView mNameView;
        final TextView mPlaceView;
        final TextView mDateView;
        Forum mItem;

        ForumListViewHolder(View view) {
            super(view);
            mView = view;
            mNameView = view.findViewById(R.id.forum_name);
            mPlaceView = view.findViewById(R.id.forum_place);
            mDateView = view.findViewById(R.id.forum_date);
        }

        void bind(final Forum forum, final OnItemClickListener listener) {
            itemView.setOnClickListener(v -> listener.onItemClick(forum));
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mPlaceView.getText() + "'";
        }
    }
}