package com.diamond.colamovie.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.diamond.colamovie.R;
import com.diamond.colamovie.model.MovieDetailModel;

import butterknife.BindView;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/20 下午5:31
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/20      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class SynopsisListAdapter extends BaseAbstractAdapter<MovieDetailModel.TitleKV> {

    public SynopsisListAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SynopsisHolder(mLayoutInflater.inflate(R.layout.item_synopsis_list, parent, false));
    }

    static class SynopsisHolder extends BaseViewHolder<MovieDetailModel.TitleKV> {
        @BindView(R.id.tv_key)
        TextView mTvKey;
        @BindView(R.id.tv_value)
        TextView mTvValue;


        SynopsisHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void bindViewData(MovieDetailModel.TitleKV data) {
            mTvKey.setText(data.key);
            mTvValue.setText(data.value);
        }

    }
}
