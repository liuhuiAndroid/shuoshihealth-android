package com.ssh.shuoshi.util;

import com.ssh.shuoshi.bean.Week;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * created by hwt on 2020/12/16
 */
public class WeekUtil {


    //根据字符串判断是周几
    public static String getWeekOfString(String time) {
        Date date= stringToDate(time);
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    /**
     * 根据指定日期，获取当前日期所在周的所有日期
     *
     * @param date
     * @return
     */
    public static Week getWeek(Date date) {

        //s1.设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //s2.设置要计算的时间
        cal.setTime(date);

        //s3.判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        //s4.获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        //s5.判断是不是一个星期的第一天
        if (1 == dayWeek) {
            //s6.如果是一个星期的第一天，则减去一天
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        //s7.设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //s8.获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        //s9.根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        //s10.获取星期一
        String monday = sdf.format(cal.getTime());

        //s11.获取星期二
        cal.add(Calendar.DATE, 1);
        String tuesday = sdf.format(cal.getTime());

        //s12.获取星期三
        cal.add(Calendar.DATE, 1);
        String wednesday = sdf.format(cal.getTime());

        //s13.获取星期四
        cal.add(Calendar.DATE, 1);
        String thursday = sdf.format(cal.getTime());

        //s14.获取星期五
        cal.add(Calendar.DATE, 1);
        String friday = sdf.format(cal.getTime());

        //s15.获取星期六
        cal.add(Calendar.DATE, 1);
        String saturday = sdf.format(cal.getTime());

        //s17.获取星期日
        cal.add(Calendar.DATE, 1);
        String sunday = sdf.format(cal.getTime());


        Week week = new Week();
        week.setMonday(monday);
        week.setTuesday(tuesday);
        week.setWednesday(wednesday);
        week.setThursday(thursday);
        week.setFriday(friday);
        week.setSaturday(saturday);
        week.setSunday(sunday);
        return week;
    }

    /**
     * 获取指定日期所在周的所有日期
     *
     * @param date
     * @return
     */
    public static List<String> getWeekDays(Date date) {
        List<String> weekDayList = new ArrayList<String>();
        //s1.设置时间格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        //s2.设置要计算的时间
        cal.setTime(date);
        //s3.判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会出问题，计算到下一周去了
        //s4.获得当前日期是一个星期的第几天
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        //s5.判断是不是一个星期的第一天
        if (1 == dayWeek) {
            //s6.如果是一个星期的第一天，则减去一天
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }
        //s7.设置一个星期的第一天，按中国的习惯一个星期的第一天是星期一
        cal.setFirstDayOfWeek(Calendar.MONDAY);
        //s8.获得当前日期是一个星期的第几天
        int day = cal.get(Calendar.DAY_OF_WEEK);
        //s9.根据日历的规则，给当前日期减去星期几与一个星期第一天的差值
        cal.add(Calendar.DATE, cal.getFirstDayOfWeek() - day);
        //s10.获取星期一
        String monday = sdf.format(cal.getTime());

        //s11.获取星期二
        cal.add(Calendar.DATE, 1);
        String tuesday = sdf.format(cal.getTime());

        //s12.获取星期三
        cal.add(Calendar.DATE, 1);
        String wednesday = sdf.format(cal.getTime());

        //s13.获取星期四
        cal.add(Calendar.DATE, 1);
        String thursday = sdf.format(cal.getTime());

        //s14.获取星期五
        cal.add(Calendar.DATE, 1);
        String friday = sdf.format(cal.getTime());

        //s15.获取星期六
        cal.add(Calendar.DATE, 1);
        String saturday = sdf.format(cal.getTime());

        //s16.获取星期日
        cal.add(Calendar.DATE, 1);
        String sunday = sdf.format(cal.getTime());
        weekDayList.add(monday);
        weekDayList.add(tuesday);
        weekDayList.add(wednesday);
        weekDayList.add(thursday);
        weekDayList.add(friday);
        weekDayList.add(saturday);
        weekDayList.add(sunday);
        return weekDayList;
    }

    public static Date stringToDate(String strTime) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatter.parse(strTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
