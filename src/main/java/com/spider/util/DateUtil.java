package com.spider.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {


    // 获取n天前的日期
    public static String getBeforeDate(int days){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DATE, - days);
        Date d = c.getTime();
        String day = format.format(d);
        return day;
    }

}
