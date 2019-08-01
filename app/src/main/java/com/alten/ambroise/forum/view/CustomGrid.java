package com.alten.ambroise.forum.view;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alten.ambroise.forum.R;
import com.alten.ambroise.forum.data.model.Mobility;
import com.alten.ambroise.forum.view.fragments.ApplicantMobilityFragment;

import java.util.ArrayList;

public class CustomGrid extends BaseAdapter{
    private Context mContext;
    private final ArrayList<Mobility> web;
    private final ApplicantMobilityFragment parent;
    private final int[] Imageid;

    public CustomGrid(Context c, ArrayList<Mobility> web, int[] Imageid, ApplicantMobilityFragment parent ) {
        mContext = c;
        this.Imageid = Imageid;
        this.web = web;
        this.parent = parent;
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

        final CustomGrid that = this;

        if (convertView == null) {

            grid = new View(mContext);
            grid = inflater.inflate(R.layout.mobility_capsule, null);
            TextView textView = (TextView) grid.findViewById(R.id.mobility_resume);
            textView.setText(web.get(position).toString());

            ImageButton buttonDelete = (ImageButton) grid.findViewById(R.id.imageButton);
            buttonDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    that.parent.deleteGeographic(web.get(position).getGeographic());
                }
            });

        } else {
            grid = (View) convertView;
        }

        return grid;
    }

}