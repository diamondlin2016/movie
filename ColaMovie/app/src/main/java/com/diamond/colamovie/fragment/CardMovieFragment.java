package com.diamond.colamovie.fragment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;

import com.diamond.colamovie.R;
import com.diamond.colamovie.adapter.CardMovieAdapter;
import com.diamond.colamovie.base.BaseViewPagerFragment;
import com.diamond.colamovie.model.TypeMovieModel;
import com.diamond.colamovie.net.IDataListener;
import com.diamond.colamovie.net.JsoupApi;
import com.malinskiy.superrecyclerview.OnMoreListener;
import com.malinskiy.superrecyclerview.SuperRecyclerView;

import butterknife.BindView;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/18 下午3:49
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/18      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class CardMovieFragment extends BaseViewPagerFragment implements IDataListener<TypeMovieModel> {
    @BindView(R.id.recycle_view)
    SuperRecyclerView mRecycleView;

    private static String URL = "url";
    private int mCurrentPage = 1;
    private boolean mIsLoading = false;
    private CardMovieAdapter mAdapter;
    private String mUrl;

    public static CardMovieFragment newInstance(String url) {
        CardMovieFragment fragment = new CardMovieFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected void setUpViewComponent() {
        mRecycleView.setRefreshingColorResources(R.color.holo_blue_light,
                R.color.holo_green_light, R.color.holo_orange_light,
                R.color.holo_red_light);

        mRecycleView.setRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onLazyLoad();
            }
        });
        mRecycleView.setupMoreListener(new OnMoreListener() {
            @Override
            public void onMoreAsked(int numberOfItems, int numberBeforeMore, int currentItemPos) {
                mCurrentPage++;
                getDataFromNet();
            }
        }, 1);
        mRecycleView.setLayoutManager(new GridLayoutManager(getActivity(),3));
    }

    @Override
    protected void onLazyLoad() {
        mCurrentPage = 1;
        getDataFromNet();
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.include_recycle_view;
    }


    @Override
    protected void getFragmentArguments() {
        mUrl = getArguments().getString(URL);
    }

    @Override
    public void onSuccess(TypeMovieModel typeMovieModel) {
        mIsLoading = false;
        if (mAdapter == null) {
            mAdapter = new CardMovieAdapter(getActivity());
            mRecycleView.setAdapter(mAdapter);
        } else if (mCurrentPage == 1) {
            mAdapter.clearItems();
        }
        mAdapter.addItems(typeMovieModel.list);
        mRecycleView.hideMoreProgress();
        mRecycleView.setRefreshing(false);
    }

    @Override
    public void onFail(int errorCode, String errorMsg) {
        mIsLoading = false;
    }


    private void getDataFromNet() {
        if (mIsLoading)
            return;
        JsoupApi.getInstance().getMovieListByType(mUrl, mCurrentPage, this);
        mIsLoading = true;
    }
}
