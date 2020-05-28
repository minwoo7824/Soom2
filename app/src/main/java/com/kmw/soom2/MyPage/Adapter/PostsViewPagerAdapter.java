package com.kmw.soom2.MyPage.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PostsViewPagerAdapter extends FragmentPagerAdapter {
    ArrayList<Fragment> arrayList = new ArrayList<>();
    int pagerCount;
    public PostsViewPagerAdapter(FragmentManager fm, int pagerCount) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    public void addFragment(Fragment fragment){
        arrayList.add(fragment);
    }
}
