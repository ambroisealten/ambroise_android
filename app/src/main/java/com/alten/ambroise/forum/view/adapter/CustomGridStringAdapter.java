package com.alten.ambroise.forum.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.view.fragments.ApplicantDiplomaFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantSkillsFragment;

import java.util.ArrayList;

public class CustomGridStringAdapter extends BaseAdapter{
    private Context mContext;
    private final ArrayList<String> web;
    private final ApplicantDiplomaFragment parent;
    private final ApplicantSkillsFragment parentF;

    public CustomGridStringAdapter(Context c, ArrayList<String> web, ApplicantDiplomaFragment parent ) {
        mContext = c;
        this.web = web;
        this.parent = parent;
        this.parentF = null;
    }

    public CustomGridStringAdapter(Context c, ArrayList<String> web, ApplicantSkillsFragment parentF ) {
        mContext = c;
        this.web = web;
        this.parentF = parentF;
        this.parent = null;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return web.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        // TODO Auto-generated method stub
        View grid;
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        final CustomGridStringAdapter that = this;

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.mobility_capsule, null);
            TextView textView = grid.findViewById(R.id.mobility_resume);
            textView.setText(web.get(position));

            ImageButton buttonDelete = grid.findViewById(R.id.imageButton);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(that.parent != null){
                        that.parent.deleteDiploma(web.get(position));
                    }
                    else{
                        that.parentF.deleteSkill(web.get(position));
                    }
                }
            });

        } else {
            grid = convertView;
        }

        return grid;
    }

}