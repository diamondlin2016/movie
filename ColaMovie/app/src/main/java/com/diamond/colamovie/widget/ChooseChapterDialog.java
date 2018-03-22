package com.diamond.colamovie.widget;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.diamond.colamovie.R;
import com.diamond.colamovie.adapter.ChapterAdapter;
import com.diamond.colamovie.model.MovieDetailModel;

import java.util.ArrayList;

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

public class ChooseChapterDialog {

    private final ChapterAdapter.OnItemClickListener mListener;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.recycle_view)
    RecyclerView mRecyclerView;

    private final Context mContext;
    private final String mName;
    private final ArrayList<MovieDetailModel.Chapter> mList;
    private final BottomSheetDialog mBottomSheetDialog;

    public ChooseChapterDialog(Activity context, String name, ArrayList<MovieDetailModel.Chapter> list, ChapterAdapter.OnItemClickListener chooseChapterListener) {
        mContext = context;
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_choose_chapter, null, false);
        ButterKnife.bind(this, rootView);
        mBottomSheetDialog = new BottomSheetDialog(context);
        mBottomSheetDialog.setContentView(rootView);
        mBottomSheetDialog.setCanceledOnTouchOutside(true);
        this.mList = list;
        this.mName = name;
        this.mListener = chooseChapterListener;
        setUpView();
    }


    public static ChooseChapterDialog builder(Activity context, String name, ArrayList<MovieDetailModel.Chapter> list, ChapterAdapter.OnItemClickListener chooseChapterListener) {
        return new ChooseChapterDialog(context, name,list,chooseChapterListener);
    }


    public void show() {
        if (mBottomSheetDialog != null && !mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.show();
        }
    }

    private void setUpView() {
        mTvName.setText(mName);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext,4));
        ChapterAdapter adapter = new ChapterAdapter(mContext);
        adapter.setOnClickListener(new ChapterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, String url) {
                mBottomSheetDialog.dismiss();
                mListener.onItemClick(position,url);
            }
        });
        mRecyclerView.setAdapter(adapter);
        adapter.addItems(mList);
    }


}
