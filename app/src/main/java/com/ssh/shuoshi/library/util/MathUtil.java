package com.ssh.shuoshi.library.util;

import android.text.TextUtils;

import java.math.BigDecimal;

/**
 * Created by lh on 2018/2/28.
 */

public class MathUtil {

    /**
     * 保留两位小数
     * @param number
     * @return
     */
    public static String keepTwoNumbersAfterThePoint(String number) {
        if(number == null){
            return "0.00";
        }else {
            try {
                if (!TextUtils.isEmpty(number)) {
                    BigDecimal bigDecimal = new BigDecimal(number);
                    return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                } else {
                    return "0.00";
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "0.00";
            }
        }
    }

}
