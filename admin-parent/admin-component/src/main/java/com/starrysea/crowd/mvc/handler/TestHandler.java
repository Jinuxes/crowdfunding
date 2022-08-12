package com.starrysea.crowd.mvc.handler;

import com.starrysea.crowd.entity.Admin;
import com.starrysea.crowd.service.api.AdminService;
import com.starrysea.crowd.util.CrowdUtil;
import com.starrysea.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @RequestMapping("/test/resultEntity.json")
    @ResponseBody
    public ResultEntity<Admin> testReceiveComposeObject(@RequestBody Admin admin, HttpServletRequest request){

        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult="+judgeResult);

        logger.info(admin.toString());
        //将对象封装到ResultEntity中返回

        String a = null;
        System.out.println(a.length());

        return ResultEntity.successWithData(admin);
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request){

        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        logger.info("judgeResult="+judgeResult);

        List<Admin> adminList = adminService.getAll();

        modelMap.addAttribute("adminList",adminList);

        // String a = null;
        // System.out.println(a.length());

        System.out.println(10/0);
        return "target";
    }
}
