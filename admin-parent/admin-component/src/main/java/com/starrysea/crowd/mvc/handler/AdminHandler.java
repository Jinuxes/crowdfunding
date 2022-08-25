package com.starrysea.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.starrysea.crowd.entity.Admin;
import com.starrysea.crowd.service.api.AdminService;
import com.starrysea.crowd.util.CrowdConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {

    @Autowired
    private AdminService adminService;

    // @RequestMapping("/admin/do/login.html")
    // public String doLogin(
    //         @RequestParam("loginAcct") String loginAcct,
    //         @RequestParam("userPswd") String userPswd,
    //         HttpSession session){
    //
    //     //调用Service方法执行登录检查
    //     //这个方法如果能返回admin对象说明登录成功，如果账号、密码不正确，service层会抛出异常，
    //     //异常处理器会对异常进行处理，跳转到异常页面，这个方法就不再执行。
    //     Admin admin = adminService.getAdminByLoginAcct(loginAcct, userPswd);
    //
    //     //将登录成功返回的admin对象存入Session域
    //     session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN, admin);
    //
    //     // 登录成功，重定向到目标页面
    //     return "redirect:/admin/to/main/page.html";
    // }

    // @RequestMapping("/admin/do/logout.html")
    // public String doLogout(HttpSession session){
    //     // 强制session失效
    //     session.invalidate();
    //
    //     return "redirect:/admin/to/login/page.html";
    // }

    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(@RequestParam(value="keyword",defaultValue="") String keyword,
                              @RequestParam(value="pageNum",defaultValue="1") Integer pageNum,
                              @RequestParam(value="pageSize",defaultValue="5") Integer pageSize,
                              ModelMap modelMap){
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword,pageNum,pageSize);
        // 将PageInfo对象存入模型
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);
        return "admin-page";
    }

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("adminId") Integer adminId,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable("keyword") String keyword){
        //执行删除
        adminService.remove(adminId);

        //页面跳转：回到分页页面
        // return "admin-page";  //直接转发到admin-page.jsp会无法显示分页数据
        // return "forward:/admin/get/page.html";  //转发到admin/get/page.html地址，删除后接着点刷新，一旦手动再次刷新页面会重复执行删除浪费性能
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @PreAuthorize("hasAuthority('user:save')")
    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/update/page.html")
    public String getAdminById(Integer id,
                               // ModelMap modelMap
                               HttpServletRequest request
                            ){
        Admin admin = adminService.getAdminById(id);
        // modelMap.addAttribute(CrowdConstant.ATTR_NAME_UPDATE_ADMIN,admin);
        request.setAttribute(CrowdConstant.ATTR_NAME_UPDATE_ADMIN,admin);
        return "admin-update";
    }

    @RequestMapping("/admin/update.html")
    public String update(Integer pageNum,
                         String keyword,
                         Admin admin){
        adminService.update(admin);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    /**
     * /admin/to/main/page.html的请求已经使用了view-controller进行配置，这里这个controller没有作用，仅仅是用来
     * 测试使用view-controller配置的映射是否能够被基于注解的异常映射处理捕获。
     * 实测结果是：view-controller配置的映射是不能被基于注解的异常映射处理捕获的，只能被基于xml的异常映射处理捕获
     */
    // @RequestMapping("/admin/to/main/page.html")
    // public String toMainPage(){
    //     return "admin-main";
    // }
}
