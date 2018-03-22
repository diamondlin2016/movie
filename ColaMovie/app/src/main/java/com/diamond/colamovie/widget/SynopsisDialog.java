package com.diamond.colamovie.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.diamond.colamovie.R;
import com.diamond.colamovie.adapter.SynopsisListAdapter;
import com.diamond.colamovie.model.MovieDetailModel;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/20 下午4:59
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/20      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class SynopsisDialog {

    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_synopsis)
    TextView mTvSynopsis;

    private final Context mContext;
    private final BottomSheetDialog mBottomSheetDialog;
    private final MovieDetailModel mDetailBean;

    public static SynopsisDialog builder(Activity context, MovieDetailModel detailBean) {
        return new SynopsisDialog(context, detailBean);
    }

    private SynopsisDialog(@NonNull Activity context, MovieDetailModel detailBean) {
        mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_synopsis, null, false);
        ButterKnife.bind(this, rootView);
        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(rootView);
//        mBottomSheetDialog.setCancelable(false);
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
        this.mDetailBean = detailBean;
        setUpView();
    }

    public void show() {
        if (mBottomSheetDialog != null && !mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.show();
        }
    }

    private void setUpView() {
        mTvName.setText(mDetailBean.name);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        SynopsisListAdapter adapter = new SynopsisListAdapter(mContext);
        mRecyclerView.setAdapter(adapter);
        adapter.addItems(mDetailBean.titleInfo);
        mTvSynopsis.setText(Html.fromHtml("<b><tt>" + mContext.getString(R.string.text_content_synopsis) + "</tt></b>" + mDetailBean.synopsis));

    }


}
