package com.wgfxer.learningprogram.presentation.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wgfxer.learningprogram.R;

import java.util.ArrayList;
import java.util.List;

public class LectorSpinnerAdapter extends BaseAdapter {
    private List<String> lectors;

    public LectorSpinnerAdapter() {
        this.lectors = new ArrayList<>();
    }

    public void setLectors(List<String> lectors) {
        this.lectors = lectors;
        notifyDataSetChanged();
    }

    public List<String> getLectors() {
        return lectors;
    }

    @Override
    public int getCount() {
        return lectors.size();
    }

    @Override
    public String getItem(int i) {
        return lectors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lector, viewGroup, false);
        TextView lectorNameTitle = itemView.findViewById(R.id.text);
        lectorNameTitle.setText(getItem(i));
        return itemView;
    }

    @Override
    public View getDropDownView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            ViewHolder viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        holder.lectorName.setText(getItem(position));
        return convertView;
    }

    private class ViewHolder {
        private final TextView lectorName;

        private ViewHolder(View view) {
            lectorName = view.findViewById(android.R.id.text1);
        }
    }
}
