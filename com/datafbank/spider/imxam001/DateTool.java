package com.datafbank.spider.imxam001;

import com.dataofbank.ryze.util.RegexUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTool {
    public static Date completionDate(String time) {
        SimpleDateFormat sdf = null;
        Calendar a = Calendar.getInstance();
        try {
            if (time.matches("^\\d{2}月\\d{2}日$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                time = a.get(Calendar.YEAR) + "-" + time.replace("月", "-").replace("日", "");
                return sdf.parse(time);
            } else if (time.matches("^\\d{2}:\\d{2}$")) {
                int year = a.get(Calendar.YEAR);
                int month = a.get(Calendar.MONTH) + 1;
                int day = a.get(Calendar.DAY_OF_MONTH);
                time = year + "-" + month + "-" + day + " " + time;
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return sdf.parse(time);
            } else if (time.matches("^\\d{4}年\\d+月\\d+日$")) {
                time = time.replace("年", "-").replace("月", "-").replace("日", "");
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(time);
            } else if (time.matches("^\\d{4}年\\d+月\\d+日 \\d+:\\d+$")) {
                time = time.replace("年", "-").replace("月", "-").replace("日", "");
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return sdf.parse(time);
            } else if (time.matches("^\\d{1,2}月\\d{2}日\\s\\d{2}:\\d{2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                time = a.get(Calendar.YEAR) + "-" + time.replace("月", "-").replace("日", "");
                return sdf.parse(time);
            } else if (time.matches("^\\d{1,2}月\\d{2}日\\d{2}:\\d{2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-ddHH:mm");
                time = a.get(Calendar.YEAR) + "-" + time.replace("月", "-").replace("日", "");
                return sdf.parse(time);
            } else if (time.matches("^\\d{4}年\\d+月\\d+日 \\d+:\\d+:\\d+$")) {
                time = time.replace("年", "-").replace("月", "-").replace("日", "");
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.parse(time);
            } else if (time.matches("^\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                return sdf.parse(time);
            } else if (time.matches("^\\d{4}/\\d{2}/\\d{2}\\s\\d{2}:\\d{2}$")) {
                sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
                return sdf.parse(time);
            } else if (time.matches("^\\d{4}-\\d{1,2}-\\d{1,2}\\s\\d{2}:\\d{1,2}:\\d{1,2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                return sdf.parse(time);
            } else if (time.matches("^\\d{4}/\\d{1,2}/\\d{1,2}\\s\\d{1,2}:\\d{2}:\\d{2}$")) {
                sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                return sdf.parse(time);
            } else if (time.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(time);
            } else if (time.matches("^\\d{2}-\\d{2}-\\d{2}$")) {
                sdf = new SimpleDateFormat("yy-MM-dd");
                return sdf.parse(time);
            } else if (time.matches("^\\d{2}\\.\\d{2}\\.\\d{4}$")) {
                sdf = new SimpleDateFormat("dd.MM.yyyy");
                return sdf.parse(time);
            } else if (time.matches("^\\d{2}-\\d{2}$")) {
                sdf = new SimpleDateFormat("yyyy-MM-dd");
                return sdf.parse(a.get(Calendar.YEAR) + "-" + time);
            } else if (time.matches("^昨天$")) {
                a.add(Calendar.DAY_OF_MONTH, -1);
                a.set(Calendar.HOUR_OF_DAY, 0);
                a.set(Calendar.MINUTE, 0);
                a.set(Calendar.SECOND, 0);
                a.set(Calendar.MILLISECOND, 0);
                return a.getTime();
            } else if (time.matches("^前天$")) {
                a.add(Calendar.DAY_OF_MONTH, -2);
                a.set(Calendar.HOUR_OF_DAY, 0);
                a.set(Calendar.MINUTE, 0);
                a.set(Calendar.SECOND, 0);
                a.set(Calendar.MILLISECOND, 0);
                return a.getTime();
            } else if (time.matches("^\\d+天前发布$")) {
                Integer amount = Integer.valueOf(RegexUtil.findFirst("\\d+", time));
                a.add(Calendar.DAY_OF_MONTH, -amount);
                a.set(Calendar.HOUR_OF_DAY, 0);
                a.set(Calendar.MINUTE, 0);
                a.set(Calendar.SECOND, 0);
                a.set(Calendar.MILLISECOND, 0);
                return a.getTime();
            } else if (time.matches("^\\d+分钟前发布$")) {
                Integer amount = Integer.valueOf(RegexUtil.findFirst("\\d+", time));
                a.set(Calendar.MINUTE, -amount);
                return a.getTime();
            } else if (time.matches("^\\d+小时前发布$")) {
                Integer amount = Integer.valueOf(RegexUtil.findFirst("\\d+", time));
                a.set(Calendar.HOUR, -amount);
                return a.getTime();
            } else if (time.matches("^\\d+秒前$")) {
                Integer amount = Integer.valueOf(RegexUtil.findFirst("\\d+", time));
                a.set(Calendar.SECOND, -amount);
                return a.getTime();
            } else if (time.matches("^\\d+年\\d+月\\d+日 (a|p)m\\d+:\\d+$")) {
                time = time.replace("年", "-").replace("月", "-").replace("日", "");
                sdf = new SimpleDateFormat("yyyy-MM-dd aHH:mm", Locale.ENGLISH);
                a.setTime(sdf.parse(time));
                if (time.contains("pm")) {
                    a.add(Calendar.HOUR_OF_DAY, 12);
                }
                return a.getTime();
            } else if (time.matches("^[a-zA-Z]+\\s+\\d+\\s+\\d{4}$")) {
                sdf = new SimpleDateFormat("MMM dd yyyy", Locale.US);
                return sdf.parse(time);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
