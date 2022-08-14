package com.starrysea.crowd.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 日期工具类
 */
public class DateUtil {

    /**
     * 获取当前日期时间
     * @return yyyy-MM-dd hh:mm:ss格式日期时间
     */
    public static String getCurrentDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.now();
        return formatter.format(localDateTime);
    }
}
