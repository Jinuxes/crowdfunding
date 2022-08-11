package com.starrysea.crowd.util;

import com.sun.deploy.net.HttpRequest;

import javax.servlet.http.HttpServletRequest;

public class CrowdUtil {

    /**
     * 判断当前请求是否为Ajax请求
     * @param request 请求对象
     * @return
     *  true：当前请求是Ajax请求
     *  false：当前请求不是Ajax请求
     */
    public static boolean judgeRequestType(HttpServletRequest request){

        // 1.获取请求头
        String acceptHeader = request.getHeader("Accept");
        String xRequestHeader = request.getHeader("X-Requested-With");

        // 2.判断
        return (acceptHeader != null && acceptHeader.contains("application/json")) ||
                (xRequestHeader != null && xRequestHeader.contains("XMLHttpRequest"));
    }
}
