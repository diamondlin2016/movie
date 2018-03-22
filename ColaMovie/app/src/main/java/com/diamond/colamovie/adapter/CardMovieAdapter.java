package com.diamond.colamovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.diamond.colamovie.R;
import com.diamond.colamovie.activity.MovieDetailActivity;
import com.diamond.colamovie.model.MovieInfoBean;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/18 下午4:38
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/18      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class CardMovieAdapter extends BaseAbstractAdapter<MovieInfoBean> {

    public CardMovieAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TypeHolder(mLayoutInflater.inflate(R.layout.item_card_movie, parent, false), this);
    }

    static class TypeHolder extends BaseViewHolder<MovieInfoBean> {
        @BindView(R.id.iv_img)
        ImageView mIvImg;
        @BindView(R.id.tv_name)
        TextView mTvName;

        CardMovieAdapter mAdapter;
        private MovieInfoBean mData;

        TypeHolder(View itemView, CardMovieAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
        }

        @Override
        public void bindViewData(MovieInfoBean data) {
            mData = data;
            Glide.with(mAdapter.mContext).load(data.img).placeholder(R.mipmap.ic_default_image).into(mIvImg);
            mTvName.setText(data.name);
            mTvName.setSelected(true);
        }

        @OnClick(R.id.card_view)
        void watchMovie() {
            MovieDetailActivity.startActivity(mData.pageHref, mAdapter.mContext);
        }

    }

}
