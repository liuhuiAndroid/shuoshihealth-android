package com.ssh.shuoshi.components;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.RequestBody;

/**
 * created by hwt on 2020/12/8
 */
public class JsonUtils {
    private static volatile Gson mGson;

    private static Gson getGson() {
        if (mGson == null) {
            synchronized (JsonUtils.class) {
                if (mGson == null) {
                    mGson = new Gson();
                }
            }
        }
        return mGson;
    }

    public static RequestBody stringToRequestBody(String data) {
        RequestBody body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        data);
        return body;
    }

    /**
     * 转成okhttp3的 RequestBody
     *
     * @param data
     * @return
     */
    public static RequestBody toRequestBody(JSONObject data) {
        RequestBody body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        data.toString());
        return body;
    }

    /**
     * 对转义字符的处理
     * @param data
     * @return
     */
    public static RequestBody toRequestBodyZ(JSONObject data) {
        RequestBody body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        data.toString().replace("\\", ""));
        return body;
    }

    public static RequestBody toRequestBody(JSONArray data) {
        RequestBody body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"),
                        data.toString());
        return body;
    }


    public static RequestBody toRequestBody(Object data) {
        String string = getGson().toJson(data);
        RequestBody body =
                RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), getGson().toJson(data));
        return body;
    }

    private static String toJson(Map map) {
        if (map.isEmpty()) return "{}";
        Gson gson = getGson();
        String json = gson.toJson(map);
        return json;
    }

}
