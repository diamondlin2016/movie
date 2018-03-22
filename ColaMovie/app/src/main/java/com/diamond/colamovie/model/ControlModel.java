package com.diamond.colamovie.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/31 下午5:45
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/31      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class ControlModel {

    public boolean canOpen = true;
    public int versionCode = 1;
    public String upData = "";

    public static ControlModel analysis(Document document) {
        return analysisClass(document);
    }

    private static ControlModel analysisClass(Document document) {
        ControlModel model = new ControlModel();
        Element lc1 = document.getElementById("LC1");
        if (lc1 != null) {
            String json = lc1.text();
            try {
                JSONObject jsonObject = new JSONObject(json);
                model.canOpen = (boolean) jsonObject.get("canOpen");
                model.versionCode = (int) jsonObject.get("versionCode");
                model.upData = (String) jsonObject.get("upData");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return model;
    }
}
