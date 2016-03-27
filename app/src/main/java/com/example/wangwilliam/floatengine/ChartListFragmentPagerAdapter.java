package com.example.wangwilliam.floatengine;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by wangwilliam on 3/13/16.
 */
public class ChartListFragmentPagerAdapter extends FragmentPagerAdapter {
    private List<ChartListFragment> fragmentList;
    private List<String> titleList;

    public ChartListFragmentPagerAdapter(FragmentManager fm, List<ChartListFragment> fragmentList, List<String> titleList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.titleList = titleList;
    }
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        //return POSITION_NONE;
        return super.getItemPosition(object);
    }
}
