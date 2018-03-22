package com.diamond.colamovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diamond.colamovie.R;
import com.diamond.colamovie.activity.FullScreenActivity;
import com.diamond.colamovie.model.CacheMovieModel;
import com.diamond.colamovie.net.down.IDownLoadListener;
import com.diamond.colamovie.net.down.M3U8DownManager;
import com.diamond.colamovie.utils.ToastHelper;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 上午9:47
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class CacheListAdapter extends BaseAbstractAdapter<CacheMovieModel.CacheBean> {

    public CacheListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CacheListHolder(mLayoutInflater.inflate(R.layout.item_cache_list, parent, false), this);
    }

    static class CacheListHolder extends BaseViewHolder<CacheMovieModel.CacheBean> implements IDownLoadListener {
        @BindView(R.id.iv_img)
        ImageView mIvImg;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_progress)
        TextView mTvProgress;

        CacheListAdapter mAdapter;
        private CacheMovieModel.CacheBean mData;
        private boolean mIsDownIng;

        CacheListHolder(View itemView, CacheListAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
        }

        @Override
        public void bindViewData(CacheMovieModel.CacheBean data) {
            mData = data;
            mTvName.setText(data.name);
            Glide.with(mAdapter.mContext).load(data.img).placeholder(R.mipmap.ic_default_image).into(mIvImg);
            if (data.isSuccess) {
                mTvProgress.setText("缓存完成，点击播放");
            } else {
                mTvProgress.setText("未完成，点击开始缓存");
            }
        }

        @OnClick(R.id.rl_root)
        void playOrCache() {
            if (mIsDownIng){
                M3U8DownManager.stop(mData.playUrl);
            }else if (mData.isSuccess) {
                FullScreenActivity.startActivity(mAdapter.mContext, mData.playUrl,mData.name);
            } else {
                mIsDownIng = true;
                M3U8DownManager.down(mData.playUrl, this);
                mTvProgress.setText("正在解析数据");
            }
        }

        @OnClick(R.id.tv_del)
        void del(){
            CacheMovieModel.deleteByUrl(mData.playUrl);
            M3U8DownManager.clearCacheByUrl(mData.playUrl);
            ToastHelper.showSnackBarMessage("删除成功", mIvImg);
            mAdapter.removeItem(getLayoutPosition());
        }

        @Override
        public void onSuccess() {
            Log.e("_____","success");
            mData.isSuccess = true;
            mIsDownIng = false;
            CacheMovieModel.putACacheBean(mData);
            mTvProgress.setText("缓存完成，点击播放");
        }

        @Override
        public void onFail(String msg) {
            mTvProgress.setText("缓存失败，点击重试");
        }

        @Override
        public void onProgress(int progress) {
            if (progress == 100){
                mTvProgress.setText("正在合并数据...");
            }else {
                mTvProgress.setText("正在缓存：" + progress+"%");
            }
        }

        @Override
        public void onStartDown() {

        }
    }
}
