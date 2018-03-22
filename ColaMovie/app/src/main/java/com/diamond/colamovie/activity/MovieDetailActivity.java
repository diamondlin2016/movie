package com.diamond.colamovie.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.diamond.colamovie.R;
import com.diamond.colamovie.adapter.CardMovieAdapter;
import com.diamond.colamovie.adapter.ChapterAdapter;
import com.diamond.colamovie.base.BaseActivity;
import com.diamond.colamovie.model.CacheMovieModel;
import com.diamond.colamovie.model.MovieDetailModel;
import com.diamond.colamovie.model.MoviePlayUrlModel;
import com.diamond.colamovie.net.IDataListener;
import com.diamond.colamovie.net.JsoupApi;
import com.diamond.colamovie.net.down.IDownLoadListener;
import com.diamond.colamovie.net.down.M3U8DownManager;
import com.diamond.colamovie.utils.ToastHelper;
import com.diamond.colamovie.widget.ChooseChapterDialog;
import com.diamond.colamovie.widget.SynopsisDialog;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.video.ManualPlayer;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/19 下午5:51
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/19      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class MovieDetailActivity extends BaseActivity implements IDataListener<MovieDetailModel> {
    @BindView(R.id.video_view)
    VideoPlayerView mVideoView;
    ManualPlayer mManualPlayer;
    @BindView(R.id.rv_chapter)
    RecyclerView mRvChapter;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_no_data)
    TextView mTvNoData;
    @BindView(R.id.tv_synopsis)
    TextView mTvSynopsis;
    @BindView(R.id.rv_recommend)
    RecyclerView mRvRecommend;
//    @BindView(R.id.bt_down2)
//    FreshDownloadView mBtDown2;

    private String mUrl;
    public static String URL = "url";
    private MovieDetailModel mDetail;
    private ChapterAdapter mAdapter;
    private MoviePlayUrlModel mMoviePlayUrlModel;
    private ChapterAdapter.OnItemClickListener mChooseChapterListener;
    private String mPlayUrl;

    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    private int mPosition;//当前播放集数

    public static void startActivity(String url, Context ctx) {
        Intent intent = new Intent(ctx, MovieDetailActivity.class);
        intent.putExtra(URL, url);
        ctx.startActivity(intent);
    }

    @Override
    protected void getArguments(Intent intent) {
        mUrl = intent.getStringExtra(URL);
    }

    protected void setViewComponent() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.BLACK);
        }
        mManualPlayer = new ManualPlayer(this, mVideoView);
        mManualPlayer.setVideoInfoListener(new VideoInfoListener() {
            @Override
            public void onPlayStart() {
            }

            @Override
            public void onLoadingChanged() {
            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {
            }

            @Override
            public void onPlayEnd() {
            }

            @Override
            public void isPlaying(boolean playWhenReady) {
            }

        });
        JsoupApi.getInstance().getMovieDetail(mUrl, this);
        showProgressDialog();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_movie_detail;
    }

    @Override
    public void onSuccess(final MovieDetailModel movieDetailModel) {
        mDetail = movieDetailModel;
        mTvName.setText(mDetail.name);
        mTvSynopsis.setText(mDetail.synopsis);
        CardMovieAdapter cardMovieAdapter = new CardMovieAdapter(this);
        mRvRecommend.setLayoutManager(new GridLayoutManager(this, 3));
        mRvRecommend.setAdapter(cardMovieAdapter);
        cardMovieAdapter.addItems(mDetail.recommend);
        getPlayUrl(mDetail.playerList.get(0).get(0).url);
    }

    private void getPlayUrl(String url) {
        JsoupApi.getInstance().getMoviePlayUrl(url, new IDataListener<MoviePlayUrlModel>() {
            @Override
            public void onSuccess(MoviePlayUrlModel moviePlayUrlModel) {
                dismissProgressDialog();
                mMoviePlayUrlModel = moviePlayUrlModel;
                mRvChapter.setLayoutManager(new LinearLayoutManager(MovieDetailActivity.this, LinearLayoutManager.HORIZONTAL, false));
                mAdapter = new ChapterAdapter(MovieDetailActivity.this);
                mChooseChapterListener = new ChapterAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, String url) {
                        playMovie(url, position);
                    }
                };
                mAdapter.setOnClickListener(mChooseChapterListener);
                mRvChapter.setAdapter(mAdapter);
                if (moviePlayUrlModel.hasData) {
                    mAdapter.addItems(moviePlayUrlModel.playerList);
                    playMovie(moviePlayUrlModel.playerList.get(0).url, 1);
                } else {
                    mTvNoData.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFail(int errorCode, String errorMsg) {
                ToastHelper.showSnackBarMessage("解析失败", mVideoView);
                dismissProgressDialog();
            }
        });

    }

    private void playMovie(final String url, final int position) {
        mPosition = position;
        if (url.contains("m3u8")) {
            mPlayUrl = url;
            mManualPlayer.setTitle(mDetail.name + "  " + "第" + position + "集");
            mManualPlayer.setPlayUri(url);
            mManualPlayer.startPlayer();
        } else {
            //解析地址
            showProgressDialog();
            JsoupApi.getInstance().analyzePlayUrl(url, new IDataListener<String>() {
                @Override
                public void onSuccess(String s) {
                    dismissProgressDialog();
                    if (TextUtils.isEmpty(s)) {
                        return;
                    }
                    mPlayUrl = s;
                    mManualPlayer.setTitle(mDetail.name + "  " + "第" + position + "集");
                    mManualPlayer.setPlayUri(s);
                    mManualPlayer.startPlayer();
                }

                @Override
                public void onFail(int errorCode, String errorMsg) {
                    dismissProgressDialog();
                    ToastHelper.showSnackBarMessage(errorMsg, mVideoView);
                }
            });
        }
    }

    @Override
    public void onFail(int errorCode, String errorMsg) {
    }

    @OnClick(R.id.ll_synopsis)
    void showSynopsis() {
        SynopsisDialog.builder(this, mDetail).show();
    }

    @OnClick(R.id.fl_choose_chapter)
    void showChooseChapter() {
        ChooseChapterDialog.builder(this, mDetail.name, mMoviePlayUrlModel.playerList, mChooseChapterListener).show();
    }


    @OnClick(R.id.bt_down)
    void down() {
        final String url = mPlayUrl;
        if (!TextUtils.isEmpty(mPlayUrl)) {
            requestAppPermissions(PERMISSIONS, new MultiplePermissionsListener() {
                @Override
                public void onPermissionsChecked(MultiplePermissionsReport report) {
                    if (report.areAllPermissionsGranted()) {
                        M3U8DownManager.down(url, new IDownLoadListener() {
                            @Override
                            public void onSuccess() {
                                CacheMovieModel.CacheBean bean = CacheMovieModel.findByUrl(url);
                                if (bean != null) {
                                    bean.isSuccess = true;
                                    CacheMovieModel.putACacheBean(bean);
                                }
                            }

                            @Override
                            public void onFail(String msg) {

                            }

                            @Override
                            public void onProgress(int progress) {
//                                mBtDown2.upDateProgress(progress);
                            }

                            @Override
                            public void onStartDown() {
                                CacheMovieModel.CacheBean bean = new CacheMovieModel.CacheBean();
                                bean.playUrl = url;
                                bean.img = mDetail.img;
                                bean.name = mDetail.name + mAdapter.getDataList().get(mPosition).name;
                                CacheMovieModel.putACacheBean(bean);
                            }
                        });

                    } else {
                        ToastHelper.showSnackBarMessage("权限不够，操作失败", mVideoView);
                    }
                }

                @Override
                public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                }

            });
            ToastHelper.showSnackBarMessage("已添加到缓存列表，请到\"我的缓存\"查看", mVideoView);

        }

    }


    @Override
    public void onBackPressed() {
        if (mManualPlayer.onBackPressed()) {
            mManualPlayer.onDestroy();
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mManualPlayer.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mManualPlayer.onPause();
    }


}
