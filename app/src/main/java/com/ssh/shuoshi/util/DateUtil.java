package com.ssh.shuoshi.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    public static String dateDiff(Date startTime, Date endTime) {
        //按照传入的格式生成一个simpledateformate对象
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        long nd = 1000 * 24 * 60 * 60;//一天的毫秒数
        long nh = 1000 * 60 * 60;//一小时的毫秒数
        long nm = 1000 * 60;//一分钟的毫秒数
        long ns = 1000;//一秒钟的毫秒数long diff;
        //获得两个时间的毫秒时间差异
        long diff;
        diff = endTime.getTime() - startTime.getTime();
        String hour = diff % nd / nh + "";//计算差多少小时
//        String hour = diff / nh + "";//计算差多少小时
        String min = diff % nd % nh / nm + "";//计算差多少分钟
        String sec = diff % nd % nh % nm / ns + "";//计算差多少秒//输出结果
        return hour + "时" + min + "分" + sec + "秒后结束";

    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
