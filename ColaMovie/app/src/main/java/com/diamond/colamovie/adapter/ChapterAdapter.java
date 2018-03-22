package com.diamond.colamovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diamond.colamovie.R;
import com.diamond.colamovie.model.MovieDetailModel;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/20 下午4:21
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/20      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class ChapterAdapter extends BaseAbstractAdapter<MovieDetailModel.Chapter> {

    private OnItemClickListener mListener;

    public ChapterAdapter(Context context) {
        super(context);
    }

    public void setOnClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ChapterHolder(mLayoutInflater.inflate(R.layout.item_chapter, parent, false), this);
    }

    static class ChapterHolder extends BaseViewHolder<MovieDetailModel.Chapter> {
        @BindView(R.id.tv_name)
        TextView mTvName;

        ChapterAdapter mAdapter;
        private MovieDetailModel.Chapter mData;

        ChapterHolder(View itemView, ChapterAdapter adapter) {
            super(itemView);
            mAdapter = adapter;
        }

        @Override
        public void bindViewData(MovieDetailModel.Chapter data) {
            mData = data;
            mTvName.setText(data.name);
        }

        @OnClick(R.id.tv_name)
        void watchMovie() {
            if (mAdapter.mListener != null) {
                mAdapter.mListener.onItemClick(getLayoutPosition(), mData.url);
            }
        }

    }

    public interface OnItemClickListener {
        void onItemClick(int position, String url);
    }

}
