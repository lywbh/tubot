package com.kamimi.tubot.module;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kamimi.tubot.utils.HttpUtils;
import com.kamimi.tubot.utils.RandomUtils;

/**
 * yandere图库
 */
public class YandereModule {

    public enum Rating {
        UNKNOWN, SAFE, QUESTIONABLE, EXPLICIT
    }

    public Rating rating(String rating) {
        rating = rating.trim();
        switch (rating) {
            case "s":
            case "safe":
                return Rating.SAFE;
            case "q":
            case "questionable":
                return Rating.QUESTIONABLE;
            case "e":
            case "explicit":
                return Rating.EXPLICIT;
            default:
                return Rating.UNKNOWN;
        }
    }

    public String randomPic() {
        return randomPic(Rating.SAFE, null);
    }

    public String randomPic(Rating rating) {
        return randomPic(rating, null);
    }

    public String randomPic(Rating rating, String tags) {
        return randomPic(rating, tags, 100);
    }

    private String randomPic(Rating rating, String tags, int bound) {
        if (bound == 0) {
            return null;
        }
        if (rating == Rating.UNKNOWN) {
            rating = Rating.SAFE;
        }
        if (tags == null) {
            tags = "";
        }
        int page = RandomUtils.getRandomInt(bound) + 1;

        String url = String.format("https://yande.re/post.json?limit=100&page=%d&tags=%s%%20rating:%s", page, tags, rating.name().toLowerCase());
        String response = HttpUtils.get(url);
        JSONArray respArray = JSON.parseArray(response);
        if (respArray.isEmpty()) {
            return randomPic(rating, tags, bound / 2);
        }
        int rc = RandomUtils.getRandomInt(respArray.size());
        JSONObject respJson = respArray.getJSONObject(rc);
        return respJson.getString("sample_url");
    }

}
