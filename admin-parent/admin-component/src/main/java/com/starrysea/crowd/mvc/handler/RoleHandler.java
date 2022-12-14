package com.starrysea.crowd.mvc.handler;

import com.github.pagehelper.PageInfo;
import com.starrysea.crowd.entity.Role;
import com.starrysea.crowd.service.api.RoleService;
import com.starrysea.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoleHandler {

    @Autowired
    private RoleService roleService;

    @PreAuthorize("hasRole('部长')")
    @RequestMapping("/role/get/page/info.json")
    @ResponseBody
    public ResultEntity<PageInfo<Role>> getPageInfo(
            @RequestParam(value="pageNum", defaultValue="1") Integer pageNum,
            @RequestParam(value="pageSize", defaultValue="5") Integer pageSize,
            @RequestParam(value="keyword", defaultValue="") String keyword){

        // 调用Service方法获取分页数据
        PageInfo<Role> pageInfo = roleService.getPageInfo(pageNum,pageSize,keyword);
        // 封装到ResultEntity对象中返回（如果上面的操作抛出异常，交给异常映射机制处理）
        return ResultEntity.successWithData(pageInfo);
    }

    @RequestMapping("/role/save.json")
    @ResponseBody
    public ResultEntity<String> saveRole(Role role){
        roleService.saveRole(role);
        return ResultEntity.successWithoutData();
    }

    @RequestMapping("/role/update.json")
    @ResponseBody
    public ResultEntity<String> updateRole(Role role){
        roleService.updateRole(role);
        return ResultEntity.successWithoutData();
    }
}
