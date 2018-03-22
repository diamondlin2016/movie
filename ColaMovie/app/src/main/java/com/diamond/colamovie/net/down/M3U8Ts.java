package com.diamond.colamovie.net.down;

/**
 * Author:    Diamond_Lin
 * Version    V1.0
 * Date:      2018/1/24 下午5:21
 * Description:
 * Modification  History:
 * Date         	Author        		Version        	Description
 * -----------------------------------------------------------------------------------
 * 2018/1/24      Diamond_Lin            1.0                    1.0
 * Why & What is modified:
 */

public class M3U8Ts {
    public String file;
    public float seconds;

    public M3U8Ts(String line, float seconds) {
        file = line;
        this.seconds = seconds;
    }

    public String getFileName() {
        String[] split = file.split("/");
        return split[split.length-1];
    }
}
