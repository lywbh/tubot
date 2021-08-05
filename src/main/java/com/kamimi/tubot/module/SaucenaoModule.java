package com.kamimi.tubot.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kamimi.tubot.config.KeyConfig;
import com.kamimi.tubot.utils.HttpUtils;
import com.kamimi.tubot.utils.LogUtils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SaucenaoModule {

    private static final String BASE_URL = "https://saucenao.com/search.php";

    public static Map<String, String> bestMatch(String imgUrl) {
        Map<String, String> result = new HashMap<>();
        if (imgUrl == null || imgUrl.isEmpty()) {
            return result;
        }
        String url = BASE_URL + "?api_key=" + KeyConfig.SAUCENAO_API_KEY + "&db=999&output_type=2&testmode=1&url=" + URLEncoder.encode(imgUrl, StandardCharsets.UTF_8);
        LogUtils.getLogger().info("啊啊啊:" + url);
        String response = HttpUtils.get(url);
        JSONArray respJson = JSON.parseObject(response).getJSONArray("results");
        if (respJson.isEmpty()) {
            return result;
        }
        JSONObject dataJson = respJson.getJSONObject(0).getJSONObject("data");
        dataJson.forEach((k, v) -> {
            if (v != null) {
                String value;
                if (v instanceof JSON) {
                    value = ((JSON) v).toJSONString();
                } else {
                    value = v.toString();
                }
                result.put(k, value);
            }
        });
        return result;
    }

}
