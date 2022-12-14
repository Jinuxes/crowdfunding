package com.starrysea.crowd.mvc.handler;

import com.starrysea.crowd.entity.Auth;
import com.starrysea.crowd.entity.Role;
import com.starrysea.crowd.service.api.AdminService;
import com.starrysea.crowd.service.api.AuthService;
import com.starrysea.crowd.service.api.RoleService;
import com.starrysea.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
public class AssignHandler {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @RequestMapping("/assign/to/page.html")
    public String toAssignRolePage(@RequestParam("id") Integer adminId,
                                   ModelMap modelMap){
        // 1.查询已分配角色
        List<Role> assignedRoleList = roleService.getAssignedRole(adminId);
        // 2.查询未分配角色
        List<Role> unAssignedRoleList = roleService.getUnAssignedRole(adminId);
        // 3.存入模型(本质上其实是：request.setAttribute("attrName",attrValue));
        modelMap.addAttribute("assignedRoleList", assignedRoleList);
        modelMap.addAttribute("unAssignedRoleList",unAssignedRoleList);

        return "assign-role";
    }

    @RequestMapping("/assign/do/role/assign.html")
    public String saveAdminRoleRelationship(@RequestParam("adminId") Integer adminId,
                                            @RequestParam("pageNum") Integer pageNum,
                                            @RequestParam("keyword") String keyword,
                                            // 允许用户在页面上取消所有已分配角色再提交表单，所以可以不提供rollIdList
                                            @RequestParam(value="roleIdList", required=false) List<Integer> roleIdList){

        adminService.saveAdminRoleRelationship(adminId, roleIdList);

        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @RequestMapping("/assign/get/all/auth.json")
    @ResponseBody
    public ResultEntity<List<Auth>> getAllAuth(){
        List<Auth> authList = authService.getAll();
        return ResultEntity.successWithData(authList);
    }

    @RequestMapping("/assign/get/auth/id.json")
    @ResponseBody
    public ResultEntity<List<Integer>> getAssignedAuthIdByRoleId(
        @RequestParam("roleId") Integer roleId
    ){
        List<Integer> authIdList = authService.getAssignedAuthIdByRoleId(roleId);
        return ResultEntity.successWithData(authIdList);
    }

    @RequestMapping("/assign/do/role/assign/auth.json")
    @ResponseBody
    public ResultEntity<String> saveRoleAuthRelationship(
            @RequestBody Map<String,List<Integer>> map){
        authService.saveRoleAuthRelationship(map);
        return ResultEntity.successWithoutData();
    }
}
