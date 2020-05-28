package com.kmw.soom2.InsertActivity.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class WorkThroughViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> arrayList;
    public WorkThroughViewPagerAdapter(FragmentManager fm,ArrayList<Fragment> arrayList) {
        super(fm);
        this.arrayList = arrayList;
    }

    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

}
