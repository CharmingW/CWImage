package com.charmingwong.cwimage.wallpaper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.charmingwong.cwimage.R;

import java.util.List;

/**
 * Created by CharmingWong on 2017/6/7.
 */

public class WallPaperTypesAdapter extends BaseAdapter {

    private Context context;
    private List<String> types;
    private int checkItemPosition = 0;

    public void setCheckItem(int position) {
        checkItemPosition = position;
        notifyDataSetChanged();
    }

    public WallPaperTypesAdapter(Context context, List<String> types) {
        this.context = context;
        this.types = types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    @Override
    public int getCount() {
        return types.size();
    }

    @Override
    public String getItem(int position) {
        return types.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView != null) {
            viewHolder = (ViewHolder) convertView.getTag();
        } else {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_category_type, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        fillValue(position, viewHolder);
        return convertView;
    }

    private void fillValue(int position, ViewHolder viewHolder) {
        viewHolder.type.setText(types.get(position));
        if (checkItemPosition != -1) {
            if (checkItemPosition == position) {
                viewHolder.type.setTextColor(context.getResources().getColor(R.color.drop_down_selected));
                viewHolder.type.setBackgroundResource(R.drawable.check_bg);
            } else {
                viewHolder.type.setTextColor(context.getResources().getColor(R.color.drop_down_unselected));
                viewHolder.type.setBackgroundResource(R.drawable.uncheck_bg);
            }
        }
    }

    private static class ViewHolder {

        TextView type;

        ViewHolder(View itemView) {
            super();
            type = (TextView) itemView.findViewById(R.id.type);
        }
    }
}
