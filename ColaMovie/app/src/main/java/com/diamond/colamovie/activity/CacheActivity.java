package com.diamond.colamovie.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;

import com.diamond.colamovie.R;
import com.diamond.colamovie.adapter.CacheListAdapter;
import com.diamond.colamovie.base.BaseActivity;
import com.diamond.colamovie.model.CacheMovieModel;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 上午9:42
 * Description:缓存列表界面
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class CacheActivity extends BaseActivity {

    @BindView(R.id.recycle_view)
    SuperRecyclerView mRecycleView;
    private CacheListAdapter mAdapter;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, CacheActivity.class));
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_cache;
    }

    @Override
    protected void setViewComponent() {
        setStateBarTransparent();
        ArrayList<CacheMovieModel.CacheBean> cacheList = CacheMovieModel.getCacheList();
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CacheListAdapter(this);
        mRecycleView.setAdapter(mAdapter);
        mAdapter.addItems(cacheList);
    }

}
