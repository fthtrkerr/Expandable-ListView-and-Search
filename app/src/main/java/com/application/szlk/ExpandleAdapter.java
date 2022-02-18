package com.application.szlk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.application.szlk.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by deepshikha Puri on 16/1/18.
 */

public class ExpandleAdapter extends BaseExpandableListAdapter {
    List<HashMap<String, Object>> lv_data;
    Context context;

    public ExpandleAdapter(List<HashMap<String, Object>> lv_data, Context context) {
        this.lv_data = lv_data;
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return lv_data.size();
    }

    @Override
    public int getChildrenCount(int i) {

        List<HashMap<String, Object>> lv_state = (List<HashMap<String, Object>>) lv_data.get(i).get("State");
        return lv_state.size();
    }

    @Override
    public Object getGroup(int i) {
        return lv_data.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {


        List<HashMap<String, Object>> lv_state = (List<HashMap<String, Object>>) lv_data.get(i).get("State");
        return lv_state.get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView tv_header = (TextView) convertView.findViewById(R.id.lblListHeader);
        tv_header.setText(lv_data.get(i).get("Name").toString());
        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }

        TextView tv_chid = (TextView) convertView.findViewById(R.id.lblListItem);

        List<HashMap<String, Object>> lv_state = (List<HashMap<String, Object>>) lv_data.get(i).get("State");
        tv_chid.setText(lv_state.get(i1).get("Name").toString());

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
