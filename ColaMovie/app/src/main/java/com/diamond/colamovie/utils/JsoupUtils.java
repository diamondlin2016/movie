package com.diamond.colamovie.utils;

import com.diamond.colamovie.model.MovieInfoBean;
import com.diamond.colamovie.net.UrlApi;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;


/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/17 下午2:13
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/17      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class JsoupUtils {

    /**
     * 获取电影小卡片信息列表
     */
    public static ArrayList<MovieInfoBean> getMovieCard(Elements movies) {
        ArrayList<MovieInfoBean> list = new ArrayList<>();
        for (Element movie : movies) {//
            Elements lazy = movie.getElementsByTag("img");//不是我要的图文格式
            if (lazy.size() == 0)
                continue;
            MovieInfoBean movieInfoBean = new MovieInfoBean();
            movieInfoBean.pageHref = movie.absUrl("href");
            Element element1 = movie.getElementsByTag("i").first();
            if (element1 != null) {
                movieInfoBean.smallTag = element1.text();
            }
            Element element = lazy.get(0);
            movieInfoBean.img = UrlApi.HOME + element.attr("data-original");
            if (movieInfoBean.img.equals(UrlApi.HOME)) {//推荐电影图片获取不到
                movieInfoBean.img = UrlApi.HOME + element.attr("src");
            }
            movieInfoBean.name = lazy.attr("alt");
            list.add(movieInfoBean);
        }
        return list;
    }
}
