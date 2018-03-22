package com.diamond.colamovie.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/18 下午3:10
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/18      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class BaseViewPagerAdapter extends FragmentPagerAdapter {
    private final List<BaseViewPagerFragment> mFragments;
    private String[] mTitles;

    public BaseViewPagerAdapter(FragmentManager fm, String[] titles, List<BaseViewPagerFragment> fragments) {
        super(fm);
        mTitles = titles;
        mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles == null ? "" : mTitles[position];
    }
}
