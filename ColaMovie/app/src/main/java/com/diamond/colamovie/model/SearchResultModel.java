package com.diamond.colamovie.model;

import com.diamond.colamovie.utils.JsoupUtils;

import org.jsoup.nodes.Document;

import java.util.ArrayList;


/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/17 下午2:01
 * Description: 搜索结果 bean
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/17      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class SearchResultModel {

    public ArrayList<MovieInfoBean> list = new ArrayList<>();


    public static SearchResultModel analysis(Document document) {
        SearchResultModel bean = new SearchResultModel();
        bean.list.addAll(analysisClass(document, "link-hover"));

        return bean;
    }

    private static ArrayList<? extends MovieInfoBean> analysisClass(Document document, String tag) {


        return JsoupUtils.getMovieCard(document.getElementsByClass(tag));
    }

}
