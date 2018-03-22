package com.diamond.colamovie.model;

import android.text.TextUtils;

import com.diamond.colamovie.net.UrlApi;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/17 上午10:43
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/17      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class HomePageModel {
    public ArrayList<Bean> mList = new ArrayList<>();

    //分类 bean  今日更新
    public static class Bean {
        public String typeTag;//
        public ArrayList<MovieInfoBean> movies;
    }


    public static HomePageModel analysis(Document document) {
        HomePageModel homePageBean = new HomePageModel();
//        homePageBean.mList.add(analysisClass(document, "index-tj clearfix"));
        homePageBean.mList.addAll(analysisClass(document, "index-tj-l"));


        homePageBean.mList.addAll(analysisClass(document, "index-area clearfix"));


        return homePageBean;
    }


    private static ArrayList<Bean> analysisClass(Document document, String clazz) {
        ArrayList<Bean> list = new ArrayList<>();

        Elements elementsByClass = document.getElementsByClass(clazz);

        ArrayList<MovieInfoBean> movies = new ArrayList<>();
        for (Element el : elementsByClass) {
            //一组电影
            Bean bean = new Bean();
            bean.typeTag = el.getElementsByTag("h1").get(0).ownText();

            if (TextUtils.isEmpty(bean.typeTag)) {
                Elements a = el.getElementsByTag("h1").get(0).getElementsByTag("a");
                bean.typeTag = a.get(a.size() - 1).text();
            }

            Elements a = el.getElementsByTag("a");
            for (Element movie : a) {
                //一个电影推荐
                Elements lazy = movie.getElementsByTag("img");//不是我要的图文格式
                if (lazy.size() == 0)
                    continue;

                MovieInfoBean movieInfoBean = new MovieInfoBean();
                movieInfoBean.pageHref = movie.absUrl("href");
                movieInfoBean.smallTag = movie.getElementsByTag("i").get(0).text();
                Element element = lazy.get(0);
                movieInfoBean.img = UrlApi.HOME + element.attr("src");
                movieInfoBean.name = lazy.attr("alt");


                movies.add(movieInfoBean);
            }
            bean.movies = movies;
            list.add(bean);
        }

        return list;
    }


}
