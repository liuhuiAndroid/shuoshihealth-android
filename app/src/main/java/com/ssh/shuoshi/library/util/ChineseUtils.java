package com.ssh.shuoshi.library.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by lh on 2018/1/12.
 */

public class ChineseUtils {

    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

}
