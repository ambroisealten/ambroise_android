package com.alten.ambroise.forum.view.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.view.fragments.ApplicantMobilityFragment;
import com.alten.ambroise.forum.view.fragments.ApplicantViewFragment;

import java.util.ArrayList;

public class CustomGridMobilityAdapter extends BaseAdapter{
    private Context mContext;
    private final ArrayList<Mobility> web;
    private final ApplicantMobilityFragment parent;
    private final ApplicantViewFragment parentV;
    private final int[] Imageid;

    public CustomGridMobilityAdapter(Context c, ArrayList<Mobility> web, int[] Imageid, ApplicantMobilityFragment parent ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
        this.parent = parent;
        this.parentV = null;
    }
    public CustomGridMobilityAdapter(Context c, ArrayList<Mobility> web, int[] Imageid, ApplicantViewFragment parentV ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
        this.parentV = parentV;
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

        final CustomGridMobilityAdapter that = this;

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.mobility_capsule, null);
            TextView textView = grid.findViewById(R.id.mobility_resume);
            String txt = web.get(position).toString();
            textView.setText(txt);

            ImageButton buttonDelete = grid.findViewById(R.id.imageButton);
            if(this.parentV != null) {
                buttonDelete.setVisibility(View.INVISIBLE);
            } else {
                buttonDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        that.parent.deleteGeographic(web.get(position).getGeographic());
                    }
                });
            }
        } else {
            grid = convertView;
        }

        return grid;
    }

}