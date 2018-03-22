package com.diamond.colamovie.fragment;

import android.support.design.widget.TabLayout;

import com.diamond.colamovie.R;
import com.diamond.colamovie.base.BaseViewPagerAdapter;
import com.diamond.colamovie.base.BaseViewPagerFragment;
import com.diamond.colamovie.net.UrlApi;
import com.diamond.colamovie.widget.CustomViewPager;

import java.util.ArrayList;

import butterknife.BindArray;
import butterknife.BindView;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/18 下午3:35
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/18      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class ColumnFragment extends BaseViewPagerFragment {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.view_pager)
    CustomViewPager mViewPager;

    @BindArray(R.array.title_home_type)
    String[] mTitles;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_column;
    }

    @Override
    protected void setUpViewComponent() {
        ArrayList<BaseViewPagerFragment> fragments = createFragments();
        mViewPager.setOffscreenPageLimit(fragments.size() - 1); //ViewPager的缓存页面数量
        BaseViewPagerAdapter adapter = new BaseViewPagerAdapter(getChildFragmentManager(), mTitles, fragments);
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private ArrayList<BaseViewPagerFragment> createFragments() {
        ArrayList<BaseViewPagerFragment> list = new ArrayList<>();
        list.add(CardMovieFragment.newInstance(UrlApi.FILM));
        list.add(CardMovieFragment.newInstance(UrlApi.OPERA));
        list.add(CardMovieFragment.newInstance(UrlApi.VARIETY));
        list.add(CardMovieFragment.newInstance(UrlApi.COMIC));
        return list;
    }

    @Override
    protected void onLazyLoad() {

    }


}
