package com.example.wangwilliam.floatengine;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by wangwilliam on 3/13/16.
 */
public class ChartAdapter extends ArrayAdapter<ChartItem> {
    private int resourceId;

    public ChartAdapter(Context context, int resourceId, List<ChartItem> objects){
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChartItem  chartItem = getItem(position);
        View view = null;
        ViewHolder viewHolder = null;

        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView)view.findViewById(R.id.listview_imageview);
            viewHolder.imageView.setImageBitmap(chartItem.bitmap);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        return view;
    }
}
