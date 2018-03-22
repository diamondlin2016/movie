package com.diamond.colamovie.net;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;

import com.diamond.colamovie.model.AnalyzeUrlModel;
import com.diamond.colamovie.model.ControlModel;
import com.diamond.colamovie.model.HomePageModel;
import com.diamond.colamovie.model.MovieDetailModel;
import com.diamond.colamovie.model.MoviePlayUrlModel;
import com.diamond.colamovie.model.SearchResultModel;
import com.diamond.colamovie.model.TypeMovieModel;

import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/17 上午10:06
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/17      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class JsoupApi {

    private static JsoupApi mJsoupApi;
    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {

        }
    };

    public static JsoupApi getInstance() {

        if (mJsoupApi == null) {
            synchronized (JsoupApi.class) {
                if (mJsoupApi == null) {
                    mJsoupApi = new JsoupApi();
                }
            }
        }
        return mJsoupApi;
    }

    private JsoupApi() {

    }

    private Connection getConnection(String url) {
        Connection connect = Jsoup.connect(url);
        connect.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36");
        return connect;
    }


    /**
     * 今日更新
     */
    public void getHomePage(@NonNull final IDataListener<HomePageModel> listener) {
        new MyThread<HomePageModel>(listener) {
            @Override
            public void run() {
                final HomePageModel pageBean;
                try {
                    Connection connect = getConnection(UrlApi.HOME);
                    pageBean = HomePageModel.analysis(connect.get());
                    onFinish(pageBean);
                } catch (IOException e) {
                    onFinish(null);
                }
            }
        }.start();
    }

    /**
     * 搜索结果
     *
     * @param page 获取搜索结果第几页
     * @param key  搜索关键字
     */
    public void searchKey(int page, String key, @NonNull final IDataListener<SearchResultModel> listener) {

        final String url = String.format(UrlApi.SEARCH, page, key);
        new MyThread<SearchResultModel>(listener) {
            @Override
            public void run() {
                final SearchResultModel bean;
                try {
                    Connection connect = getConnection(url);
                    bean = SearchResultModel.analysis(connect.get());
                    onFinish(bean);
                } catch (IOException e) {
                    onFinish(null);
                }
            }
        }.start();
    }

    /**
     * 获取电影详情
     *
     * @param url 电影 url，MovieInfoBean.pageHref 字段
     */
    public void getMovieDetail(final String url, @NonNull final IDataListener<MovieDetailModel> listener) {
        new MyThread<MovieDetailModel>(listener) {
            @Override
            public void run() {
                final MovieDetailModel bean;
                try {
                    Connection connect = getConnection(url);
                    bean = MovieDetailModel.analysis(connect.get());
                    onFinish(bean);
                } catch (IOException e) {
                    onFinish(null);
                }
            }
        }.start();
    }


    public void getMovieListByType(String url, int page, @NonNull final IDataListener<TypeMovieModel> listener) {
        final String u = String.format(url, page);
        new MyThread<TypeMovieModel>(listener) {
            @Override
            public void run() {
                final TypeMovieModel bean;
                try {
                    Connection connect = getConnection(u);
                    bean = TypeMovieModel.analysis(connect.get());
                    onFinish(bean);
                } catch (IOException e) {
                    onFinish(null);
                }
            }
        }.start();
    }

    /**
     * 获取播放地址
     *
     * @param url 视频页面,来源 Chapter.url
     */
    public void getMoviePlayUrl(final String url, final IDataListener<MoviePlayUrlModel> listener) {
        new MyThread<MoviePlayUrlModel>(listener) {
            @Override
            public void run() {
                try {
                    MoviePlayUrlModel bean;
                    Connection connect = getConnection(url);
                    bean = MoviePlayUrlModel.analysis(connect.get());
                    onFinish(bean);
                } catch (IOException e) {
                    onFinish(null);
                }
            }
        }.start();
    }
    /**
     * 获取播放地址
     *
     * @param url 视频页面,来源 Chapter.url
     */
    public void analyzePlayUrl(final String url, final IDataListener<String> listener) {
        if (url.contains("/share/")) {
            new MyThread<String>(listener) {
                @Override
                public void run() {
                    try {
                        Connection connect = getConnection(url);
                        String token = AnalyzeUrlModel.analysisToken(connect.get());
                        String baseUrl = url.split("/share/")[0];
                        String url1 = baseUrl + "/token/" + token;
                        URL u = new URL(url1);
                        HttpURLConnection urlConnection = (HttpURLConnection) u.openConnection();
                        if(200 == urlConnection.getResponseCode()){
                            //得到输入流
                            InputStream is =urlConnection.getInputStream();
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            byte[] buffer = new byte[1024];
                            int len = 0;
                            while(-1 != (len = is.read(buffer))){
                                baos.write(buffer,0,len);
                                baos.flush();
                            }
                            String json = baos.toString("utf-8");
                            JSONObject jsonObject = new JSONObject(json);
                            String play = (String) jsonObject.get("main");
                            onFinish(baseUrl + play);
                            return;
                        }
                        onFinish(null);

                    } catch (Exception e) {
                        onFinish(null);
                    }
                }
            }.start();
        } else {
            listener.onFail(0, "解析失败");
        }
    }

    /**
     * 控制 app 是否能打开
     *
     */
    public void  checkVersion(final IDataListener<ControlModel> listener) {
        new MyThread<ControlModel>(listener) {
            @Override
            public void run() {
                try {
                    ControlModel bean;
                    Connection connect = getConnection(UrlApi.CONTRAL);
                    bean = ControlModel.analysis(connect.get());
                    onFinish(bean);
                } catch (IOException e) {
                    onFinish(null);
                }
            }
        }.start();
    }

}
