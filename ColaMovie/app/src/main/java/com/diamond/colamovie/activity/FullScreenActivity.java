package com.diamond.colamovie.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;

import com.diamond.colamovie.R;
import com.diamond.colamovie.base.BaseActivity;
import com.diamond.colamovie.net.down.utils.MD5Utils;

import butterknife.BindView;
import chuangyuan.ycj.videolibrary.factory.JDefaultDataSourceFactory;
import chuangyuan.ycj.videolibrary.listener.DataSourceListener;
import chuangyuan.ycj.videolibrary.video.GestureVideoPlayer;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;

import static com.diamond.colamovie.net.down.M3U8DownManager.BASE_CACHE_URL;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 下午8:40
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class FullScreenActivity extends BaseActivity {

    @BindView(R.id.video_view)
    VideoPlayerView mVideoView;
    public static final String TAG = "FullScreenActivity";
    private GestureVideoPlayer mExoPlayerManager;
    private String mUri;
    private String mName;

    public static void startActivity(Context context, String uri, String name) {
        Intent intent = new Intent(context, FullScreenActivity.class);
        String url = BASE_CACHE_URL + MD5Utils.encode(uri) + "/local.m3u8";
        intent.putExtra(TAG, url);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }


    @Override
    protected int getLayoutResource() {
        return R.layout.activity_full_screen;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void setViewComponent() {
        getWindow().setStatusBarColor(Color.BLACK);
        mExoPlayerManager = new GestureVideoPlayer(this, mVideoView, new DataSource(this));
        mExoPlayerManager.setTitle(mName);
        mExoPlayerManager.setPlayUri(mUri);
    }

    @Override
    protected void getArguments(Intent intent) {
        super.getArguments(intent);
        mUri = getIntent().getStringExtra(TAG);
        mName = getIntent().getStringExtra("name");
    }





    public class DataSource implements DataSourceListener {
        public static final String TAG = "OfficeDataSource";

        private Context context;

        public DataSource(Context context) {
            this.context = context;
        }

        @Override
        public com.google.android.exoplayer2.upstream.DataSource.Factory getDataSourceFactory() {
            return new JDefaultDataSourceFactory(context);
        }
    }

    @Override
    public void onBackPressed() {
        if (mExoPlayerManager.onBackPressed()) {
            mExoPlayerManager.onDestroy();
            ActivityCompat.finishAfterTransition(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mExoPlayerManager.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mExoPlayerManager.onPause();
    }

}
