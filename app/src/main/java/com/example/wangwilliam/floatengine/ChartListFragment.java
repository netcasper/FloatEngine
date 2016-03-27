package com.example.wangwilliam.floatengine;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wangwilliam on 3/13/16.
 */
public class ChartListFragment extends Fragment {
    private ChartAdapter adapter = null;
    private List<ChartItem> chartList = null;
    private ListView listView = null;
    private ChartType chartType = ChartType.MINUTE;
    //private View view = null;

    public void setChartList(List<ChartItem> objects) {
        chartList = objects;
    }
    public void setChartType(ChartType charType) {this.chartType = charType; }

    public ChartListFragment(ChartType chartType, List<ChartItem> chartList){
        super();
        this.chartType = chartType;
        this.chartList = chartList;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chartlist, container, false);
        adapter = new ChartAdapter(getActivity(), R.layout.listview_item, chartList);
        listView = (ListView)view.findViewById(R.id.listview_chartlist);
        listView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void Refresh(){
        if (adapter == null || listView == null || chartList == null) return;

        adapter = new ChartAdapter(getActivity(), R.layout.listview_item, chartList);
        listView.setAdapter(adapter);
    }

    public void Clear(){
        if (adapter == null || listView == null || chartList == null) return;

        adapter = new ChartAdapter(getActivity(), R.layout.listview_item, chartList);
        listView.setAdapter(adapter);
    }
}
