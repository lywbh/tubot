package com.kamimi.tubot.module;

import com.alibaba.fastjson.JSONObject;
import com.kamimi.tubot.utils.HttpUtils;
import com.kamimi.tubot.utils.LogUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

public class LoliconModule {

    private static final String BASE_URL = "https://api.lolicon.app/setu/v2";

    @Getter
    @AllArgsConstructor
    public enum Rating {
        N(0), R18(1), MIX(2);
        private int code;
    }

    public static LoliconModule.Rating rating(String rating) {
        rating = rating.trim();
        switch (rating) {
            case "mix":
            case "MIX":
                return Rating.MIX;
            case "r18":
            case "R18":
                return Rating.R18;
            case "N":
            case "n":
            default:
                return LoliconModule.Rating.N;
        }
    }

    public static String randomPic() {
        return randomPic(Rating.N);
    }

    public static String randomPic(LoliconModule.Rating rating) {
        return randomPic(rating, null);
    }

    public static String randomPic(LoliconModule.Rating rating, List<String> tagAnd) {
        String url = BASE_URL + "?r18=" + rating.code;
        String tagStr = tagAnd == null ? "" : tagAnd.stream().reduce((tag1, tag2) -> "&tag=" + tag1 + "&tag=" + tag2).orElse("");
        String finalUrl = url + tagStr;
        String responseStr = HttpUtils.get(finalUrl);
        LogUtils.getLogger().info(responseStr);
        JSONObject responseJson = JSONObject.parseObject(responseStr);
        return responseJson.getJSONArray("data").getJSONObject(0).getJSONObject("urls").getString("original");
    }

}
