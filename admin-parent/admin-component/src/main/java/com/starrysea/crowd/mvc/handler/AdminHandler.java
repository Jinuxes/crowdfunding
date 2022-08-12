package com.starrysea.crowd.mvc.handler;

import com.starrysea.crowd.entity.Admin;
import com.starrysea.crowd.service.api.AdminService;
import com.starrysea.crowd.util.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/do/login.html")
    public String doLogin(
            @RequestParam("loginAcct") String loginAcct,
            @RequestParam("userPswd") String userPswd,
            HttpSession session){

        //调用Service方法执行登录检查
        //这个方法如果能返回admin对象说明登录成功，如果账号、密码不正确，service层会抛出异常，
        //异常处理器会对异常进行处理，跳转到异常页面，这个方法就不再执行。
        Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);

        //将登录成功返回的admin对象存入Session域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);

        // 登录成功，重定向到目标页面
        return "redirect:/admin/to/main/page.html";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        // 强制session失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }
}
