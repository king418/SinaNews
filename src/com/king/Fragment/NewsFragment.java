package com.king.Fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.king.SinaNews.R;
import com.king.adapter.NewsViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/4/21.
 */
public class NewsFragment extends Fragment {

    private OnViewPagerChangeListener viewPagerChangeListener;
    public ViewPager viewPager;
    private List<Fragment> list;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnViewPagerChangeListener) {
            viewPagerChangeListener = (OnViewPagerChangeListener) activity;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_news, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.content);
        viewPager.setAdapter(new NewsViewPagerAdapter(getChildFragmentManager(), list));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPagerChangeListener.onViewPagerChangeListener(position);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        return view;
    }

    private void initData() {
        list = new ArrayList<Fragment>();
        Bundle bundle = getArguments();
        int size = bundle.getInt("size");
        int position = bundle.getInt("position");

        for (int i = 0; i < size; i++) {
            NewsListFragment fragment = new NewsListFragment();
            Bundle bundles = new Bundle();
            bundles.putInt("type", position);
           // Log.i("NewsLOGFragment", "-------->news_type_id:　" + position);
            bundles.putString("cate_id", (i + 1) + "");
            fragment.setArguments(bundles);
            list.add(fragment);
        }
    }

    public interface OnViewPagerChangeListener {
        public void onViewPagerChangeListener(int currentPage);
    }
}
