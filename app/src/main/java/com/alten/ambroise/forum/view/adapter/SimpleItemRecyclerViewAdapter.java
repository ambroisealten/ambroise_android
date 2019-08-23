package com.alten.ambroise.forum.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.beans.Document;
import com.alten.ambroise.forum.view.activity.DocumentDetailActivity;
import com.alten.ambroise.forum.view.activity.DocumentListActivity;
import com.alten.ambroise.forum.view.fragments.DocumentDetailFragment;

import java.util.ArrayList;
import java.util.List;

public class SimpleItemRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.DocumentViewHolder> {

    private final DocumentListActivity mParentActivity;
    private final boolean mTwoPane;
    private List<Document> mValues = new ArrayList<Document>();

    public SimpleItemRecyclerViewAdapter(DocumentListActivity parent,
                                         boolean twoPane) {
        mParentActivity = parent;
        mTwoPane = twoPane;
    }

    @Override
    public DocumentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.document_list_content, parent, false);
        return new DocumentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final DocumentViewHolder holder, int position) {
        holder.mContentView.setText(mValues.get(position).title);

        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(view -> {
            Document document = (Document) view.getTag();
            if (mTwoPane) {
                Bundle arguments = new Bundle();
                arguments.putParcelable(DocumentDetailFragment.ARG_ITEM, document);
                DocumentDetailFragment fragment = new DocumentDetailFragment();
                fragment.setArguments(arguments);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.document_detail_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, DocumentDetailActivity.class);
                intent.putExtra(DocumentDetailFragment.ARG_ITEM, document);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setDocuments(final List<Document> documents) {
        this.mValues = documents;
        notifyDataSetChanged();
    }

    class DocumentViewHolder extends RecyclerView.ViewHolder {
        final TextView mContentView;

        DocumentViewHolder(View view) {
            super(view);
            mContentView = view.findViewById(R.id.document_title);
        }
    }
}
