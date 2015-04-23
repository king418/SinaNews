package com.king.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Administrator on 2015/4/22.
 */
public class NewsViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> list;
    public NewsViewPagerAdapter(FragmentManager fm,List<Fragment> list) {
        super(fm);
        this.list = list;
    }

    @Override
    public Fragment getItem(int i) {
        Fragment ret = null;
        if (list != null) {
            ret = list.get(i);
        }
        return ret;
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list!=null){
            ret = list.size();
        }
        return ret;
    }
}
