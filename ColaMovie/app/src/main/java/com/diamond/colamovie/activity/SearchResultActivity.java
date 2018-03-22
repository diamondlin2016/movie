package com.diamond.colamovie.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.diamond.colamovie.R;
import com.diamond.colamovie.adapter.CardMovieAdapter;
import com.diamond.colamovie.base.BaseActivity;
import com.diamond.colamovie.model.SearchResultModel;
import com.diamond.colamovie.net.IDataListener;
import com.diamond.colamovie.net.JsoupApi;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import butterknife.BindView;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/19 下午10:25
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/19      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class SearchResultActivity extends BaseActivity implements IDataListener<SearchResultModel> {
    @BindView(R.id.recycle_view)
    SuperRecyclerView mRecycleView;

    public static final String QUERY = "query";
    private String mQuery;
    private int mCurrentPage = 1;
    private boolean mIsLoading = false;
    private CardMovieAdapter mAdapter;


    public static void startActivity(Context context, String query) {
        Intent intent = new Intent(context, SearchResultActivity.class);
        intent.putExtra(QUERY, query);
        context.startActivity(intent);
    }

    @Override
    protected void setViewComponent() {
        setStateBarTransparent();
        mRecycleView.setRefreshingColorResources(R.color.holo_blue_light,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);

        mRecycleView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCurrentPage = 1;
                getDataFromNet();
            }
        });
        mRecycleView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                mCurrentPage++;
                getDataFromNet();
            }
        }, 1);
        mRecycleView.setLayoutManager(new GridLayoutManager(this, 3));
        getDataFromNet();
    }

    @Override
    protected void getArguments(Intent intent) {
        mQuery = intent.getStringExtra(QUERY);
    }


    @Override
    public void onSuccess(SearchResultModel searchResultModel) {
        mIsLoading = false;
        if (mAdapter == null) {
            mAdapter = new CardMovieAdapter(this);
            mRecycleView.setAdapter(mAdapter);
        } else if (mCurrentPage == 1) {
            mAdapter.clearItems();
        }
        mAdapter.addItems(searchResultModel.list);
        mRecycleView.hideMoreProgress();
        mRecycleView.setRefreshing(false);
    }

    @Override
    public void onFail(int errorCode, String errorMsg) {

    }

    private void getDataFromNet() {
        if (mIsLoading)
            return;
        JsoupApi.getInstance().searchKey(mCurrentPage, mQuery, this);
        mIsLoading = true;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_search_result;
    }
}
