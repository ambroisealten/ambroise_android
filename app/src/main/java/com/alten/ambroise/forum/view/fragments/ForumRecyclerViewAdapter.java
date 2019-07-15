package com.alten.ambroise.forum.view.fragments;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.beans.Forum;
import com.alten.ambroise.forum.data.viewModel.ForumViewModel;

import java.util.List;

public class ForumRecyclerViewAdapter extends RecyclerView.Adapter<ForumRecyclerViewAdapter.ForumListViewHolder> {

    public class ForumListViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public Forum mItem;

        public ForumListViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = view.findViewById(R.id.item_number);
            mContentView = view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    private final LayoutInflater mInflater;
    private List<Forum> mForums; // Cached copy of words

    ForumRecyclerViewAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
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
            holder.mItem = mForums.get(position);
            holder.mIdView.setText(mForums.get(position).getName());
            holder.mContentView.setText(mForums.get(position).getPlace());
        } else {
            // Covers the case of data not being ready yet.
            holder.mContentView.setText("No Word");
        }
    }

    void setWords(List<Forum> forums) {
        mForums = forums;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mForums has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mForums != null)
            return mForums.size();
        else return 0;
    }
}