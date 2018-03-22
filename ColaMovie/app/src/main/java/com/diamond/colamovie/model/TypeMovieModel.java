package com.diamond.colamovie.model;

import com.diamond.colamovie.utils.JsoupUtils;

import org.jsoup.nodes.Document;

import java.util.ArrayList;


/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/18 上午11:10
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/18      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class TypeMovieModel {
    public ArrayList<MovieInfoBean> list = new ArrayList<>();

    public static TypeMovieModel analysis(Document document) {
        TypeMovieModel typeMovieModel = new TypeMovieModel();
        typeMovieModel.list.addAll(analysisClass(document,"link-hover"));
        return typeMovieModel;
    }

    private static ArrayList<? extends MovieInfoBean> analysisClass(Document document, String tag) {


        return JsoupUtils.getMovieCard(document.getElementsByClass(tag));
    }
}
