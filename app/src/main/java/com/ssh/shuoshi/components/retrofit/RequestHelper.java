package com.ssh.shuoshi.components.retrofit;

import android.content.Context;

import com.ssh.shuoshi.Constants;
import com.ssh.shuoshi.components.storage.UserStorage;
import com.ssh.shuoshi.util.SecurityUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * created by hwt on 2020/12/8
 */
public class RequestHelper {

    private Context mContext;
    private UserStorage mUserStorage;

    public RequestHelper(Context mContext, UserStorage mUserStorage) {
        this.mContext = mContext;
        this.mUserStorage = mUserStorage;
    }

    /**
     * 签名
     *
     * @param map
     * @return
     */
    public String getRequestSign(Map<String, Object> map, long currentTimeMillis) {
        String generateDigest = SecurityUtil.getMd5(map, currentTimeMillis);
        return generateDigest;
    }

    /**
     * 公共参数
     *
     * @return
     */
    public Map<String, Object> getHttpRequestMap(long currentTimeMillis) {
        HashMap<String, Object> map = new HashMap<String, Object>();
        map.put("timestamp", currentTimeMillis);
        map.put("key", Constants.app_key);
        return map;
    }
}
